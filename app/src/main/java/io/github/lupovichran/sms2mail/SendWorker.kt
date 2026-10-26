package io.github.lupovichran.sms2mail

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.app.Notification
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit
import javax.mail.AuthenticationFailedException

class SendWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val subject = inputData.getString(KEY_SUBJECT) ?: return Result.failure()
        val body = inputData.getString(KEY_BODY) ?: return Result.failure()
        val config = Settings.load(applicationContext)
        if (!config.isComplete) {
            notifyFailure("Settings incomplete; SMS not forwarded: $subject")
            return Result.failure()
        }
        return try {
            Mailer.send(config, subject, body)
            Result.success()
        } catch (e: IllegalArgumentException) {
            notifyFailure("Settings invalid; SMS not forwarded: $subject")
            Result.failure()
        } catch (e: AuthenticationFailedException) {
            notifyFailure("SMTP login failed; check user / app password. Lost: $subject")
            Result.failure()
        } catch (e: Exception) {
            if (runAttemptCount >= MAX_ATTEMPTS) {
                notifyFailure("Could not send after $MAX_ATTEMPTS attempts: $subject")
                Result.failure()
            } else {
                Result.retry()
            }
        }
    }

    private fun notifyFailure(text: String) {
        val ctx = applicationContext
        if (Build.VERSION.SDK_INT >= 33 &&
            ctx.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return
        val nm = ctx.getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(
            NotificationChannel(CHANNEL, ctx.getString(R.string.channel_name), NotificationManager.IMPORTANCE_DEFAULT)
                .apply { lockscreenVisibility = Notification.VISIBILITY_PRIVATE }
        )
        val n = Notification.Builder(ctx, CHANNEL)
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setContentTitle(ctx.getString(R.string.app_name))
            .setContentText(text)
            .setStyle(Notification.BigTextStyle().bigText(text))
            // The text names the SMS sender: hide it on the lock screen.
            .setVisibility(Notification.VISIBILITY_PRIVATE)
            .setPublicVersion(
                Notification.Builder(ctx, CHANNEL)
                    .setSmallIcon(android.R.drawable.stat_notify_error)
                    .setContentTitle(ctx.getString(R.string.app_name))
                    .setContentText(ctx.getString(R.string.error_public))
                    .build()
            )
            .build()
        nm.notify(id.hashCode(), n)
    }

    companion object {
        private const val KEY_SUBJECT = "subject"
        private const val KEY_BODY = "body"
        private const val CHANNEL = "errors"
        private const val MAX_ATTEMPTS = 20

        fun enqueue(context: Context, subject: String, body: String) {
            val request = OneTimeWorkRequestBuilder<SendWorker>()
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
                .setInputData(workDataOf(KEY_SUBJECT to subject, KEY_BODY to body))
                .build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }
}
