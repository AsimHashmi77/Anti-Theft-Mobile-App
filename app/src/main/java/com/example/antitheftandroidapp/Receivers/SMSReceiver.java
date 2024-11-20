// SMSReceiver.java
package com.example.antitheftandroidapp.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = smsMessage.getOriginatingAddress();
                    String messageBody = smsMessage.getMessageBody();
                    if (sender != null && sender.equals("YOUR_SPECIFIC_NUMBER")) {
                        // Perform action for the specific sender
                        // For example, block the SMS or take other anti-theft measures
                        Toast.makeText(context, "Blocking SMS from specific number", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
