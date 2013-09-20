package jbedu.outthinking;

import jdedu.outthinking.util.Common;
import jdedu.outthinking.util.DAO;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InventionNote extends Activity implements OnClickListener{

	TextView twoWordTxt1, twoWordTxt2;
	EditText oneWordEdt1, oneWordEdt2, twoWordEdt1, twoWordEdt2, threeWordEdt;
	Button btnReset;
	
	DAO db;
	int today;
	String todaysWord1, todaysWord2;
	
	String[] todaysWords;
	EditText[] oneWordEdts;
	TextView[] twoWordTxts;
	
	//In PopUp Components
	EditText edtText; 
	DialogInterface popUpDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.inventionnote);
	    
	    db = new DAO(this);
	    db.open();
	    today = Common.getIntDateValue();
	    
	    
	    twoWordTxt1 = (TextView) findViewById(R.id.InventionNote_two_word1_txt);
	    twoWordTxt2 = (TextView) findViewById(R.id.InventionNote_two_word2_txt);
	    
	    oneWordEdt1 = (EditText) findViewById(R.id.InventionNote_one_word_edt1);
	    oneWordEdt1.setOnClickListener(this);
	    
	    oneWordEdt2 = (EditText) findViewById(R.id.InventionNote_one_word_edt2);
	    oneWordEdt2.setOnClickListener(this);
	    
	    twoWordEdt1 = (EditText) findViewById(R.id.InventionNote_two_word1_edt);
	    twoWordEdt1.setOnClickListener(this);
	    
	    twoWordEdt2 = (EditText) findViewById(R.id.InventionNote_two_word2_edt);
	    twoWordEdt2.setOnClickListener(this);
	    
	    threeWordEdt = (EditText) findViewById(R.id.InventionNote_three_edt);
	    threeWordEdt.setOnClickListener(this);
	    
	    btnReset = (Button) findViewById(R.id.InventionNote_btnReset);
	    btnReset.setOnClickListener(this);
	    
	    todaysWords = new String[]{todaysWord1, todaysWord2};
	    oneWordEdts = new EditText[]{oneWordEdt1, oneWordEdt2};
	    twoWordTxts = new TextView[]{twoWordTxt1, twoWordTxt2};
	    
	    checkCardChoiceToday();
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.InventionNote_one_word_edt1 :
			inputPopup("단어1", oneWordEdt1, 1);
			break;
		case R.id.InventionNote_one_word_edt2 :
			inputPopup("단어2", oneWordEdt2, 1);
			break;
		case R.id.InventionNote_two_word1_edt :
			inputPopup(twoWordTxt1.getText().toString()+"의 이미지나 느낌", twoWordEdt1, 10);
			break;
		case R.id.InventionNote_two_word2_edt :
			inputPopup(twoWordTxt2.getText().toString()+"의 이미지나 느낌", twoWordEdt2, 10);
			break;
		case R.id.InventionNote_three_edt :
			inputPopup(getString(R.string.InventionNote_three_txt), threeWordEdt, 10);
			break;
		case R.id.InventionNote_btnReset :
			oneWordEdt1.setText("");
			oneWordEdt2.setText("");
			twoWordTxt1.setText("단어1");
			twoWordTxt2.setText("단어2");
			twoWordEdt1.setText("");
			twoWordEdt2.setText("");
			threeWordEdt.setText("");
			break;
		}
	}

	private void inputPopup(String title, final TextView target, int edtLine) {
		edtText = new EditText(this);
		edtText.setLines(edtLine);
		edtText.setText(target.getText().toString());
		
		AlertDialog.Builder popUp = new AlertDialog.Builder(InventionNote.this);
		popUp
		//.setIcon(R.drawable.ic_launcher) 
		.setTitle(title)
		.setView(edtText)
		.setPositiveButton("저장", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				target.setText(edtText.getText().toString());
				
				twoWordTxt1.setText(oneWordEdt1.getText().toString());
				twoWordTxt2.setText(oneWordEdt2.getText().toString());
			}
		})
		.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				popUpDialog.dismiss();
			}
		})
		.create();
		popUpDialog = popUp.show();
	}


	private void checkCardChoiceToday() {
		Cursor historyCursor = db.selectHistoryByDate(today);
		if (historyCursor.getCount() == 0)
			return;

		historyCursor.moveToFirst();

		for (int i = 0; i < historyCursor.getCount(); i++) {
			int todayWordId = historyCursor.getInt(2);
			
			Cursor cardCursor = db.selectCardByID(todayWordId);
			cardCursor.moveToFirst();
			String todayWord = cardCursor.getString(1);
			todaysWords[i] = todayWord;
			oneWordEdts[i].setText(todayWord);
			twoWordTxts[i].setText(todayWord);
			
			historyCursor.moveToNext();
		}
		db.close();
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
