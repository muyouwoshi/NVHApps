package mode.autorange;

import com.example.mobileacquisition.R;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;

public class RangeControlPopWindow extends PopupWindow implements OnTouchListener{
	private Activity context;
	private FragmentManager manager;
	private View view;
	private int firstTop;
	private int firstBottom;
	private int secondBottom;
	private int dy=0;
	private int top=0;
	private int lastY;
	private  ImageButton img;
	private int level=1;
	public RangeControlPopWindow(Activity context) {
		this.context = context;
		ViewGroup root = (ViewGroup) context.findViewById(android.R.id.content);
		view = LayoutInflater.from(context).inflate(
				R.layout.rangecontrol_autorange, root, false);
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		this.setContentView(view);
		this.setWidth(100);
		this.setHeight(550);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 使点击弹出框以外区域使消失消失
		// 鑱介崚閿嬫煀閻樿埖锟�
		this.update();

		img = (ImageButton) view
				.findViewById(R.id.rangeControlButton);
		final FrameLayout rangeControlLayout = (FrameLayout) view
				.findViewById(R.id.rangeControlLayout);
//		view.setOnTouchListener(this);
	}

	public void getTop(int firstTop,int firstBottom,int secondBottom) {
		this.firstTop = firstTop;
		this.firstBottom = firstBottom;
		this.secondBottom = secondBottom;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(lastY ==0){
			lastY = img.getTop();
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			 dy = (int) event.getRawY() - lastY;
			if(dy<0){//往上
				if(event.getRawY()>firstTop&&event.getRawY()<=firstBottom){//三档
					top=firstTop;
					level=2;
				}else if(event.getRawY()>firstBottom&&event.getRawY()<=secondBottom){//二档
					top=firstBottom;
					level=1;
				}
			}else if(dy>0){//往下
				if(event.getRawY()>=firstBottom&&event.getRawY()<secondBottom){//二档
					top=firstBottom;
					level=1;
				}else if(event.getRawY()>=secondBottom){//一档
					top=secondBottom;
					level=0;
				}
			}
			img.layout(img.getLeft(), top, img.getRight(), top+img.getHeight());
			img.invalidate();
			lastY = (int) event.getRawY();
			break;
		}
		return false;
	}
	public int getLevel(){
		return level;
	}
	public void setLevel(int level){
		switch(level){
		case 0:
			top=secondBottom;
			img.layout(img.getLeft(), top, img.getRight(), top+img.getHeight());
			img.invalidate();
			break;
		case 1:
			top=firstBottom;
			img.layout(img.getLeft(), top, img.getRight(), top+img.getHeight());
			img.invalidate();
			break;
		case 2:
			top=firstTop;
			img.layout(img.getLeft(), top, img.getRight(), top+img.getHeight());
			img.invalidate();
			break;
		}
	}
}
