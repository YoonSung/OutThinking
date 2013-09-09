package jbedu.outthinking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import jdedu.outthinking.util.DAO;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Splash extends Activity {

	DAO db;
	AsyncTask<Void, Void, Void> initialSet ;
	BufferedReader br;
	boolean isInitialSetComplete = false;
	WaitingHandler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		handler = new WaitingHandler(Splash.this);
		db = new DAO(this);
		db.open();
		Cursor card = db.selectAllCard();
		
		
		initialSet = new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}
			
			@Override
			protected Void doInBackground(Void... arg0) {

				br = new BufferedReader(new InputStreamReader
						(getResources().openRawResource(R.raw.wordlist)));
				try {
					String readLine = br.readLine();
					while(readLine !=null) {
						String[] splitLine = readLine.split(",");
						
						for (String word : splitLine) {
							System.out.println(word);
							db.insertCard(word);
						}
						
						readLine = br.readLine();
					}
				} catch(Exception e) {
					Log.e("SPLASH", "error occur");
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				handler.sendEmptyMessage(0);
			}
		};
		
		if (card.getCount() == 0)
			initialSet.execute(null, null, null);
		else
			handler.sendEmptyMessageDelayed(0, 500);
		
	}

	static class WaitingHandler extends Handler {

		private final Splash activity;

		WaitingHandler(Splash splash) {
			activity = splash;
		}

		@Override
		public void handleMessage(Message msg) {
			activity.startActivity(new Intent(activity, Main.class));
			activity.finish();
		}
	}

}
