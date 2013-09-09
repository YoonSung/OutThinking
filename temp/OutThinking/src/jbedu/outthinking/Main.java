package jbedu.outthinking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity implements OnClickListener{

	Button btnIntro, btnTodayWord, btnNote, btnAddWord;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    btnIntro = (Button) findViewById(R.id.Main_btn_intro);
	    btnIntro.setOnClickListener(this);
	    
	    btnTodayWord = (Button) findViewById(R.id.Main_btn_todayword);
	    btnTodayWord.setOnClickListener(this);
	    
	    btnNote = (Button) findViewById(R.id.Main_btn_note);
	    btnNote.setOnClickListener(this);
	    
	    btnAddWord = (Button) findViewById(R.id.Main_btn_addword);
	    btnAddWord.setOnClickListener(this);
	    
	}

	@Override
	public void onClick(View v) {
		
		Intent intent = null;
		
		switch (v.getId()) {
		case R.id.Main_btn_intro:
			intent = new Intent(this, Intro.class);
			break;
		case R.id.Main_btn_todayword:
			intent = new Intent(this, TodaysWord.class);
			break;
		case R.id.Main_btn_note:
			intent = new Intent(this, InventionNote.class);
			break;
		case R.id.Main_btn_addword:
			intent = new Intent(this, AddWord.class);
			break;
		}
		
		startActivity(intent);
	}

}
