package mode.calibration;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;
import com.example.mobileacquisition.WifiDialog;
import com.example.mobileacquisition.ThemeLogic.ThemeChangeListener;

public class CalibrationActivity extends FragmentActivity implements OnClickListener,ThemeChangeListener{
	private CalibrationSpinner calibrationSpinner=null;
	private boolean isPlayBackState;
	protected boolean isCalibrateStart=false;
	private WifiDialog wifiDialog;//=new WifiDialog();return_bt
	private Button noise_bt,vibration_bt,start_bt,reject_bt,return_bt;
	public int hardType;
	private RelativeLayout title_Layout;
	private int[] checkedChannelIndexArray;
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		registerReceiver(wifiDialog.WifiBroadcast, filter);
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		unregisterReceiver(wifiDialog.WifiBroadcast);
		super.onStop();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// 隐去标题栏（应用程序的名字）
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐去状态栏部分(电池等图标和一切修饰部分)
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					             WindowManager.LayoutParams.FLAG_FULLSCREEN);
		switch(ThemeLogic.themeType){
		case 1:
			setTheme(R.style.mode1);
			break;
		case 2:
			setTheme(R.style.mode2);
		}
		setContentView(R.layout.calibration_activity);
		wifiDialog=new WifiDialog(this);
		calibrationSpinner=new CalibrationSpinner();
		calibrationSpinner.setContext(this);
		calibrationSpinner.setDbSpinner((Spinner)this.findViewById(R.id.db_bdSpinner));
		calibrationSpinner.setHzSpinner((Spinner)this.findViewById(R.id.hz_bdSpinner));
//		if(ThemeLogic.themeType==1){
//			
	
//			
//		}else{
//			
//			
//			
//		}
//		findViewById(R.id.return_bt).setOnClickListener(this);
//		findViewById(R.id.start_bt).setOnClickListener(this);
//		findViewById(R.id.reject_bt).setOnClickListener(this);
		return_bt=(Button)findViewById(R.id.return_bt);
		return_bt.setOnClickListener(this);
		start_bt=(Button)findViewById(R.id.start_bt);
		start_bt.setOnClickListener(this);
		reject_bt=(Button)findViewById(R.id.reject_bt);
		reject_bt.setOnClickListener(this);
		noise_bt = (Button)findViewById(R.id.noise_bt);
		noise_bt.setTextColor(Color.GREEN);
		//noise_bt.setAlpha(1);
		findViewById(R.id.noise_bt).setOnClickListener(this);
		vibration_bt = (Button)findViewById(R.id.vibration_bt);
		title_Layout = (RelativeLayout)findViewById(R.id.title_Layout);
		
		//vibration_bt.setTextColor(Color.parseColor("#FFFFFF"));
		//vibration_bt.setAlpha((float) 0.5);
		findViewById(R.id.vibration_bt).setOnClickListener(this);
		hardType=this.getIntent().getIntExtra("hardType", 0);
		checkedChannelIndexArray=this.getIntent().getIntArrayExtra("checkedChannelIndexArray");
		ThemeLogic.getInstance().addListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){	
		case R.id.return_bt:
			if(!isCalibrateStart){
				setResult(1);
				finish();
			}
			break;
		case R.id.start_bt:
			calibrationSpinner.startCalibration();
			break;
		case R.id.reject_bt:
			calibrationSpinner.rejectCalabration();
			break;
		case R.id.noise_bt:
			if(!isCalibrateStart){
				calibrationSpinner.noiseChannelTypeButton();
				noise_bt.setTextColor(Color.GREEN);
				if(ThemeLogic.themeType==1){
					vibration_bt.setTextColor(getResources().getColor(R.color.white4));
				}else{
					
					vibration_bt.setTextColor(getResources().getColor(R.color.black));
				}
				//noise_bt.setAlpha(1);
				//vibration_bt.setAlpha((float) 0.5);
			}
			break;
		case R.id.vibration_bt:
			if(!isCalibrateStart){
				calibrationSpinner.vibrationChannelTypeButton();
				vibration_bt.setTextColor(Color.GREEN);
				if(ThemeLogic.themeType==1){
					noise_bt.setTextColor(getResources().getColor(R.color.white4));
				}else{
					
					noise_bt.setTextColor(getResources().getColor(R.color.black));
				}
				//noise_bt.setAlpha((float) 0.5);
				//vibration_bt.setAlpha(1);
			}
			break;
		default:
			break;
		}
	}
	public Boolean getPlayBackState () {
		return isPlayBackState;
	}

	@SuppressLint("NewApi")
	@Override
	public void onThemeChanged() {
		switch (ThemeLogic.themeType) {

		/** 1閵嗗倸鍘涢弴瀛樻煀娑撳顣�閿涗緤绱掗敓锟� */
		case 1:
			setTheme(R.style.mode1);
			break;
		case 2:
			setTheme(R.style.mode2);
			break;
		}
		
		
		/** 2.閸愬秵褰侀崣鏍х潣閿燂拷? */
		TypedArray typedArray = obtainStyledAttributes(R.styleable.myStyle);
		title_Layout.setBackground(typedArray.getDrawable(R.styleable.myStyle_TabUpLayout));
		start_bt.setBackground(typedArray.getDrawable(R.styleable.myStyle_start_bt));
		reject_bt.setBackground(typedArray.getDrawable(R.styleable.myStyle_reject_bt));
		noise_bt.setBackground(typedArray.getDrawable(R.styleable.myStyle_noise_bt));
		return_bt.setBackground(typedArray.getDrawable(R.styleable.myStyle_back_button1));
		vibration_bt.setBackground(typedArray.getDrawable(R.styleable.myStyle_vibration_bt));
	//	noise_bt.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
	//	vibration_bt.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
        //主界面
		typedArray.recycle();
		
	}
	public int[] getCheckedChannelIndexArray(){
		return checkedChannelIndexArray;
	}
}
