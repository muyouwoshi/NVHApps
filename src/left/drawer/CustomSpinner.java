package left.drawer;

import com.example.mobileacquisition.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class CustomSpinner extends Spinner{

	public CustomSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch(event.getAction()){
		case MotionEvent.ACTION_UP:
			onClick();
		}
		return true;
	}

	private OnCostomListener onCostomListener;
	
	public void setOnCustomListener(OnCostomListener onCostomListener){
		this.onCostomListener = onCostomListener;
	}
	
	private void onClick() {
		if(onCostomListener != null && isEnabled()) {
			onCostomListener.onClick(this);
		}
	}
	
	public interface OnCostomListener{
		public void onClick(View v);
	}
}
