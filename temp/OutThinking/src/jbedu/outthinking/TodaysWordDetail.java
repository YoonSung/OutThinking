package jbedu.outthinking;

import java.io.InputStream;
import java.net.URL;

import jdedu.outthinking.util.Common;
import jdedu.outthinking.util.DAO;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TodaysWordDetail extends Activity implements OnClickListener{

	Button oneBtn, twoBtn;
	TextView oneTxt, twoTxt;
	ImageView oneImg, twoImg;
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
	    
	    oneImg = (ImageView) findViewById(R.id.TodayWordDtail_one_img);
	    twoImg = (ImageView) findViewById(R.id.TodayWordDtail_two_img);
	    
	    date = Common.getIntDateValue();
	    
	    db = new DAO(this);
	    db.open();
	    Cursor historyCursor = db.selectHistoryByDate(date);
	    historyCursor.moveToFirst();
	    
	    int wordInt1 = historyCursor.getInt(2);
	    String word1ImgUrl = historyCursor.getString(4);
	    System.out.println("ImgUrl1 : "+word1ImgUrl);//for test
	    if (word1ImgUrl != null)
	    	oneImg.setImageDrawable(LoadImageFromWebOperations(word1ImgUrl));
	    
	    historyCursor.moveToNext();
	    
	    int wordInt2 = historyCursor.getInt(2);
	    String word2ImgUrl = historyCursor.getString(4);
	    System.out.println("ImgUrl2 : "+word2ImgUrl);//for test
	    
	    if (word2ImgUrl != null)
	    	twoImg.setImageDrawable(LoadImageFromWebOperations(word2ImgUrl));
	    
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

	private Drawable LoadImageFromWebOperations(String strPhotoUrl) 
    {
        try {
        	InputStream is = (InputStream) new URL(strPhotoUrl).getContent();
        	Drawable drawable = Drawable.createFromStream(is, "src name");
        	return drawable;
        }catch (Exception e) {
        	System.out.println("       error occur      ");
        	e.printStackTrace();
        }
		return null;
    }
}
