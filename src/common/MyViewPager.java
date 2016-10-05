package common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager implements Parcelable {
	
    private boolean isCanScroll = true;
    
	public MyViewPager(Context context) {
		super(context);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if(isCanScroll) {
			try{
				return super.onTouchEvent(arg0);
			}catch(IllegalArgumentException ex){
				ex.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean onInterceptTouchEvent(MotionEvent arg0) {  
		if(isCanScroll) {
			try{
				return super.onInterceptTouchEvent(arg0);
			}catch(IllegalArgumentException  ex){
				ex.printStackTrace();
			}
		}
		return false;
	}

	public void setCanScroll(boolean isCanScroll) {
	     this.isCanScroll = isCanScroll;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}