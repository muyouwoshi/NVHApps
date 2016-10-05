package draw.map;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import common.Helper;

import com.calculate.Arith_Order;

public class OrderHelper extends Helper<Arith_Order, List<Float>> {
	public OrderHelper(Handler handler, Arith_Order algorith, ArrayList<List<Float>> result) {
		super(handler, algorith, result);

	}

	protected void getBuffer(Map<Integer,List<int[]>> dataListMap) {
		if (dataListMap == null)
			return;


		sendResult();
	}
}
