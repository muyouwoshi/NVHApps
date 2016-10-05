package draw.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import common.Helper;

import com.calculate.Arith_RPM;

public class RPMHelper extends Helper<Arith_RPM, List<Float>> {
	public RPMHelper(Handler handler, Arith_RPM algorith, ArrayList<List<Float>> result) {
		super(handler, algorith, result);

	}

	protected void getBuffer(Map<Integer,List<int[]>> dataListMap) {
		if (dataListMap == null)
			return;


		sendResult();
	}
}
