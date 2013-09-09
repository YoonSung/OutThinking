package jbedu.outthinking;

import jdedu.outthinking.util.Common;
import jdedu.outthinking.util.DAO;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TodaysWord extends Activity implements OnClickListener {

	Button btn1, btn2, btn3, btn4, btn5, btn6;
	Button[] btns;

	int today;
	DAO db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todaysword);

		//Default Init
		today = Common.getIntDateValue();
		db = new DAO(TodaysWord.this);
		db.open();

		checkCardChoiceLimite();
		
		btn1 = (Button) findViewById(R.id.todaysword_btn1);
		btn1.setOnClickListener(this);

		btn2 = (Button) findViewById(R.id.todaysword_btn2);
		btn2.setOnClickListener(this);

		btn3 = (Button) findViewById(R.id.todaysword_btn3);
		btn3.setOnClickListener(this);

		btn4 = (Button) findViewById(R.id.todaysword_btn4);
		btn4.setOnClickListener(this);

		btn5 = (Button) findViewById(R.id.todaysword_btn5);
		btn5.setOnClickListener(this);

		btn6 = (Button) findViewById(R.id.todaysword_btn6);
		btn6.setOnClickListener(this);

		btns = new Button[] { btn1, btn2, btn3, btn4, btn5, btn6 };
		
		checkCardChoiceToday();
	}

	private void checkCardChoiceToday() {
		Cursor historyCursor = db.selectHistoryByDate(today);
		if (historyCursor.getCount() == 0)
			return;

		historyCursor.moveToFirst();

		for (int i = 1; i <= historyCursor.getCount(); i++) {
			int todayWordId = historyCursor.getInt(2);
			int btnNumber = historyCursor.getInt(3);
			
			Cursor cardCursor = db.selectCardByID(todayWordId);
			cardCursor.moveToFirst();
			String todayWord = cardCursor.getString(1);
			btns[btnNumber-1].setText(todayWord);
			
			historyCursor.moveToNext();
		}
	}

	private void checkCardChoiceLimite() {
		Cursor historyCursor = db.selectHistoryByDate(today);

		if (historyCursor.getCount() >= 2) {
			startActivity(new Intent(TodaysWord.this, TodaysWordDetail.class));
			
			historyCursor.close();
			finish();
		}
	}

	@Override
	public void onClick(View v) {

		checkCardChoiceLimite();

		System.out.println(today); //FOR TEST
		int count = db.selectAllCard().getCount();
		int random = (int) ((Math.random() * count) + 1);
		System.out.println(random); //FOR TEST
		Cursor cardCursor = db.selectCardByID(random);

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
		}

		db.insertHistory(today, cardCursor.getInt(0), btnNumber);
		btns[btnNumber - 1].setText(cardCursor.getString(1));
		cardCursor.close();
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
}
