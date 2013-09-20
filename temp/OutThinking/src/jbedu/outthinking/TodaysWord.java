package jbedu.outthinking;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import jdedu.outthinking.util.Common;
import jdedu.outthinking.util.DAO;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;

public class TodaysWord extends Activity implements OnClickListener,
		AnimationListener {

	Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8;
	Button[] btns;
	int[] btnResources;
	int today;
	DAO db;

	// for test
	String thumbNail;
	String link;

	private Animation animationFold;
	private Animation animationUnfold;
	Button currentClickBtn;
	String currentBtnWord;
	int currentWordNumber;
	Handler handler ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todaysword);

		// Default Init
		today = Common.getIntDateValue();
		db = new DAO(TodaysWord.this);
		db.open();

		if (checkCardChoiceNumber() >= 2) {
			startActivity(new Intent(TodaysWord.this, TodaysWordDetail.class));
			finish();
		}
			

		btns = new Button[] { btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8 };
		btnResources = new int[] { R.id.todaysword_btn1, R.id.todaysword_btn2,
				R.id.todaysword_btn3, R.id.todaysword_btn4,
				R.id.todaysword_btn5, R.id.todaysword_btn6,
				R.id.todaysword_btn7, R.id.todaysword_btn8 };

		for (int index = 0; index < btns.length; index++) {
			btns[index] = (Button) findViewById(btnResources[index]);
			btns[index].setOnClickListener(this);
		}

		animationFold = AnimationUtils.loadAnimation(this, R.anim.to_middle);
		animationFold.setAnimationListener(this);
		animationUnfold = AnimationUtils
				.loadAnimation(this, R.anim.from_middle);
		animationUnfold.setAnimationListener(this);

		checkCardChoiceToday();
	}

	private void checkCardChoiceToday() {
		Cursor historyCursor = db.selectHistoryByDate(today);
		if (historyCursor.getCount() == 0) {
			historyCursor.close();
			return;
		}
		historyCursor.moveToFirst();

		for (int i = 1; i <= historyCursor.getCount(); i++) {
			int todayWordId = historyCursor.getInt(2);
			int btnNumber = historyCursor.getInt(3);

			Cursor cardCursor = db.selectCardByID(todayWordId);
			cardCursor.moveToFirst();
			String todayWord = cardCursor.getString(1);
			btns[btnNumber - 1].setBackgroundResource(R.drawable.empty128);
			btns[btnNumber - 1].setText(todayWord);
			btns[btnNumber - 1].setClickable(false);

			cardCursor.close();
			historyCursor.moveToNext();
		}
		
		historyCursor.close();
	}

	private int checkCardChoiceNumber() {
		Cursor historyCursor = db.selectHistoryByDate(today);
		int choiceCardNumber = historyCursor.getCount();
		historyCursor.close();
		return choiceCardNumber;
	}

	@Override
	public void onClick(View v) {

		System.out.println(today); //FOR TEST
		Cursor cardCursor = db.selectAllCard(); 
		int count = cardCursor.getCount();
		cardCursor.close();
		
		
		int random = (int) ((Math.random() * count) + 1);
		System.out.println(random); //FOR TEST
		cardCursor = db.selectCardByID(random);
		cardCursor.moveToFirst();
		
		int btnNumber = 0;

		switch (v.getId()) {
			case R.id.todaysword_btn1:
				btnNumber = 1;
				break;
			case R.id.todaysword_btn2:
				btnNumber = 2;
				break;
			case R.id.todaysword_btn3:
				btnNumber = 3;
				break;
			case R.id.todaysword_btn4:
				btnNumber = 4;
				break;
			case R.id.todaysword_btn5:
				btnNumber = 5;
				break;
			case R.id.todaysword_btn6:
				btnNumber = 6;
				break;
			case R.id.todaysword_btn7:
				btnNumber = 7;
				break;
			case R.id.todaysword_btn8:
				btnNumber = 8;
				break;
		}
		currentWordNumber = cardCursor.getInt(0);
		db.insertHistory(today, currentWordNumber, btnNumber);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				DAO threadDB =  new DAO(TodaysWord.this);
				threadDB.open();
				
				Cursor threadHistoryCursor = threadDB.selectHistoryByPickID(currentWordNumber);
				threadHistoryCursor.moveToFirst();
				int threadRowId = threadHistoryCursor.getInt(0);
				threadHistoryCursor.close();
				
				Cursor cardCursor = threadDB.selectCardByID(threadRowId);
				cardCursor.moveToFirst();
				String word = cardCursor.getString(1);
				cardCursor.close();
				
				searchImage(word);
				
				System.out.println("link : "+link);
				System.out.println("thumbNail : "+thumbNail);
				long result = threadDB.updateHistory(threadRowId, link);
				System.out.println("result = "+result);
				
				threadDB.close();
			}
		}).start();
		
		
		currentClickBtn = btns[btnNumber - 1];
		currentBtnWord = cardCursor.getString(1);
		currentClickBtn.clearAnimation();
		currentClickBtn.setAnimation(animationFold);
		currentClickBtn.startAnimation(animationFold);
		currentClickBtn.setClickable(false);
		
		cardCursor.close();
		if (checkCardChoiceNumber() >=2 ) {
			for (Button button : btns) {
				button.setClickable(false);
			}
			
			handler = new Handler(){
				public void handleMessage(android.os.Message msg) {
					startActivity(new Intent(TodaysWord.this, TodaysWordDetail.class));
					finish();
				};
			};
			
			handler.sendEmptyMessageDelayed(0,200);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		db.close();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		db.open();
	}
	
	@Override
	public void onAnimationEnd(Animation animation) {
		currentClickBtn.setBackgroundResource(R.drawable.empty128);
		currentClickBtn.setText(currentBtnWord);
		currentClickBtn.clearAnimation();
		currentClickBtn.setAnimation(animationUnfold);
		currentClickBtn.startAnimation(animationUnfold);
		currentClickBtn.clearAnimation();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}

	private void searchImage(String word) {

		String searchInfo = null;
		// ///////////////////////////////////////////////
		try {
			searchInfo = URLEncoder.encode(word, "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// ///////////////////////////////////////////////
		try {
			URL requestUrl = new URL("http://openapi.naver.com/search"
					+ "?key=" + Common.NAVER_SEARCH_KEY + "&query="
					+ searchInfo + "&target=image" + "&start=1" + "&display=1");

			XmlPullParserFactory parserCreator = XmlPullParserFactory
					.newInstance();
			XmlPullParser parser = parserCreator.newPullParser();

			parser.setInput(requestUrl.openStream(), null);

			int parseEvent = parser.getEventType();

			while (parseEvent != XmlPullParser.END_DOCUMENT) {
				switch (parseEvent) {

				case XmlPullParser.START_TAG:
					String tag = parser.getName();
					if (tag.compareTo("link") == 0)
						link = parser.nextText();
					if (tag.compareTo("thumbnail") == 0)
						thumbNail = parser.nextText();
					break;
				}
				parseEvent = parser.next();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		// ///////////////////////////////////////////////

		System.out.println("link : " + link);
		System.out.println("link : " + link);
		System.out.println("thumbNail : " + thumbNail);
	}
}
