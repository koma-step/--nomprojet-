package com.example.testnumpicker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView numpick = null;
	TextView text1 = null;
	int num = 1000;
	int yi = 0;
	int delta = 0;
	Button b = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		numpick = (TextView) findViewById(R.id.textView2);
		b = (Button) findViewById(R.id.button1);
		text1 = (TextView) findViewById(R.id.textView1);
		numpick.setText(String.valueOf(num));
		numpick.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int y = (int) event.getY();
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					yi = (int) event.getY();
					break;

				case MotionEvent.ACTION_MOVE:
					delta = yi - y;
					numpick.setText(String.valueOf(arrondi(num + delta)));
					break;

				case MotionEvent.ACTION_UP:
					if (arrondi(num + delta) < 0)
						num = 0;
					else
						num = arrondi(num + delta);
					numpick.setText(String.valueOf(num));
					break;
				}
				return true;
			}
		});
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				text1.setText(String.valueOf(num));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public int arrondi(int i) {
		return (int) (Math.floor((num + delta) / 10) * 10);
	}

}
