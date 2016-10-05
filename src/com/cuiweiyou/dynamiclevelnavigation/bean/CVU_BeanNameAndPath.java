package com.cuiweiyou.dynamiclevelnavigation.bean;

/**
 * <b>����</b>: BeanNameAndPath��<br/>
 * <b>˵��</b>: �ļ������������ڣ�����·�� <br/>
 */
public class CVU_BeanNameAndPath {

	private String name;
	private String createTime;
	private String path;

	/** 
	 * 
	 * @param name ���ļ�����ȡ����ʾ����
	 * @param createTime ���ļ�����ȡ�Ĵ�������
	 * @param path �ļ�ȫ·��
	 * */
	public CVU_BeanNameAndPath(String name, String createTime, String path) {
		super();
		this.name = name;
		this.createTime = createTime;
		this.path = path;
	}

	public CVU_BeanNameAndPath() {
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

	@Override
	public String toString() {
		return "BeanNameAndPath [name=" + name + ", createTime=" + createTime + ", path=" + path + "]";
	}

}
