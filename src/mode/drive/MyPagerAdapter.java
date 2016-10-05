package mode.drive;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyPagerAdapter extends PagerAdapter{

	public ArrayList<View> views;
	public MyPagerAdapter(ArrayList<View> views) {
		// TODO Auto-generated constructor stub
		this.views = views;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(views.get(position));
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(views.get(position));
		return views.get(position);
	}
	@Override
    public int getItemPosition(Object object) {
        // refresh all fragments when data set changed
        return PagerAdapter.POSITION_NONE;
    }
	public void setData(ArrayList<View> views){
        this.views = views;
	}
	public void addView (View v, int position)
	  {
	    views.add (position, v);
	  }

	  public void removeView (int position)
	  {
	    views.remove (position);
	  }
}
