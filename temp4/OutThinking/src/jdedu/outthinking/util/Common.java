package jdedu.outthinking.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Common {
	Context context;
	SharedPreferences spf;
	SharedPreferences.Editor editor;
	static SimpleDateFormat dataFormat;
	
	
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
