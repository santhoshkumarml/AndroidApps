package com.dev.alarm.app;

import java.util.logging.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.android.alarm.R;

/**
 *
 * @author Santhosh Kumar M L
 *
 */
public class AlarmActivity extends Activity {

	private static final Logger LOGGER = Logger.getLogger(AlarmActivity.class.getCanonicalName());


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);

		Button start = (Button)findViewById(R.id.button1);
        Button stop = (Button)findViewById(R.id.button2);

        start.setOnClickListener(startListener);
        stop.setOnClickListener(stopListener);
	}


	private OnClickListener startListener = new OnClickListener() {
		public void onClick(View v){
			startService(new Intent(AlarmActivity.this, NotificationService.class));
		}
	};

	private OnClickListener stopListener = new OnClickListener() {
		public void onClick(View v){
			stopService(new Intent(AlarmActivity.this, NotificationService.class));
		}
	};

}
