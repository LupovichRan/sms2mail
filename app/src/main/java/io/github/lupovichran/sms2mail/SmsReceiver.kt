package io.github.lupovichran.sms2mail

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return
        if (!Settings.load(context).enabled) return
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent) ?: return
        // A multi-part SMS arrives as several PDUs in one broadcast; group them by sender.
        messages.groupBy { it.displayOriginatingAddress }.forEach { (sender, parts) ->
            SendWorker.enqueue(
                context,
                subject = MessageFormatter.subject(sender),
                body = MessageFormatter.body(parts.map { it.displayMessageBody }, parts.first().timestampMillis),
            )
        }
    }
}
