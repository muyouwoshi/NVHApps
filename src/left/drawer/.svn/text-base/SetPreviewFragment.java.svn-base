package left.drawer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;
import common.XclSingalTransfer;
public class SetPreviewFragment extends Fragment implements OnClickListener{
	private View view=null;
	private LayoutInflater inflater = null;
	private LinearLayout previewChannel;
	private LinearLayout previewAnalysis;
	private LinearLayout previewDigital;
	public List<String> checkedChannelResult = new ArrayList<String>();
	private Button return_bt;
	public TextView channel_setting,Analysis,speed_channel,Line;
	private LinearLayout chan_set_title;
	private View digital;
	public RelativeLayout title;
	private ArrayList<Integer> AlgorithmId= new ArrayList<Integer>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.set_preview, container,false);
		this.inflater = inflater;
		channel_setting=(TextView)view.findViewById(R.id.channel_setting);
		Line=(TextView)view.findViewById(R.id.Line);
		Analysis=(TextView)view.findViewById(R.id.Analysis);
		speed_channel = (TextView)view.findViewById(R.id.preview_digital_text);
		chan_set_title=(LinearLayout)view.findViewById(R.id.chan_set_title);
		title=(RelativeLayout)view.findViewById(R.id.title);
		return_bt=(Button)view.findViewById(R.id.return_bt);
		return_bt.setOnClickListener(this);
		previewChannel=(LinearLayout)view.findViewById(R.id.preview_channel);
		previewAnalysis=(LinearLayout)view.findViewById(R.id.preview_analysis);
		previewDigital = (LinearLayout)view.findViewById(R.id.preview_digital);
//		addPreviewChannel("hz_5D");
//		addPreviewAnalysis("hz_5D");
		//skinChanged();
		return view;
	}
	public void addPreviewChannel(String name) {
		int equipment_Num = getActivity().getSharedPreferences(
				"Equipment", 0).getInt("Equipment", 0);
		switch (equipment_Num){
		case 0:
			equipment_Num = 64;
		break;
		case 1:
			equipment_Num = 1;
			break;
		case 2:
			equipment_Num = 8;
			break;
		default:
			equipment_Num= 64;
			break;
		}
		SharedPreferences preference = getActivity().getSharedPreferences(name, 0);
		/*String s = preference.getString("checkChannelNum","0");
		Integer.valueOf(s);*/
		int ChannelNum = ((MainActivity)getActivity()).bottomOperate.addChannelViewPager.isActivated_ChanNum.size();
		if(ChannelNum>0){
			channel_setting.setVisibility(View.VISIBLE);
			chan_set_title.setVisibility(View.VISIBLE);
			Line.setVisibility(View.VISIBLE);
			((TextView)view.findViewById(R.id.channel_end_Line)).setVisibility(View.VISIBLE);
		}else{
			channel_setting.setVisibility(View.GONE);
			chan_set_title.setVisibility(View.GONE);
			Line.setVisibility(View.GONE);
			((TextView)view.findViewById(R.id.channel_end_Line)).setVisibility(View.GONE);
			
		}
		previewChannel.removeAllViews();
		for(int i = 0; i < equipment_Num; i ++) {
			int channelNum =Integer.parseInt( preference.getString("Channel"+i,""+-1));
			if(channelNum == i){
				View view = inflater.inflate(R.layout.set_preview_channel, previewChannel, false);
				((TextView)view.findViewById(R.id.chan_number)).setText(""+ (i+1) );
				((TextView)view.findViewById(R.id.Physical_Preview)).setText(preference.getString("Physical"+i, "none") );
				((TextView)view.findViewById(R.id.Transducer_Preview)).setText(preference.getString("Transducer"+i, "none") );
				((TextView)view.findViewById(R.id.Point_Preview)).setText(preference.getString("Point"+i, "none") );
				((TextView)view.findViewById(R.id.Direction_Preview)).setText(preference.getString("Direction"+i, "none") );
				((TextView)view.findViewById(R.id.Coupling_Preview)).setText(preference.getString("Coupling"+i, "none") );
				((TextView)view.findViewById(R.id.Sensitivity_Preview)).setText(preference.getString("Sensitivity"+i, "none") );
				((TextView)view.findViewById(R.id.Unit_Preview)).setText(preference.getString("Unit"+i, "none") );
				((TextView)view.findViewById(R.id.Range_Preview)).setText(preference.getString("Range"+i, "none") );
				previewChannel.addView( view, new LayoutParams(-1,-2));
			}
		}
		
	}
	
	public void setViewVisble() {
		int viewCount = previewChannel.getChildCount();
		
		XclSingalTransfer xclSingalTransfer = XclSingalTransfer.getInstance();
		
		if (xclSingalTransfer.containsKey("analogVisibleMap")) {
			Map<Integer, Integer> viewHolderMap = (Map<Integer, Integer>) xclSingalTransfer
					.getValue("analogVisibleMap");
			Iterator<Integer> it = viewHolderMap.keySet().iterator();
			
			while (it.hasNext()) {
				int viewHolderID = (int) it.next();
				for(int i =0 ; i< viewCount ;i++){
					View childView = previewChannel.getChildAt(i);
					setVisible(childView,viewHolderID, (int) viewHolderMap.get(viewHolderID));
				}
			}
		}
	}
	
	private void setVisible(View childView, int viewHolderID, int ifVisible) {
		// TODO Auto-generated method stub
		switch (viewHolderID) {
		case 0:
			chan_set_title.findViewById(R.id.Physical).setVisibility(ifVisible);
			childView.findViewById(R.id.Physical_Preview).setVisibility(ifVisible);
			break;
		case 1:
			chan_set_title.findViewById(R.id.Transducer).setVisibility(ifVisible);

			childView.findViewById(R.id.Transducer_Preview).setVisibility(ifVisible);
			break;
		case 2:
			chan_set_title.findViewById(R.id.Point).setVisibility(ifVisible);

			childView.findViewById(R.id.Point_Preview).setVisibility(ifVisible);
			break;
		case 3:
			chan_set_title.findViewById(R.id.Direction).setVisibility(ifVisible);

			childView.findViewById(R.id.Direction_Preview).setVisibility(ifVisible);
			break;
		case 4:
			chan_set_title.findViewById(R.id.Coupling).setVisibility(ifVisible);

			childView.findViewById(R.id.Coupling_Preview).setVisibility(ifVisible);
			break;
		case 5:
			chan_set_title.findViewById(R.id.Sensitivity).setVisibility(ifVisible);
			chan_set_title.findViewById(R.id.Sensitivity_unit).setVisibility(ifVisible);
			childView.findViewById(R.id.Sensitivity_Preview).setVisibility(ifVisible);
			childView.findViewById(R.id.Unit_Preview).setVisibility(ifVisible);
			break;
		case 6:
			chan_set_title.findViewById(R.id.Range).setVisibility(ifVisible);
			childView.findViewById(R.id.Range_Preview).setVisibility(ifVisible);
			break;
		}
	}
	public void addPreviewAnalysis(String name) {
		
		SharedPreferences preference = getActivity().getSharedPreferences(name, 0);
		String str = preference.getString("Signal", "close");
		previewAnalysis.removeAllViews();
		if(str.equals("open")) {
			AlgorithmId.add(1);
			View signal = inflater.inflate(R.layout.preview_signal, previewAnalysis, true);
//			String str1 =preference.getString("Signal_XAxis", "s");
			((TextView)signal.findViewById(R.id.SignalXAxis_textview)).setText(preference.getString("Signal_XAxis", "s"));
			((TextView)signal.findViewById(R.id.SignalYAxis_textview)).setText(preference.getString("Signal_YAxis", "Pa"));
		}else{
			removeAlgorithmId(1);
		}
		str = preference.getString("SPL", "close");
		if(str.equals("open")) {
			AlgorithmId.add(2);
			View spl = inflater.inflate(R.layout.preview_spl, previewAnalysis, true);
			
			((TextView)spl.findViewById(R.id.SPLFreqWeight_textview)).setText(preference.getString("SPL_FreqWeight", "None"));
			((TextView)spl.findViewById(R.id.SPLTimeWeight_textview)).setText(preference.getString("SPL_TimeWeight", "Fast"));
			((TextView)spl.findViewById(R.id.SPLXAxis_textview)).setText(preference.getString("SPL_XAxis", "s"));
			((TextView)spl.findViewById(R.id.SPLYAxis_textview)).setText(preference.getString("SPL_YAxis", "Pa"));
		}else{
			removeAlgorithmId(2);
		}
		
		str = preference.getString("OCT", "close");
		if(str.equals("open")) {
			AlgorithmId.add(3);
			View oct = inflater.inflate(R.layout.preview_oct, previewAnalysis, true);
			((TextView)oct.findViewById(R.id.OCTBandType_TextView)).setText(preference.getString("OCT_BandType", "1/1"));
			((TextView)oct.findViewById(R.id.OCTFreqWeight_TextView)).setText(preference.getString("OCT_FreqWeight", "None"));
			/*((TextView)oct.findViewById(R.id.OCTFreqRange_textview)).setText(preference.getString("OCT_FreqRange", "��1"));
			((TextView)oct.findViewById(R.id.OCTLower_textview)).setText(preference.getString("OCT_Lower", "��1"));
			((TextView)oct.findViewById(R.id.OCTUpper_TextView)).setText(preference.getString("OCT_Upper", "��1"));*/
			((TextView)oct.findViewById(R.id.OCTXAxis_textview)).setText(preference.getString("OCT_XAxis", "Hz"));
			((TextView)oct.findViewById(R.id.OCTYAxis_textview)).setText(preference.getString("OCT_YAxis", "Pa"));
		}else{
			removeAlgorithmId(3);
		}
		
		str = preference.getString("FFT", "close");
		if(str.equals("open")) {
			AlgorithmId.add(4);
			FftFragment fftFragment = (FftFragment) getActivity().getSupportFragmentManager().findFragmentByTag("fft");
			View fft = inflater.inflate(R.layout.preview_fft, previewAnalysis, true);
			((TextView)fft.findViewById(R.id.FFTFreqRange_textview)).setText(preference.getString("FFT_FreqRange", "0.3125")+" kHz");
			((TextView)fft.findViewById(R.id.FFTOverlap_textview)).setText(preference.getString("FFT_Overlap", "")+(preference.getString("FFT_Overlap", "").equals("")? "":" %"));
			((TextView)fft.findViewById(R.id.FFTFreqRes_textview)).setText(preference.getString("FFT_FreqRes", "")+(preference.getString("FFT_FreqRes", "").equals("")? "    Hz":" Hz"));
			((TextView)fft.findViewById(R.id.FFTFreqWeight_textview)).setText(preference.getString("FFT_FreqWeight", "None"));
			((TextView)fft.findViewById(R.id.FFTAveraging_textview)).setText(preference.getString("FFT_Averaging", ""));
			String windowStr = ""+fftFragment.getWindowType();
			((TextView)fft.findViewById(R.id.FFTWindow_textview)).setText(windowStr);
			((TextView)fft.findViewById(R.id.FFTXAxis_textview)).setText(preference.getString("FFT_XAxis", "Hz"));
			((TextView)fft.findViewById(R.id.FFTYAxis_textview)).setText(preference.getString("FFT_YAxis", "Pa"));
		}else{
			removeAlgorithmId(4);
		}
		
		str = preference.getString("AI", "close");
		if(str.equals("open")) {
			AlgorithmId.add(5);
			View ai = inflater.inflate(R.layout.preview_ai, previewAnalysis, true);
			((TextView)ai.findViewById(R.id.AIMode_textview)).setText(preference.getString("AI_Mode", "Open"));
			((TextView)ai.findViewById(R.id.AIXAxis_textview)).setText(preference.getString("AI_XAxis", "s"));
			((TextView)ai.findViewById(R.id.AIYAxis_textview)).setText(preference.getString("AI_YAxis", "Pa"));
		}else{
			removeAlgorithmId(5);
		}
		
		str = preference.getString("RPM", "close");
		if(str.equals("open")) {
			AlgorithmId.add(6);
			View rpm = inflater.inflate(R.layout.preview_rpm, previewAnalysis, true);
			String RPMDisplay_text = preference.getString("RPM_Display", "Digit");
			TextView RPMRPMXAxis = (TextView)rpm.findViewById(R.id.RPMXAxis_textview);
			TextView RPMRPMYAxis = (TextView)rpm.findViewById(R.id.RPMYAxis_textview);

			
			((TextView)rpm.findViewById(R.id.RPMDisplay_textview)).setText(preference.getString("RPM_Display", "Digit"));
			if(RPMDisplay_text.equals("Curve")){
				RPMRPMXAxis.setText(preference.getString("RPM_XAxis", "s"));
				RPMRPMYAxis.setText(preference.getString("RPM_YAxis", "Pa"));
			}else{
				RPMRPMXAxis.setVisibility(View.GONE);
				RPMRPMYAxis.setVisibility(View.GONE);
				((TextView)rpm.findViewById(R.id.RPMY_Axis)).setVisibility(View.GONE);
				((TextView)rpm.findViewById(R.id.RPMX_Axis)).setVisibility(View.GONE);
			}
			
		}else{
			removeAlgorithmId(6);
		}

		str = preference.getString("Waterfall", "close");
		if(str.equals("open")) {
			AlgorithmId.add(7);
			ColormapFragment colormapFragment = (ColormapFragment) getActivity().getSupportFragmentManager().findFragmentByTag("colormap");
			View colorMap = inflater.inflate(R.layout.preview_colormap, previewAnalysis, true);
			((TextView)colorMap.findViewById(R.id.colorMapFreqRange_textview)).setText(preference.getString("colorMapFreqRange", "0.4")+" kHz");
			((TextView)colorMap.findViewById(R.id.colorMapOverlap_textview)).setText(preference.getString("colorMapOverlap", "")+(preference.getString("FFT_Overlap", "").equals("")? "":" %"));
			((TextView)colorMap.findViewById(R.id.colorMapFreqRes_textview)).setText(preference.getString("colorMapFreqRes", "0.78")+(preference.getString("FFT_FreqRes", "").equals("")? "    Hz":" Hz"));
			((TextView)colorMap.findViewById(R.id.colorMapAveraging_textview)).setText(preference.getString("colorMapAveraging", ""));
			String windowStr = ""+colormapFragment.getWindowType();
			((TextView)colorMap.findViewById(R.id.colorMapWindow_textview)).setText(windowStr);
			((TextView)colorMap.findViewById(R.id.colorMapFreqWeight_textview)).setText(preference.getString("ColorMap_FreqWeight", "None"));
			((TextView)colorMap.findViewById(R.id.ColorMapXAxis_textview)).setText(preference.getString("ColorMap_XAxis", "s"));
			((TextView)colorMap.findViewById(R.id.ColorMapYAxis_textview)).setText(preference.getString("ColorMap_YAxis", "Hz"));
		}else{
			removeAlgorithmId(7);
		}

		str = preference.getString("Order", "close");
		if(str.equals("open")) {
			AlgorithmId.add(8);
			/*TextView order_title=new TextView(getActivity());
			LayoutParams lp=new LayoutParams(-1,-2);
			lp.height=dip2px(40);
			lp.leftMargin=dip2px(10);
			order_title.setLayoutParams(lp);
			order_title.setText(R.string.Order);*/
			View order_unit =  inflater.inflate(R.layout.preview_order_unit, previewAnalysis, false);
			((TextView)order_unit.findViewById(R.id.OrderXAxis_textview)).setText(preference.getString("Order_XAxis", "RPM"));
			((TextView)order_unit.findViewById(R.id.OrderYAxis_textview)).setText(preference.getString("Order_YAxis", "Pa"));
			previewAnalysis.addView(order_unit);
			ArrayList<String> checked_order_list = new ArrayList<String>();
			ArrayList<TextView> order_text_list = new ArrayList<TextView>();
			String order_names =preference.getString("order_names", "");
			String order_names_str[] = order_names.split(" ");  
			String order_values =preference.getString("order_values", "");
			String order_values_str[] = order_values.split(" "); 
			
			for(int i=0;i<order_values_str.length;i++){
				if(order_values_str[i].equals("true")){
					checked_order_list.add(order_names_str[i]);
				}
			}
			if(checked_order_list.size()==0){
				
			}else{
				int size=checked_order_list.size();
				if(checked_order_list.size()%5==0){
					size=checked_order_list.size()/5;
				}else{
					size=checked_order_list.size()/5+1;
				}
				for(int i=0;i<size;i++){
					View order =  inflater.inflate(R.layout.preview_order, previewAnalysis, false);
					order_text_list.add((TextView)order.findViewById(R.id.pre_one_order));
					order_text_list.add((TextView)order.findViewById(R.id.pre_two_order));
					order_text_list.add((TextView)order.findViewById(R.id.pre_three_order));
					order_text_list.add((TextView)order.findViewById(R.id.pre_four_order));
					order_text_list.add((TextView)order.findViewById(R.id.pre_five_order));
					previewAnalysis.addView(order,new LayoutParams(-1, -2));
				}
			}
			
			for(int i=0;i<checked_order_list.size();i++){
				order_text_list.get(i).setVisibility(View.VISIBLE);
				order_text_list.get(i).setText(checked_order_list.get(i)+" "+
						getActivity().getResources().getString(R.string.order_unit));
			}
			TextView line=new TextView(getActivity());
			line.setBackgroundColor(Color.parseColor("#465468"));
			LayoutParams lp=new LayoutParams(-1,-2);
			lp=new LayoutParams(-1,-2);
			lp.height=dip2px((float) 0.5);
			line.setLayoutParams(lp);
			previewAnalysis.addView(line);
		}else{
			removeAlgorithmId(8);
		}
		if(AlgorithmId.size()==0){
			Analysis.setVisibility(View.GONE);
		}else{
			Analysis.setVisibility(View.VISIBLE);
		}
		
		
		
	}
    private void removeAlgorithmId(int num){
    	for(int i=0;i<AlgorithmId.size();i++){
    		if(AlgorithmId.get(i)==num){
    			AlgorithmId.remove(i);
    		}
    	}
    }
	public void addPreviewDigital() {
		SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
		previewDigital.removeAllViews();
		digital = inflater.inflate(R.layout.preview_digital, previewDigital, true);
		LinearLayout chan1Layout = (LinearLayout) digital.findViewById(R.id.set_preview_chan1);
		LinearLayout chan2Layout = (LinearLayout) digital.findViewById(R.id.set_preview_chan2);
		boolean chan1Key = preference.getString("Digital_Chan1","false").equals("true");
		boolean chan2Key = preference.getString("Digital_Chan2","false").equals("true");
		if(chan1Key){
			speed_channel.setVisibility(View.VISIBLE);
			chan1Layout.setVisibility(View.VISIBLE);
			((TextView)digital.findViewById(R.id.threshold1_value_Preview)).setText(preference.getString("Ch1_Threshold","")+(preference.getString("Ch1_Threshold","").equals("")? "":" V"));
			((TextView)digital.findViewById(R.id.pluse1_value_Preview)).setText(preference.getString("Ch1_Pluse",""));

		}else{
			chan1Layout.setVisibility(View.GONE);
			
		}
		if(chan2Key){
			speed_channel.setVisibility(View.VISIBLE);
			chan2Layout.setVisibility(View.VISIBLE);
			((TextView)digital.findViewById(R.id.threshold2_value_Preview)).setText(preference.getString("Ch2_Threshold","")+(preference.getString("Ch2_Threshold","").equals("")? "":" V"));
			((TextView)digital.findViewById(R.id.pluse2_value_Preview)).setText(preference.getString("Ch2_Pluse",""));
		}else{
			chan2Layout.setVisibility(View.GONE);
		}
		((TextView)view.findViewById(R.id.line1)).setVisibility(View.VISIBLE);
		
		if(!(chan2Key||chan1Key)){
			speed_channel.setVisibility(View.GONE);
			((TextView)view.findViewById(R.id.line1)).setVisibility(View.GONE);
		}
		
		//�ɼ�
		((TextView)view.findViewById(R.id.sampling_rate_n_value)).setText(preference.getString("AcquiFreq_spinner_values", "12800")+" Hz");
		//((TextView)view.findViewById(R.id.resolution_value)).setText(preference.getString("FreqRes_spinner_values", "2")+" Hz");
		((TextView)view.findViewById(R.id.sampling_rate_v_value)).setText(preference.getString("sampling_rate_v_spinner_values", "12800")+" Hz");
		//����
		String triggerMode = preference.getString("Trigger_Mode", "Time");
		((TextView)view.findViewById(R.id.trigger_mode_value)).setText(triggerMode);
		if(!triggerMode.equals("Time")){ 
			((TextView)view.findViewById(R.id.trigger_lower_text)).setVisibility(View.VISIBLE);
			((TextView)view.findViewById(R.id.trigger_upper_text)).setVisibility(View.VISIBLE);
			((TextView)view.findViewById(R.id.trigger_step_text)).setVisibility(View.VISIBLE);
			((TextView)view.findViewById(R.id.trigger_lower_value)).setVisibility(View.VISIBLE);
			((TextView)view.findViewById(R.id.trigger_upper_value)).setVisibility(View.VISIBLE);
			((TextView)view.findViewById(R.id.trigger_step_value)).setVisibility(View.VISIBLE);
			((TextView)view.findViewById(R.id.trigger_type_text)).setVisibility(View.GONE);
			((TextView)view.findViewById(R.id.trigger_type_value)).setVisibility(View.GONE);
			((TextView)view.findViewById(R.id.trigger_duration_text)).setVisibility(View.GONE);
			((TextView)view.findViewById(R.id.trigger_duration_value)).setVisibility(View.GONE);
			((TextView)view.findViewById(R.id.trigger_lower_value)).setText(preference.getString("Trigger_Lower", "")+" RPM");
			((TextView)view.findViewById(R.id.trigger_upper_value)).setText(preference.getString("Trigger_Upper", "")+" RPM");
			((TextView)view.findViewById(R.id.trigger_step_value)).setText(preference.getString("Trigger_Step_Length", "")+" RPM");
		
		}else{
			((TextView)view.findViewById(R.id.trigger_lower_text)).setVisibility(View.GONE);
			((TextView)view.findViewById(R.id.trigger_upper_text)).setVisibility(View.GONE);
			((TextView)view.findViewById(R.id.trigger_step_text)).setVisibility(View.GONE);
			((TextView)view.findViewById(R.id.trigger_lower_value)).setVisibility(View.GONE);
			((TextView)view.findViewById(R.id.trigger_upper_value)).setVisibility(View.GONE);
			((TextView)view.findViewById(R.id.trigger_step_value)).setVisibility(View.GONE);
			((TextView)view.findViewById(R.id.trigger_type_text)).setVisibility(View.VISIBLE);
			((TextView)view.findViewById(R.id.trigger_type_value)).setVisibility(View.VISIBLE);
			((TextView)view.findViewById(R.id.trigger_type_value)).setText(preference.getString("Trigger_Type", "Start to Stop"));
			if(!preference.getString("Trigger_Type", "Start to Stop").equals("Start to Stop")){
				((TextView)view.findViewById(R.id.trigger_duration_text)).setVisibility(View.VISIBLE);
				((TextView)view.findViewById(R.id.trigger_duration_value)).setVisibility(View.VISIBLE);
				((TextView)view.findViewById(R.id.trigger_duration_value)).setText(preference.getString("Trigger_Duration", "")+(preference.getString("Trigger_Duration", "").equals("")? "":" s"));
			}else{
				((TextView)view.findViewById(R.id.trigger_duration_text)).setVisibility(View.GONE);
				((TextView)view.findViewById(R.id.trigger_duration_value)).setVisibility(View.GONE);
			}
		}
		((TextView)view.findViewById(R.id.PreTrigger_time_value)).setText(preference.getString("Pre_Trigger_Time", "")+(preference.getString("Pre_Trigger_Time", "").equals("")? "":" s"));
		
	}

	
	@Override
	public void onClick(View arg0) {
		((MainActivity)getActivity()).leftDrawerOpen();
		FragmentManager managerFather=getFragmentManager();
		MainFragment mainFragment= (MainFragment) managerFather.findFragmentByTag("main");
		SetPreviewFragment setPreviewFragment=(SetPreviewFragment)managerFather.findFragmentByTag("setPreview");
		managerFather.beginTransaction().hide(setPreviewFragment).show(mainFragment).commit();
		if(mainFragment.isShowFloatWindow()){
			mainFragment.show();
		}
	}
	public int dip2px(float dpValue){
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	public void languageChanged(){
		((TextView)view.findViewById(R.id.set_preview_title)).setText(R.string.set_preview);
		channel_setting.setText(R.string.channel_setting);
		speed_channel.setText(R.string.Tacho);
		if(digital!=null){
			((TextView)digital.findViewById(R.id.preview_chan1)).setText(R.string.chan1);
	 		((TextView)digital.findViewById(R.id.threshold1_text_Preview)).setText(R.string.Threshold);
	 		((TextView)digital.findViewById(R.id.pluse1_text_Preview)).setText(R.string.text_Pulse);
	 		((TextView)digital.findViewById(R.id.preview_chan2)).setText(R.string.chan2);
	 		((TextView)digital.findViewById(R.id.threshold2_text_Preview)).setText(R.string.Threshold);
	 		((TextView)digital.findViewById(R.id.pluse2_text_Preview)).setText(R.string.text_Pulse);
		}
		Analysis.setText(R.string.Analysis);
		((TextView)view.findViewById(R.id.Chan)).setText(R.string.Chan);
		((TextView)view.findViewById(R.id.Physical)).setText(R.string.Physical);
		((TextView)view.findViewById(R.id.Transducer)).setText(R.string.Transducer);
		((TextView)view.findViewById(R.id.Point)).setText(R.string.Point);
		((TextView)view.findViewById(R.id.Direction)).setText(R.string.Direction);
		((TextView)view.findViewById(R.id.Coupling)).setText(R.string.Coupling);
		((TextView)view.findViewById(R.id.Sensitivity)).setText(R.string.Sensitivity);
		((TextView)view.findViewById(R.id.Sensitivity_unit)).setText(R.string.Sensitivity_unit);
		((TextView)view.findViewById(R.id.Range)).setText(R.string.Range);
		((TextView)view.findViewById(R.id.preView_acquisition_text)).setText(R.string.AcquisitionSetting);
		((TextView)view.findViewById(R.id.sampling_rate_n_text)).setText(R.string.sampling_rate_n);
		((TextView)view.findViewById(R.id.sampling_rate_v_text)).setText(R.string.sampling_rate_v);
		//((TextView)view.findViewById(R.id.resolution_text)).setText(R.string.FreqResolution);
		((TextView)view.findViewById(R.id.preView_Trigger_text)).setText(R.string.Trigger);
		((TextView)view.findViewById(R.id.trigger_mode_text)).setText(R.string.Mode);
		((TextView)view.findViewById(R.id.trigger_lower_text)).setText(R.string.Lower);
		((TextView)view.findViewById(R.id.trigger_upper_text)).setText(R.string.Upper);
		((TextView)view.findViewById(R.id.trigger_step_text)).setText(R.string.Step);
		((TextView)view.findViewById(R.id.trigger_type_text)).setText(R.string.Type);
		((TextView)view.findViewById(R.id.trigger_duration_text)).setText(R.string.Duration);
		((TextView)view.findViewById(R.id.preView_PreTrigger_text)).setText(R.string.left_childItems_22);
		((TextView)view.findViewById(R.id.PreTrigger_time_text)).setText(R.string.Pre_Trigger_time);
	}
	public void skinChanged(TypedArray typedArray){
		title.setBackgroundColor(typedArray.getColor(R.styleable.myStyle_TabUpLayout,Color.YELLOW));
	}
}