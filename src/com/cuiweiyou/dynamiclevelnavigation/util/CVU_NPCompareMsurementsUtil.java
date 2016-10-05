package com.cuiweiyou.dynamiclevelnavigation.util;

import java.util.Comparator;

import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanSecondStep;

/** 自定义 BeanNameAndPath 排序比较器 */
public class CVU_NPCompareMsurementsUtil<T> implements Comparator<T> {

	@Override
	public int compare(T lhs, T rhs) {
		
		String createTimeL = ((CVU_BeanSecondStep) lhs).getCreateTime();
		String createTimeR = ((CVU_BeanSecondStep) rhs).getCreateTime();
		
		if("".equals(createTimeL) && !"".equals(createTimeR))
			return 1;
		else if(!"".equals(createTimeL) && "".equals(createTimeR))
			return -1;
		else if("".equals(createTimeL) && "".equals(createTimeR))
			return 0;
		
		Long valueL = Long.valueOf(createTimeL);
		Long valueR = Long.valueOf(createTimeR);
		
		int b = valueL < valueR ? 1 : -1;
		
		if(valueL == valueR)
			b = 0;
		
		return b;
	}

}
