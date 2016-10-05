package com.cuiweiyou.dynamiclevelnavigation.adapter;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
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
import left.drawer.OctFragment;
import left.drawer.OrderFragment;
import left.drawer.PreTriggerFragment;
import left.drawer.RpmFragment;
import left.drawer.SetPreviewFragment;
import left.drawer.SignalFragment;
import left.drawer.SplFragment;
import left.drawer.TriggerFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cuiweiyou.dynamiclevelnavigation.back.CVU_ILogBack;
import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanNameAndPath;
import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanSecondStep;
import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanThreadStep;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_FileUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_JSONUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_SPUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_ViewUtil;
import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;
import common.PullBookParser;

/** projectX/�µ�measurements�б������� */
public class CVU_AdapterSecondStep extends BaseExpandableListAdapter {

	private Context ctx;
	private CVU_BeanNameAndPath parent;
	private List<CVU_BeanSecondStep> res;
	// private Map<String, BeanItem> views = new HashMap<String, BeanItem>();
	private boolean isEditable;
	public ArrayList<String> selectedViews = new ArrayList<String>();
	private CVU_ILogBack back;

	public CVU_AdapterSecondStep(Context ctx, CVU_ILogBack back, CVU_BeanNameAndPath parent, List<CVU_BeanSecondStep> res) {
		this.ctx = ctx;
		this.back = back;
		this.parent = parent;
		this.res = res;
	}

	@Override
	public int getGroupCount() {
		return res.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return res.get(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// Log.e("ard", "getGroupView");

		// / if (convertView == null) {
		convertView = View.inflate(ctx, R.layout.cvu_item_projectlist_step1, null);
		// }
		LinearLayout cvu_selector_step1 = (LinearLayout) convertView.findViewById(R.id.cvu_selector_step1);

		CVU_BeanSecondStep bean = res.get(groupPosition);
		String name = bean.getName();

		TextView tv = (TextView) convertView.findViewById(R.id.item_project_name);
		// tv.setBackgroundColor(Color.RED);
		// tv.setTextSize(12f);

		tv.setText(name);

		String t4sr = CVU_SPUtil.getTemplate4SaveRecord(ctx);
		if (t4sr.equals(bean.getPath())) {
			tv.setTextColor(Color.parseColor("#ff9900"));
		} else {
			if (ThemeLogic.themeType == 1) {
				tv.setTextColor(Color.rgb(255, 255, 255));
			} else {
				tv.setTextColor(Color.rgb(0, 0, 0));
			}
		}
		ImageView img = (ImageView) convertView.findViewById(R.id.item_project_img);
		if (name.endsWith(".xml") || name.endsWith(".pcm")) {

			if (ThemeLogic.themeType == 1) {
				img.setBackgroundResource(R.drawable.cvu_file);
			} else {
				img.setBackgroundResource(R.drawable.ico_file_blue1);
			}
			if (name.endsWith(".xml")) { // projectX�º�measurementXͬ����xml����
//				convertView.setOnClickListener(new TplInPrjClickListener(bean));
				convertView.setOnLongClickListener(new TplInPrjClickListener(bean));
			}
		} else {

			if (ThemeLogic.themeType == 1) {
				img.setBackgroundResource(R.drawable.cvu_folder);
			} else {
				img.setBackgroundResource(R.drawable.ico_folder_blue1);
			}
		}

		ImageView selector = (ImageView) (convertView.findViewById(R.id.item_project_selector));
		selector.setVisibility(View.GONE);
		if (isEditable)
			selector.setVisibility(View.VISIBLE);
		else
			selector.setVisibility(View.GONE);

		if (selectedViews.contains(groupPosition + "")) { // �������Ƿ��д˶���
			selector.setImageResource(R.drawable.cvu_checked_active);
		} else {
			selector.setImageResource(R.drawable.cvu_checked_normal);
		}

		// BeanItem item = new BeanItem(false, selector);
		// views.put(groupPosition+"", item);
		if (ThemeLogic.themeType == 1) {
			cvu_selector_step1.setBackgroundResource(R.drawable.cvu_selector_item_list);
		} else {
			cvu_selector_step1.setBackgroundResource(R.drawable.cvu_selector_item_list1);
		}
		return convertView;
	}

	// ---------------------------------------------------------------------------

	@Override
	public int getChildrenCount(int groupPosition) {
		int count = 0;

		CVU_BeanSecondStep bean = res.get(groupPosition);
		if (null == bean)
			return count;

		List<CVU_BeanThreadStep> childs = bean.getSeconds();
		if (null == childs)
			return count;

		return childs.size();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		CVU_BeanThreadStep child = null;

		CVU_BeanSecondStep bean = res.get(groupPosition);
		if (null == bean)
			return child;

		List<CVU_BeanThreadStep> seconds = bean.getSeconds();
		if (null == seconds)
			return child;

		child = seconds.get(childPosition);

		return child;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final ExpandableListView thread_step_view = CVU_ViewUtil.getExpandableListView(ctx);
		final CVU_BeanThreadStep beanThreadStep = res.get(groupPosition).getSeconds().get(childPosition);
		final List<CVU_BeanThreadStep> seconds = new ArrayList<CVU_BeanThreadStep>();
		seconds.add(beanThreadStep);
		thread_step_view.setAdapter(new CVU_AdapterThreadStep(ctx, seconds));

		thread_step_view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				CVU_BeanNameAndPath child = seconds.get(groupPosition).getThreads().get(childPosition);
				String name = child.getName();
				String path = child.getPath();
				Log.e("ard", "thread_step_view item ���:" + name + " = " + path);

				String p2 = new File(path).getParent(); // log.txt�ĸ�Ŀ¼
				int lio = p2.lastIndexOf("/"); // ����/����ļ������
				String substr = p2.substring(lio + 1); // ȡ����
				((MainActivity) ctx).setReplayPath(path);
				if ("Result".equals(substr)) { // �����Result����ô���log.txt����Ŀ���ļ�
					createLogDialog(path);
				}

				return true;
			}
		});
		
		thread_step_view.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				
				Integer tag = (Integer) view.getTag(R.id.tmp_long_group);
				if(null == tag){
//					Log.e("ard", "�����ˣ�" + view.getClass().getPackage() + "/" + view.getClass().getSimpleName());
					return false;
				}
				
				if (beanThreadStep.getName().endsWith(".xml")) { // measurementX�µ�ģ�����
					getTempelate(beanThreadStep.getPath(), beanThreadStep.getName(), "mes");
					Log.e("ard", "adaptersecondstep ���� projectN/mesurementN/xml�ļ�������");

					notifyDataSetInvalidated();
				}

				return true;
			}
		});
		
		thread_step_view.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				Log.e("ard", "adaptersecondstep ���projectNĿ¼�µ�mesurementN�µ�xml�ļ�" + groupPosition + " - " + beanThreadStep.getPath());

				int count = thread_step_view.getChildCount();
				for (int i = 0; i < count; i++) {
					if (groupPosition != i) {
						thread_step_view.collapseGroup(i); // �ر�����group
					}
				}
				return false; // Ĭ��Ϊfalse����Ϊtrueʱ������¼�����չ��Group
			}
		});

		// ��ExpandableListViewչ��ʱ����Ϊgroupֻ��һ�������ExpandableListView���ܸ߶�=
		// ����ExpandableListView��child���� + 1 ��* ÿһ��ĸ߶�
		thread_step_view.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (beanThreadStep.getThreads().size() + 1) * (int) ctx.getResources().getDimension(R.dimen.cvu_expitemheight));
				thread_step_view.setLayoutParams(lp);
			}
		});

		// ��ExpandableListView�ر�ʱ����ʱֻʣ��group��һ�
		// ������ExpandableListView���ܸ߶ȼ�Ϊһ��ĸ߶�
		thread_step_view.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			@Override
			public void onGroupCollapse(int groupPosition) {

				LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) ctx.getResources().getDimension(R.dimen.cvu_expitemheight));
				thread_step_view.setLayoutParams(lp);
			}
		});

		return thread_step_view;
	}

	// ---------------------------------------------------------------------------

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// ����Ŀ��ȡ����¼�
		return true;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	// /////////////////////////////////////////////////

	public void setEditable(boolean b) {
		this.isEditable = b;

		notifyDataSetChanged();
	}

	public void addSelectedAllViews() {
		selectedViews = new ArrayList<String>();
		for (int i = 0; i < res.size(); i++) {
			if (!selectedViews.contains(i + "")) {
				selectedViews.add(i + "");
			}
		}

		notifyDataSetChanged();
	}

	public void addSelectedView(String string) {
		if (!selectedViews.contains(string)) {
			selectedViews.add(string);
		}

		notifyDataSetChanged();
	}

	public void removeSelectedAllViews() {
		selectedViews.clear();
		selectedViews = new ArrayList<String>();
		notifyDataSetChanged();
	}

	public void removeSelectedView(String string) {
		selectedViews.remove(string);
		notifyDataSetChanged();
	}

	/** ɾ��ѡ�е�ȫ��item */
	public void removeSelecteds() {
		List<CVU_BeanSecondStep> tmp = new ArrayList<CVU_BeanSecondStep>();
		for (int i = 0; i < res.size(); i++) {
			if (selectedViews.contains(i + "")) {
				tmp.add(res.get(i));
				selectedViews.remove(i + "");
			}
		}

		Iterator<CVU_BeanSecondStep> it = res.iterator();

		while (it.hasNext()) {
			CVU_BeanSecondStep next = it.next();
			for (int i = 0; i < tmp.size(); i++) {
				CVU_BeanSecondStep b2 = tmp.get(i);
				if (b2.getName().equals(next.getName())) {
					it.remove();

					CVU_FileUtil.removeFilesOrDirs(b2.getPath());

					break;
				}
			}
		}

		notifyDataSetChanged();
	}

	// ------------------------------------------------------------------

	/** �ڲ��࣬��Ŀ��Ŀ¼��ģ��xml�����¼������� */
	class TplInPrjClickListener implements View.OnLongClickListener {

		private CVU_BeanSecondStep bean;

		/** �ڲ��࣬��Ŀ��Ŀ¼��ģ��xml�����¼���������projectX�º�measurementXͬ����xml����*/
		public TplInPrjClickListener(CVU_BeanSecondStep bean) {
			this.bean = bean;
		}

		@Override
		public boolean onLongClick(View v) {
			String path = bean.getPath();
			String name = bean.getName();
			getTempelate(path, name, "prj");

			Log.e("ard", "adaptersecondstep ���� meaurementXĿ¼��ͬ��xml��" + path);

			notifyDataSetInvalidated();
			
			return true;
		}
	}

	// ---------------------------------------------------------------------

	/** ����Project/measurements�µ�xmlģ�� */
	// 16.3.29 ��
	public void getTempelate(String oldPath, String name, String flag) {
		refresh(oldPath, name, flag);
		Toast.makeText(ctx, R.string.loadTemplate, Toast.LENGTH_SHORT).show();
	}

	private void refresh(String oldPath, String name, String flag) {
		FragmentActivity activity = (FragmentActivity) ctx;
		android.support.v4.app.FragmentManager manager = activity.getSupportFragmentManager();

		PullBookParser templateParser = new PullBookParser(oldPath);
		SharedPreferences preferences = activity.getSharedPreferences("hz_5D", 0);

		SharedPreferences preference = activity.getSharedPreferences("Equipment", 0);
		int equipment_Num = preference.getInt("Equipment", 0);
		int Device_ChannelNum = 0;

		ChannelSettingFragment channelSettingFragment = (ChannelSettingFragment) manager.findFragmentByTag("channelSetting");
		AnalogFragment analogFragment = channelSettingFragment.getAnalog();

		if (analogFragment.getActivity() != null) {
			analogFragment.clearOldChannel();
		}

		reLoadPreferences(templateParser, preferences);

		switch (equipment_Num) {
		case 0:// ��Ӳ��
			Device_ChannelNum = 64;
			break;
		case 1:// �ֻ�
			Device_ChannelNum = 1;
			break;
		case 2:// Ӳ��
			Device_ChannelNum = 8;
			break;
		}

		if (analogFragment != null) {
			if (analogFragment.lvt != null) {
				analogFragment.lvt.removeAllViews();
				analogFragment.InitChannelList(Device_ChannelNum);
			}
		}

		DigitalFragment digital = channelSettingFragment.getDigital();
		if (digital.getView() == null)
			digital.readFromXml(activity);
		else
			digital.reset(activity);

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
		// OrderFragment orderFragment = (OrderFragment)
		// manager.findFragmentByTag("order");

		AcquisitionFragment acquisitionFragment = (AcquisitionFragment) manager.findFragmentByTag("acqui");
		acquisitionFragment.reset(activity);
		
		PreTriggerFragment preTriggerFragment = (PreTriggerFragment) manager.findFragmentByTag("pretrig");
		preTriggerFragment.reset(activity); 
		
		TriggerFragment triggerFragment = (TriggerFragment) manager.findFragmentByTag("trig");
		triggerFragment.reset(activity);

		Log.e("ard", "prj�¸���ģ�壺" + flag);

		CVU_SPUtil.saveTemplateFromWhere(ctx, flag);
		CVU_SPUtil.saveTemplate4SaveRecord(ctx, oldPath);
		CVU_SPUtil.saveTemplate4SaveRecordParent(ctx, CVU_JSONUtil.bean2Json(parent));
	}

	private void reLoadPreferences(PullBookParser templateParser, SharedPreferences preferences) {
		SharedPreferences.Editor editor = preferences.edit();
		Map<String, String> map = templateParser.getValueMap();
		Set<String> keySet = map.keySet();

		for (String key : keySet) {
			String value = map.get(key);
			editor.putString(key, value);
		}
		editor.commit();
	}

	// //////////////////////////////////////////////////////////////////////////

	/**
	 * <b>����</b>: createLogDialog��������ʾlog<br/>
	 * <b>����</b>: 2016-4-1_����9:57:35 <br/>
	 * 
	 * @param path log�ļ�·��
	 * @author weiyou.cui@ts-tech.com.cn <br/>
	 */
	private void createLogDialog(final String path) {
		if (!path.endsWith(".txt"))
			return;
		
		back.startGetLog();
		
		new AsyncTask<Void, Void, String>(){

			private float maxWid;

			@Override
			protected String doInBackground(Void... params) {
				Rect bounds = new Rect();
				TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);  
			    paint.density = ctx.getResources().getDisplayMetrics().density;
				maxWid = 0f;

				File file = new File(path);
				long length = file.length();
				
				StringBuilder sb = new StringBuilder();
				
				try {
					RandomAccessFile raf = new RandomAccessFile(file, "r");
					if(length > 128000) // 125kb
						raf.seek(length - 128000);
					
					while(raf.getFilePointer() < raf.length()){
						String line = raf.readLine();
						line = new String(line.getBytes("8859_1"), "utf-8") + "\r\n";
						
						sb.append(line);
						
						paint.getTextBounds(line, 0, line.length(), bounds);
						int width = bounds.width();
//						float width = paint.measureText(line);
						if(width > maxWid)
							maxWid = width;
					}
					
					raf.close();
					
				} catch (Exception e) {
					sb.append(e.getMessage() + "\r\n");
				}

				return sb.toString();
			}
			
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				
				back.getLogDatas(result, maxWid);
			}
		}.execute();
	}
}
