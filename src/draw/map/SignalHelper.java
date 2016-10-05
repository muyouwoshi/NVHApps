package draw.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import common.Helper;

import com.calculate.Arith_AI;
import com.calculate.Arith_SPL;

public class SignalHelper extends Helper<Object, List<Float>> {

	private ArrayList<List<Float>> result;
	protected List<Integer> lineDataSizeList;//存储各通道计算到第几个int[]
	protected int lineDataSizeListIndex;
	protected int dataIndex;
	private boolean exit=false;
	private int dataHasCalculateOverSize;
	public SignalHelper(Handler handler, Object algorith, ArrayList<List<Float>> result) {
		super(handler, algorith, result);
		this.result=result;
		algorith=null;
		lineDataSizeList = new ArrayList<Integer>();
		lineDataSizeList.add(-1);
	}

	protected void getBuffer(Map<Integer,List<int[]>> dataListMap) {
		if (dataListMap == null || dataListMap.size() == 0)
			return;
		if (lineDataSizeList.get(0) == -1) {
			int size = dataListMap.size();
			if (size > 0) {
				lineDataSizeList.set(0, 0);
				for (int i = 1; i < size; i++) {
					lineDataSizeList.add(0);
				}
			}

		}
		Iterator<Integer> it = dataListMap.keySet().iterator();
		while (it.hasNext()) {
			int index = it.next();
			List<int[]> dataList = dataListMap.get(index);
			dataIndex = lineDataSizeList.get(lineDataSizeListIndex);// 获取当前通道计算到哪个int[]
			// ------------当该通道数据利用殆尽后的处理----------------
			if (dataIndex == dataList.size()) {
				lineDataSizeList.set(lineDataSizeListIndex, dataList.size() - 1);
				continue;
			}
			// ---------------计算-------------------------
			int[] buffer = dataList.get(dataIndex);
		
			if (result.size() < lineDataSizeListIndex + 1)
				result.add(new ArrayList<Float>());
			List<Float> lineList = result.get(lineDataSizeListIndex);
			for (int j = 0; j < buffer.length; j++) {
				lineList.add((float)buffer[j]);
				if (lineList.size() == length)
					lineList.remove(0);
			}
			if (lineDataSizeList.size() == 0) {
				break;
			}
			dataIndex += 1;
			lineDataSizeList.set(lineDataSizeListIndex, dataIndex);
			lineDataSizeListIndex += 1;
		}
		lineDataSizeListIndex = 0;
			sendResult();
	}
}
