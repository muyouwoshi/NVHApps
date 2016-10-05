package mode.autorange;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class PopupWindowView extends View {
	protected Paint channelDataPaint;
	private float top;
	private RangeSurfaceView rangeSurfaceView;
	public PopupWindowView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		channelDataPaint=new Paint();
		channelDataPaint.setColor(Color.WHITE);
		channelDataPaint.setTextSize(30);
		top = canvas.getHeight() / 4;
		if(rangeSurfaceView!=null){
			rangeSurfaceView.changeStaffView(rangeSurfaceView.getLevel());
		}
		// --------------------ªÊ÷∆±Í≥ﬂ-----------------------------------------------
		canvas.drawText("10v", 10, top, channelDataPaint);
		canvas.drawLine(0, top, 10, top, channelDataPaint);

		canvas.drawText("1v", 10, 2 * top, channelDataPaint);
		canvas.drawLine(0, 2 * top, 10, 2 * top, channelDataPaint);

		canvas.drawText("100mv", 10, 3 * top, channelDataPaint);
		canvas.drawLine(0, 3 * top, 10, 3 * top, channelDataPaint);
	}
	public float getPopStaffTop(){
		return top;
	}
	public void setRangeSurfaceView(RangeSurfaceView rangeSurfaceView){
		this.rangeSurfaceView=rangeSurfaceView;
	}
}
