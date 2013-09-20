package jbedu.outthinking;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class Intro extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    Display display=((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    int width =(int)display.getWidth();
	    
	    ScrollView sv=new ScrollView(this);
	    LinearLayout lay=new LinearLayout(this);
	    LinearLayout.LayoutParams lay_param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
	    lay.setGravity(Gravity.CENTER);
	    ImageView iv=new ImageView(this);
	    iv.setBackgroundResource(R.drawable.aboutoutthinking);
	    LinearLayout.LayoutParams iv_param=new LinearLayout.LayoutParams(width,width*3);
	    lay.addView(iv, iv_param);
	    sv.addView(lay,lay_param);
	    setContentView(sv);
	}

}
