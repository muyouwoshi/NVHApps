package left.drawer;

import java.io.Serializable;

import com.example.mobileacquisition.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CanFragment extends Fragment implements Serializable{

	private static final long serialVersionUID = -2610439704553472115L;
	private View view=null;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.can, container, false);
		/*RelativeLayout can_layout=(RelativeLayout)view.findViewById(R.id.can_layout);
		String getSkinValues = getActivity().getIntent().getStringExtra("SkinIntent");
		if (getSkinValues != null) {
			if (getSkinValues.equals("Skin0")) {
				can_layout.setBackgroundColor(Color.WHITE);
			}else if (getSkinValues.equals("Skin1")) {
				can_layout.setBackgroundColor(Color.LTGRAY);
			}
		}*/
		return view;
	}
}
