package jbedu.outthinking;

import jdedu.outthinking.util.Common;
import jdedu.outthinking.util.DAO;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TodaysWordDetail extends Activity implements OnClickListener{

	Button oneBtn, twoBtn;
	TextView oneTxt, twoTxt;
	DAO db;
	int date;
	String word1;
	String word2;
	String searchUrl = "http://m.krdic.naver.com/search/entry/1/";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.todayworlddetail);
	    
	    oneBtn = (Button) findViewById(R.id.TodayWorldDetail_one_btn);
	    twoBtn = (Button) findViewById(R.id.TodayWorldDetail_two_btn);
	    oneBtn.setOnClickListener(this);
	    twoBtn.setOnClickListener(this);
	    
	    oneTxt = (TextView) findViewById(R.id.TodayWorldDetail_one_txt);
	    twoTxt = (TextView) findViewById(R.id.TodayWorldDetail_two_txt);
	    
	    date = Common.getIntDateValue();
	    
	    db = new DAO(this);
	    db.open();
	    Cursor historyCursor = db.selectHistoryByDate(date);
	    historyCursor.moveToFirst();
	    
	    int wordInt1 = historyCursor.getInt(2);
	    historyCursor.moveToNext();
	    int wordInt2 = historyCursor.getInt(2);
	    historyCursor.close();
	    
	    Cursor cardCursor = db.selectCardByID(wordInt1);
	    cardCursor.moveToFirst();
	    word1 = cardCursor.getString(1);
	    
	    cardCursor = db.selectCardByID(wordInt2);
	    cardCursor.moveToFirst();
	    word2 = cardCursor.getString(1);
	    
	    oneTxt.setText(word1);
	    twoTxt.setText(word2);
	    
	    db.close();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.TodayWorldDetail_one_btn) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl+word1)));
		} else {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl+word2)));
		}
	}

}
