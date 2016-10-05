package com.cuiweiyou.fragment;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cuiweiyou.dynamiclevelnavigation.adapter.CVU_AdapterDataAndTmp;
import com.cuiweiyou.dynamiclevelnavigation.adapter.CVU_AdapterSecondStep;
import com.cuiweiyou.dynamiclevelnavigation.back.CVU_IData2ClickBack;
import com.cuiweiyou.dynamiclevelnavigation.back.CVU_ILogBack;
import com.cuiweiyou.dynamiclevelnavigation.back.CVU_ISecondDataBack;
import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanNameAndPath;
import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanSecondStep;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_FileUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_JSONUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_SPUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_ScreenUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_TimeUtil;
import com.cuiweiyou.glideview.adapter.CVU_ProjectsAdapter;
import com.cuiweiyou.glideview.view.CVU_GlideListView;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;
import com.example.mobileacquisition.ThemeLogic.ThemeChangeListener;

/**
 * <b>类名</b>: CVU_RightDrawerFragment 右侧拉<br/>
 * <b>说明</b>: <br/>
 * 
 * @version 1 <br/>
 */
public class CVU_RightDrawerFragment extends Fragment implements CVU_ISecondDataBack, CVU_IData2ClickBack, CVU_ILogBack, ThemeChangeListener {
	HorizontalScrollView navigationScrollView;
	/** 导航条容器 */
	private LinearLayout mNavigation;
	/** 第3级，measurementX目录列表 */
	private ExpandableListView mMeaseurmentList;
	/** 第2级，projectX目录列表 */
	private CVU_GlideListView mProjectList;
	// private ListView mProjectList;
	/** 第1级，Data和Template */
	private ExpandableListView mDataAndTmpList;
	/** 目录箭头偏移 */
	protected int marginLeft = -75;
	/** 第一级工具条，主目录显示“Data1”、“Template” */
	private View mCtrlbarStep1;
	/** 第2级工具条，进入“Data1/2”显示projects列表 */
	private View mCtrlbarStep2;
	/** 第2级工具条，创建新项目project */
	private View mCtrlbarStep2Add;
	/** 第2级工具条，编辑项目列表，进入ctrlbatstep4 */
	private View mCtrlbarStep2Edit;
	/** 第3级工具条，进入projects,显示measurementX列表 */
	private View mCtrlbarStep3;
	/** 第3级工具条，编辑measurements列表 */
	private View mCtrlbarStep3Edit;
	/** 第3级工具条，加载当前项目 */
	private View mCtrlbarStep3Load;
	/** 第4级工具条，编辑模式 */
	private View mCtrlbarStep4;
	/** 第4级工具条，删除 */
	private View mCtrlbarStep4Delete;
	/** 第4级工具条，取消编辑 */
	private View mCtrlbarStep4Cancel;
	/** 第4级工具条，选择 */
	private View mCtrlbarStep4Select;
	private ImageView mCtrlbarStep4SelectImg, cvu_cancel, cvu_delete, cvu_edit_imag, cvu_load_imag, cvu_edit, cvu_add, cvu_enter;

	/** 进入第4级工具条的flag 0=第2级projects列表，1=第3级measurements列表 */
	private int batStep = -1;
	/** 进入第2级工具条的flag 0=Data1，1=Data2 */
	public static int dataFlag = -1;
	/** 当前展开的project */
	private CVU_BeanNameAndPath currentProject;
	/** measurements列表适配器 */
	private CVU_AdapterSecondStep measurementsAdapter;
	/** projects列表适配器 */
	public CVU_ProjectsAdapter projectsAdapter;
	/** 编辑模式下是否全选了 */
	protected boolean isSelectedAll = false;
	/** 是否进入编辑模式 */
	private boolean isEdit = false;
	private View contentView;
	private CVU_AdapterDataAndTmp adapter;
	private View vStart, vGo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		contentView = View.inflate(getActivity(), R.layout.cvu_activity_main, null);

		initNavigation();
		initList();
		initCtrlbar();
		switch (ThemeLogic.themeType) {
		case 1:
			contentView.getContext().setTheme(R.style.mode1);
			break;
		case 2:
			contentView.getContext().setTheme(R.style.mode2);
		}
		ThemeLogic.getInstance().addListener(this);
		return contentView;
	}

	// ctrlbar_step1
	/** 导航条 */
	private void initNavigation() {
		navigationScrollView = (HorizontalScrollView) contentView.findViewById(R.id.navigationScrollView);
		mNavigation = (LinearLayout) contentView.findViewById(R.id.navigation);
		mNavigation.addView(getStartView("Home", mNavigation.getChildCount()), mNavigation.getChildCount());

	}

	/** 文件列表 */
	private void initList() {
		String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();

		mMeaseurmentList = (ExpandableListView) contentView.findViewById(R.id.measurement_list);
		mProjectList = (CVU_GlideListView) contentView.findViewById(R.id.project_list);
		mDataAndTmpList = (ExpandableListView) contentView.findViewById(R.id.root_list);

		adapter = new CVU_AdapterDataAndTmp(getActivity(), this);
		mDataAndTmpList.setAdapter(adapter);
		mDataAndTmpList.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// Log.e("ard", "dadaN点击 " + groupPosition);
				if (0 == groupPosition) {
					dataFlag = 0;
					startNavigate("Data");
					Log.e("ard", "rightdrawerfmg 点击 data1");
				}
				return false;
			}
		});
		mDataAndTmpList.expandGroup(1);

		mMeaseurmentList.setVisibility(View.GONE);
		mProjectList.setVisibility(View.GONE);
		mDataAndTmpList.setVisibility(View.VISIBLE);
	}

	/**
	 * 开始加载列表 <br/>
	 * title 导航条 <br/>
	 * flag 0=data1,1=data2
	 */
	protected void startNavigate(String title) {
		mDataAndTmpList.setVisibility(View.GONE);

		mNavigation.addView(getGoView(title, mNavigation.getChildCount()), mNavigation.getChildCount());
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				navigationScrollView.scrollTo(mNavigation.getRight(), 0);
			}
		}, 100);

		projectsAdapter = new CVU_ProjectsAdapter(this, this, getActivity(), dataFlag);
		mProjectList.setAdapter(projectsAdapter);
		mProjectList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				projectsAdapter.selectItem(position);
			}
		});
		mProjectList.setVisibility(View.VISIBLE);

		mCtrlbarStep1.setVisibility(View.GONE);
		mCtrlbarStep2.setVisibility(View.VISIBLE);
	}

	/**
	 * <b>功能</b>: getProject，进入此项目对应的measurements列表 <br/>
	 * 
	 * @author : weiyou.cui@ts-tech.com.cn <br/>
	 * @version 1 <br/>
	 */
	public void getProject(CVU_BeanNameAndPath bean, int flag) {
		mProjectList.setVisibility(View.GONE);

		currentProject = bean;

		View view = getGoView(bean.getName(), mNavigation.getChildCount());
		mNavigation.addView(view, mNavigation.getChildCount());// - 1); //
																// 位置被占用，则后移
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				navigationScrollView.scrollTo(mNavigation.getRight(), 0);
			}
		}, 100);

		final List<CVU_BeanSecondStep> list = CVU_FileUtil.getSecondAndThreadStepFilesAndFolders(bean.getPath() + "/", flag);
		measurementsAdapter = new CVU_AdapterSecondStep(getActivity(), this, currentProject, list);
		mMeaseurmentList.setAdapter(measurementsAdapter);
		mMeaseurmentList.setVisibility(View.VISIBLE);
		mMeaseurmentList.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// Log.e("ard", "measurements goup点击：" + groupPosition);

				if (isEdit) {
					if (measurementsAdapter.selectedViews.contains(groupPosition + "")) {
						measurementsAdapter.removeSelectedView(groupPosition + "");
					} else {
						measurementsAdapter.addSelectedView(groupPosition + "");
					}
					return true;
				} else {
					return false;
				}

			}
		});

		mMeaseurmentList.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				int count = mMeaseurmentList.getChildCount();
				for (int i = 0; i < count; i++) {
					if (groupPosition != i) {
						mMeaseurmentList.collapseGroup(i); // 关闭其它group
					}
				}
			}
		});

		mCtrlbarStep4.setVisibility(View.GONE);
		mCtrlbarStep2.setVisibility(View.GONE);
		mCtrlbarStep1.setVisibility(View.GONE);
		mCtrlbarStep3.setVisibility(View.VISIBLE);
	}

	@Override
	public void clickSecondDataBack() {
		dataFlag = 1;
		startNavigate("Template");
	}

	/** <b>功能</b>initCtrlbar，初始化工具条，定义控件，添加事件 */
	private void initCtrlbar() {
		mCtrlbarStep1 = contentView.findViewById(R.id.ctrlbar_step1);
		mCtrlbarStep2 = contentView.findViewById(R.id.ctrlbar_step2);
		mCtrlbarStep2Add = mCtrlbarStep2.findViewById(R.id.ctrlbar_step2_add);
		mCtrlbarStep2Edit = mCtrlbarStep2.findViewById(R.id.ctrlbar_step2_edit);
		mCtrlbarStep3 = contentView.findViewById(R.id.ctrlbar_step3);
		mCtrlbarStep3Edit = mCtrlbarStep3.findViewById(R.id.ctrlbar_step3_edit);
		mCtrlbarStep3Load = mCtrlbarStep3.findViewById(R.id.ctrlbar_step3_load);
		mCtrlbarStep4 = contentView.findViewById(R.id.ctrlbar_step4);
		mCtrlbarStep4Delete = mCtrlbarStep4.findViewById(R.id.ctrlbar_step4_delete);
		mCtrlbarStep4Cancel = mCtrlbarStep4.findViewById(R.id.ctrlbar_step4_cancel);
		mCtrlbarStep4Select = mCtrlbarStep4.findViewById(R.id.ctrlbar_step4_select);
		mCtrlbarStep4SelectImg = (ImageView) mCtrlbarStep4.findViewById(R.id.ctrlbar_step4_select_img);

		cvu_cancel = (ImageView) mCtrlbarStep4Cancel.findViewById(R.id.cvu_cancel);
		cvu_delete = (ImageView) mCtrlbarStep4Delete.findViewById(R.id.cvu_delete);
		cvu_edit_imag = (ImageView) mCtrlbarStep3Edit.findViewById(R.id.cvu_edit_imag);
		cvu_load_imag = (ImageView) mCtrlbarStep3Load.findViewById(R.id.cvu_load_imag);
		cvu_edit = (ImageView) mCtrlbarStep2Edit.findViewById(R.id.cvu_edit);
		cvu_add = (ImageView) mCtrlbarStep2Add.findViewById(R.id.cvu_add);
		cvu_enter = (ImageView) mCtrlbarStep1.findViewById(R.id.cvu_enter);

		// 直接导航到上次 load 的项目
		mCtrlbarStep1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String currentDataFlag = CVU_SPUtil.getCurrentProject(getActivity(), "dataFlag");
				String currentProjectX = CVU_SPUtil.getCurrentProject(getActivity(), "currentProject");

				if ("".equals(currentDataFlag) || "".equals(currentProjectX)) {
					Toast.makeText(getActivity(), R.string.no_loaded, 0).show();
					return;
				}

				int flagX = Integer.valueOf(currentDataFlag);

				if (0 == flagX) {
					dataFlag = 0;
					startNavigate("Data");
				} else if (1 == flagX) {
					dataFlag = 1;
					startNavigate("Template");
				}

				getProject(CVU_JSONUtil.json2Bean(currentProjectX), flagX);
			}
		});

		// 创建新的 Project
		mCtrlbarStep2Add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog2CreateProject();
			}
		});

		// 进入projects编辑状态
		mCtrlbarStep2Edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				batStep = 0;
				mCtrlbarStep1.setVisibility(View.GONE);
				mCtrlbarStep2.setVisibility(View.GONE);
				mCtrlbarStep3.setVisibility(View.GONE);
				mCtrlbarStep4.setVisibility(View.VISIBLE);

				isEdit = true;
				projectsAdapter.setEditable(isEdit);
			}
		});

		// load 加载当前项目
		mCtrlbarStep3Load.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CVU_SPUtil.saveCurrentProject(getActivity(), "dataFlag", dataFlag + "");
				CVU_SPUtil.saveCurrentProject(getActivity(), "currentProject", CVU_JSONUtil.bean2Json(currentProject));

				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.project) + currentProject.getName() + getActivity().getResources().getString(R.string.loadproject), 0).show();
			}
		});

		// 编辑已经展开项目的 measurements
		mCtrlbarStep3Edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				batStep = 1;
				mCtrlbarStep1.setVisibility(View.GONE);
				mCtrlbarStep2.setVisibility(View.GONE);
				mCtrlbarStep3.setVisibility(View.GONE);
				mCtrlbarStep4.setVisibility(View.VISIBLE);

				isEdit = true;
				measurementsAdapter.setEditable(isEdit);

				// Toast.makeText(getActivity(), "编辑当前项目", 0).show();
			}
		});

		// 删除
		mCtrlbarStep4Delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				createAlterdialog(batStep);

			}
		});

		mCtrlbarStep4Cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				exitEditMode();
			}
		});

		mCtrlbarStep4Select.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (batStep == 0) {
					if (isSelectedAll) {
						isSelectedAll = false;

						projectsAdapter.removeSelectedAllViews();
					} else {
						isSelectedAll = true;

						projectsAdapter.addSelectedAllViews();
					}

				} else if (batStep == 1) {
					if (isSelectedAll) {
						isSelectedAll = false;

						measurementsAdapter.removeSelectedAllViews();
					} else {
						isSelectedAll = true;

						measurementsAdapter.addSelectedAllViews();
					}

				}

				// Toast.makeText(getActivity(), "选择", 0).show();
			}
		});
	} // end. initCtrlbar，初始化工具条，定义控件，添加事件

	/** 底部工具条取消列表编辑 */
	public void exitEditMode() {
		mCtrlbarStep4.setVisibility(View.GONE);
		mCtrlbarStep1.setVisibility(View.GONE);

		isSelectedAll = false;
		isEdit = false;

		switch (batStep) {
		case 0:
			mCtrlbarStep3.setVisibility(View.GONE);
			mCtrlbarStep2.setVisibility(View.VISIBLE);

			projectsAdapter.setEditable(isEdit);
			projectsAdapter.removeSelectedAllViews();

			// List<CVU_BeanNameAndPath> list = CVU_FileUtil.getFoldersInPro();
			// projectsAdapter = new CVU_GlideAdapter(this, getActivity(), list,
			// isEdit);
			// mProjectList.setAdapter(projectsAdapter);

			break;
		case 1:
			mCtrlbarStep2.setVisibility(View.GONE);
			mCtrlbarStep3.setVisibility(View.VISIBLE);

			measurementsAdapter.setEditable(isEdit);
			measurementsAdapter.removeSelectedAllViews();
			break;
		}
		// 删除可以不
		if (ThemeLogic.themeType == 1) {
			mCtrlbarStep4SelectImg.setImageResource(R.drawable.cvu_all_normal);
		} else {
			mCtrlbarStep4SelectImg.setImageResource(R.drawable.all1);
		}

		batStep = -1;
	}

	// ////////////////////////////////////////////////////////

	/**
	 * <b>功能</b>: createUDFDialog，创建新的项目<br/>
	 */
	private void dialog2CreateProject() {

		// 1，'fileNameRule':prj下命名规则，默认"project"。
		// 2，'measurementfileNameRule':measurement下命名规则，默认"measurement"。
		// 3，'modeChange':prj下命名模式，默认"0"-自定义。1-默认
		// 4，'measurementModeChange':measurement下命名模式，默认"0"-自定义。1-默认
		// 5，'Dir':存储位置0-SD卡，1-手机。
		// 6，'project':工程设置

		final String prjMode = CVU_SPUtil.getLeftNamingRule(getActivity(), 3);
		final String mesMode = CVU_SPUtil.getLeftNamingRule(getActivity(), 4);
		final String prjRule = CVU_SPUtil.getLeftNamingRule(getActivity(), 1);
		final String mesRule = CVU_SPUtil.getLeftNamingRule(getActivity(), 2);

		final List<CVU_BeanNameAndPath> list = CVU_FileUtil.getFoldersInPro();
		final int size = list.size();
		int cc = getMaxPrjSN(size, list, prjRule);
		String blankname = getActivity().getResources().getString(R.string.leave_a_blank);
		final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
		View view = View.inflate(getActivity(), R.layout.cvu_view_createproject, null);
		final EditText mEtMsg = (EditText) view.findViewById(R.id.view_createproject_name);

		if ("1".equals(prjMode))
			mEtMsg.setHint(Html.fromHtml("<small><small>" + blankname + "Project" + "_" + cc + "</small></small>"));
		else
			mEtMsg.setHint(Html.fromHtml("<small><small>" + blankname + prjRule + "_" + cc + "</small></small>"));
		Button mBtnOK = (Button) view.findViewById(R.id.view_createproject_ok);
		Button mBtnNO = (Button) view.findViewById(R.id.view_createproject_cancel);
		RelativeLayout rl_dialog_content = (RelativeLayout) view.findViewById(R.id.rl_dialog_content);
		TextView view_createproject_title = (TextView) view.findViewById(R.id.view_createproject_title);
		mBtnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (createNewPrj(size, list, prjRule, prjMode, mEtMsg))
					dialog.dismiss();
			}
		});
		mBtnNO.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		if (ThemeLogic.themeType == 1) {
			rl_dialog_content.setBackgroundResource(R.drawable.welcome_title_corners_bg);
			mBtnOK.setBackgroundResource(R.drawable.btn_gray);
			mBtnNO.setBackgroundResource(R.drawable.corners_bg3);
			view_createproject_title.setTextColor(Color.argb(255, 25, 25, 112));
		} else {
			rl_dialog_content.setBackgroundResource(R.drawable.welcome_title_corners_bg1);
			mBtnOK.setBackgroundResource(R.drawable.bt_gray_selector2);
			mBtnNO.setBackgroundResource(R.drawable.bt_blue_selector1);
			view_createproject_title.setTextColor(Color.argb(255, 255, 255, 255));
		}
		dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(view);
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
	} // end. createUDFDialog，创建新的项目

	/**
	 * 获取项目的最大编号
	 * 
	 * @param size
	 *            项目总数
	 * @param list
	 *            项目集合
	 * @param prjRule
	 *            默认命名
	 * @return 按此默认命名规则的最大编号
	 */
	private int getMaxPrjSN(int size, List<CVU_BeanNameAndPath> list, String prjRule) {
		int cc = 0;
		for (int i = 0; i < size; i++) {
			// ///////////// 按照新需求，全部的项目名称默认都自动加“_数字”进行编号，因此获取最后一个下划线后的数字判断
			// ///////////////////16.4.27 崔
			String name = list.get(i).getName();
			if (name.toLowerCase().contains("_")) { // 规定用户手动输入的名字不能使用下划线，所以有下划线的都是规则的名字，可以读取编号
				int iii = name.lastIndexOf("_");
				String number = name.substring(iii + 1, name.length());
				try {
					int nb = Integer.valueOf(number);
					if (nb > cc)
						cc = nb;
				} catch (NumberFormatException e) {
					continue;
				}
			} else {
				continue;
			}
		}
		cc++;
		return cc;
	}

	/**
	 * @param size
	 *            项目总数量
	 * @param list
	 *            项目集合
	 * @param prjRule
	 *            项目命名默认值
	 * @param prjMode
	 *            命名模式 0-自定义 1-默认
	 * @param mEtMsg
	 *            用户手动输入的名称
	 * @return boolean false-名称重复，true-新建成功
	 */
	protected boolean createNewPrj(int size, List<CVU_BeanNameAndPath> list, String prjRule, String prjMode, EditText mEtMsg) {
		int cc = getMaxPrjSN(size, list, prjRule);

		String name = mEtMsg.getText().toString();
		if (null != name && !"".equals(name)) { // 用户手动输入了名称
			name = name.replace(" ", "").replace("_", ""); // 去掉空格和下划线
			if ("".equals(name)) {
				if ("1".equals(prjMode))
					name = "Project" + "_" + cc;
				else
					name = prjRule + "_" + cc;
			}
		} else {
			if ("1".equals(prjMode))
				name = "Project" + "_" + cc;
			else
				name = prjRule + "_" + cc;
		}

		// //////////////////////////////////////////////// 重名 -bgein
		for (int i = 0; i < size; i++) {
			String name2 = list.get(i).getName();

			if (name.equalsIgnoreCase(name2)) {
				Toast.makeText(getActivity(), R.string.project_exists, 0).show();
				return false;
			}
		}
		// //////////////////////////////////////////////// -over

		String tmp = name;
		name = CVU_TimeUtil.getTimestamp() + name;

		CVU_FileUtil.createProject(name);
		projectsAdapter = new CVU_ProjectsAdapter(CVU_RightDrawerFragment.this, CVU_RightDrawerFragment.this, getActivity(), dataFlag);
		mProjectList.setAdapter(projectsAdapter);

		String tmplSource = getActivity().getFilesDir().getAbsolutePath() + "/shared_prefs/hz_5D.xml";
		String tmplTarget = CVU_FileUtil.dataPath + name + "/template.xml";
		CVU_FileUtil.copyFile(new File(tmplSource), new File(tmplTarget));

		Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.new_projects) + tmp + getActivity().getResources().getString(R.string.created_projects), 0).show();

		return true;
	}

	/**
	 * <b>功能</b>: createAlterdialog，确认删除所选的项目或测量 <br/>
	 * 
	 * @param step
	 *            0-项目列表，1-measurement列表
	 */
	private void createAlterdialog(final int step) {
		if (0 == step) {
			if (projectsAdapter.selectedViews.size() < 1) {
				Toast.makeText(getActivity(), R.string.delete_no_file, 0).show();
				return;
			}
		} else if (1 == step) {
			if (measurementsAdapter.selectedViews.size() < 1) {
				Toast.makeText(getActivity(), R.string.delete_no_file, 0).show();
				return;
			}
		}

		final AlertDialog builder = new AlertDialog.Builder(getActivity()).create();
		// 2. 删除对话框
		RelativeLayout layout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_del, null, false);
		RelativeLayout rl_dialog_content = (RelativeLayout) layout.findViewById(R.id.rl_dialog_content);

		builder.setCancelable(false);
		builder.show();
		builder.getWindow().setContentView(layout);
		builder.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (0 == step) { // projects
					projectsAdapter.removeSelecteds();
				} else if (1 == step) { // measurements
					measurementsAdapter.removeSelecteds();
				}

				builder.dismiss();
			}
		});
		// 4. 取消按钮
		Button btnCancel = (Button) layout.findViewById(R.id.dialog_cancel);
		TextView dialog_title = (TextView) layout.findViewById(R.id.dialog_title);
		if (ThemeLogic.themeType == 1) {
			rl_dialog_content.setBackgroundResource(R.drawable.welcome_title_corners_bg);
			btnOK.setBackgroundResource(R.drawable.btn_gray);
			btnCancel.setBackgroundResource(R.drawable.corners_bg3);
			dialog_title.setTextColor(Color.argb(255, 25, 25, 112));
		} else {
			rl_dialog_content.setBackgroundResource(R.drawable.welcome_title_corners_bg1);
			btnOK.setBackgroundResource(R.drawable.bt_gray_selector2);
			btnCancel.setBackgroundResource(R.drawable.bt_blue_selector1);
			dialog_title.setTextColor(Color.argb(255, 255, 255, 255));
		}

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});
	} // end. createAlterdialog，确认删除所选的项目或测量

	// ////////////////////////////////////////////////////////

	/** 根据tag删减导航键 */
	private void navigationBack(Object tag) {

		int index = Integer.parseInt(tag.toString());
		int count = mNavigation.getChildCount();

		for (int i = count - 1; i > index; i--) {
			mNavigation.removeViewAt(i);
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				navigationScrollView.scrollTo(mNavigation.getRight(), 0);
			}
		}, 100);

		isEdit = false;

		if (index == 0) {
			mMeaseurmentList.setVisibility(View.GONE);
			mProjectList.setVisibility(View.GONE);
			mDataAndTmpList.setVisibility(View.VISIBLE);

			mCtrlbarStep4.setVisibility(View.GONE);
			mCtrlbarStep3.setVisibility(View.GONE);
			mCtrlbarStep2.setVisibility(View.GONE);
			mCtrlbarStep1.setVisibility(View.VISIBLE);

			initList();
		} else if (index == 1) {

			mMeaseurmentList.setVisibility(View.GONE);

			mCtrlbarStep4.setVisibility(View.GONE);
			mCtrlbarStep3.setVisibility(View.GONE);
			mCtrlbarStep1.setVisibility(View.GONE);
			mCtrlbarStep2.setVisibility(View.VISIBLE);

			isSelectedAll = false;
			//
			projectsAdapter = new CVU_ProjectsAdapter(this, this, getActivity(), dataFlag);
			mProjectList.setAdapter(projectsAdapter);
			mProjectList.setVisibility(View.VISIBLE);
		} else if (index == 2) {
			// Toast.makeText(getActivity(), "measurements 导航按钮点击", 0).show();

			final List<CVU_BeanSecondStep> list = CVU_FileUtil.getSecondAndThreadStepFilesAndFolders(currentProject.getPath() + "/", dataFlag);
			measurementsAdapter = new CVU_AdapterSecondStep(getActivity(), this, currentProject, list);
			mMeaseurmentList.setAdapter(measurementsAdapter);

			mCtrlbarStep4.setVisibility(View.GONE);
			mCtrlbarStep1.setVisibility(View.GONE);
			mCtrlbarStep2.setVisibility(View.GONE);
			mCtrlbarStep3.setVisibility(View.VISIBLE);
		}
	}

	/** 创建“主目录，开始”导航键 */
	private View getStartView(String title, int tag) {
		vStart = View.inflate(getActivity(), R.layout.cvu_item_navi_start, null);
		Button btn = (Button) vStart.findViewById(R.id.item_start_btn);
		btn.setText(title);
		btn.setTag(tag);
		if (ThemeLogic.themeType == 1) {
			btn.setTextColor(Color.rgb(255, 255, 255));
			btn.setBackgroundResource(R.drawable.cvu_selector_arrow_start_background);
		} else {
			btn.setTextColor(Color.rgb(0, 0, 0));
			btn.setBackgroundResource(R.drawable.cvu_selector_arrow_start_background1);
		}

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				navigationBack(v.getTag());
			}
		});

		return vStart;
	}

	/** 创建“继续”导航键 */
	private View getGoView(String title, int tag) {
		vGo = View.inflate(getActivity(), R.layout.cvu_item_navi_go, null);
		Button item_go_btn = (Button) vGo.findViewById(R.id.item_go_btn);
		item_go_btn.setText(title);
		item_go_btn.setTag(tag);
		if (ThemeLogic.themeType == 1) {
			item_go_btn.setTextColor(Color.rgb(255, 255, 255));
			item_go_btn.setBackgroundResource(R.drawable.cvu_selector_arrow_go_background);
		} else {
			item_go_btn.setTextColor(Color.rgb(0, 0, 0));
			item_go_btn.setBackgroundResource(R.drawable.cvu_selector_arrow_go_background1);
		}

		// 16.5.24 防止导航条缝隙 崔 - begin
		int count = mNavigation.getChildCount();
		for (int i = 1; i < count; i++) {
			final View v = mNavigation.getChildAt(i);
			ViewTreeObserver vto = v.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onGlobalLayout() {
					v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					LinearLayout.LayoutParams paramsGo = (android.widget.LinearLayout.LayoutParams) v.getLayoutParams();
					paramsGo.leftMargin = marginLeft;
					v.setLayoutParams(paramsGo);
				}
			});
		}
		// - end

		ViewTreeObserver vto = vGo.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				vGo.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				LinearLayout.LayoutParams paramsGo = (android.widget.LinearLayout.LayoutParams) vGo.getLayoutParams();
				paramsGo.leftMargin = marginLeft;
				vGo.setLayoutParams(paramsGo);
			}
		});

		item_go_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				navigationBack(v.getTag());
			}
		});

		return vGo;
	}
	/** 创建“结束”导航键 */
	@SuppressWarnings("unused")
	private View getEndView() {
		final View vEnd = View.inflate(getActivity(), R.layout.cvu_item_navi_end, null);
		ViewTreeObserver vtoEnd = vEnd.getViewTreeObserver();
		vtoEnd.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				vEnd.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				LinearLayout.LayoutParams paramsEnd = (android.widget.LinearLayout.LayoutParams) vEnd.getLayoutParams();
				paramsEnd.leftMargin = marginLeft;
				vEnd.setLayoutParams(paramsEnd);
			}
		});

		return vEnd;
	}

	@Override
	public void clickOne(int position) {
		getProject(projectsAdapter.getItem(position), dataFlag);
	}

	// ///////////////////////////////////////

	@Override
	public void onDestroyView() {
		super.onDestroy();
		CVU_SPUtil.deleteCurrentProject(getActivity());
		// Log.e("ard", "onDestroyView 删除currentview");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		CVU_SPUtil.deleteCurrentProject(getActivity());
		// Log.e("ard", "onDestroy 删除currentview");
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void onThemeChanged() {
		switch (ThemeLogic.themeType) {

		/** 1閵嗗倸鍘涢弴瀛樻煀娑撳顣�閿涗緤绱掗敓锟� */
		case 1:
			contentView.getContext().setTheme(R.style.mode1);
			break;
		case 2:
			contentView.getContext().setTheme(R.style.mode2);
			break;
		}
		adapter.notifyDataSetChanged();
		if (measurementsAdapter != null) {
			measurementsAdapter.notifyDataSetChanged();

		}
		if (projectsAdapter != null) {
			projectsAdapter.notifyDataSetChanged();
		}
		/** 2.閸愬秵褰侀崣鏍х潣閿燂拷? */
		TypedArray typedArray = contentView.getContext().obtainStyledAttributes(R.styleable.myStyle);
		if (vStart != null) {
			Button item_start_btn = (Button) vStart.findViewById(R.id.item_start_btn);
			item_start_btn.setBackground(typedArray.getDrawable(R.styleable.myStyle_cvu_selector_arrow_start_background));
			item_start_btn.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text, Color.YELLOW));
		}
		if (vGo != null) {
			Button item_go_btn = ((Button) vGo.findViewById(R.id.item_go_btn));
			item_go_btn.setBackground(typedArray.getDrawable(R.styleable.myStyle_cvu_selector_arrow_go_background));
			item_go_btn.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text, Color.YELLOW));
		}
		if (mNavigation.getChildCount() > 1) {
			Button item_go_btn = ((Button) mNavigation.findViewById(R.id.item_go_btn));
			for (int g = 1; g < mNavigation.getChildCount(); g++) {
				item_go_btn.setBackground(typedArray.getDrawable(R.styleable.myStyle_cvu_selector_arrow_go_background));
				item_go_btn.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text, Color.YELLOW));
			}
		}
		LinearLayout cvu_listview_bkg = (LinearLayout) contentView.findViewById(R.id.cvu_listview_bkg);
		cvu_listview_bkg.setBackgroundColor(typedArray.getColor(R.styleable.myStyle_cvu_listview_bkg, Color.YELLOW));
		mCtrlbarStep1.setBackground(typedArray.getDrawable(R.styleable.myStyle_cvu_selector_button));
		mCtrlbarStep2Add.setBackground(typedArray.getDrawable(R.styleable.myStyle_cvu_selector_button));
		mCtrlbarStep2Edit.setBackground(typedArray.getDrawable(R.styleable.myStyle_cvu_selector_button));
		mCtrlbarStep3Load.setBackground(typedArray.getDrawable(R.styleable.myStyle_cvu_selector_button));
		mCtrlbarStep3Edit.setBackground(typedArray.getDrawable(R.styleable.myStyle_cvu_selector_button));
		mCtrlbarStep4Delete.setBackground(typedArray.getDrawable(R.styleable.myStyle_cvu_selector_button));
		mCtrlbarStep4Cancel.setBackground(typedArray.getDrawable(R.styleable.myStyle_cvu_selector_button));
		mCtrlbarStep4Select.setBackground(typedArray.getDrawable(R.styleable.myStyle_cvu_selector_button));
		cvu_cancel.setBackground(typedArray.getDrawable(R.styleable.myStyle_btn_linealayout1));

		mCtrlbarStep4SelectImg.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_cvu_all_normal));
		cvu_delete.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_cvu_delete));
		cvu_edit_imag.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_cvu_edit));
		cvu_load_imag.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_cvu_load));
		cvu_edit.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_cvu_edit));
		cvu_add.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_cvu_add));
		cvu_enter.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_cvu_enter));
		cvu_cancel.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_cvu_cancel));
		typedArray.recycle();

	}

	@Override
	public void startGetLog() {
		getActivity().findViewById(R.id.loadlogs_hat).setVisibility(View.VISIBLE);
		getActivity().findViewById(R.id.loadlogs).setVisibility(View.VISIBLE);
	}

	@Override
	public void getLogDatas(String datas, float maxWid) {

		Point p = new Point();
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getSize(p);
		int height = p.y;
		
	    AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
	    View view = View.inflate(getActivity(), R.layout.cvu_view_showlogfiledialog, null);

	    RelativeLayout msvShowLogfile = (RelativeLayout) view.findViewById(R.id.sv_showlogfile);
//	    RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, height / 4 * 3);
//	    msvShowLogfile.setLayoutParams(layoutParams);
		switch (ThemeLogic.themeType) {
		case 1:
			msvShowLogfile.setBackgroundColor(Color.parseColor("#46a3ff"));
			break;
		case 2:
			msvShowLogfile.setBackgroundColor(Color.parseColor("#d0d0d0"));
			break;
		}
	    TextView mtvShowLogfile = (TextView) view.findViewById(R.id.tv_showlogfile);
	    mtvShowLogfile.setText(datas);
	    
	    dialog.setView(view);
	    dialog.show();
	    
	    // 必须在show出来之后
	    
	    msvShowLogfile.measure(0, 0);
	    int measuredWidth = msvShowLogfile.getMeasuredWidth();
	    
	    Window alertWindow = dialog.getWindow();
        WindowManager.LayoutParams lParams = alertWindow.getAttributes();
        lParams.width = (int) (measuredWidth + maxWid);
        lParams.height = height / 4 * 3;
        alertWindow.setAttributes(lParams);

		getActivity().findViewById(R.id.loadlogs_hat).setVisibility(View.GONE);
		getActivity().findViewById(R.id.loadlogs).setVisibility(View.GONE);
	}
}
