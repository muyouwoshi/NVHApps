package com.cuiweiyou.dynamiclevelnavigation.back;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.fortysevendeg.android.swipelistview.BaseSwipeListViewListener;

/** Project item 侧滑的互动事件处理器 */
public class CVU_EProjectSwipeListener extends BaseSwipeListViewListener {
	String TAG = "ard";
	private CVU_IData2ClickBack back;
	
	/** 侧滑listview的互动事件处理器 
	 * @param back 回调器*/
	public CVU_EProjectSwipeListener(CVU_IData2ClickBack back){
		this.back = back;
	}

	@Override
	public void onClickFrontView(int position) {
		Log.d(TAG, "onClickFrontView:" + position);
		Toast.makeText((Context)back, "epsl " + position, 0).show();
		
		back.clickOne(position);
	}
	
	@Override
	public void onChoiceChanged(int position, boolean selected) {
//		Log.d(TAG, "onChoiceChanged:" + position + ", " + selected);
	}

	@Override
	public void onChoiceEnded() {
//		Log.d(TAG, "onChoiceEnded");
	}

	@Override
	public void onChoiceStarted() {
//		Log.d(TAG, "onChoiceStarted");
	}

	@Override
	public void onClickBackView(int position) {
//		Log.d(TAG, "onClickBackView:" + position);
	}

	@Override
	public void onListChanged() {
//		Log.d(TAG, "onListChanged");
	}

	@Override
	public void onClosed(int position, boolean fromRight) {
//		Log.d(TAG, "onClosed:" + position + "," + fromRight);
	}

	@Override
	public void onDismiss(int[] arg0) {
//		Log.d(TAG, "onDismiss");
	}

	@Override
	public void onFirstListItem() {
//		Log.d(TAG, "onFirstListItem");
	}

	@Override
	public void onLastListItem() {
//		Log.d(TAG, "onLastListItem");
	}

	@Override
	public void onMove(int position, float x) {
//		Log.d(TAG, "onMove:" + position + "," + x);
	}

	@Override
	public void onOpened(int position, boolean toRight) {
//		Log.d(TAG, "onOpened:" + position + "," + toRight);
	}

	@Override
	public void onStartClose(int position, boolean right) {
//		Log.d(TAG, "onStartClose:" + position + "," + right);
	}

	@Override
	public void onStartOpen(int position, int action, boolean right) {
//		Log.d(TAG, "onStartOpen:" + position + "," + action + "," + right);
	}
}
