package kr.spyec.spyecvote;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 최예찬 on 2016-08-22.
 */
public class SMSReceiver extends BroadcastReceiver {


    static final String logTag = "SmsReceiver";
    static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!DataManager.getInstance().isReceiverActive()) return;
        if (intent.getAction().equals(ACTION)) {
            //Bundel 널 체크
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }

            //pdu 객체 널 체크
            Object[] pdusObj = (Object[]) bundle.get("pdus");
            if (pdusObj == null) {
                return;
            }

            for (Object aPdusObj : pdusObj) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) aPdusObj);
                String origNumber = message.getOriginatingAddress();
                String messageBody = message.getMessageBody();

                try {

                    DataManager.getInstance().vote(origNumber, messageBody);
                } catch (DataManager.AlreadyVoteException e) {
                    sendSMS(context, origNumber, DataManager.getInstance().getAlreadyVoteMessage());
                } catch (DataManager.UnknownTargetVoteException e) {
//                    sendSMS(context, origNumber, DataManager.getInstance().getUnknownTargetVoteMessage());
                }
            }

            context.sendBroadcast(new Intent("data.update"));
        }
    }


    public void sendSMS(Context context, String smsNumber, String smsText) {
//        PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT_ACTION"), 0);
//        PendingIntent deliveredIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_DELIVERED_ACTION"), 0);
//
//        context.registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                switch(getResultCode()){
//                    case Activity.RESULT_OK:
//                        // 전송 성공
//                        Toast.makeText(mContext, "전송 완료", Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        // 전송 실패
//                        Toast.makeText(mContext, "전송 실패", Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_NO_SERVICE:
//                        // 서비스 지역 아님
//                        Toast.makeText(mContext, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        // 무선 꺼짐
//                        Toast.makeText(mContext, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_NULL_PDU:
//                        // PDU 실패
//                        Toast.makeText(mContext, "PDU Null", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        }, new IntentFilter("SMS_SENT_ACTION"));
//
//        context.registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                switch (getResultCode()){
//                    case Activity.RESULT_OK:
//                        // 도착 완료
//                        Toast.makeText(mContext, "SMS 도착 완료", Toast.LENGTH_SHORT).show();
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        // 도착 안됨
//                        Toast.makeText(mContext, "SMS 도착 실패", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        }, new IntentFilter("SMS_DELIVERED_ACTION"));

        SmsManager mSmsManager = SmsManager.getDefault();
//        mSmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);
        mSmsManager.sendTextMessage(smsNumber, null, smsText, null, null);
    }

}
