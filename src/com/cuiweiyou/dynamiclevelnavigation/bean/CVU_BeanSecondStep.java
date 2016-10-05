package com.cuiweiyou.dynamiclevelnavigation.bean;

import java.util.List;

/**
 * <b>����</b>: BeanSecondStep��project�ļ�����Ϣ<br/>
 */
public class CVU_BeanSecondStep {

	private String name;
	private String createTime;
	private String path;
	private List<CVU_BeanThreadStep> seconds;

	/**
	 * <b>����</b>: BeanSecondStep��project�ļ�����Ϣ<br/>
	 * @param name ���ļ�����ȡ����ʾ����
	 * @param createTime ���ļ�����ȡ�Ĵ�������
	 * @param path �ļ�ȫ·��
	 * @param seconds ��һ��Ŀ¼�������
	 * */
	public CVU_BeanSecondStep(String name, String createTime, String path, List<CVU_BeanThreadStep> seconds) {
		super();
		this.name = name;
		this.createTime = createTime;
		this.path = path;
		this.seconds = seconds;
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

	public List<CVU_BeanThreadStep> getSeconds() {
		return seconds;
	}

	public void setSeconds(List<CVU_BeanThreadStep> seconds) {
		this.seconds = seconds;
	}

	@Override
	public String toString() {
		return "BeanSecondStep [name=" + name + ", createTime=" + createTime + ", path=" + path + ", seconds=" + seconds + "]";
	}

}
