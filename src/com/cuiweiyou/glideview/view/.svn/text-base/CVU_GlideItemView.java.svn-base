package com.cuiweiyou.glideview.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanNameAndPath;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_JSONUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_SPUtil;
import com.cuiweiyou.glideview.back.Back4ProjectDelete;
import com.cuiweiyou.glideview.back.Back4ProjectLoad;
import com.cuiweiyou.glideview.back.Back4TemplateDelete;
import com.cuiweiyou.glideview.back.Back4TemplateLoad;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

/**
 * <b>类名</b>: CVU_GlideItemView，可侧滑Item<br/>
 * <b>说明</b>: CVU_GlideListView、CVU_ExpandableListView都能用 <br/>
 * extends LinearLayout
 */
public class CVU_GlideItemView extends LinearLayout implements OnClickListener {
	public static final String TAG = "ard";

	private static final int TAN = 2;
	private int mHolderWidth = 160;
	private float mLastX = 0;
	private float mLastY = 0;
	private RelativeLayout mProjectContent;
	private RelativeLayout mViewContent;
	private Scroller mScroller;
	private Context mContext;
	private TextView back;

	private int positionPrj;
	private int childPositionTpl;
	private int groupPositionTpl;

	private ImageView prj_load;
	private ImageView prj_del;
	private ImageView tpl_delete;
	private ImageView tpl_load;

	private Back4TemplateDelete back4TemplateDel;
	private Back4ProjectDelete back4ProjectDel;

	private Back4ProjectLoad loaderPrj;
	private Back4TemplateLoad loaderTml;

	private String currentprj;

	private String currenttmpl;

	public TextView getBack() {
		return back;
	}

	public CVU_GlideItemView(Context context, Resources resources, View content, int groupPosition, int childPosition, String name, Back4TemplateDelete back, Back4TemplateLoad loaderTml) {
		super(context);
		this.groupPositionTpl = groupPosition;
		this.childPositionTpl = childPosition;
		this.currenttmpl = name;
		this.back4TemplateDel = back;
		this.loaderTml = loaderTml;
		initTemplateView(context, resources, content);
	}

	public CVU_GlideItemView(Context context, Resources resources, View content, int position, String name, Back4ProjectDelete back4ProjectDel, Back4ProjectLoad loaderPrj) {
		super(context);
		this.positionPrj = position;
		this.currentprj = name;
		this.back4ProjectDel = back4ProjectDel;
		this.loaderPrj = loaderPrj;
		initProjectView(context, resources, content);
	}

	private void initProjectView(Context context, Resources resources, View content) {
		setOrientation(LinearLayout.HORIZONTAL);
		this.mContext = context;

		mScroller = new Scroller(context);

		View view = LayoutInflater.from(context).inflate(resources.getLayout(R.layout.cvu_glide_item_bottom), this);
		prj_load = (ImageView) view.findViewById(R.id.prj_load);
		prj_del = (ImageView) findViewById(R.id.prj_del);
		if(ThemeLogic.themeType==1){
			prj_load.setBackgroundResource(R.drawable.button_blue_n);//cvu_btn_load
//			prj_load.setImageResource(R.drawable.load);
//			prj_del.setImageResource(R.drawable.delete);
		}else{
			prj_load.setBackgroundResource(R.drawable.title_bg1);
//			prj_load.setImageResource(R.drawable.load1);
//			prj_del.setImageResource(R.drawable.delete1);
		}
//		prj_load.setText(R.string.Load);
//		prj_del.setText(R.string.Delete);
		prj_load.setOnClickListener(this);
		prj_del.setOnClickListener(this);

		mHolderWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics()));

		mProjectContent = (RelativeLayout) view.findViewById(R.id.view_content);
		if (content != null) {
			mProjectContent.addView(content);
		}
	}

	private void initTemplateView(Context context, Resources resources, View content) {
		setOrientation(LinearLayout.HORIZONTAL);
		this.mContext = context;
		mScroller = new Scroller(context);

		View view = LayoutInflater.from(context).inflate(resources.getLayout(R.layout.cvu_glide_item_bottom), this);
//		tpl_delete = (TextView) view.findViewById(R.id.delete);
//		tpl_delete.setText(R.string.Delete);
//		tpl_delete.setOnClickListener(this);
		
		tpl_load = (ImageView) view.findViewById(R.id.prj_load);
		tpl_delete = (ImageView) findViewById(R.id.prj_del);
		if(ThemeLogic.themeType==1){
			tpl_load.setBackgroundResource(R.drawable.button_blue_n);//cvu_btn_load
//			tpl_load.setImageResource(R.drawable.load);
//			tpl_delete.setImageResource(R.drawable.delete);
		}else{
			tpl_load.setBackgroundResource(R.drawable.title_bg1);
//			tpl_load.setImageResource(R.drawable.load1);
//			tpl_delete.setImageResource(R.drawable.delete1);
		}
//		tpl_load.setText(R.string.Load);
//		tpl_delete.setText(R.string.Delete);
		tpl_load.setOnClickListener(this);
		tpl_delete.setOnClickListener(this);

		mHolderWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics()));

		mViewContent = (RelativeLayout) view.findViewById(R.id.view_content);
		if (content != null) {
			mViewContent.addView(content);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float x = event.getX();
			float y = event.getY();
			float deltaX = x - mLastX;
			float deltaY = y - mLastY;
			mLastX = x;
			mLastY = y;
			if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
				break;
			}
			if (deltaX != 0) {
				float newScrollX = getScrollX() - deltaX;
				if (newScrollX < 0) {
					newScrollX = 0;
				} else if (newScrollX > mHolderWidth) {
					newScrollX = mHolderWidth;
				}
				this.scrollTo((int) newScrollX, 0);
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	private void smoothScrollTo(int destX, int destY) {
		int scrollX = getScrollX();
		int delta = destX - scrollX;
		mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 1);
		invalidate();
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	/**
	 * 获取view是需要重置缓存状态
	 */
	public void shrink() {
		int offset = getScrollX();
		if (offset == 0) {
			return;
		}
		scrollTo(0, 0);
	}

	public void setContentView(View view) {
		if (mViewContent != null) {
			mViewContent.addView(view);
		}
	}

	public void setPositon(int position) {
		this.positionPrj = position;
	}

	public void reset() {
		int offset = getScrollX();
		if (offset == 0) {
			return;
		}
		smoothScrollTo(0, 0);
	}

	public void adjust(boolean left) {
		int offset = getScrollX();
		if (offset == 0) {
			return;
		}
		if (offset < 20) {
			this.smoothScrollTo(0, 0);
		} else if (offset < mHolderWidth - 20) {
			if (left) {
				this.smoothScrollTo(mHolderWidth, 0);
			} else {
				this.smoothScrollTo(0, 0);
			}
		} else {
			this.smoothScrollTo(mHolderWidth, 0);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == prj_load) {
			loaderPrj.projectLoad(positionPrj);
		} else if (v == tpl_load) {
			loaderTml.templateLoad(groupPositionTpl, childPositionTpl);
		} else if (v == prj_del) {
			if( 1 == checkCurrentProject()){ // 判断是不是当前加载的项目
				dialogDelete(false);
			}
		} else if (v == tpl_delete) {
			if(1== checkCurrentTemplate()){
				dialogDelete(true);
			}
		}
	}

	/**
	 * isTemplate：true-abc下的模板，false-dataX下的项目
	 */
	private void dialogDelete(final boolean isTemplate) {
		final AlertDialog builder = new AlertDialog.Builder(mContext).create();
		// 2. 删除对话框
		RelativeLayout layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.dialog_del, null, false);
		
		builder.setCancelable(false);
		builder.show();
		builder.getWindow().setContentView(layout);
		builder.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isTemplate) {
					back4ProjectDel.projectDelete(positionPrj);
				} else {
					back4TemplateDel.templateDelete(childPositionTpl);
				}

				builder.dismiss();
			}
		});
		// 4. 取消按钮
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
				builder.dismiss();
			}
		});
	}

	/** @return 0 = 选中的是加载的模板，禁止删除。1 = 可以删除 */
	private int checkCurrentTemplate() {
		
		String t4sr = CVU_SPUtil.getTemplate4SaveRecord(mContext);
		if(t4sr.equals(currenttmpl)){
			Toast.makeText(mContext, R.string.template_cant_delete_message, 0).show();
			return 0;
		}
		
		return 1;
	}

	/** @return 0 = 选中的是加载项目，禁止删除。1 = 可以删除 */
	private int checkCurrentProject() {
		String currentProjectX = CVU_SPUtil.getCurrentProject(mContext, "currentProject");
		
		if(null != currentProjectX && !"".equals(currentProjectX)){
			CVU_BeanNameAndPath b222 = CVU_JSONUtil.json2Bean(currentProjectX);
			String name = b222.getName();
			
			if(name.equals(currentprj)){
				Toast.makeText(mContext, R.string.project_cant_delete_message, 0).show();
				return 0;
			}
			else {
				return 1;
			}
			
		} else {
			return 1;
		}
	}
}
