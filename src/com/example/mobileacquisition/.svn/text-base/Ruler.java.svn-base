package com.example.mobileacquisition;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import common.MyViewPager;
import common.ScaleView;

public class Ruler extends RelativeLayout implements OnTouchListener{
	public View rulerView;
	private ImageButton rulerButton;
	private MyViewPager viewPager;
	private ScaleView view;
	
	public Ruler(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public Ruler(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	public Ruler(Context context,int parentHeight, MyViewPager viewPager) {
		super(context);
		// TODO Auto-generated constructor stub
		this.viewPager = viewPager;
		
		rulerView = new RulerView(context);
		rulerView.setId(1000);
		LayoutParams viewParams = new LayoutParams(-1,-1);
		viewParams.leftMargin = 50;
		viewParams.height = parentHeight - 50;
		addView(rulerView, viewParams);
		
		rulerButton = new ImageButton(context);
		rulerButton.setBackgroundResource(R.drawable.ico_biaochi01);
		LayoutParams buttonParams = new LayoutParams(-2,-2);
		buttonParams.leftMargin = 25;
		buttonParams.addRule(RelativeLayout.BELOW, 1000);
		addView(rulerButton, buttonParams);
		rulerButton.setOnTouchListener(this);
		
//		this.setLayoutParams(new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.MATCH_PARENT));
		
	}
	
	public void setView(ScaleView view) {
		this.view = view;
	}

	int startX = 0, moveX = 0;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		v.performClick();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			viewPager.setCanScroll(false);
			startX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			moveX += (int) event.getX() - startX;
			rulerButton.layout(25 + moveX, rulerButton.getTop(), 25 + moveX + rulerButton.getWidth(), rulerButton.getBottom());
			rulerView.invalidate();
			break;
		case MotionEvent.ACTION_UP:
			viewPager.setCanScroll(true);
			break;
		}
		return true;
	}
	
	public void setHeight(int height) {
		LayoutParams viewParams = new LayoutParams(-1, -1);
		viewParams.leftMargin = 50;
		viewParams.height = height - 50;
		rulerView.setLayoutParams(viewParams);

		this.postInvalidate();

	}
	
	class RulerView extends View {

		private Paint paint;
		
		public RulerView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			paint = new Paint();
			paint.setColor(Color.rgb(115, 115, 115));
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
//			view.getBuffer();
			canvas.drawLine(1 + moveX, 0, 1 + moveX, canvas.getHeight(), paint);
			super.onDraw(canvas);
			drawValue(canvas);
		}
		
		public void drawValue(Canvas canvas) {
			if(view != null){
				Paint LablePaint = new Paint();
				LablePaint.setColor(Color.rgb(64, 64, 64));
				float xValue = view.getCursorValue(moveX).get(0);
				float yValue = view.getCursorValue(moveX).get(1);
				float yPosition = view.getCursorValue(moveX).get(2);

				canvas.drawText(("" + xValue + " , " + yValue), moveX,yPosition, LablePaint);
			}	
		}
	}
}
