package bottom.drawer;

import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;

public class ChanPagerAdapter extends bottom.drawer.PagerAdapter{
	
	Context context;
	private List<View> chan_views;
	
	public ChanPagerAdapter(Context context, List<View> views) {
		this.context = context;
		this.chan_views = views;
	}
	
	@Override
	public void destroyItem(View container, int position, Object arg2) {
		((VerticalViewPager)container).removeView(chan_views.get(position));
		
	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return chan_views.size();
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((VerticalViewPager)container).addView(chan_views.get(position));
		return chan_views.get(position);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
}