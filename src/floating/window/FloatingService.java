package floating.window;
import left.drawer.MainFragment;

import com.example.mobileacquisition.R;

import android.app.ActivityManager;
import android.app.Service;  
import android.content.Context;
import android.content.Intent;  
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.PixelFormat;  
import android.graphics.Rect;  
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.IBinder;  
import android.util.FloatMath;
import android.view.Gravity;  
import android.view.LayoutInflater;  
import android.view.MotionEvent;  
import android.view.View;  
import android.view.WindowManager;  
import android.view.View.OnTouchListener;  
import android.view.WindowManager.LayoutParams;  
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
  
/** 
 * 悬浮窗Service 该服务会在后台一直运行一个悬浮的透明的窗体。 
 *  
 * @author 
 *  
 */  
public class FloatingService extends Service implements OnTouchListener{  
    private int statusBarHeight;// 状态栏高度  
    private View mFloatLayout;// 透明窗体  
    public View getmFloatLayout() {
		return mFloatLayout;
	}
	public void setmFloatLayout(View mFloatLayout) {
		this.mFloatLayout = mFloatLayout;
	}
	private RelativeLayout float_window;
    private boolean viewAdded = false;// 透明窗体是否已经显示  
    private WindowManager windowManager;  
    private WindowManager.LayoutParams layoutParams;  
    private float bigCircleR=0;//大圆半径
	private float downR;//手指按下的点距离圆心的距离
	private float X_0,Y_0;//圆心坐标
	private float smallCircleR=50;//小圆半径
	private float[] temp= new float[] { 0f, 0f };
	private ImageButton float_center,float_top;
	private RelativeLayout table_layout;//表布局
	private ImageView panelimage,needle;
	//private boolean isShown=false;//悬浮窗显示的标记
	private int width,height;//获取悬浮窗的宽度和高度
	private long downTime,upTime;//手指按下和抬起的时间
	boolean isBig=false;//悬浮窗放大标记
	boolean isShownTable=false;//表显示标记
	private MainFragment mainFragment = null;
    public MainFragment getMainFragment() {
		return mainFragment;
	}
	public void setMainFragment(MainFragment mainFragment) {
		this.mainFragment = mainFragment;
	}
	@Override  
    public void onCreate() {  
        super.onCreate();  
        createFloatView();
    }  
    public void createFloatView(){
    	mFloatLayout = LayoutInflater.from(this).inflate(R.layout.floating_window, null);  
        windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);  
        layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,  
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.TYPE_SYSTEM_ERROR,  
                LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);  
         layoutParams.gravity = Gravity.RIGHT|Gravity.TOP; //悬浮窗开始在右下角显示    
         table_layout=(RelativeLayout)mFloatLayout.findViewById(R.id.table_layout);
         float_center=(ImageButton)mFloatLayout.findViewById(R.id.float_center);
         float_top=(ImageButton)mFloatLayout.findViewById(R.id.float_top);
         panelimage=(ImageView)mFloatLayout.findViewById(R.id.panelimage);
         needle=(ImageView)mFloatLayout.findViewById(R.id.needle);
         mFloatLayout.setOnTouchListener(this);     
    }
    float x ,y;
	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		// TODO Auto-generated method stub
		layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        switch (event.getAction()) {  
        case MotionEvent.ACTION_DOWN: // 按下事件，记录按下时手指在悬浮窗的XY坐标值  
            temp[0] = event.getX();  
            temp[1] = event.getY();  
            //downTime=System.currentTimeMillis();
            downTime=event.getDownTime();
            break;  
        case MotionEvent.ACTION_MOVE:  
            refreshView((int) (event.getRawX() - temp[0]), (int) (event  
                    .getRawY() - temp[1]));  
            break;  
		case MotionEvent.ACTION_UP:  	
			//upTime=System.currentTimeMillis();
			upTime=event.getEventTime();
			if(!isShownTable){
				smallCircleR=float_center.getHeight()/2-30;
			}else{
				smallCircleR=panelimage.getHeight()/2-30;
			}
			bigCircleR=mFloatLayout.getWidth()/2;
		    X_0=mFloatLayout.getX()+mFloatLayout.getWidth()/2;
		    Y_0=mFloatLayout.getY()+mFloatLayout.getHeight()/2;
	        x = temp[0] - X_0;
			y = temp[1] - Y_0;
			downR=FloatMath.sqrt(x * x + y * y);
			if(upTime-downTime<500&&(event.getX()-temp[0]==0||event.getY()-temp[1]==0)){//
		 		if(float_top.getVisibility()==View.GONE){
		 			float_top.setVisibility(View.VISIBLE);
		 		}else{
		 			 if(downR<=smallCircleR){
		 				 float_top.setVisibility(View.GONE);
		 			 }
		 		}
		 		if(float_top.getVisibility()==View.VISIBLE){
		 			onTouchListener();
		 		}
		 	}
		 	break;
        } 
        return false;  
	}
	private void onTouchListener(){
			if(downR>=smallCircleR&&downR<=bigCircleR){
				if(temp[1]-Y_0>0){
					if(temp[0]-X_0>0){
						changedSmall();
					}else{
						//changedBig();
						mainFragment.setShowFloatWindow(false);
						removeView();
						mainFragment.stopService();
					}
				}else{
					if(temp[1]/downR>1/2&&temp[1]/downR<Math.pow(2,-1)*Math.sqrt(3)){
						 if(!isShownTable){
						   isShownTable=true;
						   table_layout.setVisibility(View.VISIBLE);
						   FloatingTable FloatingTable= new FloatingTable();
						   FloatingTable.setView(mFloatLayout);
						   smallCircleR=panelimage.getHeight()/2-30;
						   float_center.setVisibility(View.GONE);
					   }else{
						   isShownTable=false;
						   table_layout.setVisibility(View.GONE);
						   float_center.setVisibility(View.VISIBLE);
						   smallCircleR=float_center.getHeight()/2-30;
					   }
					}else{
						if(temp[0]-X_0>0){
							changedSmall();
						}else{
							//changedBig();
							mainFragment.setShowFloatWindow(false);
							removeView();
							mainFragment.stopService();
						}
					}
				}
			  }
		}
 
    private void changedSmall(){
    	if(isBig){								   
			   float_center.setImageResource(R.drawable.xuanfu_bg01_en);
			   float_top.setImageResource(R.drawable.xuanfu_bg03_en);
			   panelimage.setImageResource(R.drawable.xuanfu_bg02);
			   needle.setImageResource(R.drawable.xuanfuz_zhizhen);
			   isBig=false;
			 //  smallCircleR=float_center.getHeight()/2-30;
		     //  bigCircleR=mFloatLayout.getWidth()/2;
		   }else{
			   float_center.setImageResource(R.drawable.xuanfu_bg01_en_x2);
			    float_top.setImageResource(R.drawable.xuanfu_bg03_en_x2);
			    panelimage.setImageResource(R.drawable.xuanfu_bg02_en_x2);
			    needle.setImageResource(R.drawable.xuanfuz_zhizhen_x2);
			    isBig=true;
		   }
    }
    private void changedBig(){
    	if(!isBig){
		    float_center.setImageResource(R.drawable.xuanfu_bg01_en_x2);
		    float_top.setImageResource(R.drawable.xuanfu_bg03_en_x2);
		    panelimage.setImageResource(R.drawable.xuanfu_bg02_en_x2);
		    needle.setImageResource(R.drawable.xuanfuz_zhizhen_x2);
		    isBig=true;
		  //  smallCircleR=float_center.getHeight()/2-30;
		 //   bigCircleR=mFloatLayout.getWidth()/2;
	   }
    }
    /** 
     * 刷新悬浮窗 
     *  
     * @param x 
     *            拖动后的X轴坐标 
     * @param y 
     *            拖动后的Y轴坐标 
     */  
    public void refreshView(int x, int y) {  
        //状态栏高度不能立即取，不然得到的值是0  
        if(statusBarHeight == 0){  
            View rootView  = mFloatLayout.getRootView();  
            Rect r = new Rect();  
            rootView.getWindowVisibleDisplayFrame(r);  
            statusBarHeight = r.top;  
        }  
          
        layoutParams.x = x;  
        // y轴减去状态栏的高度，因为状态栏不是用户可以绘制的区域，不然拖动的时候会有跳动  
        layoutParams.y = y - statusBarHeight;//STATUS_HEIGHT;  
        refresh();  
    }  
    /** 
     * 添加悬浮窗或者更新悬浮窗 如果悬浮窗还没添加则添加 如果已经添加则更新其位置 
     */  
    private void refresh() {  
        if (viewAdded) {  
            windowManager.updateViewLayout(mFloatLayout, layoutParams);  
        } else {  
            windowManager.addView(mFloatLayout, layoutParams);  
            viewAdded = true;  
        }  
    }  
	@Override  
    public void onStart(Intent intent, int startId) {  
        super.onStart(intent, startId);  
        refresh();  
    }  
  
    /** 
     * 关闭悬浮窗 
     */  
    public void removeView() {  
        if (viewAdded) {  
            windowManager.removeView(mFloatLayout);  
            viewAdded = false;  
        }  
    }  
  
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
        removeView();  
    }  
      
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return myBinder;
	} 
	public class MyBinder extends Binder{

		public FloatingService getService(){
		return FloatingService.this;
		}
		}

		private MyBinder myBinder = new MyBinder();
}  