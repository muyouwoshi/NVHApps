package mode.autorange;

import java.util.ArrayList;
import java.util.List;

import left.drawer.AnalogFragment;

import com.example.mobileacquisition.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageButton;

public class AutoRangeDataCollection {

	// private int channelCount;
	private RangeLayout rangeLayout;
	private List<Float> voltageDateList;
	private List<Integer> levelList;
	private List<Integer> channelList;
	private int i = 0;
	private Handler viewPageHandler;
	private Thread thread;
	private VoltageDataCollection voltageDataCollection;
	private int state;
	private ImageButton startCollection;
	private Activity context;
	private SharedPreferences preference;

	public AutoRangeDataCollection(Activity context) {
		voltageDateList = new ArrayList<Float>();
		levelList = new ArrayList<Integer>();
		voltageDataCollection = new VoltageDataCollection();
		this.context = context;
		preference = context.getSharedPreferences("hz_5D", 0);
	}

	public void stop() {
		state = 0;
		thread.interrupt();
	}

	public void start() {
		levelList.clear();
		voltageDateList.clear();
		for (int i = 0; i < channelList.size(); i++) {
			String level = preference.getString("Range"
					+ channelList.get(i).toString(), null);
			
			if("10V".equals(level)){
				levelList.add(0);
			} else if("1V".equals(level)){
				levelList.add(1);
			} else if("100mV".equals(level)){
				levelList.add(2);
			}
//			switch (level) {
//			case "10V":
//				levelList.add(0);
//				break;
//			case "1V":
//				levelList.add(1);
//				break;
//			case "100mV":
//				levelList.add(2);
//				break;
//			}
			voltageDateList.add(i, (float) (10 * (i + 1)));
		}
		thread = new Thread(voltageDataCollection);
		state = 1;
		thread.start();
	}
	public void setRangeLayout(RangeLayout rangeLayout) {
		this.rangeLayout = rangeLayout;
	}

	public List<Float> getVoltageDateList() {
		return voltageDateList;
	}

	public List<Integer> getLevelList() {
		return levelList;
	}

	public void setLevelList(List<Integer>levelList) {
		this.levelList=levelList;
	}
	public List<Integer> getChannelList() {
		return channelList;
	}
	public int getChannelSize(){
		return channelList.size();
	}
	public void setHandler(Handler viewPageHandler) {
		this.viewPageHandler = viewPageHandler;
	}

	public void setChannelList(List<Integer> channelList) {
		this.channelList = channelList;
	}

	class VoltageDataCollection implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (state == 1) {
				if (i < channelList.size()) {
					Message msg = viewPageHandler.obtainMessage();
		
					viewPageHandler.sendMessage(msg);
				} else {
					i = 0;
				}
				i++;
			}
		}

	}
}