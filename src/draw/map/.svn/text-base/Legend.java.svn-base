package draw.map;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Legend extends View{
	public static Paint linePaint,textPaint;
	public static List<Paint> linePaintList=new ArrayList<Paint>();
	private ArrayList<Integer> channelList= new ArrayList<Integer>();
	private int j=0,m=0,n=0;
	public Legend(Context context,ArrayList<Integer> channelList) {
		super(context); 
		this.channelList=channelList;
//		linePaint = new Paint();
//		linePaint.setStrokeWidth((float) 5.0); //设置线宽  
		textPaint=new Paint();
		textPaint.setColor(Color.BLACK);
		//textPaint.setStrokeWidth((float) 5.0); 
		textPaint.setTextSize(18);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {	
		linePaintList.clear();
		for(int i=0;i<channelList.size();i++){
			double num=(channelList.get(i))%16;
			linePaint = new Paint();
			linePaint.setStrokeWidth((float) 5.0); //设置线宽  
			if(num==0){
				linePaint.setColor(Color.rgb(255, 255, 0));
				textPaint.setColor(Color.rgb(255, 255, 0));
			}else if(num==1){
				linePaint.setColor(Color.rgb(238, 118, 0));
				textPaint.setColor(Color.rgb(238, 118, 0));
			}else if(num==2){
				linePaint.setColor(Color.rgb(0, 191, 255));
				textPaint.setColor(Color.rgb(0, 191, 255));
	
			}else if(num==3){
				linePaint.setColor(Color.rgb(34, 139, 34));
				textPaint.setColor(Color.rgb(34, 139, 34));

			}else if(num==4){
				linePaint.setColor(Color.rgb(204, 0, 255));
				textPaint.setColor(Color.rgb(204, 0, 255));
			}else if(num==5){
				linePaint.setColor(Color.rgb(255, 0, 51));
				textPaint.setColor(Color.rgb(255, 0, 51));
			}else if(num==6){
				linePaint.setColor(Color.rgb(0, 51, 0));
				textPaint.setColor(Color.rgb(0, 51, 0));
			}else if(num==7){
				linePaint.setColor(Color.rgb(0, 0, 255));
				textPaint.setColor(Color.rgb(0, 0, 255));
			}else if(num==8){
				linePaint.setColor(Color.rgb(102, 0, 153));
				textPaint.setColor(Color.rgb(102, 0, 153));
			}else if(num==9){
				linePaint.setColor(Color.rgb(0, 102, 0));
				textPaint.setColor(Color.rgb(0, 102, 0));
			}else if(num==10){
				linePaint.setColor(Color.rgb(192, 192,192));
				textPaint.setColor(Color.rgb(192, 192,192));
			}else if(num==11){
				linePaint.setColor(Color.rgb(100, 140, 255));
				textPaint.setColor(Color.rgb(100, 140, 255));
			}else if(num==12){
				linePaint.setColor(Color.rgb(134, 50, 0));
				textPaint.setColor(Color.rgb(134, 50, 0));
			}else if(num==13){
				linePaint.setColor(Color.rgb(100, 150, 155));
				textPaint.setColor(Color.rgb(100, 150, 155));
			}else if(num==14){
				linePaint.setColor(Color.rgb(200, 170, 255));
				textPaint.setColor(Color.rgb(200, 170, 255));
			}else if(num==15){
				linePaint.setColor(Color.rgb(250, 0, 155));
				textPaint.setColor(Color.rgb(250, 0, 155));
			}
			if(i<=15){
				canvas.drawLine(20,i*24+22,60,i*24+22,linePaint);
				canvas.drawText("Channel"+channelList.get(i), 70,i*24+22, textPaint);
			}else if(i>15&&i<=31){
				canvas.drawLine(180,j*24+22,220,j*24+22,linePaint);
				canvas.drawText("Channel"+channelList.get(i), 230,j*24+22, textPaint);
				j++;
			}else if(i>31&&i<=47){
				canvas.drawLine(340,m*24+22,380,m*24+22,linePaint);
				canvas.drawText("Channel"+channelList.get(i), 390,m*24+22, textPaint);
				m++;
			}else{
				canvas.drawLine(500,n*24+22,540,n*24+22,linePaint);
				canvas.drawText("Channel"+channelList.get(i), 550,n*24+22, textPaint);
				n++;
			}		
			linePaintList.add(linePaint);
		}
		super.onDraw(canvas);
	}
}
