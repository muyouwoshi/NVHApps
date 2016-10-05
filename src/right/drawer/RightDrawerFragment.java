package right.drawer;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

import common.XclSingalTransfer;
import com.example.mobileacquisition.MainActivity;

//import android.app.Fragment;

public class RightDrawerFragment extends Fragment implements OnClickListener {

	private View view;
	private FileListAdapter fileListAdapter;
	private AlertDialog dialog = null;
	private boolean checkedAllFile = false;
	private Handler speedEnterHandler;
	private RightExplandableListView rightExplandableListView;
	private String newProject;
	protected boolean ifoldProjectDeleted = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.right_drawer, container, false);
		rightExplandableListView = (RightExplandableListView) view
				.findViewById(R.id.right_expandableList);
		((ImageButton) view.findViewById(R.id.add)).setOnClickListener(this);// �½��ļ���
		((ImageButton) view.findViewById(R.id.edit)).setOnClickListener(this);// ����༭״̬
		((ImageButton) view.findViewById(R.id.delete)).setOnClickListener(this);// ɾ���ļ����ļ���
		((ImageButton) view.findViewById(R.id.cancel)).setOnClickListener(this);// �����༭
		((ImageButton) view.findViewById(R.id.all)).setOnClickListener(this);// ȫѡ
		((ImageButton) view.findViewById(R.id.speedenter)).setOnClickListener(this);// ���ٽ���
		((ImageButton) view.findViewById(R.id.load)).setOnClickListener(this);// ���ٽ���
		return view;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		fileListAdapter = rightExplandableListView.getFileListAdapter();
		switch (view.getId()) {
		case R.id.add:
			createFoldersInProject();
			break;
		case R.id.edit:
			if (fileListAdapter != null) {
				editFiles();
			}
			break;
		case R.id.delete:
			fileListAdapter.ifDeleteFile(true);
			delete();
			break;
		case R.id.cancel:
			exitEditState();
			break;
		case R.id.all:
			allSeleted();
			break;
		case R.id.speedenter:
			speedEnter();
			break;
		case R.id.load:
			load();
			break;
		}
	}

	private void createFoldersInProject() {
		final List<FileNode> fileNodeList = fileListAdapter.getFileNodeList();
		final List<String> filesList = fileListAdapter.getFilesList();
		int projectID;
		if(fileNodeList.size()>0){
			int i=0;
			String projectName=fileNodeList.get(i).getFileName();
			while(!projectName.contains("project")){
				i++;
			}
			 projectID=Integer.valueOf(projectName.substring(7, projectName.length()))+1;
		}else{
			projectID=1;
		}
		// ----����del�ĶԻ����޸�Ϊ�½��Ի���----
		final RelativeLayout layout = (RelativeLayout) LayoutInflater.from(
				getActivity()).inflate(R.layout.dialog, null, false);
		((TextView) layout.findViewById(R.id.dialog_title))
				.setText("Create a new Project?");
		// 2. �½��Ի������
		final AlertDialog dialog = new AlertDialog.Builder(getActivity())
				.create();
		dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(layout);
		dialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		((EditText)layout.findViewById(R.id.dialog_edit)).setText("project"+projectID);
		// 3. ȷ����ť
		Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				File file = new File("/sdcard/data/pro/"
						+((EditText)layout.findViewById(R.id.dialog_edit)).getText().toString());
				newProject = file.getName();
				file.mkdirs();
				fileNodeList.add(null);
				filesList.add(null);
				FileNode fileNode = new FileNode(-1, 0,
						newProject);
				fileNode.setIco(R.drawable.ico_folder_blue);
				fileNode.setParentFileNode(null);
				
				for(int i=fileNodeList.size()-2;i>=0;i--){
					fileNodeList.get(i).setID(i+1);
					fileNodeList.set(i+1,fileNodeList.get(i));
					filesList.set(i+1,filesList.get(i));
				}
				fileNodeList.set(0,fileNode);
				filesList.set(0,newProject);

				fileListAdapter.setFilesList(filesList);
				fileListAdapter.setFileNodeList(fileNodeList);
				fileListAdapter.notifyDataSetChanged();
				dialog.dismiss();

				XclSingalTransfer xclSingalTransfer = XclSingalTransfer
						.getInstance();
				Handler newProjectHandler = (Handler) xclSingalTransfer
						.getValue("newProjectHandler");
				Message msg = newProjectHandler.obtainMessage();
				msg.obj = newProject;
				newProjectHandler.sendMessage(msg);
				Toast.makeText(getActivity(), newProject + " has be loaded!",
						Toast.LENGTH_SHORT).show();
				ifoldProjectDeleted = false;
			}
		});
		// 5. ȡ����ť
		Button btnCancel = (Button) layout.findViewById(R.id.dialog_cancel);
		RelativeLayout rl_dialog_content = (RelativeLayout) layout.findViewById(R.id.rl_dialog_content);
		TextView dialog_title = (TextView) layout.findViewById(R.id.dialog_title);
		if(ThemeLogic.themeType==1){
			rl_dialog_content.setBackgroundResource(R.drawable.welcome_title_corners_bg);
			btnOK.setBackgroundResource(R.drawable.btn_gray);
			btnCancel.setBackgroundResource(R.drawable.corners_bg3);
			dialog_title.setTextColor(Color.argb(255, 25, 25, 112));
		}else{
			rl_dialog_content.setBackgroundResource(R.drawable.welcome_title_corners_bg1);
			btnOK.setBackgroundResource(R.drawable.bt_gray_selector2);
			btnCancel.setBackgroundResource(R.drawable.bt_blue_selector1);
			dialog_title.setTextColor(Color.argb(255, 255, 255, 255));
		}
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	// �༭
	private void editFiles() {
		fileListAdapter.ifEntetToEditMode(true);
		fileListAdapter.notifyDataSetChanged();
		((ImageButton) view.findViewById(R.id.add)).setVisibility(View.GONE);
		((ImageButton) view.findViewById(R.id.edit)).setVisibility(View.GONE);
		((ImageButton) view.findViewById(R.id.load)).setVisibility(View.GONE);
		((ImageButton) view.findViewById(R.id.delete))
				.setVisibility(View.VISIBLE);
		((ImageButton) view.findViewById(R.id.cancel))
				.setVisibility(View.VISIBLE);
		((ImageButton) view.findViewById(R.id.all)).setVisibility(View.VISIBLE);

	}

	public void delete() {
		final List<String> filesList = fileListAdapter.getFilesList();
		final List<FileNode> fileNodeList = fileListAdapter.getFileNodeList();
		final List<FileNode> checkedFileNodeList = fileListAdapter
				.getCheckedFileNodeList();
		if (checkedFileNodeList.size() == 0) {
			Toast.makeText(getActivity(), "You have not selected any files!",
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			// 2. ɾ���Ի���
			RelativeLayout layout = (RelativeLayout) LayoutInflater.from(
					getActivity()).inflate(R.layout.dialog_del, null, false);
			dialog = new AlertDialog.Builder(getActivity()).create();
			dialog.setCancelable(false);
			dialog.show();
			dialog.getWindow().setContentView(layout);
			dialog.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

			// 3. ȷ����ť
			Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
			btnOK.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					SharedPreferences sharedPreferences = ((MainActivity) getActivity())
							.getSharedPreferences("hz_5D", 0);
					String oldProject;
					if (sharedPreferences != null) {
						oldProject = sharedPreferences.getString("oldProject",
								null);
					}

					if (!checkedAllFile) {
						Iterator<FileNode> it = checkedFileNodeList.iterator();
						while (it.hasNext()) {
							FileNode fileNode = it.next();
							File file = new File("/sdcard/data/pro/"
									+ "/"
									+ fileNode.getPartFileName(fileNode
											.getFileName()));
							if (deleteFiles(file) == 2) {
								file.delete();
							}
							fileNode.deleteNodes(fileNodeList);
							if (!ifoldProjectDeleted) {
								if (file.getName().equals(newProject)) {
									Editor editor = sharedPreferences.edit();
									editor.putString("oldProject", null);
									editor.commit();
								}
								if (file.getName().equals(newProject)) {
									newProject = null;
									XclSingalTransfer xclSingalTransfer = XclSingalTransfer
											.getInstance();
									Handler newProjectHandler = (Handler) xclSingalTransfer
											.getValue("newProjectHandler");
									Message msg = newProjectHandler
											.obtainMessage();
									msg.obj = null;
									newProjectHandler.sendMessage(msg);
								}
								ifoldProjectDeleted = true;
							}
						}
						checkedAllFile = true;
						allSeleted();
					} else if (checkedAllFile) {
						while (fileNodeList.size() > 0) {
							FileNode fileNode = fileNodeList.get(0);
							File file = new File("/sdcard/data/pro/"
									+ "/"
									+ fileNode.getPartFileName(fileNode
											.getFileName()));
							if (deleteFiles(file) == 2) {
								file.delete();
							}
							fileNode.deleteNodes(fileNodeList);
							if (!ifoldProjectDeleted) {
								if (file.getName().equals("oldProject")) {
									Editor editor = sharedPreferences.edit();
									editor.putString("oldProject", null);
									editor.commit();
								}
								if (file.getName().equals(newProject)) {
									newProject = null;
									XclSingalTransfer xclSingalTransfer = XclSingalTransfer
											.getInstance();
									Handler newProjectHandler = (Handler) xclSingalTransfer
											.getValue("newProjectHandler");
									Message msg = newProjectHandler
											.obtainMessage();
									msg.obj = null;
									newProjectHandler.sendMessage(msg);
								}
								ifoldProjectDeleted = true;
							}
						}
						checkedAllFile = true;
					}
					// -------���½����������ļ��б���
					Iterator<FileNode> itOfFileNodeList = fileNodeList
							.iterator();
					filesList.clear();
					while (itOfFileNodeList.hasNext()) {
						filesList.add(itOfFileNodeList.next().getFileName());
					}
					checkedFileNodeList.clear();// ��ձ�ѡ�е�checked����
					// --------����
					fileListAdapter.setFilesList(filesList);
					fileListAdapter.setCheckedFileNodeList(checkedFileNodeList);
					fileListAdapter.setFileNodeList(fileNodeList);
					fileListAdapter.notifyDataSetChanged();
					dialog.dismiss();

					Toast.makeText(getActivity(), "The file has been deleted!",
							Toast.LENGTH_SHORT).show();
				}
			});
			// 4. ȡ����ť
			Button btnCancel = (Button) layout.findViewById(R.id.dialog_cancel);
			RelativeLayout rl_dialog_content = (RelativeLayout) layout.findViewById(R.id.rl_dialog_content);
			TextView dialog_title = (TextView) layout.findViewById(R.id.dialog_title);
			if(ThemeLogic.themeType==1){
				rl_dialog_content.setBackgroundResource(R.drawable.welcome_title_corners_bg);
				btnOK.setBackgroundResource(R.drawable.btn_gray);
				btnCancel.setBackgroundResource(R.drawable.corners_bg3);
				dialog_title.setTextColor(Color.argb(255, 25, 25, 112));
			}else{
				rl_dialog_content.setBackgroundResource(R.drawable.welcome_title_corners_bg1);
				btnOK.setBackgroundResource(R.drawable.bt_gray_selector2);
				btnCancel.setBackgroundResource(R.drawable.bt_blue_selector1);
				dialog_title.setTextColor(Color.argb(255, 255, 255, 255));
			}
			btnCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}
	}

	// ɾ��
	public int deleteFiles(File seletedFile) {// 0״̬���д���1״̬��ɾ���ļ��ɹ���2״̬������ļ��гɹ�
		int deleteState = 0;
		if (seletedFile == null) {
			return deleteState;
		}
		if (seletedFile.isFile()) {
			seletedFile.delete();
			deleteState = 1;
		}
		if (seletedFile.isDirectory()) {
			File[] files = seletedFile.listFiles();
			if (files != null && files.length > 0) {
				for (File f : files) {
					if (f.isDirectory()) {
						deleteFiles(f);
						try {
							f.delete();
						} catch (Exception e) {
							deleteState = 0;
						}
					} else {
						try {
							f.delete();
						} catch (Exception e) {
							deleteState = 0;
						}
					}
				}
			}
			deleteState = 2;
		}
		return deleteState;
	}

	// �˳��༭״̬
	private void exitEditState() {
		checkedAllFile = true;
		allSeleted();
		fileListAdapter.ifEntetToEditMode(false);
		fileListAdapter.notifyDataSetChanged();
		if (fileListAdapter.ifEnterToRunCatalog()) {
			((ImageButton) view.findViewById(R.id.add))
					.setVisibility(View.GONE);
			((ImageButton) view.findViewById(R.id.load))
			.setVisibility(View.VISIBLE);
		} else {
			((ImageButton) view.findViewById(R.id.add))
					.setVisibility(View.VISIBLE);
			((ImageButton) view.findViewById(R.id.load))
			.setVisibility(View.GONE);
		}
		((ImageButton) view.findViewById(R.id.edit))
				.setVisibility(View.VISIBLE);
		((ImageButton) view.findViewById(R.id.delete)).setVisibility(View.GONE);
		((ImageButton) view.findViewById(R.id.cancel)).setVisibility(View.GONE);
		((ImageButton) view.findViewById(R.id.all)).setVisibility(View.GONE);
	}

	// ȫѡ
	private void allSeleted() {
		List<FileNode> fileNodeList = fileListAdapter.getFileNodeList();
		Iterator<FileNode> it = fileNodeList.iterator();
		if (checkedAllFile == false) {
			while (it.hasNext()) {
				FileNode fileNode = it.next();
				if (!fileNode.ifChecked()) {
					fileNode.setChecked(true);
				}
			}
			checkedAllFile = true;
		} else if (checkedAllFile == true) {
			while (it.hasNext()) {
				FileNode fileNode = it.next();
				if (fileNode.ifChecked()) {
					fileNode.setChecked(false);
				}
			}
			checkedAllFile = false;
		}
		fileListAdapter.notifyDataSetChanged();
	}

	// ���ٽ���
	private void speedEnter() {
		if (speedEnterHandler == null) {
			speedEnterHandler = rightExplandableListView.getSpeedEnterHandler();
		}
		File folder = new File("sdcard/data/pro");
		if(!folder.exists()){
			folder.mkdirs();
			Toast.makeText(getActivity(), "���豸û�н��й�ʵ�飬�޷��������һ�εĹ����ļ���", Toast.LENGTH_SHORT).show();
		}else if(folder.list().length==0||newProject==null){
			Toast.makeText(getActivity(), "���豸û�н��й�ʵ��������ʵ�����ݣ��޷��������һ�εĹ����ļ���", Toast.LENGTH_SHORT).show();
		}else{
			
			Message speedEnterHandlerMessage = speedEnterHandler.obtainMessage();
			speedEnterHandlerMessage.what = 1;
			speedEnterHandlerMessage.obj =newProject;
			speedEnterHandler.sendMessage(speedEnterHandlerMessage);
		}
	}

	public void setFileListAdapter(FileListAdapter fileListAdapter) {
		this.fileListAdapter = fileListAdapter;
	}

	// ����project
	private void load() {
		newProject = fileListAdapter.getProjectName();
		XclSingalTransfer xclSingalTransfer = XclSingalTransfer.getInstance();
		Handler newProjectHandler = (Handler) xclSingalTransfer
				.getValue("newProjectHandler");
		Message msg = newProjectHandler.obtainMessage();
		msg.obj = newProject;
		newProjectHandler.sendMessage(msg);
		ifoldProjectDeleted = false;
		Toast.makeText(getActivity(), newProject + " has be loaded!",
				Toast.LENGTH_SHORT).show();
	}

//	public String getNewProject() {
//		return newProject;
//	}
	public void setNewProject(String newProject) {
		this.newProject=newProject;
	}
	public FileListAdapter getFileListAdapter() {
		return rightExplandableListView.getFileListAdapter();
	}
}
