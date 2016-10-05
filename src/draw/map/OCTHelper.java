package draw.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import common.Helper;

import com.calculate.Arith_OCT;

public class OCTHelper extends Helper<Arith_OCT, List<Float>> {
	public OCTHelper(Handler handler, Arith_OCT algorith, ArrayList<List<Float>> result) {
		super(handler, algorith, result);

	}


	@Override
	protected void getBuffer(Map<Integer, List<int[]>> dataListMap) {
		// TODO Auto-generated method stub
		if (dataListMap == null)
			return;


		sendResult();
	}
}
