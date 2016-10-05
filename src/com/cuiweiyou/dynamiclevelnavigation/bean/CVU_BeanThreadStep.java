package com.cuiweiyou.dynamiclevelnavigation.bean;

import java.util.List;

/** measurement�ļ�����Ϣ */
public class CVU_BeanThreadStep {

	private String name;
	private String createTime;
	private String path;
	private List<CVU_BeanNameAndPath> threads;

	/**
	 *  measurement�ļ�����Ϣ
	 * @param name ���ļ�����ȡ����ʾ����
	 * @param createTime ���ļ�����ȡ�Ĵ�������
	 * @param path �ļ�ȫ·��
	 * @param threads ���һ��Ŀ¼�������
	 * */
	public CVU_BeanThreadStep(String name, String createTime, String path, List<CVU_BeanNameAndPath> threads) {
		super();
		this.name = name;
		this.createTime = createTime;
		this.path = path;
		this.threads = threads;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<CVU_BeanNameAndPath> getThreads() {
		return threads;
	}

	public void setThreads(List<CVU_BeanNameAndPath> threads) {
		this.threads = threads;
	}

	@Override
	public String toString() {
		return "BeanThreadStep [name=" + name + ", createTime=" + createTime + ", path=" + path + ", threads=" + threads + "]";
	}

}
