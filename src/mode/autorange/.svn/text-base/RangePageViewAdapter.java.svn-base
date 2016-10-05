package mode.autorange;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class RangePageViewAdapter extends PagerAdapter{
	private List<View> rangePageViewList;
	private View view;
	private int pageNum=0;
	public RangePageViewAdapter( List<View> rangePageViewList){
		this.rangePageViewList=rangePageViewList;
	}
	public void setRangePageViewList(List<View> rangePageViewList){
		this.rangePageViewList=rangePageViewList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return rangePageViewList.size();
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(rangePageViewList.get(position));
		view=rangePageViewList.get(position);
		return view;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(rangePageViewList.get(position));
	} 
	public View getView(){
		return view;
	}
	public int getPageNum(){
		pageNum= rangePageViewList.indexOf(view);
		if(pageNum==-1){
			pageNum=0;
		}
		return pageNum;
	}
}
