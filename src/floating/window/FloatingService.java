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
 * ������Service �÷�����ں�̨һֱ����һ��������͸���Ĵ��塣 
 *  
 * @author 
 *  
 */  
public class FloatingService extends Service implements OnTouchListener{  
    private int statusBarHeight;// ״̬���߶�  
    private View mFloatLayout;// ͸������  
    public View getmFloatLayout() {
		return mFloatLayout;
	}
	public void setmFloatLayout(View mFloatLayout) {
		this.mFloatLayout = mFloatLayout;
	}
	private RelativeLayout float_window;
    private boolean viewAdded = false;// ͸�������Ƿ��Ѿ���ʾ  
    private WindowManager windowManager;  
    private WindowManager.LayoutParams layoutParams;  
    private float bigCircleR=0;//��Բ�뾶
	private float downR;//��ָ���µĵ����Բ�ĵľ���
	private float X_0,Y_0;//Բ������
	private float smallCircleR=50;//СԲ�뾶
	private float[] temp= new float[] { 0f, 0f };
	private ImageButton float_center,float_top;
	private RelativeLayout table_layout;//����
	private ImageView panelimage,needle;
	//private boolean isShown=false;//��������ʾ�ı��
	private int width,height;//��ȡ�������Ŀ�Ⱥ͸߶�
	private long downTime,upTime;//��ָ���º�̧���ʱ��
	boolean isBig=false;//�������Ŵ���
	boolean isShownTable=false;//����ʾ���
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
         layoutParams.gravity = Gravity.RIGHT|Gravity.TOP; //��������ʼ�����½���ʾ    
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
        case MotionEvent.ACTION_DOWN: // �����¼�����¼����ʱ��ָ����������XY����ֵ  
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
     * ˢ�������� 
     *  
     * @param x 
     *            �϶����X������ 
     * @param y 
     *            �϶����Y������ 
     */  
    public void refreshView(int x, int y) {  
        //״̬���߶Ȳ�������ȡ����Ȼ�õ���ֵ��0  
        if(statusBarHeight == 0){  
            View rootView  = mFloatLayout.getRootView();  
            Rect r = new Rect();  
            rootView.getWindowVisibleDisplayFrame(r);  
            statusBarHeight = r.top;  
        }  
          
        layoutParams.x = x;  
        // y���ȥ״̬���ĸ߶ȣ���Ϊ״̬�������û����Ի��Ƶ����򣬲�Ȼ�϶���ʱ���������  
        layoutParams.y = y - statusBarHeight;//STATUS_HEIGHT;  
        refresh();  
    }  
    /** 
     * ������������߸��������� �����������û�������� ����Ѿ�����������λ�� 
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
     * �ر������� 
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