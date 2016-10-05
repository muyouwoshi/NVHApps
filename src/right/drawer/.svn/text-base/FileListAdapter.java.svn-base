package right.drawer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import left.drawer.AcquisitionFragment;
import left.drawer.AiFragment;
import left.drawer.AnalogFragment;
import left.drawer.ChannelSettingFragment;
import left.drawer.ColormapFragment;
import left.drawer.DigitalFragment;
import left.drawer.FftFragment;
import left.drawer.MainFragment;
import left.drawer.OctFragment;
import left.drawer.OrderFragment;
import left.drawer.RpmFragment;
import left.drawer.SetPreviewFragment;
import left.drawer.SignalFragment;
import left.drawer.SplFragment;
import left.drawer.TriggerFragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;
import common.PullBookParser;

public class FileListAdapter extends BaseAdapter {
	private List<String> filesList = null;
	private MainActivity activity = null;
	private boolean ifEnterToRunCatalog = false;
	private boolean ifEnterToDataCatalog = false;
	private boolean ifEntetToEditMode = false;
	private boolean ifCheckAllFile = false;
	private boolean ifDeleteFile = false;
	private Button firstLevel_catalogButton;
	private String projectName;
	private String runName;
	// private FunctionButtonsListener functionButtonsListener;
	private ArrayList<String> fileNames = new ArrayList<String>();
	private File theEnterFolder = new File("/sdcard/data/pro/");
	private boolean ifCheckFileBean = false;
	private boolean initX = false;
	private boolean ifEnterToTemplate = false;
	private FileNode fileNode;
	private List<FileNode> fileNodeList = new ArrayList<FileNode>();
	private List<FileNode> checkedFileNodeList = new ArrayList<FileNode>();
	private float ico_initX = 0;
	private int newFoldPosition = -1;
	public static final String TEMPLATE_PATH = "/sdcard/data/Template";
	protected RightDownListener rightDownListener;
	protected boolean ifEnterTemplateDataFile=false;
	public FileListAdapter(List<String> filesList, MainActivity activity) {
		this.filesList = filesList;
		this.activity = activity;
		setProjectCatalog();
		rightDownListener=new RightDownListener(activity,this);
	}

	public void setFilesList(List<String> filesList) {
		this.filesList = filesList;
	}

	public List<String> getFilesList() {
		return filesList;
	}

	@Override
	public int getCount() {
		if (filesList == null) {
			return 0;
		}
		return filesList.size();
	}

	@Override
	public Object getItem(int position) {
		return filesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(activity).inflate(
					R.layout.right_filelist, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.fileName = ((TextView) convertView
					.findViewById(R.id.file_name));
			viewHolder.checkBox = ((CheckBox) convertView
					.findViewById(R.id.file_checkbox));
			viewHolder.ico = ((ImageView) convertView
					.findViewById(R.id.file_image));
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.ico.setLeft(15);
		viewHolder.fileName.setPadding(0, 15, 0, 15);
		viewHolder.ico.setPadding(0, 15, 0, 15);
		viewHolder.checkBox.setPadding(0, 15, 0, 15);
		if (ifEntetToEditMode) {
			viewHolder.checkBox.setVisibility(View.VISIBLE);
			addCheckedListener(viewHolder, position);
			viewHolder.checkBox.setChecked(fileNodeList.get(position)
					.ifChecked());
		} else {
			viewHolder.checkBox.setVisibility(View.GONE);
		}
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!ifEntetToEditMode) {
					if (!ifEnterToTemplate) {
						if (!ifEnterToRunCatalog) {
							setRunCatalog(filesList.get(position), position);
						} else {
							File dataFile = new File("sdcard/data/pro/"
									+ fileNodeList.get(position)
											.getPartFileName(
													filesList.get(position)));
							if (dataFile.isDirectory()) {// 文件夹点击事件
								ifEnterToDataCatalog = true;
								setDataCatalog(filesList.get(position),
										position);
							} else if (dataFile.isFile()) {// 文件点击事件
								String fileType = dataFile.getPath().substring(dataFile.getPath().lastIndexOf(".") + 1);
								if (fileType.equals( "pcm") || fileType .equals("wav")){
									activity.setReplayPath(dataFile.getPath());
									Toast.makeText(activity, "get the file", Toast.LENGTH_SHORT).show();
								}
							}
						}
					} else {
						MainFragment main = (MainFragment) activity
								.getSupportFragmentManager().findFragmentByTag(
										"main");
						String path = main.getActivity()
								.getApplicationContext().getFilesDir()
								.getAbsolutePath();
						int num = path.lastIndexOf("/");
						String str = path.substring(0, num + 1);
						String newPath = str+ "shared_prefs/hz_5D.xml";
						
						
						String oldPath = TEMPLATE_PATH
								+ "/"
								+ ((ViewHolder) arg0.getTag()).fileName
										.getText().toString();
				
						
						
//						main.SaveTo(oldPath, newPath);
						
						refresh(oldPath);			
						Toast.makeText(activity, R.string.loadTemplate, Toast.LENGTH_SHORT).show();
					
					}
				} else {
					FileNode fileNode = fileNodeList.get(position);
					if (!viewHolder.checkBox.isChecked()) {
						viewHolder.checkBox.setChecked(true);
						if (checkedFileNodeList.indexOf(fileNode) < 0) {
							checkedFileNodeList.add(fileNode);
						}
						ifCheckFileBean = true;
					} else {
						viewHolder.checkBox.setChecked(false);
						if (checkedFileNodeList.indexOf(fileNode) > -1) {
							checkedFileNodeList.remove(fileNode);
						}
						ifCheckFileBean = false;
					}
					if (fileNodeList.get(position).isExpand()) {
						setDataCatalog(
								viewHolder.fileName.getText().toString(),
								position);
					}
				}
			}

			

			
		});
		viewHolder.fileName.setText(filesList.get(position).toString());
		if (fileNodeList.size() > 0) {
			if (!initX) {
				ico_initX = viewHolder.ico.getX();
				initX = true;
			}
			viewHolder.ico
					.setImageResource(fileNodeList.get(position).getIco());
			/*
			 * viewHolder.ico.setX(ico_initX +
			 * fileNodeList.get(position).getLevel() * 30);
			 */
		} else if (ifEnterToTemplate) {
			viewHolder.ico.setImageResource(R.drawable.ico_file_blue);// doc

		} else {
			viewHolder.ico.setImageResource(R.drawable.ico_folder_blue);
		}
		convertView.setOnTouchListener(rightDownListener);
		return convertView;
	}
	
	private void refresh(String oldPath) {
		android.support.v4.app.FragmentManager manager = activity.getSupportFragmentManager();
		
		PullBookParser templateParser = new PullBookParser(oldPath);
		SharedPreferences preferences = activity.getSharedPreferences("hz_5D",0);
		
		SharedPreferences preference = activity.getSharedPreferences("Equipment", 0);
		int equipment_Num = preference.getInt("Equipment", 0);
		int Device_ChannelNum = 0;
		
		ChannelSettingFragment channelSettingFragment = (ChannelSettingFragment)manager.findFragmentByTag("channelSetting");
		AnalogFragment analogFragment = channelSettingFragment.getAnalog();
		
		if(analogFragment.getActivity() != null){
			analogFragment.clearOldChannel();
		}
		
		reLoadPreferences(templateParser,preferences);
		
		
		switch (equipment_Num) {
		case 0:// 无硬件
			Device_ChannelNum = 64;
			break;
		case 1:// 手机
			Device_ChannelNum = 1;
			break;
		case 2:// 硬件
			Device_ChannelNum = 8;
			break;
		}
		if(analogFragment!= null){
		
			if(analogFragment.lvt != null){
				analogFragment.lvt.removeAllViews();
				analogFragment.InitChannelList(Device_ChannelNum);
			}
			
		}
		
		DigitalFragment digital = channelSettingFragment.getDigital();
		if(digital.getView() == null) digital.readFromXml(activity);
		else digital.reset(activity);
		
		
		// TODO Auto-generated method stub
		SetPreviewFragment spf = (SetPreviewFragment) manager.findFragmentByTag("setPreview");
		spf.addPreviewChannel("hz_5D");
		spf.addPreviewAnalysis("hz_5D");
		spf.addPreviewDigital();
		
		SignalFragment signalFragment = (SignalFragment) manager.findFragmentByTag("signal");     
		signalFragment.readFromXml(activity);
		signalFragment.init();
		
		SplFragment splFragment = (SplFragment) manager.findFragmentByTag("spl");
		splFragment.readFromXml(activity);
		splFragment.init();
		
		OctFragment octFragment = (OctFragment) manager.findFragmentByTag("oct");
		octFragment.readFromXml(activity);
		octFragment.init();
		
		FftFragment fftFragment = (FftFragment) manager.findFragmentByTag("fft");
		fftFragment.readFromXml(activity);
		fftFragment.init();
		
		AiFragment aiFragment = (AiFragment) manager.findFragmentByTag("ai");
		aiFragment.readFromXml(activity);
		aiFragment.init();

		RpmFragment rpmFragment = (RpmFragment) manager.findFragmentByTag("rpm");
		rpmFragment.readFromXml(activity);
		rpmFragment.init();
		
		ColormapFragment colormapFragment = (ColormapFragment) manager.findFragmentByTag("colormap");
		colormapFragment.readFromXml(activity);
		colormapFragment.init();
		
		OrderFragment orderFragment = (OrderFragment) manager.findFragmentByTag("order");
		orderFragment.reset(activity);
		//		OrderFragment orderFragment = (OrderFragment) manager.findFragmentByTag("order");
		
		AcquisitionFragment acquisitionFragment = (AcquisitionFragment) manager.findFragmentByTag("acqui");		
		acquisitionFragment.reset(activity);
		
		TriggerFragment triggerFragment = (TriggerFragment) manager.findFragmentByTag("trig");
		triggerFragment.reset(activity);
	}

	private void reLoadPreferences(PullBookParser templateParser,SharedPreferences preferences) {
		SharedPreferences.Editor editor = preferences.edit();
		Map<String,String> map = templateParser.getValueMap();
		Set<String> keySet = map.keySet();
		
		for(String key: keySet){
			String value = map.get(key);
			editor.putString(key, value);
		}
		editor.commit();
	}

	private void setProjectCatalog() {
		firstLevel_catalogButton = (Button) activity
				.findViewById(R.id.firstLevel_catalogButton);
		firstLevel_catalogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				/*
				 * firstLevel_catalogButton.setBackgroundResource(R.drawable.
				 * right_secondproject_selector);
				 */
				if (ifEntetToEditMode == false) {
					newFoldPosition = -1;
					theEnterFolder = new File("/sdcard/data/pro/");
					if (theEnterFolder.exists() && theEnterFolder.isDirectory()) {
						String[] theEnterFolderArray = theEnterFolder.list();
						if(theEnterFolderArray==null){
							return;
						}
						filesList.clear();
						fileNodeList.clear();
						//----将文件按时间顺序排列-----
						List<String> filesListAsTime=Arrays.asList(theEnterFolderArray);
						Collections.reverse(filesListAsTime);
						//----初始化--------------
						for (int i = 0; i < filesListAsTime.size(); i++) {
							fileNode = new FileNode(-1, i,
									filesListAsTime.get(i));
							fileNode.setParentFileNode(null);
							fileNode.setIco(R.drawable.ico_folder_blue);
							fileNodeList.add(fileNode);
							filesList.add(filesListAsTime.get(i));
						}
					}
					ifEnterToRunCatalog = false;
					((Button) activity
							.findViewById(R.id.secondLevel_catalogButton))
							.setVisibility(View.GONE);
					/*
					 * ((ImageView) activity
					 * .findViewById(R.id.secondLevelView))
					 * .setVisibility(View.GONE); ((ImageView) activity
					 * .findViewById(R.id.firstLevel_imageBackground))
					 * .setVisibility(View.GONE);
					 */
					activity.findViewById(R.id.add).setVisibility(View.VISIBLE);
					activity.findViewById(R.id.funcitonArea).setVisibility(
							View.VISIBLE);
					activity.findViewById(R.id.load).setVisibility(View.GONE);
					activity.findViewById(R.id.speedEnterArea).setVisibility(
							View.GONE);

					notifyDataSetChanged();
					rightDownListener.ifEnterToProject=true;
					rightDownListener.ifEnterToTemplate=false;
				}
			}
		});
		// functionButtonsListener.setFileListAdapter(this);
	}

	private void setRunCatalog(final String projectName, int projectPosition) {
		try {
			newFoldPosition = -1;
			theEnterFolder = new File("/sdcard/data/pro/" + projectName);
			if (theEnterFolder.exists() && theEnterFolder.isDirectory()) {

				Button secondLevel_catalogButton = (Button) activity
						.findViewById(R.id.secondLevel_catalogButton);

				secondLevel_catalogButton.setVisibility(View.VISIBLE);
				firstLevel_catalogButton = (Button) activity
						.findViewById(R.id.firstLevel_catalogButton);
				firstLevel_catalogButton
						.setBackgroundResource(R.drawable.right_secondproject_selector);
				/*
				 * ((ImageView) activity
				 * .findViewById(R.id.firstLevel_imageBackground))
				 * .setVisibility(View.VISIBLE);
				 */
				activity.findViewById(R.id.funcitonArea).setVisibility(
						View.VISIBLE);
				activity.findViewById(R.id.add).setVisibility(View.GONE);
				activity.findViewById(R.id.load).setVisibility(View.VISIBLE);
				activity.findViewById(R.id.speedEnterArea).setVisibility(
						View.GONE);
				secondLevel_catalogButton.setText(projectName);
				secondLevel_catalogButton.setTextColor(Color.WHITE);
				secondLevel_catalogButton.setTextSize(15);
				String[] theEnterFolderArray = theEnterFolder.list();
				if(theEnterFolderArray==null){
					return;
				}
				
				filesList.clear();
				FileNode projectNode = fileNodeList.get(projectPosition);
				fileNodeList.clear();
//				//----将文件按时间顺序排列-----
//				List<String> filesListAsTime=Arrays.asList(theEnterFolderArray);
//				Collections.reverse(filesListAsTime);
				for (int i = 0; i < theEnterFolderArray.length; i++) {
					if(!ifEnterTemplateDataFile){
						if(theEnterFolderArray[i].contains("xml")){
							continue;
						}
					}
					fileNode = new FileNode(-1, i, theEnterFolderArray[i]);
					fileNode.setParentFileNode(projectNode);
					fileNode.setIco(R.drawable.ico_folder_blue);
					fileNodeList.add(fileNode);
					filesList.add(fileNode.getFileName());
				}
				this.projectName = projectName;
				ifEnterToRunCatalog = true;
				notifyDataSetChanged();
				// functionButtonsListener.setFileListAdapter(this);
				rightDownListener.ifEnterToProject=false;
				rightDownListener.ifEnterToTemplate=false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setDataCatalog(String runName, int position) {
		fileNode = fileNodeList.get(position);
		theEnterFolder = new File("/sdcard/data/pro/"
				+ fileNode.getPartFileName(runName));
		if (theEnterFolder.exists() && theEnterFolder.isDirectory()) {
			newFoldPosition = position;
			String[] theEnterFolderArray = theEnterFolder.list();
			if(theEnterFolderArray==null){
				return;
			}
			if (!ifEntetToEditMode) {
				if (!fileNode.isExpand()) {
					fileNode.setExpand(true);
					if (theEnterFolderArray == null
							|| theEnterFolderArray.length == 0) {
						return;
					}
					if (fileNode.getChildFileNodeLength() == 0) {
						List<FileNode> childFileNodesList = fileNode
								.getChildFileNode();
						for (int i = 0; i < theEnterFolderArray.length; i++) {
							if(!ifEnterTemplateDataFile){
								if(theEnterFolderArray[i].contains("xml")){
									continue;
								}
							}else{
								if(!theEnterFolderArray[i].contains("xml")){
									continue;
								}
							}
							FileNode childFileNode = new FileNode(position, i,
									theEnterFolderArray[i]);
							childFileNode.setParentFileNode(fileNode);

							File file = new File(
									"/sdcard/data/pro/"
											+ childFileNode
													.getPartFileName(theEnterFolderArray[i]));
							if (file.isDirectory()) {
								childFileNode
										.setIco(R.drawable.ico_folder_blue);// ico_folder_01
							} else if (file.isFile()) {
								childFileNode.setIco(R.drawable.ico_file_blue);// doc
							}
							childFileNodesList.add(childFileNode);
						}
						fileNode.setChildFileNode(childFileNodesList);
					}
					fileNode.openNodes(position, fileNodeList);
				} else {
					fileNode.setExpand(false);
					if (theEnterFolderArray == null
							|| theEnterFolderArray.length == 0) {
						return;
					}
					fileNode.closeNodes(fileNodeList);
				}

				Iterator<FileNode> it = fileNodeList.iterator();
				filesList.removeAll(filesList);
				while (it.hasNext()) {
					filesList.add(it.next().getFileName());
				}
			} else {
				fileNode.checkedNodes(ifCheckFileBean, checkedFileNodeList);
			}
			notifyDataSetChanged();
			rightDownListener.ifEnterToProject=false;
			rightDownListener.ifEnterToTemplate=false;
		} else {

		}
		this.runName = runName;
	}

	private void addCheckedListener(final ViewHolder viewHolder,
			final int position) {
		CheckBox checkBox = viewHolder.checkBox;
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				FileNode fileNode = fileNodeList.get(position);
				fileNode.setChecked(isChecked);
				if (checkedFileNodeList.indexOf(fileNode) < 0) {
					if (isChecked) {
						checkedFileNodeList.add(fileNode);
					}
				} else {
					if (!isChecked) {
						checkedFileNodeList.remove(fileNode);
					}
				}
				ifCheckFileBean = isChecked;
				if (fileNodeList.get(position).isExpand()) {
					setDataCatalog(viewHolder.fileName.getText().toString(),
							position);
				}
			}
		});
	}

	public void ifEntetToEditMode(boolean ifEntetToEditMode) {
		this.ifEntetToEditMode = ifEntetToEditMode;
	}

	public boolean getIfEntetToEditMode() {
		return ifEntetToEditMode;
	}

	public void ifCheckAllFile(boolean ifCheckAllFile) {
		this.ifCheckAllFile = ifCheckAllFile;
	}

	public boolean getIfCheckAllFile() {
		return ifCheckAllFile;
	}

	public void ifDeleteFile(boolean ifDeleteFile) {
		this.ifDeleteFile = ifDeleteFile;
	}

	public ArrayList<String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(ArrayList<String> fileNames) {
		this.fileNames = fileNames;
	}

	public String getTheEnterFolderName() {
		return theEnterFolder.getPath();
	}

	public List<FileNode> getFileNodeList() {
		return fileNodeList;
	}

	public void setFileNodeList(List<FileNode> fileNodeList) {
		this.fileNodeList = fileNodeList;
	}

	public List<FileNode> getCheckedFileNodeList() {
		return checkedFileNodeList;
	}

	public void setCheckedFileNodeList(List<FileNode> checkedFileNodeList) {
		this.checkedFileNodeList = checkedFileNodeList;
	}

	public String getProjectName() {
		return projectName;
	}

	public int getNewFoldPosition() {
		return newFoldPosition;
	}

	public void ifEnterToTemplate(boolean ifEnterToTemplate) {
		this.ifEnterToTemplate = ifEnterToTemplate;
	}
	public void setIfEnterToRunCatalog(boolean ifEnterToRunCatalog) {
		this.ifEnterToRunCatalog = ifEnterToRunCatalog;
	}
	public boolean ifEnterToRunCatalog() {
		return ifEnterToRunCatalog;
	}

	public static class ViewHolder {
		TextView fileName;
		CheckBox checkBox;
		ImageView ico;
	}
}
