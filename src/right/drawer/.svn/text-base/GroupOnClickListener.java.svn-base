package right.drawer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;
import right.drawer.RightDrawerFragment;

public class GroupOnClickListener implements
		ExpandableListView.OnGroupClickListener {

	private MainActivity activity;
	private ListView filesListView;
	private Button exit_catalogButton;
	private ImageView mainLevelView;
	private Handler speedEnterHandler;
	private FileListAdapter fileListAdapter;
	private RightExplandableListView rightExplandableListView;
	private ChildOnClickListener childOnClickListener;
	private List<String> filesList;
	private List<FileNode> fileNodeList;

	public GroupOnClickListener(Context context) {
		activity = (MainActivity) context;
	}

	@Override
	public boolean onGroupClick(ExpandableListView arg0, View v,
			int groupPosition, long id) {
		if (groupPosition == 0) {
			if (filesListView == null) {
				filesListView = (ListView) activity
						.findViewById(R.id.file_list);
			}
			activity.findViewById(R.id.right_expandableList).setVisibility(
					View.GONE);
			filesListView.setVisibility(View.VISIBLE);
			activity.findViewById(R.id.funcitonArea)
					.setVisibility(View.VISIBLE);
			activity.findViewById(R.id.load).setVisibility(View.GONE);
			activity.findViewById(R.id.speedEnterArea).setVisibility(View.GONE);
			activity.findViewById(R.id.catalogButtonArea).setVisibility(
					View.VISIBLE);
			activity.findViewById(R.id.templateFile_list).setVisibility(
					View.GONE);
			setProjectFileList();
			setExitCatalog();
			childOnClickListener.setIfHasEnterToTemplateFilesList(false);
			fileListAdapter.ifEnterTemplateDataFile = false;
		} else if (groupPosition == 1) {
			activity.findViewById(R.id.templateFile_list).setVisibility(
					View.GONE);
		}
		return false;
	}

	public void setProjectFileList() {
		File file = new File("/sdcard/data/", "pro");
		if (file.exists()) {
			// ------针对fileListAdapter获取的处理------------
			if (fileListAdapter == null) {
				if ((fileListAdapter = childOnClickListener
						.getFileListAdapter()) == null) {
					String[] filesArray = file.list();
					if (filesArray == null) {
						return;
					}
					// ----将文件按时间顺序排列-----
					List<String> filesListAsTime = Arrays.asList(filesArray);
					Collections.reverse(filesListAsTime);
					// -----文件初始化----------
					filesList = new ArrayList<String>();
					fileNodeList = new ArrayList<FileNode>();
					fileListAdapter = new FileListAdapter(filesList, activity);
					// --------初始化文件列表的处理------------------
					for (int i = 0; i < filesListAsTime.size(); i++) {
						FileNode fileNode = new FileNode(-1, i,
								filesListAsTime.get(i));
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

			if (childOnClickListener.ifHasEnterToTemplateFilesList() == true) {
				if (filesList == null) {
					filesList = childOnClickListener.getFileList();
				}
				if ((fileNodeList = childOnClickListener.getFileNodeList()) == null) {
					fileNodeList = new ArrayList<FileNode>();
				}
				filesList.clear();
				fileNodeList.clear();
				String[] filesArray = file.list();

				if (filesArray == null) {
					return;
				}
				// ----将文件按时间顺序排列-----
				List<String> filesListAsTime = Arrays.asList(filesArray);
				Collections.reverse(filesListAsTime);
				// --------初始化文件列表的处理------------------
				for (int i = 0; i < filesListAsTime.size(); i++) {
					FileNode fileNode = new FileNode(-1, i,
							filesListAsTime.get(i));
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

	public Handler getSpeedEnterHandler() {
		if (speedEnterHandler == null) {
			speedEnterHandler = new Handler() {
				@Override
				public void handleMessage(Message age) {
					if (filesListView == null) {
						filesListView = (ListView) activity
								.findViewById(R.id.file_list);
					}
					String projectName = (String) age.obj;
					File file = new File("/sdcard/data/pro/" + projectName
							+ "/");
					if (file.exists()) {
						// ----更换界面----
						activity.findViewById(R.id.right_expandableList)
								.setVisibility(View.GONE);
						filesListView.setVisibility(View.VISIBLE);
						activity.findViewById(R.id.funcitonArea).setVisibility(
								View.VISIBLE);
						activity.findViewById(R.id.speedEnterArea)
								.setVisibility(View.GONE);
						activity.findViewById(R.id.catalogButtonArea)
								.setVisibility(View.VISIBLE);
						activity.findViewById(R.id.edit).setVisibility(
								View.VISIBLE);
						activity.findViewById(R.id.add)
								.setVisibility(View.GONE);
						activity.findViewById(R.id.templateFile_list)
								.setVisibility(View.GONE);

						Button secondLevel_catalogButton = (Button) activity
								.findViewById(R.id.secondLevel_catalogButton);
						secondLevel_catalogButton.setVisibility(View.VISIBLE);
						Button firstLevel_catalogButton = (Button) activity
								.findViewById(R.id.firstLevel_catalogButton);
						firstLevel_catalogButton
								.setBackgroundResource(R.drawable.right_secondproject_selector);
						// ((ImageView) activity
						// .findViewById(R.id.secondLevelView))
						// .setVisibility(View.VISIBLE);
						secondLevel_catalogButton.setText(projectName);
						secondLevel_catalogButton.setTextSize(13);
						// ---------更换数据------------
						String[] filesArray = file.list();
						if (fileListAdapter == null) {
							List<String> filesList = new ArrayList<String>();
							List<FileNode> fileNodeList = new ArrayList<FileNode>();
							if (filesArray != null) {
								// ----将文件按时间顺序排列-----
								List<String> filesListAsTime = Arrays
										.asList(filesArray);
								Collections.reverse(filesListAsTime);
								// ------初始化------------
								FileNode parentFileNode = new FileNode(-1,
										filesListAsTime.indexOf(projectName),
										projectName);
								parentFileNode.setParentFileNode(null);
								parentFileNode
										.setIco(R.drawable.ico_folder_blue);

								for (int i = 0; i < filesListAsTime.size(); i++) {
									if (filesListAsTime.get(i).contains("xml")) {
										continue;
									}
									FileNode fileNode = new FileNode(-1, i,
											filesListAsTime.get(i));
									fileNode.setParentFileNode(parentFileNode);
									fileNode.setIco(R.drawable.ico_folder_blue);
									fileNodeList.add(fileNode);
									filesList.add(filesListAsTime.get(i));
								}
							}
							fileListAdapter = new FileListAdapter(filesList,
									activity);
							fileListAdapter.setFileNodeList(fileNodeList);
							fileListAdapter.ifEnterToTemplate(false);
							fileListAdapter.setIfEnterToRunCatalog(true);
							filesListView.setAdapter(fileListAdapter);
						} else {
							List<String> filesList = fileListAdapter
									.getFilesList();
							List<FileNode> fileNodeList = fileListAdapter
									.getFileNodeList();
							fileNodeList.clear();
							filesList.clear();
							if (filesArray != null) {
								// ----将文件按时间顺序排列-----
								List<String> filesListAsTime = Arrays
										.asList(filesArray);
								Collections.reverse(filesListAsTime);
								// ------初始化------------
								FileNode parentFileNode = new FileNode(-1,
										filesListAsTime.indexOf(projectName),
										projectName);
								parentFileNode.setParentFileNode(null);
								parentFileNode
										.setIco(R.drawable.ico_folder_blue);
								for (int i = 0; i < filesListAsTime.size(); i++) {
									if (filesListAsTime.get(i).contains("xml")) {
										continue;
									}
									FileNode fileNode = new FileNode(-1, i,
											filesListAsTime.get(i));
									fileNode.setParentFileNode(parentFileNode);
									fileNode.setIco(R.drawable.ico_folder_blue);
									fileNodeList.add(fileNode);
									filesList.add(filesListAsTime.get(i));
								}
								fileListAdapter.setFilesList(filesList);
								fileListAdapter.setFileNodeList(fileNodeList);
								fileListAdapter.ifEnterToTemplate(false);
								fileListAdapter.setIfEnterToRunCatalog(true);
								filesListView.setAdapter(fileListAdapter);
							}
						}
						fileListAdapter.rightDownListener.ifEnterToProject = false;
						fileListAdapter.rightDownListener.ifEnterToTemplate = false;
						rightExplandableListView
								.setFileListAdapter(fileListAdapter);
					} else {
						Toast.makeText(activity, projectName + "工程已被删除，无法进入",
								Toast.LENGTH_SHORT).show();
					}
					setExitCatalog();
				}
			};
		}
		return speedEnterHandler;
	}

	public FileListAdapter getFileListAdapter() {
		return fileListAdapter;
	}

	public void setRightExplandableListView(
			RightExplandableListView rightExplandableListView) {
		this.rightExplandableListView = rightExplandableListView;
	}

	public void setChildOnClickListener(
			ChildOnClickListener childOnClickListener) {
		this.childOnClickListener = childOnClickListener;
	}

	public List<String> getFileList() {
		return filesList;
	}

	public List<FileNode> getFileNodeList() {
		return fileNodeList;
	}
}
