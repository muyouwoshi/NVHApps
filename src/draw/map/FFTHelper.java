package draw.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import common.Helper;

import com.calculate.Arith_FFT;

public class FFTHelper extends Helper<Arith_FFT, List<Float>>{
	public FFTHelper(Handler handler, Arith_FFT algorith, ArrayList<List<Float>> result) {
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
