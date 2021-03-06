package jdedu.outthinking.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jbedu.outthinking.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Common {
	private Context context;
	private SharedPreferences spf;
	private SharedPreferences.Editor editor;
	private static SimpleDateFormat dataFormat;
	public static String NAVER_SEARCH_KEY = "355b8b961f07c3eaa8ff527e0e8e09ef";
	public static int[] drawableResources128 = new int[]{
																							R.drawable.clover128,
																							R.drawable.diamonds128,
																							R.drawable.spades128,
																							R.drawable.heart128
																							}; 

	public static int[] drawableResources256 = new int[]{
		R.drawable.clover256,
		R.drawable.diamonds256,
		R.drawable.spades256,
		R.drawable.heart256
		};
	
	public Common(Context context) {
		this.context = context; 
		spf=PreferenceManager.getDefaultSharedPreferences(context);
		editor=spf.edit();
	}
	
	public void savePreference(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}
	
	public void savePreference(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public String readStringPreference(String key) {
		return spf.getString(key, null);
	}
	
	
	public boolean isFirstLogin(String key) {
		return spf.getBoolean(key, true);
	}
	
	public static int getIntDateValue() {
	    dataFormat = new SimpleDateFormat("MMdd", Locale.KOREA);
	    Date date = new Date();
	    int today = Integer.parseInt(dataFormat.format(date));
	    
	    return today;
	}
}
