package jbedu.outthinking;

import jdedu.outthinking.util.DAO;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddWord extends Activity implements OnClickListener,
		AnimationListener {

	DAO db;
	Button btnAdd, btnEnd;
	TextView cardTxt, endTxt;
	LinearLayout cardLinear, shuffleLinear1, shuffleLinear2, shuffleLinear3,
			shuffleLinear4, shuffleLinear5, shuffleLinear6, shuffleLinear7,
			shuffleLinear8, endLinear;
	private Animation animationFold;
	private Animation animationUnfold;
	Handler handler;
	boolean isEmptyCardInFront = false;

	// In PopUp Components
	EditText edtText;
	DialogInterface popUpDialog;

	private int HANDLER_FRONT_TO_BACK = 0;
	private int HANDLER_BACK_TO_FRONT = 1;
	private int HANDLER_SHUFFLE = 2;
	private int HANDLER_MSG_FAIL = 3;
	private int DATABASE_INSERT_FAIL = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addword);

		db = new DAO(this);
		db.open();

		btnAdd = (Button) findViewById(R.id.AddWord_btnAdd);
		btnAdd.setOnClickListener(this);

		btnEnd = (Button) findViewById(R.id.AddWord_btnEnd);
		btnEnd.setOnClickListener(this);
		
		cardTxt = (TextView) findViewById(R.id.AddWord_txt);
		cardLinear = (LinearLayout) findViewById(R.id.AddWord_cardLinear);

		endTxt = (TextView) findViewById(R.id.AddWord_txtEnd);
		endLinear = (LinearLayout) findViewById(R.id.AddWord_endLinear);
		
		animationFold = AnimationUtils.loadAnimation(this, R.anim.to_middle);
		animationFold.setAnimationListener(this);
		animationUnfold = AnimationUtils
				.loadAnimation(this, R.anim.from_middle);
		animationUnfold.setAnimationListener(this);

		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == HANDLER_FRONT_TO_BACK) {
					frontToBackAnimation();
				} else if (msg.what == HANDLER_BACK_TO_FRONT) {
					backToFrontAnimation();
				} else if (msg.what == HANDLER_SHUFFLE) {
					shuffleAnimation();
				} else if (msg.what == HANDLER_MSG_FAIL) {
					handler.sendEmptyMessage(HANDLER_BACK_TO_FRONT);
					handler.sendEmptyMessageDelayed(HANDLER_FRONT_TO_BACK, 400);
				}
			};
		};
		handler.sendEmptyMessageDelayed(HANDLER_BACK_TO_FRONT, 400);

		db.close();
	}

	void frontToBackAnimation() {
		isEmptyCardInFront = false;
		startAnimation();
	}

	void backToFrontAnimation() {
		isEmptyCardInFront = true;
		startAnimation();
	}

	void startAnimation() {
		cardLinear.clearAnimation();
		cardLinear.setAnimation(animationFold);
		cardLinear.startAnimation(animationFold);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.AddWord_btnEnd)
			finish();
		inputPopup();
	}

	private void inputPopup() {
		edtText = new EditText(this);
		edtText.setLines(1);

		AlertDialog.Builder popUp = new AlertDialog.Builder(AddWord.this);
		popUp
		// .setIcon(R.drawable.ic_launcher)
		.setTitle(getString(R.string.AddWord_btnAdd))
				.setView(edtText)
				.setPositiveButton("저장", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String addWord = edtText.getText().toString().trim();
						if ("".equals(addWord)) {
							Toast.makeText(AddWord.this, "값이 입력되지 않았습니다.",
									Toast.LENGTH_SHORT).show();
							edtText.setText("");
						}

						if (db.insertCard(edtText.getText().toString()) == DATABASE_INSERT_FAIL) {
							cardLinear.clearAnimation();
							Toast.makeText(AddWord.this,
									"저장에 실패했습니다. 다시 시도해 주세요",
									Toast.LENGTH_SHORT).show();
							handler.sendEmptyMessageDelayed(HANDLER_MSG_FAIL,
									300);
						} else {
							popUpDialog.dismiss();
							cardTxt.setText(addWord);
							btnAdd.setVisibility(View.GONE);
							handler.sendEmptyMessageDelayed(
									HANDLER_FRONT_TO_BACK, 800);
							handler.sendEmptyMessageDelayed(HANDLER_SHUFFLE,
									1100);
						}
					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						popUpDialog.dismiss();
					}
				}).create();
		popUpDialog = popUp.show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		db.open();
	}

	@Override
	protected void onPause() {
		super.onPause();
		db.close();
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		if (isEmptyCardInFront) {
			cardLinear.setBackgroundResource(R.drawable.empty256);
			btnAdd.setVisibility(View.VISIBLE);
			cardTxt.setVisibility(View.VISIBLE);

			cardLinear.clearAnimation();
			cardLinear.setAnimation(animationUnfold);
			cardLinear.startAnimation(animationUnfold);
			cardLinear.clearAnimation();
		} else {
			cardLinear.setBackgroundResource(R.drawable.diamonds256);
			btnAdd.setVisibility(View.INVISIBLE);
			cardTxt.setVisibility(View.INVISIBLE);

			cardLinear.clearAnimation();
			cardLinear.setAnimation(animationFold);
			cardLinear.startAnimation(animationFold);
			cardLinear.clearAnimation();
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {}

	@Override
	public void onAnimationStart(Animation animation) {}

	private TranslateAnimation leftToRightAnimation() {
		return new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
				(float) -1.0, Animation.RELATIVE_TO_PARENT, (float) 0.0,
				Animation.RELATIVE_TO_PARENT, (float) 0.0,
				Animation.RELATIVE_TO_PARENT, (float) 0.0);
	}

	private TranslateAnimation rightToLeftAnimation() {
		return new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
				(float) 1.0, Animation.RELATIVE_TO_PARENT, (float) 0.0,
				Animation.RELATIVE_TO_PARENT, (float) 0.0,
				Animation.RELATIVE_TO_PARENT, (float) 0.0);
	}

	private void shuffleAnimation() {
		int durationTime = 400;
		int intervalTime = 400;

		shuffleLinear1 = (LinearLayout) findViewById(R.id.AddWord_shuffle1);
		shuffleLinear2 = (LinearLayout) findViewById(R.id.AddWord_shuffle2);
		shuffleLinear3 = (LinearLayout) findViewById(R.id.AddWord_shuffle3);
		shuffleLinear4 = (LinearLayout) findViewById(R.id.AddWord_shuffle4);
		shuffleLinear5 = (LinearLayout) findViewById(R.id.AddWord_shuffle5);
		shuffleLinear6 = (LinearLayout) findViewById(R.id.AddWord_shuffle6);
		shuffleLinear7 = (LinearLayout) findViewById(R.id.AddWord_shuffle7);
		shuffleLinear8 = (LinearLayout) findViewById(R.id.AddWord_shuffle8);

		LinearLayout[] shuffleLinears = new LinearLayout[] { shuffleLinear1,
				shuffleLinear2, shuffleLinear3, shuffleLinear4, shuffleLinear5,
				shuffleLinear6, shuffleLinear7, shuffleLinear8 };

		TranslateAnimation ani1 = leftToRightAnimation();
		TranslateAnimation ani2 = rightToLeftAnimation();
		TranslateAnimation ani3 = leftToRightAnimation();
		TranslateAnimation ani4 = rightToLeftAnimation();
		TranslateAnimation ani5 = leftToRightAnimation();
		TranslateAnimation ani6 = rightToLeftAnimation();
		TranslateAnimation ani7 = leftToRightAnimation();
		TranslateAnimation ani8 = rightToLeftAnimation();

		TranslateAnimation[] anis = new TranslateAnimation[] { ani1, ani2,
				ani3, ani4, ani5, ani6, ani7, ani8 };

		int number = 0;
		for (TranslateAnimation animation : anis) {
			//animation.setInterpolator(this, android.R.anim.fade_in);
			animation.setDuration(durationTime);
			animation.setStartOffset(number * intervalTime);
			animation.setFillAfter(true);
			number++;
		}

		number = 0;
		for (LinearLayout shuffleLinear : shuffleLinears) {
			shuffleLinear.setVisibility(View.VISIBLE);
			shuffleLinear.startAnimation(anis[number]);
			number++;
		}
		
		AlphaAnimation aniLast = new AlphaAnimation(0, 1);
		aniLast.setDuration(durationTime);
		aniLast.setStartOffset(number * intervalTime);
		endLinear.setVisibility(View.VISIBLE);
		endLinear.startAnimation(aniLast);
	}
}