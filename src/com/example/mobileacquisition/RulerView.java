package com.example.mobileacquisition;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageButton;

import common.MyViewPager;
import common.ScaleView;

import draw.map.ColorMap;

public class RulerView extends View implements OnTouchListener,OnGlobalLayoutListener{
	private Paint paint;
	private float moveX = 0;
	private float moveY = 0;
	private ScaleView view;
	private ImageButton rulerButton = null;
	private MyViewPager viewPager;
	private boolean showInMap = true;
	private int startX = 0;
	private int startY  = 0;

	public RulerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		paint = new Paint();
		paint.setColor(Color.rgb(115, 115, 115));
		
	}

	
	public RulerView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		paint = new Paint();
		paint.setColor(Color.rgb(115, 115, 115));
		this.setOnTouchListener(this);
	}
	
	public void init(MyViewPager viewPager,ScaleView view,ImageButton rulerButton){
		this.viewPager = viewPager;
		this.view = view;
		this.rulerButton = rulerButton;
		this.setVisible(View.VISIBLE);
		this.paint.setStrokeWidth(1);
		rulerButton.getViewTreeObserver().addOnGlobalLayoutListener(this);
		rulerButton.setOnTouchListener(this);
		setOnTouchListener(null);
		if(view!=null){
			view.setRuler(this);
		}
		
		moveY = -10;
		moveX = 0;
		
		this.invalidate();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		
		if(rulerButton == null){
			moveY = h - 20;
			startY = h - 20;
			moveX = 20;
		}
		
		this.invalidate();
	}

	
	
	public void init(MyViewPager viewPager,ScaleView view){
		this.viewPager = viewPager;
		this.view = view;
		this.paint.setStrokeWidth(2);
		setOnTouchListener(this);
		this.rulerButton = null;
		
		moveY = this.getHeight() - 20;
		startY = this.getHeight()-20;
		moveX = 20;
		startX = 20;
		
		this.setVisibility(View.VISIBLE);
		this.invalidate();
	}
	
	public void init(MyViewPager viewPager,ImageButton rulerButton){
		this.viewPager = viewPager;
		this.rulerButton = rulerButton;
		this.setVisible(View.GONE);
		setOnTouchListener(null);
		
		this.invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		if(this.rulerButton== null){
			canvas.drawLine((int)(moveX-20), (int)moveY, (int)(moveX+20), (int)moveY, paint);
			canvas.drawLine((int)(moveX),(int)( moveY-20), (int)(moveX), (int)(moveY+20), paint);

		}else{
			canvas.drawLine((int)(1 + moveX), 0, (int)(1 + moveX), canvas.getHeight(), paint);
			
		}
		drawValue(canvas);
		super.onDraw(canvas);
		
	}
	
	public void setVisible(int visibility){
		if(!showInMap) return;
		this.setVisibility(visibility);
		if(rulerButton != null) rulerButton.setVisibility(visibility);
		moveX = 0;
	}
	
	public void drawValue(Canvas canvas) {
		if(view != null){
			Paint LablePaint = new Paint();
//			LablePaint.setTextSize(24);
			LablePaint.setColor(Color.rgb(64, 64, 64));
//			LablePaint.setColor(Color.WHITE);
			LablePaint.setTextSize(50);
			if(rulerButton!= null){
				
				float xValue = view.getCursorValue(moveX).get(0);
				float yValue = view.getCursorValue(moveX).get(1);
				float yPosition = view.getCursorValue(moveX).get(2);

				canvas.drawText(("( " + xValue + " , " + yValue+" )"), moveX,yPosition, LablePaint);
			}
			else{
				float xValue = ((ColorMap)view).getCursorValue(moveX,moveY).get(0);
				float yValue = ((ColorMap)view).getCursorValue(moveX,moveY).get(1);
				float zValue=((ColorMap)view).getCursorValue(moveX,moveY).get(2);
				canvas.drawText(("( " + xValue + " , " + yValue+" , "+zValue+" )"), (moveX+20),moveY, LablePaint);
			}
		}
	}

	boolean onPoint = false;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		v.performClick();
		int vieww = view.getWidth();
		int viewh = view.getHeight();
//		boolean onPoint = false;
		if(rulerButton == null){
			if (event.getAction() == MotionEvent.ACTION_DOWN){

				if((event.getY() < moveY + 40) && ( event.getY()> moveY-40) && (event.getX() < moveX + 40) && ( event.getX()> moveX-40)){
					viewPager.setCanScroll(false);
					startX = (int) event.getX();
					startY = (int) event.getY();
					viewPager.setCanScroll(false);
					onPoint = true;
				}else {
					viewPager.setCanScroll(true);
					onPoint = false;
				}
			}	
			else if(event.getAction() == MotionEvent.ACTION_MOVE && onPoint){
				moveX += (int) event.getX() - startX;
				moveY += (int) event.getY() - startY;
				if (moveX  <= 0)
					moveX = 0;
				if (moveX >= vieww - 75)
					moveX = (int) (vieww - 75);
				if (moveY  <= 0)
					moveY = 0;
				if (moveY >= viewh-50)
					moveY = viewh-50;
				startX = (int) event.getX();
				startY = (int) event.getY();
				
				this.invalidate();
			}
			else if(event.getAction() == MotionEvent.ACTION_UP && onPoint){
				viewPager.setCanScroll(true);

			}
			return onPoint;
		}
		else{
		
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				viewPager.setCanScroll(false);
				startX = (int) event.getX();
	
				break;
			case MotionEvent.ACTION_MOVE:
				
				moveX += (int) event.getX() - startX;
	
				if (moveX  <= 0)
					moveX = 0;
				if (moveX >= vieww - 75)
					moveX = (int) (vieww - 75);
				
	
				rulerButton.layout((int)(25 + moveX), (int)rulerButton.getTop(),(int)( 25 + moveX + rulerButton.getWidth()), (int)rulerButton.getBottom());
				this.invalidate();
				break;
			case MotionEvent.ACTION_UP:
				viewPager.setCanScroll(true);
				break;
			}
			return true;
		}
	}


	@Override
	public void onGlobalLayout() {
		// TODO Auto-generated method stub
		if(rulerButton!= null && rulerButton.isShown()){
			rulerButton.layout((int)(25 + moveX), (int)rulerButton.getTop(),(int)( 25 + moveX + rulerButton.getWidth()), (int)rulerButton.getBottom());
		}
	}


	public void showInMap(boolean beShow) {
		// TODO Auto-generated method stub
		this.showInMap = beShow;
		if(showInMap) setVisible(View.VISIBLE);
		else setVisible(View.GONE);
	}


	public void setMoveX(float i) {
		// TODO Auto-generated method stub
		this.moveX = i;
		this.invalidate();
	}


	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasWindowFocus);
		if(rulerButton!= null && rulerButton.isShown()){
			rulerButton.layout((int)(25 + moveX), (int)rulerButton.getTop(),(int)( 25 + moveX + rulerButton.getWidth()), (int)rulerButton.getBottom());
		}
	}
	
	
}
	
