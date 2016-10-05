package floating.window;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import com.example.mobileacquisition.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 定时刷新，顺时逆时转动的仪表盘
 * 
 * @author hjhrq1991
 * 
 */
public class FloatingTable{
	private ImageView needleView; // 指针图片
	private Timer timer; // 时间
	private float maxDegree = 0.0f;
	private float degree = 0.0f; // 记录指针旋转
	private RotateAnimation animation;
	private boolean flag = true;
	public void setView(View view) {
		// TODO Auto-generated method stub
		// 指针
		needleView = (ImageView) view.findViewById(R.id.needle);
		Timer timer = new Timer();
		timer.schedule(mTimerTask, 0, 3000);
	}
	// TimerTask时器
		private TimerTask mTimerTask = new TimerTask() {

			public void run() {
				Message message1 = new Message();
				message1.what = 1;
				thandler.sendMessage(message1);
			}

		};
		private Handler thandler = new Handler() {

			public void handleMessage(Message msg1) {

				// 产生随机数0-40
				Random random = new Random();
				int a =random.nextInt(241);
				// 设置最高值
				maxDegree = Float.parseFloat(String.valueOf(a));
				// 开始转动
				timer = new Timer();
				// 设置每10毫秒转动一下
				timer.schedule(new NeedleTask(), 0, 10);
				flag = true;
			}

		};
	private class NeedleTask extends TimerTask {
		@Override
		public void run() {
			if (degree <= maxDegree * (241 / 100.0f)) {
				handler.sendEmptyMessage(0);
			}
			if (degree > maxDegree * (241 / 100.0f) && flag == true) {
				handler2.sendEmptyMessage(0);
			}
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 设置仪表盘指针转动动画
			// 仪表盘最大是271度
			if (degree >= maxDegree * (241 / 100.0f)) {
				timer.cancel();
			}else{
			degree += 5.0f;
			animation = new RotateAnimation(degree, maxDegree * (241 / 100.0f),
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			}
			// 设置动画时间1秒
			animation.setDuration(10);
			animation.setFillAfter(true);
			//needleView.startAnimation(animation);
			flag = false;
		}
	};

	private Handler handler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) { // 设置仪表盘指针转动动画
			// 仪表盘最大是172度，这个是自己测出来的
			if (degree <= maxDegree * (241 / 100.0f)) {
				timer.cancel();
			}else{
			degree += -5.0f;
			animation = new RotateAnimation(degree, maxDegree * (241 / 100.0f),
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			}
			// 设置动画时间5毫秒
			animation.setDuration(10);
			animation.setFillAfter(true);
			//needleView.startAnimation(animation);
			flag = true;
		}
	};
}
