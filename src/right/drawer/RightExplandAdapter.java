package right.drawer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.mobileacquisition.R;

public class RightExplandAdapter extends BaseExpandableListAdapter {
	private  String[] groupItems=null;
	private String[][] childItems=null;
	private Activity context;
	public void setGroupItems(String[] groupItems){
		this.groupItems=groupItems;
	}
	public void setChildItems(String[][] childItems){	
		this.childItems=childItems;
	}
	public void setContext(Context context){
		this.context=(Activity)context;
	}
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groupItems.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return childItems[groupPosition].length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groupItems[groupPosition];
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childItems[groupPosition][childPosition];
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TextView txt_father;
//		ImageView img_father;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.left_father_item, parent, false);
		}
		txt_father = (TextView) convertView.findViewById(R.id.father_item);
//		img_father = (ImageView) convertView
//				.findViewById(R.id.father_image);
//		img_father.setImageResource(father_icon[groupPosition]);
		txt_father.setText(groupItems[groupPosition]);
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		TextView txt_child;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.left_child_item, parent, false);
		}
		txt_child = (TextView) convertView.findViewById(R.id.child_item_all);
		txt_child.setText(childItems[groupPosition][childPosition]);
		/*
		 * »»·ô
		 */
		/*SkinChanged skinChanged=new SkinChanged();
		skinChanged.setLeftChildItemView(convertView);
		String getSkinValues=context.getIntent().getStringExtra("SkinIntent");
		if(getSkinValues!=null){
			if(getSkinValues.equals("Skin0")){
				skinChanged.leftChildItemSkin0();
			}else if(getSkinValues.equals("Skin1")){
				skinChanged.leftChildItemSkin1();
			}
		}*/
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
}