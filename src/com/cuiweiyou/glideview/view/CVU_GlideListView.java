package com.cuiweiyou.glideview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * �໬ListView
 */
public class CVU_GlideListView extends ListView {

	/** �û�������С���� */
	private int touchSlop;
	/** �Ƿ���Ӧ���� */
	private boolean isSliding;
	/** ��ָ����ʱx���� */
	private int xDown;
	/** ��ָ����ʱ��y���� */
	private int yDown;
	/** ��ָ�ƶ�ʱ��x���� */
	private int xMove;
	/** ��ָ�ƶ�ʱ��y���� */
	private int yMove;

	boolean isChild;

	private View itemLayout;

	private CVU_GlideItemView mFocusedItemView;

	/**
	 * ��ǰ��ָ������λ��
	 */
	private int mCurrentViewPos = -1;

	public CVU_GlideListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData(context);
	}

	public CVU_GlideListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context);
	}

	public CVU_GlideListView(Context context) {
		super(context);
		initData(context);
	}

	private void initData(Context context) {
		LayoutInflater.from(context);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			xDown = x;
			yDown = y;
			// ��õ�ǰ��ָ����ʱ��item��λ��
			int position = pointToPosition(xDown, yDown);
			if (mCurrentViewPos != position || isSliding) {
				mCurrentViewPos = position;
				isSliding = false;
				if (mFocusedItemView != null) {
					mFocusedItemView.reset();
				}
			}
			// ��õ�ǰ��ָ����ʱ��item
			itemLayout = getChildAt(mCurrentViewPos - getFirstVisiblePosition());
			if (itemLayout instanceof RelativeLayout) {
				isChild = false;
			} else {
				isChild = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			xMove = x;
			yMove = y;
			int dx = xMove - xDown;
			int dy = yMove - yDown;
			/**
			 * �ж��Ƿ��Ǵ��ҵ���Ļ���
			 */
			if (xMove < xDown && Math.abs(dx) > touchSlop && Math.abs(dy) < touchSlop && isChild) {
				isSliding = true;
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		/**
		 * ����Ǵ��ҵ���Ļ�������Ӧ
		 */
		if (isSliding) {
			switch (action) {
			case MotionEvent.ACTION_MOVE:
				if (mCurrentViewPos != -1) {
					if (Math.abs(yDown - y) < 30 && Math.abs(xDown - x) > 20) {
						int first = this.getFirstVisiblePosition();
						int index = mCurrentViewPos - first;
						mFocusedItemView = (CVU_GlideItemView) getChildAt(index);
						if(mFocusedItemView==null){
							return false;
						}
						mFocusedItemView.onTouchEvent(ev);
						return true;
					}
				}

				break;
			case MotionEvent.ACTION_UP:
				isChild = false;
				if (isSliding) {
					if (mFocusedItemView != null) {
						mFocusedItemView.adjust(xDown - x > 0);
						return true;
					}
				}
			}
			// ��Ӧ�����ڼ���ĻitemClick�¼������ⷢ����ͻ
			return true;
		}

		return super.onTouchEvent(ev);
	}
}
