package left.drawer;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_ScreenUtil;
import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

public class LanguagePopWindow extends PopupWindow implements OnItemClickListener {
	public Activity context = null;
	public ListView listView = null;
	public ArrayList<RelativeLayout> Layout_list = new ArrayList<RelativeLayout>();
	private int index = 0;
	private final String[] language = { "", "", "", "", "", "", "" };
	private final int[] language_ico = { R.drawable.ico_gb, R.drawable.ico_de, R.drawable.france, R.drawable.ico_cn, R.drawable.japan, R.drawable.korea, R.drawable.ico_ru };
	private MainFragment mainFragment = null;
	public LanguagePopWindow(Activity context, MainFragment mainFragment) { // ,View v
		this.context = context;
		this.mainFragment = mainFragment;

		View view = View.inflate(context, R.layout.left_popwindow, null); // TODO
		this.setContentView(view);
		this.setFocusable(true);
		this.setTouchable(true);
		this.setOutsideTouchable(true);
		this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
		TypedArray typedArray = view.getContext().obtainStyledAttributes(R.styleable.myStyle);
		this.setBackgroundDrawable(typedArray.getDrawable(R.styleable.myStyle_input_box2));
		typedArray.recycle();
		this.update();

		listView = (ListView) view.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		InitListView(language);
		
		SharedPreferences language_preference = context.getSharedPreferences("Language", 0);
		String language = language_preference.getString("language", "chinese");
		if (language.equals("chinese")) {
			index = 3;
		} else if (language.equals("english")) {
			index = 0;
		}
	}

	public void InitListView(String[] list) {
		MyArrayAdapter adapter = new MyArrayAdapter(context, R.layout.skin_pop_list_item);
		for (int i = 0; i < list.length; i++) {
			adapter.add(list[i]);
		}
		listView.setAdapter(adapter);
		
		int screenHeight = CVU_ScreenUtil.getScreenHeight(context);
		this.setHeight(screenHeight/2);

	}

	public class MyArrayAdapter extends ArrayAdapter<String> {

		private int mResource;

		public MyArrayAdapter(Context context, int resource) {
			super(context, resource);
			this.mResource = resource;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(mResource, parent, false);
				holder.text = (TextView) convertView.findViewById(R.id.textView);
				holder.layout = (RelativeLayout) convertView.findViewById(R.id.layout);
				holder.popuwindow_layout = (RelativeLayout)convertView.findViewById(R.id.popuwindow_layout);
				// viewHolders.add(holder);
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			TypedArray typedArray = convertView.getContext().obtainStyledAttributes(R.styleable.myStyle);
			holder.popuwindow_layout.setBackground(typedArray.getDrawable(R.styleable.myStyle_popuWindowItem));
			typedArray.recycle();
			holder.text.setText("");// list[position]
			holder.text.setBackgroundResource(language_ico[position]);
			holder.text.setGravity(Gravity.CENTER);
			holder.text.setTextColor(Color.WHITE);
			// viewHolders.get(3).layout.setVisibility(View.VISIBLE);
			Layout_list.add(holder.layout);
			if (Layout_list.size() > index) {
				if(position==index){
					Layout_list.get(index).setVisibility(View.VISIBLE);
				}else{
					Layout_list.get(position).setVisibility(View.GONE);
				}
			}
			return convertView;
		}

	}

	public final class ViewHolder {
		public TextView text;
		public RelativeLayout layout;
		public RelativeLayout popuwindow_layout;
	}

	public MainFragment getMainFragment() {
		return mainFragment;
	}
	
	public void showPopupWindow(View v) {
		this.setWidth(v.getWidth());
		if (!this.isShowing()) {
			this.showAsDropDown(v, 0, 5); 
		} else {
			this.dismiss();
		}
	}

	public int dip2px(float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SharedPreferences language_preference = context.getSharedPreferences("Language", 0);
		SharedPreferences.Editor editor = language_preference.edit();

		MainActivity parentActivity = (MainActivity) context;
		//
//		String getLanguageValues = context.getIntent().getStringExtra("LanguageIntent");
		switch (position) {

		case 0:// USB
			editor.putString("language", "english");
			editor.commit();
			parentActivity.updateLange(Locale.ENGLISH);
			mainFragment.languageNumber = 1;
			//parentActivity.onReStart();
			mainFragment.onReStart();
			mainFragment.adapter.notifyDataSetChanged();
			context.getIntent().putExtra("LanguageIntent", "EnglishEnter");
			// index=0;
			break;
		case 2:// 3G/4G
			break;
		case 3:// wifi
			editor.putString("language", "chinese");
			editor.commit();
			parentActivity.updateLange(Locale.SIMPLIFIED_CHINESE);
			mainFragment.languageNumber = -1;
			//parentActivity.onReStart();
			mainFragment.onReStart();
			mainFragment.adapter.notifyDataSetChanged();
			context.getIntent().putExtra("LanguageIntent", "ChineseEnter");
			// index=3;
			break;
		}
		for (int i = 0; i < Layout_list.size(); i++) {
			if (Layout_list.get(i).equals(position)) {
				Layout_list.get(i).setVisibility(View.VISIBLE);
			} else {
				Layout_list.get(i).setVisibility(View.GONE);
			}
		}
		index = position;
		this.dismiss();
	}
}
