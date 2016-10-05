package left.drawer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import bottom.drawer.AddChannelViewPager;
import bottom.drawer.BottomOperate;
import bottom.drawer.CustomImageButton;

import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.SpinnerValues;
import com.example.mobileacquisition.ThemeLogic;
import com.example.mobileacquisition.ThemeLogic.ThemeChangeListener;
import com.example.mobileacquisition.WifiDialog;
import common.DataCollection;

import floating.window.FloatingService;
import floating.window.FloatingService.MyBinder;
//import android.graphics.Color;

@SuppressLint("SdCardPath")
public class MainFragment extends Fragment implements OnClickListener,ThemeChangeListener{
//ThemeLogic
	private static final String PATH = "/sdcard/data/Template/";
	private View view=null;
	private PreLeftFragment preLeftFragment=null;
	private PopWindow popWindow=null;
	private SkinPopWindow skinPopWindow=null;
	public int languageNumber = 0;
	private LanguagePopWindow languagePopWindow=null;
//	private LanguagePopWindow_CVU languagePopWindow=null;
	private FragmentManager manager;
//	private TextView ggg1; 
	int lanAndEn= 0;
	private ChannelSettingFragment channelSetting;
	private LinearLayout bottombutton;
	private SetPreviewFragment setPreviewFragment=null;
	private MainFragment mainFragment=null;
	private Button SaveTemplate,PreView,skin_bt,conn_bt,lan_bt,setup_bt;
	private BottomOperate bottomOperate=null;
	public WifiDialog wifiDialog;//=new WifiDialog();
	private int skinChangedId;
	//private Intent floatIntent;
	private SpinnerValues spinnerValues;
	private LeftExplandableListView expandableList;
	private TextView algorithm_spinner;
	private SaveTemplatePopWindow savePopWindow;
	private FloatingService floatingService;
	
	public TextView getAlgorithm_spinner() {
		return algorithm_spinner;
	}
	public void setAlgorithm_spinner(TextView algorithm_spinner) {
		this.algorithm_spinner = algorithm_spinner;
	}
	public ArrayList<Integer> channelCount= new ArrayList<Integer>();
	public ArrayList<RelativeLayout> LegendList = new ArrayList<RelativeLayout>();
	
	public ArrayList<RelativeLayout> getLegendList() {
		return LegendList;
	}
	public void setLegendList(ArrayList<RelativeLayout> legendList) {
		LegendList = legendList;
	}
	private AddChannelViewPager addChannelViewPager;
	
	public AddChannelViewPager getAddChannelViewPager() {
		return addChannelViewPager;
	}
	public void setAddChannelViewPager(AddChannelViewPager addChannelViewPager) {
		this.addChannelViewPager = addChannelViewPager;
	}
	public ArrayList<Integer> getChannelCount() {
		return channelCount;
	}
	public void setChannelCount(ArrayList<Integer> channelCount) {
		this.channelCount = channelCount;
	}
	private ArrayList<CustomImageButton> chanButtonList = new ArrayList<CustomImageButton>();

	public ArrayList<CustomImageButton> getChanButtonList() {
		return chanButtonList;
	}
	public void setChanButtonList(ArrayList<CustomImageButton> chanButtonList) {
		this.chanButtonList = chanButtonList;
	}
	private RelativeLayout legendLayout;
	
	public RelativeLayout getLegendLayout() {
		return legendLayout;
	}
	public void setLegendLayout(RelativeLayout legendLayout) {
		this.legendLayout = legendLayout;
	}
	
	public SpinnerValues getSpinnerValues() {
		return spinnerValues;
	}
	public void setSpinnerValues(SpinnerValues spinnerValues) {
		this.spinnerValues = spinnerValues;
	}
	private boolean isShowFloatWindow=false;

	public boolean isShowFloatWindow() {
		return isShowFloatWindow;
	}
	public void setShowFloatWindow(boolean isShowFloatWindow) {
		this.isShowFloatWindow = isShowFloatWindow;
	}
	public  void setBottomOperate(BottomOperate bottomOperate){//闂佽法鍠愰弸濠氬箯妞嬪簺浠泈ifi闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柡鍌樺�鐎氬綊鏌ㄩ悢鍛婄伄闁瑰嚖鎷�
		this.bottomOperate=bottomOperate;
	}
	public LeftExplandAdapter adapter;
	public  void setAdapter(LeftExplandAdapter adapter){//闂佽法鍠愰弸濠氬箯妞嬪簺浠泈ifi闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柡鍌樺�鐎氬綊鏌ㄩ悢鍛婄伄闁瑰嚖鎷�
		this.adapter=adapter;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.left_drawer, container,false);
		switch(ThemeLogic.themeType){
		case 1:
			view.getContext().setTheme(R.style.mode1);
			break;
		case 2:
			view.getContext().setTheme(R.style.mode2);
		}
		
		manager = getFragmentManager();
		mainFragment= (MainFragment) manager.findFragmentByTag("main");
		wifiDialog=new WifiDialog(getActivity());
		preLeftFragment = new PreLeftFragment();
		manager.beginTransaction().add(R.id.maintab, preLeftFragment,"preleft").commit(); 
		setPreviewFragment = new SetPreviewFragment();
		manager.beginTransaction().add(R.id.maintab, setPreviewFragment,"setPreview").hide(setPreviewFragment).commit(); 
		
		SaveTemplate=(Button)view.findViewById(R.id.SaveTemplate);
		SaveTemplate.setOnClickListener(this);
		PreView=(Button)view.findViewById(R.id.PreView);
		PreView.setOnClickListener(this);
		skin_bt=(Button)view.findViewById(R.id.skin);
		skin_bt.setOnClickListener(this);
		conn_bt=(Button)view.findViewById(R.id.conn);
		conn_bt.setOnClickListener(this);
		lan_bt=(Button)view.findViewById(R.id.lan);
		lan_bt.setOnClickListener(this);
		setup_bt=(Button)view.findViewById(R.id.setup);
		setup_bt.setOnClickListener(this);
		expandableList = (LeftExplandableListView)view.findViewById(R.id.expandableList);
		bottombutton = (LinearLayout) view.findViewById(R.id.bottombutton);
		skinChanged();
		channelSetting =  expandableList.getChannelSettingFragment();//findFragmentByTag("channelSetting");
		addPreviewAnalysis();
		
//		 ggg1 = (TextView)view.findViewById(R.id.ggg1);
		ThemeLogic.getInstance().addListener(this);
		skinPopWindow = new SkinPopWindow(getActivity());
		languagePopWindow =new LanguagePopWindow(getActivity(), mainFragment);
//		languagePopWindow =new LanguagePopWindow_CVU(getActivity(), mainFragment);

		popWindow = new PopWindow(getActivity());
		popWindow.setValues(wifiDialog,bottomOperate,manager);

		//skinPopWindow.InitListView(skin);
		return view;
	}
	
	public LeftExplandableListView getLeftExplandableListView(){
		return this.expandableList;
	}
	public void addPreviewAnalysis() {
		
		SignalFragment signalFragment = expandableList.getSignalFragment();
		signalFragment.readFromXml(getActivity());
	
		SplFragment splFragment = expandableList.getSplFragment();
		splFragment.readFromXml(getActivity());	
		OctFragment octFragment = expandableList.getOctFragment();
		octFragment.readFromXml(getActivity());	
		FftFragment fftFragment = expandableList.getFftFragment();
		fftFragment.readFromXml(getActivity());		
		AiFragment aiFragment = expandableList.getAiFragment();
		aiFragment.readFromXml(getActivity());			
		RpmFragment rpmFragment = expandableList.getRpmFragment();
		rpmFragment.readFromXml(getActivity());
		ColormapFragment colormapFragment = expandableList.getColormapFragment();
		colormapFragment.readFromXml(getActivity());	
	
		OrderFragment orderFragment = expandableList.getOrderFragment();
		orderFragment.readFromXml(getActivity());
		
		AcquisitionFragment acquisitionFragment = expandableList.getAcquisitionFragment();
		acquisitionFragment.readFromXml(getActivity());
		
		TriggerFragment triggerFragment = expandableList.getTriggerFragment();
		triggerFragment.readFromXml(getActivity());
		expandableList.getPreTriggerFragment().readFromXml(getActivity());
	}
	
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.SaveTemplate://闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柡鍌樺�鐎氳绋夐崫鍕澖闂佽法鍠愰弸濠氬箯闁垮瑔渚�煥閻斿憡鐏柟鍑ゆ嫹
			
			savePopWindow = new SaveTemplatePopWindow(getActivity(),this);
			View root = (ViewGroup) getActivity().findViewById(android.R.id.content);
			savePopWindow.SetTemplateXY(view.getWidth()+10,(int) v.getY());
			savePopWindow.showAtLocation(root, Gravity.NO_GRAVITY, view.getWidth()+10, (int) v.getY());

			break;
		case R.id.PreView://闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柡鍌樺�鐎氳锛愰崟顖涙櫢闁哄倶鍊栫�锟�
			manager.beginTransaction().hide(mainFragment).show(setPreviewFragment).commit(); 			
			setPreviewFragment.addPreviewChannel("hz_5D");
			setPreviewFragment.addPreviewAnalysis("hz_5D");
			setPreviewFragment.setViewVisble();
			setPreviewFragment.addPreviewDigital();
			stopFloat();
			setLeftHandleWidth();
			break;
		case R.id.skin:
			skinPopWindow.showPopupWindow(v);
			break;
		case R.id.conn:
			if(DataCollection.isRecording){
				Toast.makeText(getActivity().getApplicationContext(),
						 R.string.Collecting_data, Toast.LENGTH_SHORT)//璇疯緭鍏ュ瘑鐮�
						.show();
				return;
			}
			popWindow.showPopupWindow(v);
			break;
		case R.id.lan:
			languagePopWindow.showPopupWindow(v);
			break;
		case R.id.setup:
			FragmentManager manager = getActivity().getSupportFragmentManager();
			manager.beginTransaction().hide(mainFragment).show(preLeftFragment).commit();
			stopFloat();
			setLeftHandleWidth();
			break;
		default:
			break;
		}
	}
	
	public void saveTemplate(String fileName){
	
		String path = getActivity().getApplicationContext().getFilesDir().getAbsolutePath();
		int num = path.lastIndexOf("/");
		String str = path.substring(0,num+1);
		String oldPath = str + "shared_prefs/hz_5D.xml";

		String newPath = PATH + fileName+".xml";
		File newfile = new File(newPath);
		
		if(!newfile.getParentFile().exists()) {
       	   newfile.getParentFile().mkdirs();  
        }
		
		if(newfile.exists()){
			/*
			 * YDZD-1722 左侧拉-保存为实验模板-重复命名-系统自动加后缀生成
			 */
			Toast.makeText(getActivity(), fileName+getResources().getString(R.string.Template_name_exists), Toast.LENGTH_SHORT).show();
			return;
			/*int fileNum = 1;
		    while(new File(PATH+fileName+"("+fileNum+")"+".xml").exists()){
			   fileNum ++;
		    }
    		fileName = fileName+"("+fileNum+")";
    		newPath = PATH + fileName+".xml";*/
    	}		
		SaveTo(oldPath,newPath);

		Toast.makeText(getActivity(), fileName+getResources().getString(R.string.Template_Saved_Successfully), Toast.LENGTH_SHORT).show();
		if(savePopWindow!=null){
			savePopWindow.dismiss();
		}
	}
	
	public void setLeftHandleWidth(){
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(-1, -1);
		params.width=dip2px(255);//MainActivity.screen_width-
		params.height=-1;
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		((MainActivity)getActivity()).leftDrawer.setLayoutParams(params);
		params=new RelativeLayout.LayoutParams(-1, -1);
		params.width=0;
		params.height=-1;
		((MainActivity)getActivity()).leftHandle.setLayoutParams(params);	
		params = new RelativeLayout.LayoutParams(-1, -1);
		params.width = dip2px(250);
		params.height = -1;
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		((MainActivity)getActivity()).rightDrawer.setLayoutParams(params);
		params = new RelativeLayout.LayoutParams(-1, -1);
		params.width =0;
		params.height = -1;
		((MainActivity)getActivity()).rightHandle.setLayoutParams(params);
		params = new RelativeLayout.LayoutParams(-1, -1);
		params.width = -1;
		params.height = dip2px(120);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		((MainActivity)getActivity()).bottomDrawer.setLayoutParams(params);
		params = new RelativeLayout.LayoutParams(-1, -1);
		params.rightMargin = 0;
		params.leftMargin = 0;
		params.width = -1;
		params.height = 0;
		((MainActivity)getActivity()).bottomHandle.setLayoutParams(params);
		
	}
	public int dip2px(float dpValue){
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	public void SaveTo(String oldPath, String newPath) {
		try {   
           int byteread = 0;   
           File oldfile = new File(oldPath);
           File newfile = new File(newPath);
           if(!newfile.getParentFile().exists()) {
        	   newfile.getParentFile().mkdirs();
   
           }
           if (oldfile.exists()) { //闂佽法鍠嶉懠搴ｆ兜闁垮顏堕梺璺ㄥ枑閺嬪骞忛悜鑺ユ櫢闁哄倶鍊栫�褰掑籍閿燂拷  
               InputStream inStream = new FileInputStream(oldPath); //闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柡鍌樺�鐎氬綊宕㈤悢鍏兼櫢濞撴哎鍎抽妴瀣箯閿燂拷  
               FileOutputStream fs = new FileOutputStream(newPath);
               byte[] buffer = new byte[1024];
               while ( (byteread = inStream.read(buffer)) != -1) {
                   fs.write(buffer, 0, byteread);   
               }
               inStream.close();
               fs.close();
           }   
       }   
       catch (Exception e) {   
//    	   Toast.makeText(getActivity().getApplicationContext(), "闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柡鍌樺�鐎氱懓螣閿熺姵鏅搁柡鍌樺�鐎氬綊鏌ㄩ悢鍛婄伄闁归鍏橀弫鎾绘晸閿燂拷,Toast.LENGTH_SHORT).show();  
           e.printStackTrace(); 
       }
	}

	public void refreshActivity(Intent mainIntent){
		String getValues=getActivity().getIntent().getStringExtra("mainIntent");
		if(getValues!=null){
			if(getValues.equals("MobilePhoneEnter")){
				mainIntent.putExtra("mainIntent", "MobilePhoneEnter");
			}else if(getValues.equals("NoHardwareEnter")){
				mainIntent.putExtra("mainIntent", "NoHardwareEnter");
			}else{
				mainIntent.putExtra("mainIntent", "Enter");
			}
		}
		 /*
		  * 闁活煈鍣ｉ弫鎾诲棘閵堝棗顏堕梺璺ㄥ枎瑜般劍瀵煎▎鎰伓
		  */
		if(skinChangedId==0){
			mainIntent.putExtra("SkinIntent", "Skin0");
		}else if(skinChangedId==1){
			mainIntent.putExtra("SkinIntent", "Skin1");
		}
		mainIntent.putExtra("Intent", "LanguageChanged");
		startActivity(mainIntent);
		getActivity().finish();
		
	}
	public WifiDialog getWifiDialog(){
		return wifiDialog;
	}
	public int getLanguageNumber() {
		return languageNumber;
	}
	private void stopFloat(){
		if(isShowFloatWindow){
			//this.getActivity().stopService(new Intent(getActivity(), FloatingService.class));
			unBind();
		}
	}
	
	@SuppressLint({ "NewApi", "ResourceAsColor" })
	@Override
	public void onThemeChanged() {
		switch (ThemeLogic.themeType) {
		
		/** 1闂侀潧妫楅崐鎼佸储濞戙垹鍗抽悗娑櫳戦悡锟芥繛鎴炴尭椤兘銆傞敓浠嬫煥濞戞绛忕紒杈ㄥ浮閺佸秹鏁撻敓锟�*/
		case 1:
			view.getContext().setTheme(R.style.mode1);
			for(int g=0;g<mainFragment.getChanButtonList().size();g++){
				mainFragment.getChanButtonList().get(g).setBackgroundResource(R.drawable.ico_channel_noactive);
			}
			expandableList.setSelector(R.color.left_child_item);
			break;
		case 2:
			view.getContext().setTheme(R.style.mode2);
			for(int g=0;g<mainFragment.getChanButtonList().size();g++){
				mainFragment.getChanButtonList().get(g).setBackgroundResource(R.drawable.ico_channel_noactive1);
			}
			expandableList.setSelector(R.color.left_child_item1);
			break;
		}
		
		adapter.notifyDataSetChanged();
		skinChanged();
		/** 2.闂佸憡鍔曠粔浣冦亹娓氾拷瀹曪綁寮借濞硷綁鏌熼濠冨 */
		TypedArray typedArray = view.getContext().obtainStyledAttributes(R.styleable.myStyle);
		popWindow.setBackgroundDrawable(typedArray.getDrawable(R.styleable.myStyle_input_box2));
		languagePopWindow.setBackgroundDrawable(typedArray.getDrawable(R.styleable.myStyle_input_box2));
		skinPopWindow.setBackgroundDrawable(typedArray.getDrawable(R.styleable.myStyle_input_box2));
		((MainActivity)getActivity()).mainCustomTab.skinChanged(typedArray);
		if(channelSetting!=null){
			channelSetting.skinChanged(typedArray);
		}
		if(preLeftFragment!=null){
			preLeftFragment.skinChanged(typedArray);
		}
		RelativeLayout	leftDrawerLayout = (RelativeLayout) view.findViewById(R.id.leftDrawerLayout);
		leftDrawerLayout.setBackgroundColor(typedArray.getColor(R.styleable.myStyle_lef_draw,Color.YELLOW));
		expandableList.getAcquisitionFragment().skinChanged(typedArray);
		expandableList.getPreTriggerFragment().skinChanged(typedArray);
		expandableList.getTriggerFragment().skinChanged(typedArray);
		expandableList.getSignalFragment().skinChanged(typedArray);
		expandableList.getSplFragment().skinChanged(typedArray);
		expandableList.getOctFragment().skinChanged(typedArray);
		expandableList.getFftFragment().skinChanged(typedArray);
		expandableList.getAiFragment().skinChanged(typedArray);
		expandableList.getRpmFragment().skinChanged(typedArray);
		expandableList.getColormapFragment().skinChanged(typedArray);
		expandableList.getOrderFragment().skinChanged(typedArray);
		expandableList.getDisplayFragment().skinChanged(typedArray);
		setPreviewFragment.skinChanged(typedArray);
		((MainActivity)getActivity()).bottomOperate.skinChanged(typedArray);
		typedArray.recycle();
	}
	public void skinChanged(){
		TypedArray typedArray = view.getContext().obtainStyledAttributes(R.styleable.myStyle);
		PreView.setBackground(typedArray.getDrawable(R.styleable.myStyle_bt_blue_selector));
		SaveTemplate.setBackground(typedArray.getDrawable(R.styleable.myStyle_bt_gray_selector));
		SaveTemplate.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		PreView.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		skin_bt.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		lan_bt.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		conn_bt.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		setup_bt.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		bottombutton.setBackgroundColor(typedArray.getColor(R.styleable.myStyle_background1,Color.YELLOW));
		skin_bt.setBackground(typedArray.getDrawable(R.styleable.myStyle_btn_linealayout1));
		conn_bt.setBackground(typedArray.getDrawable(R.styleable.myStyle_btn_linealayout1));
		lan_bt.setBackground(typedArray.getDrawable(R.styleable.myStyle_btn_linealayout1));
		setup_bt.setBackground(typedArray.getDrawable(R.styleable.myStyle_btn_linealayout1));
		expandableList.setChildDivider(typedArray.getDrawable(R.styleable.myStyle_leftdivider));
		typedArray.recycle();
	}
	/** 重置语言 **/
    public void onReStart(){
    	
      
	    if(channelSetting!=null){
		    channelSetting.languageChanged();
	    }
	    AcquisitionFragment acquisition = expandableList.getAcquisitionFragment();
		acquisition.languageChanged();
		PreTriggerFragment pretrigger = expandableList.getPreTriggerFragment();
		pretrigger.languageChanged();
		TriggerFragment trigger = expandableList.getTriggerFragment();
		trigger.languageChanged();
		DisplayFragment display = expandableList.getDisplayFragment();
		display.languageChanged();
    	SignalFragment signalFragment = expandableList.getSignalFragment();
    	signalFragment.languageChanged();
    	SplFragment splFragment = expandableList.getSplFragment();
    	splFragment.languageChanged();
		OctFragment octFragment = expandableList.getOctFragment();
		octFragment.languageChanged();
		FftFragment fftFragment = expandableList.getFftFragment();
		fftFragment.languageChanged();
		AiFragment aiFragment = expandableList.getAiFragment();
		aiFragment.languageChanged();
		RpmFragment rpmFragment = expandableList.getRpmFragment();
		rpmFragment.languageChanged();
		ColormapFragment colormapFragment = expandableList.getColormapFragment();
		colormapFragment.languageChanged();
		OrderFragment order = expandableList.getOrderFragment();

		order.languageChanged();
		if(savePopWindow!=null){
			savePopWindow.languageChanged();
		}
		setPreviewFragment.languageChanged();
		preLeftFragment.languageChanged();
		((Button) view.findViewById(R.id.PreView)).setText(R.string.Preview);
		((Button) view.findViewById(R.id.SaveTemplate)).setText(R.string.SaveTemplate);
		((Button) view.findViewById(R.id.skin)).setText(R.string.Skin);
		((Button) view.findViewById(R.id.conn)).setText(R.string.Connection);
		((Button) view.findViewById(R.id.lan)).setText(R.string.Language);
		((Button) view.findViewById(R.id.setup)).setText(R.string.Set);
		((MainActivity)getActivity()).languageChanged();
/*		Switch OctSwitch = (Switch) octFragment.getView().findViewById(R.id.OCTSwitchButton);
		Switch ColorMapSwitch = (Switch) colormapFragment.getView().findViewById(R.id.ColorMapSwitchButton);
		Switch RPMSwitch = (Switch) rpmFragment.getView().findViewById(R.id.RPMSwitchButton);
		Switch AISwitch = (Switch) aiFragment.getView().findViewById(R.id.AISwitchButton);
		Switch FFTSwitch = (Switch) fftFragment.getView().findViewById(R.id.FFTSwitchButton);
		Switch SPLSwitch = (Switch) splFragment.getView().findViewById(R.id.SPLSwitchButton);
		Switch SignalSwitch = (Switch) signalFragment.getView().findViewById(R.id.SignalSwitchButton);
		Switch OrderSwitch = (Switch) order.getView().findViewById(R.id.OrderSwitchButton);
		  Toast.makeText(getActivity().getApplicationContext(),
				  OctSwitch.getTextOff().toString()+ ColorMapSwitch.getTextOff().toString()+
				  RPMSwitch.getTextOff().toString()+AISwitch.getTextOff().toString()+
				  FFTSwitch.getTextOff().toString()+SPLSwitch.getTextOff().toString()+
				  OrderSwitch.getTextOff().toString()+SignalSwitch.getTextOff().toString(), Toast.LENGTH_SHORT)
			     	.show();
		*/
	
     }
    
    public void startService(){
        Intent intent = new Intent(getActivity(),FloatingService.class);
        getActivity().bindService(intent, connnn, getActivity().BIND_AUTO_CREATE);
    }
	public  void unBind(){
		if(floatingService!=null){
	    	floatingService.getmFloatLayout().setVisibility(View.GONE);
		}
   }
	public void stopService(){
    	getActivity().unbindService(connnn);
	}
	ServiceConnection connnn = new ServiceConnection() {
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyBinder binder = (MyBinder)service;//FloatingService 
            floatingService = binder.getService();
            floatingService.getmFloatLayout().setVisibility(View.VISIBLE);
            floatingService.setMainFragment(mainFragment);
        }

    };
    public void show(){
    	if(floatingService!=null){
    		floatingService.getmFloatLayout().setVisibility(View.VISIBLE);
    	}
    }
	private View mFloatLayout;
	public View getmFloatLayout() {
		return mFloatLayout;
	}
	public void setmFloatLayout(View mFloatLayout) {
		this.mFloatLayout = mFloatLayout;
	}
	
}