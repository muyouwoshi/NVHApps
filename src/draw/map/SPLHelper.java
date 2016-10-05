package draw.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import common.Helper;

import com.calculate.Arith_AI;
import com.calculate.Arith_SPL;

public class SPLHelper extends Helper<Arith_SPL, List<Double>> {
	private Arith_SPL spl;
	private ArrayList<List<Double>> result;
	private List<Integer> lineDataSizeList;//存储各通道计算到第几个int[]
	private int lineDataSizeListIndex;
	private int dataIndex;
	private boolean exit=false;
	private int dataHasCalculateOverSize;
	public SPLHelper(Handler handler, Arith_SPL algorith, ArrayList<List<Double>> result) {
		super(handler, algorith, result);
		spl=new Arith_SPL();
		this.result=result;
		lineDataSizeList=new ArrayList<Integer>();
	}

	protected void getBuffer(Map<Integer,List<int[]>> dataListMap) {
		if (dataListMap == null||dataListMap.size()==0)
			return;
		while(!exit){
			lineDataSizeListIndex=0;
			Iterator <Integer>it=dataListMap.keySet().iterator();
			while(it.hasNext()){
				int index=it.next();
				List<int[]> dataList=dataListMap.get(index);
				//------------初始化lineDataSizeList-------------
				if(lineDataSizeList.size()<lineDataSizeListIndex+1){
					for(int j=lineDataSizeList.size();j<lineDataSizeListIndex+1;j++){
							lineDataSizeList.add(0);
					}
				}
				dataIndex=lineDataSizeList.get(lineDataSizeListIndex);//获取当前通道计算到哪个int[] 
				//------------当该通道数据利用殆尽后的处理----------------
				if(dataIndex==dataList.size()){
					lineDataSizeList.set(lineDataSizeListIndex, dataList.size()-1);
					if(dataHasCalculateOverSize!=lineDataSizeList.size()-1){
						++dataHasCalculateOverSize;
					}else{
						exit=true;
					}
					continue;
				}
				//---------------计算-------------------------
				int[] buffer=dataList.get(dataIndex);
				spl.calculate(buffer, buffer.length);
				double[] SPLResult = spl.getResult();
				
				List<Double> lineList=result.get(index);
				for (int j = 0; j < SPLResult.length;j++) {
					lineList.add(SPLResult[j]);
				}
				++lineDataSizeListIndex;
			}
			lineDataSizeList.set(lineDataSizeListIndex, ++dataIndex);
		}
		sendResult();
	}
}
