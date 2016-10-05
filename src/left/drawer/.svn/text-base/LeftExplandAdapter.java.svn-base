package left.drawer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

@SuppressLint("ResourceAsColor")
public class LeftExplandAdapter extends BaseExpandableListAdapter {
	

	private int[] groupItems = {R.string.left_groupItems_1,R.string.left_groupItems_2,R.string.left_groupItems_3,R.string.left_groupItems_4};
	private int[] groupItems1 = {R.color.whites,R.color.whites,R.color.whites,R.color.whites};
	private int[][] childItems= {{R.string.left_childItems_11 ,R.string.left_childItems_12 ,R.string.left_childItems_13 },
								 {R.string.left_childItems_21 ,R.string.left_childItems_22,R.string.left_childItems_23 },
								 {R.string.left_childItems_31 ,R.string.left_childItems_32 ,R.string.left_childItems_33 ,
								  R.string.left_childItems_34,R.string.left_childItems_35 ,R.string.left_childItems_36,R.string.left_childItems_37,R.string.left_childItems_38 }};
	private Activity context;
	
	public LeftExplandAdapter(Context context){
		this.context=(Activity)context;
	}
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groupItems.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub\
		if(groupPosition == 3) return 0;
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

	@SuppressLint("ResourceAsColor")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TextView txt_father;
		 int[] father_icon = new int[4];

		ImageView img_father;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.left_father_item, parent, false);
		}
		txt_father = (TextView) convertView.findViewById(R.id.father_item);
		txt_father.setText(groupItems[groupPosition]);
		
		if(ThemeLogic.themeType==1){
//			txt_father.setBackgroundColor(R.color.left_item);
			txt_father.setTextColor(Color.rgb(255, 255, 255));
			father_icon[0]=R.drawable.channel;
			father_icon[1]=R.drawable.acquisition;
			father_icon[2]=R.drawable.analysis;
			father_icon[3]=R.drawable.display;
		}else{
//			txt_father.setBackgroundColor(R.color.left_item1);
			txt_father.setTextColor(Color.rgb(0,0, 0));
			father_icon[0]=R.drawable.channel_skin2;
			father_icon[1]=R.drawable.acquisition_skin2;
			father_icon[2]=R.drawable.analysis_skin2;
			father_icon[3]=R.drawable.display_skin2;	
			
		}
		
		img_father = (ImageView) convertView
				.findViewById(R.id.father_image);
		img_father.setImageResource(father_icon[groupPosition]);
		return convertView;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		TextView txt_child;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.left_child_item, parent, false);
		}
		txt_child = (TextView) convertView.findViewById(R.id.child_item_all);
		txt_child.setText(childItems[groupPosition][childPosition]);
		if(ThemeLogic.themeType==1){
			txt_child.setBackgroundColor(R.color.left_item);
			txt_child.setTextColor(Color.rgb(255,255, 255));
		}else{
			txt_child.setBackgroundColor(R.color.left_item1);
			txt_child.setTextColor(Color.rgb(0,0, 0));
		}
		
	
	
		
		/*
		 * ����
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