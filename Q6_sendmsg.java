MainActivity.java
package com.example.smsdeliveryreport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	EditText phoneNumberEditText, messageEditText;
	Button sendButton;
	TextView statusTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);

    	phoneNumberEditText = findViewById(R.id.phoneNumber);
    	messageEditText = findViewById(R.id.messageText);
    	sendButton = findViewById(R.id.sendButton);
    	statusTextView = findViewById(R.id.statusTextView);

    	sendButton.setOnClickListener(v -> sendSMS());
	}

	private void sendSMS() {
    	String phoneNumber = phoneNumberEditText.getText().toString();
    	String message = messageEditText.getText().toString();

    	if (!phoneNumber.isEmpty() && !message.isEmpty()) {
        	SmsManager smsManager = SmsManager.getDefault();
        	smsManager.sendTextMessage(phoneNumber, null, message, null, null);

        	// Show a message that the SMS is sent
        	statusTextView.setText("Message Sent. Waiting for delivery report...");
    	}
	}

	@Override
	protected void onStart() {
    	super.onStart();
    	// Register the broadcast receiver for delivery reports
    	IntentFilter filter = new IntentFilter("android.intent.action.SMS_DELIVER");
    	registerReceiver(smsReceiver, filter);
	}

	@Override
	protected void onStop() {
    	super.onStop();
    	// Unregister the receiver when the activity is stopped
    	unregisterReceiver(smsReceiver);
	}

	// BroadcastReceiver to receive the delivery report
	private final BroadcastReceiver smsReceiver = new BroadcastReceiver() {
    	@Override
    	public void onReceive(Context context, Intent intent) {
        	String status = "SMS not delivered";
        	// Check the result code to determine if the message was delivered
        	switch (getResultCode()) {
            	case Activity.RESULT_OK:
                	status = "SMS delivered successfully!";
                	break;
            	case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                	status = "SMS delivery failed!";
                	break;
            	case SmsManager.RESULT_ERROR_NO_SERVICE:
                	status = "No service available!";
                	break;
            	case SmsManager.RESULT_ERROR_NULL_PDU:
                	status = "Error with the PDU!";
                	break;
        	}
        	statusTextView.setText(status);
    	}
	};
}


