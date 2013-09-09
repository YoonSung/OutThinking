package jbedu.outthinking;

import jdedu.outthinking.util.DAO;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddWord extends Activity implements OnClickListener{

	DAO db;
	Button btnAdd;
	
	//In PopUp Components
	EditText edtText; 
	DialogInterface popUpDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.addword);
	    
	    db = new DAO(this);
	    db.open();
	    
	    btnAdd = (Button) findViewById(R.id.AddWord_btnAdd);
	    btnAdd.setOnClickListener(this);
	    
	    db.close();
	}

	@Override
	public void onClick(View arg0) {
		inputPopup();
	}

	private void inputPopup() {
		edtText = new EditText(this);
		edtText.setLines(1);
		
		AlertDialog.Builder popUp = new AlertDialog.Builder(AddWord.this);
		popUp
		//.setIcon(R.drawable.ic_launcher) 
		.setTitle(getString(R.string.AddWord_btnAdd))
		.setView(edtText)
		.setPositiveButton("저장", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				db.insertCard(edtText.getText().toString());
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
