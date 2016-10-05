package mode.calibration;

import java.io.Serializable;

import left.drawer.AnalogFragment.ViewHolder;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

import common.XclSingalTransfer;

import draw.map.FFT;
import draw.map.SPL;

public class CalibrationSpinner implements Serializable {
	
	private static final long serialVersionUID = -6166232491712081734L;
	private Activity context;
	private Button startButton;
	private Spinner hzSpinner;
	private Spinner dbSpinner;
	private Handler handler;
	private Handler dataHandler;
	private Handler textChangeHandler;
	private Handler fftPeakVauleHandler;
	private Handler splPeakVauleHandler;
	private boolean ifStartCalibration = false;
	private Thread thread = null;
	private CalibrationStartThread calibrationStartThread = null;
	private int ChannelIndex;
	private TextView sensitivityText;
	private FFT fft;
	private SPL spl;
	private RelativeLayout drawLayout;
	private TextView dbunit;
	private TextView sensiUnit;
	private String channelType;
	private int channelTypeId;
	//private TextView sensiText;
	public CalibrationSpinner() {
	}

	public void setContext(final Activity context) {
		this.context = context;
		handler = new Handler() {
			@Override
			public void handleMessage(Message age) {
				startButton = (Button) context
						.findViewById(R.id.start_bt);
				startButton.setTextColor(age.arg1);
				startButton.setText((String)age.obj);
				Button rejectButton = (Button) context
						.findViewById(R.id.reject_bt);
				rejectButton.setTextColor(age.arg2);
				if(age.what==2){
					hzSpinner.setClickable(true);
					dbSpinner.setClickable(true);
				}
			}
		};
		textChangeHandler = new Handler() {
			@Override
			public void handleMessage(Message age) {
				sensitivityText = (TextView) context
						.findViewById(R.id.sensi_editText);
				sensitivityText.setText(String.valueOf(age.obj));
				ChannelIndex = age.arg2;
			}
		};     
		dbunit = (TextView) context.findViewById(R.id.v_unit);
		sensiUnit=(TextView)context.findViewById(R.id.sensi_unit);
		startButton=(Button)context.findViewById(R.id.start_bt);
		sensitivityText=(TextView)context.findViewById(R.id.sensi_editText);
		initSurface(0);
	}

	public void setHzSpinner(Spinner hzSpinner) {
		this.hzSpinner = hzSpinner;
		// initHzSpinner();
		if(ThemeLogic.themeType==1){
			hzSpinner.setPopupBackgroundResource(R.drawable.input_box);
		}else{
			hzSpinner.setPopupBackgroundResource(R.drawable.input_box1);
		}
		ArrayAdapter<String> adapter=new CalibrationSpinnerAdapter(context,context.getResources().getStringArray(R.array.calibration_NoiseHzSpinner));  
        hzSpinner.setAdapter(adapter);
        hzSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(view != null && fft!= null){
					TextView tv = (TextView)view;
					fft.setFreqRang(Float.parseFloat(tv.getText().toString())*2);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void setDbSpinner(Spinner dbSpinner) {
		this.dbSpinner = dbSpinner;
		if(ThemeLogic.themeType==1){
			dbSpinner.setPopupBackgroundResource(R.drawable.input_box);
		}else{
			dbSpinner.setPopupBackgroundResource(R.drawable.input_box1);
		}
		ArrayAdapter<String> adapter=new CalibrationSpinnerAdapter(context,context.getResources().getStringArray(R.array.calibration_NoiseDBSpinner));   
		dbSpinner.setAdapter(adapter);
//		this.dbSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				if(view != null && fft!= null){
//					TextView tv = (TextView)view;
//					fft.setFreqRang(Float.parseFloat(tv.getText().toString())*2);
//				}
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
	}

	protected void noiseChannelTypeButton() {
		ArrayAdapter<String> adapter=new CalibrationSpinnerAdapter(context,context.getResources().getStringArray(R.array.calibration_NoiseHzSpinner));  
        hzSpinner.setAdapter(adapter);
        adapter=new CalibrationSpinnerAdapter(context,context.getResources().getStringArray(R.array.calibration_NoiseDBSpinner));   
        dbSpinner.setAdapter(adapter);
		dbunit.setText("dB");
		sensiUnit.setText("mV/Pa");
		sensitivityText.setText("");
		initSurface(0);
	}

	protected void vibrationChannelTypeButton() {
		ArrayAdapter<String> adapter=new CalibrationSpinnerAdapter(context,context.getResources().getStringArray(R.array.calibration_VibrationHzSpinner));  
        hzSpinner.setAdapter(adapter);
        adapter=new CalibrationSpinnerAdapter(context,context.getResources().getStringArray(R.array.calibration_VibrationGSpinner));    
        dbSpinner.setAdapter(adapter);
		dbunit.setText("g");
		sensiUnit.setText("mV/g");
		sensitivityText.setText("");
		initSurface(1);
	}
	private void initSurface(int index) {
		if (drawLayout == null) {
			drawLayout = (RelativeLayout) context
					.findViewById(R.id.bd_Layout_left);
		} else {
			drawLayout.removeAllViews();
		}
		switch (index) {
		case 0:
//			if (fft == null) {
				fft = new FFT(context,FFT.MODE_CALIBRATION);
//			}
				fft.setYLableState("dB");
				fft.setFreqRang(500);
			((RelativeLayout) context.findViewById(R.id.bd_Layout_left))
					.addView(fft, new RelativeLayout.LayoutParams(-1, -1));
			channelType="Noise";
			channelTypeId=0;
			break;
		case 1:
//			if (spl == null) {
				spl = new SPL(context,SPL.MODE_CALIBRATION);
//			}
				spl.setYLableState("g");

			((RelativeLayout) context.findViewById(R.id.bd_Layout_left))
					.addView(spl, new RelativeLayout.LayoutParams(-1, -1));
			channelType="Vibration";
			channelTypeId=1;
			break;
		}
		if (dataHandler == null) {
			dataHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.arg1) {
					case 0:
						if (fft.getFFTPeakVauleHandler() == null) {
							fft.setFFTPeakVauleHandler(getFFTPeakVauleHandler());
						}
						fft.invalidate();
						break;
					case 1:
						if (spl.getSPLPeakVauleHandler() == null) {
							spl.setSPLPeakVauleHandler(getSPLPeakVauleHandler());
						}
						spl.invalidate();
						break;
					}
				}
			};
		}
	}

	public void startCalibration() {
		if (thread != null && !thread.isAlive()) {
			ifStartCalibration = false;
			thread.interrupt();
			thread = null;
			calibrationStartThread.ifStop(false);
		}
		if (!ifStartCalibration) {
			ifStartCalibration = true;
			startButton.setText(R.string.end);
			((CalibrationActivity)context).isCalibrateStart=true;
			hzSpinner.setClickable(false);
			dbSpinner.setClickable(false);
			if (thread == null) {
				calibrationStartThread = new CalibrationStartThread();
				calibrationStartThread.ifStop(false);
				calibrationStartThread.setSelectedItem(channelType, channelTypeId);
				calibrationStartThread.setHandler(handler);
				calibrationStartThread.setDataHandler(dataHandler);
				calibrationStartThread.setTextChangeHandler(textChangeHandler);
				calibrationStartThread.setContext(context);
				calibrationStartThread.setThePaOfNoise(dbSpinner
						.getSelectedItemPosition());
				thread = new Thread(calibrationStartThread);
			}
			thread.start();
		} else {
			startButton.setText(R.string.calibration_st);
			((CalibrationActivity)context).isCalibrateStart=false;
			hzSpinner.setClickable(true);
			dbSpinner.setClickable(true);
			ifStartCalibration = false;
			thread.interrupt();
			thread = null;
			calibrationStartThread.ifStop(true);
			calibrationStartThread.getDataCollection().Stop();
		}
	}

	public void rejectCalabration() {
		try {
			if (thread != null) {
				if (thread.getState() == Thread.State.RUNNABLE) {
					Toast.makeText(context, R.string.reject_calibration, 	Toast.LENGTH_SHORT).show();
					return;
				}
				if (thread.getState() == Thread.State.TERMINATED) {
					Toast.makeText(context, R.string.reject_calibration, 	Toast.LENGTH_SHORT).show();
					return;
				}
				if (thread.getState() == Thread.State.TIMED_WAITING) {
					XclSingalTransfer xclSingalTransfer = XclSingalTransfer
							.getInstance();
					ViewHolder viewHolder = (ViewHolder) xclSingalTransfer
							.getValue(String.valueOf(ChannelIndex));
					sensitivityText.setText(viewHolder.Sensitivity.getText().toString());
					ifStartCalibration = true;
					startCalibration();
					Handler repeatCalibrationHandler = calibrationStartThread
							.getRepeatCalibration();
					Message repeatCalibrationMessage = repeatCalibrationHandler
							.obtainMessage();
					repeatCalibrationMessage.arg1 = ChannelIndex;
					repeatCalibrationHandler
							.sendMessage(repeatCalibrationMessage);
					ifStartCalibration = false;
					startCalibration();
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public Handler getFFTPeakVauleHandler() {
		if (fftPeakVauleHandler == null) {
			fftPeakVauleHandler = new Handler() {
				@Override
				public void handleMessage(Message age) {
					calibrationStartThread.setRealPeakValue((Double) age.obj);
				}
			};
		}
		return fftPeakVauleHandler;
	}

	public Handler getSPLPeakVauleHandler() {
		if (splPeakVauleHandler == null) {
			splPeakVauleHandler = new Handler() {
				@Override
				public void handleMessage(Message age) {
					calibrationStartThread.setRealPeakValue((Double) age.obj);
				}
			};
		}
		return splPeakVauleHandler;
	}
}
