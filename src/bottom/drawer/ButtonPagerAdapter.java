package bottom.drawer;

import java.util.List;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ButtonPagerAdapter extends PagerAdapter{
	private List<View> bt_views;
	public ButtonPagerAdapter(List<View> views) {
		this.bt_views = views;
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bt_views.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position,
			Object object) {
		// TODO Auto-generated method stub
		container.removeView(bt_views.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(bt_views.get(position));

		return bt_views.get(position);
	}
	
}
