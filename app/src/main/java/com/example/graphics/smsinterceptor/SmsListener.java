package com.example.graphics.smsinterceptor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by graphics on 11/5/2016.
 */

public class SmsListener extends BroadcastReceiver{

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
            String prefString = sharedPreferences.getString("receivedMessage","");
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        prefString += "Sender : " +msg_from + "\nMessage : "+msgBody+"\n\n\n";
                    }
                }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                }
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("receivedMessage",prefString);
        }
    }
}
