package com.cuiweiyou.dynamiclevelnavigation.back;

/** 打印日志接口 */
public interface CVU_ILogBack {
	/** 开始从文件读日志 */
	void startGetLog();
	/** 读取完毕，显示。maxWid：行文本最大宽度 */
	void getLogDatas(String datas, float maxWid);
}
