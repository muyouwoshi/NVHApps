package right.drawer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import right.drawer.FileListAdapter.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cuiweiyou.fragment.CVU_RightDrawerFragment;
import com.example.mobileacquisition.MainActivity;
import common.XclSingalTransfer;
public class RightDownListener implements OnTouchListener {
	private View convertView;
	private ViewGroup parent;
	private float x, y, upx, upy;
	private long startTime = 0;
	private long endTime = 0;
	private Context context;
	private List<FileNode> fileNodeList;
	private List<String> filesList;
	private FileListAdapter fileListAdapter;
	private String fileName;
//	private RightDrawerFragment rightDrawerFragment;
	private CVU_RightDrawerFragment rightDrawerFragment;
	protected boolean ifEnterToTemplate=false;
	protected boolean ifEnterToProject=false;
	protected int position=-1;
	
	public RightDownListener(Activity context,FileListAdapter fileListAdapter) {
		this.context = context;
		this.fileListAdapter=fileListAdapter;
		rightDrawerFragment=((MainActivity)context).rightDrawerFragment;
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		
		fileNodeList=fileListAdapter.getFileNodeList();
		filesList=fileListAdapter.getFilesList();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			x = event.getX(0);
			y = event.getY(0);
			startTime = event.getDownTime();
		}
		fileName=((ViewHolder)view.getTag()).fileName.getText().toString();
		if (event.getAction() == MotionEvent.ACTION_UP) {
			upx = event.getX(event.getPointerCount() - 1) - x;
			upy = Math.abs(event.getY(event.getPointerCount() - 1) - y);
			endTime = event.getEventTime();
			// final int position1 = ((ListView) view).pointToPosition((int) x,
			// (int) y);
			if (upy < 100 && endTime - startTime > 50) {//向左滑
				if (upx < -150 && ifEnterToProject) {
					//载入
					if(ifEnterToProject){
						XclSingalTransfer xclSingalTransfer = XclSingalTransfer.getInstance();
						Handler newProjectHandler = (Handler) xclSingalTransfer
								.getValue("newProjectHandler");
						Message msg = newProjectHandler.obtainMessage();
						msg.obj = fileName;
						newProjectHandler.sendMessage(msg);
//						rightDrawerFragment.ifoldProjectDeleted = false; TODO 修改为CVU的
						Toast.makeText(context, fileName + " has be loaded!",
								Toast.LENGTH_SHORT).show();
					}
				} else if (upx > 100) {//向右滑				
					//删除
					if(ifEnterToTemplate){
						File file = new File("/sdcard/data/Template/"+fileName);
//						rightDrawerFragment.deleteFiles(file); TODO 修改为CVU的
						filesList.remove(fileName);
						fileListAdapter.setFilesList(filesList);
						fileListAdapter.notifyDataSetChanged();
					}else{
						FileNode fileNode=fileNodeList.get(filesList.indexOf(fileName));
						File file = new File("/sdcard/data/pro/"
								+ "/"
								+ fileNode.getPartFileName(fileName));
//						rightDrawerFragment.deleteFiles(file); TODO 修改为CVU的
						filesList.remove(fileName);
						fileNodeList.remove(fileNode);
						fileListAdapter.setFilesList(filesList);
						fileListAdapter.setFileNodeList(fileNodeList);
						fileListAdapter.notifyDataSetChanged();
					}
					Toast.makeText(context, fileName + " has be deleted!",
							Toast.LENGTH_SHORT).show();
					return true;
				}
			}
		}
		return false;
	}
	// /* 删除item，并播放动画
	// @param rowView 播放动画的view
	// @param positon 要删除的item位置
	// */
	// public void removeListItem(View rowView, final int position) {
	//
	// final Animation animation = (Animation)
	// AnimationUtils.loadAnimation(rowView.getContext(), R.drawable.item_anim);
	// animation.setAnimationListener(new AnimationListener() {
	// public void onAnimationStart(Animation animation) {}
	// public void onAnimationRepeat(Animation animation) {}
	// public void onAnimationEnd(Animation animation) {
	// animation.cancel();
	// }
	// });
	// rowView.startAnimation(animation);
	// }
}
