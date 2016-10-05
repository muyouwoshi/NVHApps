package mode.calibration;

import java.util.HashMap;
import java.util.Iterator;

import com.example.mobileacquisition.R;

import left.drawer.AnalogFragment.ViewHolder;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.widget.EditText;
import common.DataCollection;
import common.XclSingalTransfer;

public class CalibrationStartThread implements Runnable {
	private String selectedItem;
	private Handler handler;
	private CalibrationDataCollection dataCollection;
	private Handler dataHandler;
	private Handler textChangeHandler;
	private Handler repeatCalibrationHandler;
	private Activity context;
	private String index;
	private boolean ifStop = false;
	private boolean startCalibration = false;
	private XclSingalTransfer xclSingalTransfer;
	private int channelType;
	private int thePaOfNoise = 0;
	private double realPeakValue = 0;
	private EditText edit;
	private Handler changeHolderText;
	private SharedPreferences.Editor editor;
	public void setSelectedItem(String selectedItem, int channelType) {
		this.selectedItem = selectedItem;
		this.channelType = channelType;
		changeViewHolderText();
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public void setDataHandler(Handler dataHandler) {
		this.dataHandler = dataHandler;
	}

	public void ifStop(boolean ifStop) {
		this.ifStop = ifStop;
	}

	public CalibrationDataCollection getDataCollection() {
		return dataCollection;
	}

	public void setContext(Activity context) {
		this.context = context;
		initTread();
	}

	public void setThePaOfNoise(int thePaOfNoise) {
		this.thePaOfNoise = thePaOfNoise;
	}

	public void setTextChangeHandler(Handler textChangeHandler) {
		this.textChangeHandler = textChangeHandler;
	}

	public void setRealPeakValue(double realPeakValue) {
		this.realPeakValue = realPeakValue;
	}

	public Handler getRepeatCalibration() {
		if (repeatCalibrationHandler == null) {
			repeatCalibrationHandler = new Handler() {
				@Override
				public void handleMessage(Message age) {
					index = String.valueOf(age.arg1);
				}
			};
		}
		return repeatCalibrationHandler;
	}

	private void initTread() {
		if(editor==null){
			SharedPreferences preference = context.getSharedPreferences("hz_5D", 0);
			editor=preference.edit();
		}
		xclSingalTransfer = XclSingalTransfer.getInstance();
		HashMap<String, Object> propertyMap = xclSingalTransfer.getMap();
		Iterator<String> it = propertyMap.keySet().iterator();
		while (it.hasNext()) {
			String index = it.next();
			if (index.matches("[0-9]+")) {
				this.index = index;
				break;
			}
		}
		// index = xclSingalTransfer.getFirstIndex();
	}

	private void changeViewHolderText() {
		changeHolderText = new Handler() {
			public void handleMessage(Message msg) {
				edit.setText(String.valueOf(msg.obj));
				editor.putString("Sensitivity" + edit.getTag(),String.valueOf(msg.obj));
				editor.commit();
			}
		};
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Message buttonColorMessage;
			Message textChangeMessage;
			HashMap<String, Object> propertyMap = xclSingalTransfer.getMap();
			Iterator<String> it = propertyMap.keySet().iterator();
			dataCollection = new CalibrationDataCollection(dataHandler);
			startCalibration = false;
			if (this.index == null || this.index.equals("")) {
				return;
			}
			while (it.hasNext()) {
				if (ifStop) {
					dataCollection.Stop();
					break;
				}
				buttonColorMessage = handler.obtainMessage();
				buttonColorMessage.arg1 = Color.RED;
				buttonColorMessage.arg2 = Color.GRAY;
				buttonColorMessage.obj = context.getResources().getString(R.string.end);// "����";
				handler.sendMessage(buttonColorMessage);

				String index = it.next();
				if (this.index.equals(index)) {
					startCalibration = true;
				}

				if (startCalibration) {
					if (index.matches("[0-9]+")) {
						ViewHolder viewholder = (ViewHolder) xclSingalTransfer.getValue(index);
						if (viewholder.Physical.getSelectedItem().toString().equals(selectedItem)) {
							dataCollection.setChannelType(channelType);
							dataCollection.Start(((CalibrationActivity) context).hardType);
							buttonColorMessage = handler.obtainMessage();
							buttonColorMessage.arg1 = Color.RED;
							buttonColorMessage.arg2 = Color.GRAY;
							buttonColorMessage.obj = String.format(context.getResources().getString(R.string.end),
									index);// "����";
							handler.sendMessage(buttonColorMessage);
							while (realPeakValue <= 0) {
							}
							Object resultData = 0;
							// ----�궨����-----
							if (channelType == 0) {
								switch (thePaOfNoise) {
								case 0:
									resultData = realPeakValue;
									break;
								case 1:
									resultData = realPeakValue / 10;
									break;
								case 2:
									resultData = realPeakValue / 31.7f;
									break;
								}
							} else if (channelType == 1) {
								resultData = realPeakValue;
							}
							// ----����----
							textChangeMessage = textChangeHandler.obtainMessage();
							textChangeMessage.obj = resultData;
							textChangeMessage.arg2 = Integer.valueOf(index);
							textChangeHandler.sendMessage(textChangeMessage);

							buttonColorMessage = handler.obtainMessage();
							buttonColorMessage.arg1 = Color.RED;
							buttonColorMessage.arg2 = Color.GREEN;
							buttonColorMessage.obj = String.format(context.getResources().getString(R.string.end),
									index);// "����";
							handler.sendMessage(buttonColorMessage);
							SystemClock.sleep(5000);
							edit = viewholder.Sensitivity;
							Message msg = changeHolderText.obtainMessage();
							msg.obj = resultData;
							changeHolderText.sendMessage(msg);
							dataCollection.Stop();
							realPeakValue = 0;
						}
					}
				}
			}
			buttonColorMessage = handler.obtainMessage();
			buttonColorMessage.arg1 = Color.GREEN;
			buttonColorMessage.arg2 = Color.GREEN;
			buttonColorMessage.obj = context.getResources().getString(R.string.calibration_st);// "��ʼ";
			buttonColorMessage.what = 2;
			handler.sendMessage(buttonColorMessage);
			((CalibrationActivity) context).isCalibrateStart = false;
		} catch (Exception e) {
			return;
		}
	}
}
