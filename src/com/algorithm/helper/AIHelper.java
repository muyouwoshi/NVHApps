package com.algorithm.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.calculate.Arith_AI;
import common.DataCollection;

import draw.map.AI;

public class AIHelper extends Helper<Arith_AI, Void> {

	private static class AIHelperHolder {
		private static final AIHelper INSTANCE = new AIHelper();
	}

	protected AIHelper() {
		super();
		arith = new Arith_AI();
	}

	public static final AIHelper getInstance() {
		return AIHelperHolder.INSTANCE;
	}

	protected void caculate(Map<Integer, List<int[]>> dataListMap) {
		// TODO Auto-generated method stub
		if (dataListMap == null || dataListMap.size() == 0)
			return;
		// while(!exit){
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
		ArrayList<List<Float>> result = null;
		while (it.hasNext()) {
			int index = it.next();
			List<int[]> dataList = dataListMap.get(index);
			dataIndex = lineDataSizeList.get(lineDataSizeListIndex);// 获取当前通道计算到哪个int[]
			if (dataList == null)
				return;
			// ------------当该通道数据利用殆尽后的处理----------------
			if (dataIndex == dataList.size()) {
				lineDataSizeList
						.set(lineDataSizeListIndex, dataList.size() - 1);
				continue;
			}
			// ---------------计算-------------------------
			int[] buffer = dataList.get(dataIndex);
			arith.calculate(buffer, buffer.length);
			float[] AIResultData = arith.getAi_Time(0);

			for (int i = 0; i < resultList.size(); i++) {
				AI aiview = (AI) viewList.get(i);
				result = aiview.getResultBuffer();
				if (result.size() < lineDataSizeListIndex + 1)
					result.add(new ArrayList<Float>());
				List<Float> lineList = result.get(lineDataSizeListIndex);
				for (int j = 0; j < AIResultData.length; j++) {
					lineList.add(AIResultData[j]);

					if (lineList.size() >= resultListMaxlenght.get(i)) {
						lineList.remove(0);

					}

				}

			}
			if (lineDataSizeList.size() == 0) {
				break;
			}
			dataIndex += 1;
			lineDataSizeList.set(lineDataSizeListIndex, dataIndex);
			lineDataSizeListIndex += 1;
		}
		// }
		lineDataSizeListIndex = 0;

		notifyDataChange();
	}

	@Override
	protected void caculate(int[] buffer) {
		// TODO Auto-generated method stub
		if (buffer == null || buffer.length == 0)
			return;

		ArrayList<List<Float>> result = null;

		arith.calculate(buffer, buffer.length);
		float[] AIResultData = arith.getAi_Time(0);

		for (int i = 0; i < resultList.size(); i++) {
			AI aiview = (AI) viewList.get(i);
			result = aiview.getResultBuffer();
			if(result.size() ==0) result.add(new ArrayList<Float>());
			List<Float> lineList =result.get(0);
			for (int j = 0; j < AIResultData.length; j++) {
				lineList.add(AIResultData[j]);

				if (lineList.size() >= resultListMaxlenght.get(i)) {
					lineList.remove(0);
				}

			}
//			result.add(lineList);

		}
		notifyDataChange();
	}
}
