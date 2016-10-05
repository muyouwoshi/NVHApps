package right.drawer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class RightExplandableListView extends ExpandableListView{
	private String[] groupItems = { "Data", "Template"};
	private String[][] childItems = {{},{"Data","ABC" }};
	private FragmentActivity context = null;
	private RightExplandAdapter adapter = null;
	private GroupOnClickListener groupOnClickListener;
	private ChildOnClickListener childOnClickListener;
	private FileListAdapter fileListAdapter;
	public RightExplandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = (FragmentActivity)context;
		this.setGroupIndicator(null);
		this.setVerticalScrollBarEnabled(false);
		/* 隐藏选择的黄色高亮 */
		ColorDrawable drawable_tranparent = new ColorDrawable(Color.rgb(75, 119, 190));
		setSelector(drawable_tranparent);
		adapter = new RightExplandAdapter();
		adapter.setChildItems(childItems);
		
		adapter.setGroupItems(groupItems);
		adapter.setContext(context);
		setAdapter(adapter);
		
		groupOnClickListener=new GroupOnClickListener(context);
		groupOnClickListener.setRightExplandableListView(this);
		setOnGroupClickListener(groupOnClickListener);
//		
		 childOnClickListener=new ChildOnClickListener(context);
		 childOnClickListener.setRightExplandableListView(this);
		setOnChildClickListener(childOnClickListener);
		
		groupOnClickListener.setChildOnClickListener(childOnClickListener);
		childOnClickListener.setGroupOnClickListener(groupOnClickListener);
	}
	public void setFileListAdapter(FileListAdapter fileListAdapter){
		this.fileListAdapter=fileListAdapter;
	}
	public FileListAdapter getFileListAdapter(){
		return fileListAdapter;
	}
	public Handler getSpeedEnterHandler(){
		return groupOnClickListener.getSpeedEnterHandler();
	}
}
