package right.drawer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;

public class ChildOnClickListener implements
		ExpandableListView.OnChildClickListener {
	private Context context;
	private ListView filesListView;
	private ListView templateFilesListView;
	private Button exit_catalogButton;
	private ImageView mainLevelView;
	private MainActivity activity;
	private boolean ifOpenTemplateFilesList = false;
	private boolean ifHasEnterToTemplateFilesList = false;
	private FileListAdapter fileListAdapter;
	private RightExplandableListView rightExplandableListView;
	private List<String> filesList;
	private List<FileNode> fileNodeList;
	public static final String TEMPLATE_PATH = "/sdcard/data/Template";
	private GroupOnClickListener groupOnClickListener;

	public ChildOnClickListener(Context context) {
		activity = (MainActivity) context;
	}

	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
			int groupPosition, long id) {
		templateFilesListView = (ListView) activity
				.findViewById(R.id.templateFile_list);
		if (groupPosition == 0) {// 点击Template下Data菜单项
			filesListView = (ListView) activity.findViewById(R.id.file_list);
			filesListView.setVisibility(View.VISIBLE);
			templateFilesListView.setVisibility(View.GONE);
			ifOpenTemplateFilesList = false;
			activity.findViewById(R.id.right_expandableList).setVisibility(
					View.GONE);
			activity.findViewById(R.id.funcitonArea)
					.setVisibility(View.VISIBLE);
			activity.findViewById(R.id.load).setVisibility(View.GONE);
			activity.findViewById(R.id.speedEnterArea).setVisibility(View.GONE);
			activity.findViewById(R.id.catalogButtonArea).setVisibility(
					View.VISIBLE);
			setProjectFileList();
			setExitCatalog();
			ifHasEnterToTemplateFilesList = false;
			fileListAdapter.ifEnterTemplateDataFile = true;
		} else if (groupPosition == 1) {// 点击Template下“设备名”菜单项
			if (ifOpenTemplateFilesList == false) {
				templateFilesListView.setVisibility(View.VISIBLE);
				setTemplateProjectFileList();
				ifOpenTemplateFilesList = true;
				ifHasEnterToTemplateFilesList = true;
			} else {
				templateFilesListView.setVisibility(View.GONE);
				ifOpenTemplateFilesList = false;
			}
			fileListAdapter.ifEnterTemplateDataFile = false;
		}
		return false;
	}

	public void setProjectFileList() {
		File file = new File("/sdcard/data/", "pro");
		if (file.exists()) {
			// ------针对fileListAdapter获取的处理------------
			if (fileListAdapter == null) {
				if ((fileListAdapter = groupOnClickListener
						.getFileListAdapter()) == null) {
					//----将文件按时间顺序排列-----
					String[] filesArray = file.list();
					if (filesArray == null) {
						return;
					}
					List<String> filesListAsTime=Arrays.asList(filesArray);
					Collections.reverse(filesListAsTime);
					//----初始化文件------------
					filesList = new ArrayList<String>();
					fileNodeList = new ArrayList<FileNode>();
					fileListAdapter = new FileListAdapter(filesList, activity);
					// --------初始化文件列表的处理------------------
					for (int i = 0; i < filesListAsTime.size(); i++) {
						FileNode fileNode = new FileNode(-1, i, filesListAsTime.get(i));
						fileNode.setParentFileNode(null);
						fileNode.setIco(R.drawable.ico_folder_blue);
						fileNodeList.add(fileNode);
						filesList.add(filesListAsTime.get(i));
					}
					// --------加载adapter的处理--------
					fileListAdapter.setFilesList(filesList);
					fileListAdapter.setFileNodeList(fileNodeList);
				}
			}

			if (ifHasEnterToTemplateFilesList() == true) {
				if (filesList == null) {
					filesList = groupOnClickListener.getFileList();
				}
				if ((fileNodeList = groupOnClickListener.getFileNodeList()) == null) {
					fileNodeList = new ArrayList<FileNode>();
				}
				filesList.clear();
				fileNodeList.clear();
				String[] filesArray = file.list();

				if (filesArray == null) {
					return;
				}
				//----将文件按时间顺序排列-----
				List<String> filesListAsTime=Arrays.asList(filesArray);
				Collections.reverse(filesListAsTime);
				// --------初始化文件列表的处理------------------
				for (int i = 0; i <filesListAsTime.size(); i++) {
					FileNode fileNode = new FileNode(-1, i, filesListAsTime.get(i));
					fileNode.setParentFileNode(null);
					fileNode.setIco(R.drawable.ico_folder_blue);
					fileNodeList.add(fileNode);
					filesList.add(filesListAsTime.get(i));
				}
				// --------加载adapter的处理--------
				fileListAdapter.setFilesList(filesList);
				fileListAdapter.setFileNodeList(fileNodeList);
			}

			fileListAdapter.ifEnterToTemplate(false);
			filesListView.setAdapter(fileListAdapter);
			rightExplandableListView.setFileListAdapter(fileListAdapter);
		} else {
			file.mkdirs();
		}
		fileListAdapter.rightDownListener.ifEnterToProject = true;
		fileListAdapter.rightDownListener.ifEnterToTemplate = false;
	}

	public void setTemplateProjectFileList() {
		File file = new File("/sdcard/data/Template");
		if (file.exists()) {
			String[] filesArray = file.list();
			if (filesArray == null) {
				return;
			}
			//----将文件按时间顺序排列-----
			List<String> filesListAsTime=Arrays.asList(filesArray);
			Collections.reverse(filesListAsTime);
			// ------针对fileListAdapter获取的处理------------
			if (fileListAdapter == null) {
				if ((fileListAdapter = groupOnClickListener
						.getFileListAdapter()) == null) {
					filesList = new ArrayList<String>();
					fileListAdapter = new FileListAdapter(filesList, activity);
				}
			}
			// --------初始化文件列表的处理------------------
			if (filesList == null) {
				filesList = groupOnClickListener.getFileList();
			}
			filesList.clear();
			for (int i = 0; i < filesListAsTime.size(); i++) {
				filesList.add(filesListAsTime.get(i));
			}
			// --------加载adapter的处理--------
			fileListAdapter.setFilesList(filesList);
			fileListAdapter.ifEnterToTemplate(true);
			templateFilesListView.setAdapter(fileListAdapter);
		} else {
			file.mkdirs();
		}
		fileListAdapter.rightDownListener.ifEnterToProject = false;
		fileListAdapter.rightDownListener.ifEnterToTemplate = true;
	}

	private void setExitCatalog() {
		exit_catalogButton = (Button) activity
				.findViewById(R.id.exit_catalogButton);
		exit_catalogButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (fileListAdapter.getIfEntetToEditMode()) {
					return;
				}

				activity.findViewById(R.id.file_list).setVisibility(View.GONE);
				activity.findViewById(R.id.right_expandableList).setVisibility(
						View.VISIBLE);
				activity.findViewById(R.id.funcitonArea).setVisibility(
						View.GONE);
				activity.findViewById(R.id.speedEnterArea).setVisibility(
						View.VISIBLE);
				activity.findViewById(R.id.catalogButtonArea).setVisibility(
						View.GONE);
				activity.findViewById(R.id.templateFile_list).setVisibility(
						View.GONE);
				fileListAdapter.rightDownListener.ifEnterToProject = false;
				fileListAdapter.rightDownListener.ifEnterToTemplate = false;
			}
		});
	}

	public FileListAdapter getFileListAdapter() {
		return fileListAdapter;
	}

	public void setGroupOnClickListener(
			GroupOnClickListener groupOnClickListener) {
		this.groupOnClickListener = groupOnClickListener;
	}

	public void setRightExplandableListView(
			RightExplandableListView rightExplandableListView) {
		this.rightExplandableListView = rightExplandableListView;
	}

	public boolean ifHasEnterToTemplateFilesList() {
		return ifHasEnterToTemplateFilesList;
	}

	public void setIfHasEnterToTemplateFilesList(
			boolean ifHasEnterToTemplateFilesList) {
		this.ifHasEnterToTemplateFilesList = ifHasEnterToTemplateFilesList;
	}

	public List<String> getFileList() {
		return filesList;
	}

	public List<FileNode> getFileNodeList() {
		return fileNodeList;
	}
}
