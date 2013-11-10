package com.exoTut.exotut2;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private DisplayMetrics ecran = new DisplayMetrics();
	private ImageView imageView = null;
	private Button bouton = null;
	private boolean alle = true;
	private EditText texte = null;
	private Animation aller = null;
	private Animation retour = null;
	private int tpsdelta = 0;
	private int largeur = 0;
	private int w_image = 0;
	private int temps_total = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imageView = (ImageView) findViewById(R.id.image);
		bouton = (Button) findViewById(R.id.bouton);
		texte = (EditText) findViewById(R.id.text);
		getWindowManager().getDefaultDisplay().getMetrics(ecran);
		largeur = ecran.widthPixels;
		w_image = imageView.getDrawable().getIntrinsicWidth();
		bouton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tpsdelta > 0) {
					temps_total = largeur * tpsdelta / 10;
					bouge();
				} else {
					Toast.makeText(getApplicationContext(),
							"Entrer d'abord une valeur", Toast.LENGTH_LONG)
							.show();
				}

			}
		});
	}

	public void bouge() {

		if (alle) {
			aller = new TranslateAnimation(0, largeur - w_image, 0, 0);
			aller.setDuration(temps_total);
			aller.setFillAfter(true);
			aller.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					bouton.setEnabled(false);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					alle = false;
					bouton.setEnabled(true);
				}
			});
			imageView.startAnimation(aller);

		} else {
			retour = new TranslateAnimation(largeur - w_image, 0, 0, 0);
			retour.setDuration(temps_total);
			retour.setFillAfter(true);
			retour.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					bouton.setEnabled(false);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					alle = true;
					bouton.setEnabled(true);
				}
			});
			imageView.startAnimation(retour);

		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			tpsdelta = Integer.parseInt(texte.getText().toString());
			return true;
		}
		return false;
	}
}
