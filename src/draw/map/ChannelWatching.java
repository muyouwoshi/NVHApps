package draw.map;

import java.util.ArrayList;
import java.util.List;

import com.example.mobileacquisition.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import bottom.drawer.AddChannelViewPager;
import common.DataCollection;
import common.IAudioTrack;
import common.MyViewPager;
import common.ScaleView;
import  mode.drive.DriveModeActivity;
public class ChannelWatching extends ScaleView {
	private Activity context;
	private int[] checkedChannelIndexArray;
	private Paint circlePaint;
	private Paint textPaint;
	private SharedPreferences preferences;
	public ChannelWatching(Context context) {
		super(context);
		this.context = (Activity) context;
		circlePaint=new Paint();
		textPaint=new Paint();
		textPaint.setColor(Color.BLACK);
		textPaint.setTextSize(20);
		preferences= context.getSharedPreferences("hz_5D", 0);
		// TODO Auto-generated constructor stub
	}

	public ChannelWatching(Context context, MyViewPager viewPager) {
		super(context, viewPager);
		this.context = (Activity) context;
		circlePaint=new Paint();
		textPaint=new Paint();
		textPaint.setColor(Color.BLACK);
		textPaint.setTextSize(20);
		preferences= context.getSharedPreferences("hz_5D", 0);
		// TODO Auto-generated constructor stub
	}
	
	protected void drawYLable(Canvas canvas){
		
	}
	protected void drawXLable(Canvas canvas){
		
	}
	protected void drawYLine(Canvas canvas){
		if (CoordinatePaint == null) {
			CoordinatePaint = new Paint();
		}
		canvas.drawLine(left, 0, left, canvas.getHeight() - 50, CoordinatePaint);
	}
	protected void drawXLine(Canvas canvas){
		if (CoordinatePaint == null) {
			CoordinatePaint = new Paint();
		}
		canvas.drawLine(left, canvas.getHeight() - 50, canvas.getWidth(),
				canvas.getHeight() - 50, CoordinatePaint);
	}
	
	protected void setRate(MotionEvent event){
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		if (context.getClass().getSimpleName().equals("MainActivity")) {
//			if((checkedChannelIndexArray=((MainActivity)context).bottomOperate.addChannelViewPager.checkedChannelIndexArray)==null){
//				return;
//			}
//			
//		} else if (context.getClass().getSimpleName()
//				.equals("DriveModeActivity")) {
//			if((checkedChannelIndexArray=((DriveModeActivity)context).getAddChannelViewPager().checkedChannelIndexArray)==null){
//				return;
//			}
//		}
		if(checkedChannelIndexArray==null) return;
		int xSize = checkedChannelIndexArray.length / 8;//����
		int ySize = checkedChannelIndexArray.length % 8;//����
		int height = this.getHeight();
		int width = this.getWidth();
		int hol_index=0;//����
		int ver_index=0;//����
		for(int i=0;i<checkedChannelIndexArray.length;i++){
			if(checkedChannelIndexArray[i]==0){
				continue;
			}
			switch(checkedChannelIndexArray[i]){
			case 1://�����޵�ѹ״̬
				circlePaint.setColor(Color.GREEN);
				break;
			case 2://�͵�ѹ״̬
				circlePaint.setColor(Color.GRAY);
				break;
			case 3://���ѹ״̬
				circlePaint.setColor(Color.GREEN);
				break;
			case 4://�ߵ�ѹ״̬
				circlePaint.setColor(Color.RED);
				break;
			}
			canvas.drawCircle(width / 9 * ( ver_index+ 1),
					height / 9 * (hol_index + 1), 10, circlePaint);
			if (i < 10) {
				canvas.drawText(String.valueOf(i+1), width / 9
						* (ver_index + 1) - 5, height / 9 * (hol_index + 1)
						+ (height / 9 / 2), textPaint);
			} else {
				canvas.drawText(String.valueOf(i+1), width / 9
						* (ver_index + 1) - 10, height / 9 * (hol_index + 1)
						+ (height / 9 / 2), textPaint);
			}
			++ver_index;
			if(ver_index==8){
				ver_index=0;
				++hol_index;
			}
		}
		if(preferences.getString("Digital_Chan1", "false").equals("true")){
			canvas.drawCircle(width / 9 * ( ver_index+ 1),
					height / 9 * (hol_index + 1), 10, circlePaint);
			canvas.drawText("speed1", width / 9
					* (ver_index + 1) - 30, height / 9 * (hol_index + 1)
					+ (height / 9 / 2), textPaint);
			++ver_index;
			if(ver_index==8){
				ver_index=0;
				++hol_index;
			}
		}
		if(preferences.getString("Digital_Chan2", "false").equals("true")){
			canvas.drawCircle(width / 9 * ( ver_index+ 1),
					height / 9 * (hol_index + 1), 10, circlePaint);
			canvas.drawText("speed2", width / 9
					* (ver_index + 1) - 30, height / 9 * (hol_index + 1)
					+ (height / 9 / 2), textPaint);
			++ver_index;
			if(ver_index==8){
				ver_index=0;
				++hol_index;
			}
		}
	}

	public void setChannelNum() {

	}

	@Override
	public ArrayList<List<Float>> getResultBuffer() {
		// TODO Auto-generated method stub
		return null;
	}
}
