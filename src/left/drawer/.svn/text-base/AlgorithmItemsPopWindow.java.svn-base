package left.drawer;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.example.mobileacquisition.R;

public class AlgorithmItemsPopWindow extends PopupWindow implements OnItemClickListener {
	public View view = null;
	public Activity context = null;
	public ListView listView = null;
	private ArrayList<String> list = new ArrayList<String>();

	public AlgorithmItemsPopWindow(Activity context, View v) {
		this.view = v;
		this.context = context;
		ViewGroup root = (ViewGroup) context.findViewById(android.R.id.content);
		View view = LayoutInflater.from(context).inflate(R.layout.left_algorithm_popwindow, root, false);
		listView = (ListView) view.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		this.setContentView(view);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setWidth(v.getWidth());
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// this.setBackgroundDrawable(new ColorDrawable(0x00000000));
		this.setBackgroundDrawable(new ColorDrawable(Color.CYAN));
		this.update();
	}

	public void InitListView(ArrayList<String> list) {
		this.list = list;
		MyArrayAdapter adapter = new MyArrayAdapter(context, R.layout.pop_list_item);
		for (int i = 0; i < list.size(); i++) {
			adapter.add(list.get(i));
		}
		// 婵炶揪绲界涵娌瑂tView闁荤姳绀佹晶浠嬫偪閸滎櫔apter
		listView.setAdapter(adapter);
		// this.setHeight(dip2px(35)*list.size());//+dip2px(5));
	}

	public class MyArrayAdapter extends ArrayAdapter<String> {

		private int mResource;

		public MyArrayAdapter(Context context, int resource) {
			super(context, resource);
			this.mResource = resource;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(mResource, parent, false);
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.textView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// holder.text.setBackgroundResource(R.drawable.ico_cn);
			holder.text.setText(list.get(position));
			holder.text.setTextColor(Color.WHITE);
			if(((TextView)view).getText().toString().equals(holder.text.getText().toString())){
				holder.text.setTextColor(Color.BLACK);
			}
			return convertView;
		}
	}

	public final class ViewHolder {
		public TextView text;
	}

	public void showPopupWindow() {
		if (!this.isShowing()) {
			this.showAsDropDown(view, 0, -dip2px(55) - list.size() * dip2px(35));
		} else {
			this.dismiss();
		}
	}
	public void showPopupWindow2() {
		if (!this.isShowing()) {
			this.showAsDropDown(view, 0, -dip2px(25)- list.size() * dip2px(35));
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
		((TextView) this.view).setText(list.get(position));
		this.dismiss();
	}
}