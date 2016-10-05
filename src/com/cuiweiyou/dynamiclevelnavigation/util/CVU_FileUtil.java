package com.cuiweiyou.dynamiclevelnavigation.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanNameAndPath;
import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanSecondStep;
import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanThreadStep;
import common.DataCollection;

/**
 * <b>类名</b>: FileUtil，文件目录读写<br/>
 * <b>说明</b>: <br/>
 */
public class CVU_FileUtil {

	/** /pro/ 目录绝对路径 */
	public static final String dataPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/pro/";
	public static final String dataTmpPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/";
	/** /Template/ 目录绝对路径 */
	public static final String tempPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/Template/";

	private CVU_FileUtil() {
	}

	/**
	 * 最外层数据封装。flag=0为Data1，1为Data2<br/>
	 * 0显示文件夹和pcm，1只显示xml<br/>
	 * projects为第1层<br/>
	 * measurements为第2层<br/>
	 * result和signal为第3层<br/>
	 * x.pcm为第4层
	 * 
	 * @param path
	 *            第一层路径
	 * @param flag
	 *            标识
	 */
	public static List<CVU_BeanSecondStep> getSecondAndThreadStepFilesAndFolders(String path, int flag) {

		/** measurements列表 */
		List<CVU_BeanSecondStep> listMeasurements = new ArrayList<CVU_BeanSecondStep>();

		File pathF2ed = new File(path);
		if (pathF2ed.isDirectory()) {
			File[] files2ed = pathF2ed.listFiles();
			for (int i = 0; i < files2ed.length; i++) {
				File file2 = files2ed[i];
				String name2 = file2.getName();
				String path2 = file2.getPath();

				/** result和signal列表 */
				List<CVU_BeanThreadStep> list3rd = new ArrayList<CVU_BeanThreadStep>();

				File pathF3rd = new File(path2 + "/");
				if (pathF3rd.isDirectory()) {

					File[] files3rd = pathF3rd.listFiles();
					for (int j = 0; j < files3rd.length; j++) {
						File file3 = files3rd[j];
						String name3 = file3.getName();
						String path3 = file3.getPath();

						/** X.pcm文件列表 */
						List<CVU_BeanNameAndPath> list4th = new ArrayList<CVU_BeanNameAndPath>();

						File pathF4th = new File(path3 + "/");
						if (pathF4th.isDirectory()) {

							File[] files4th = pathF4th.listFiles();
							for (int k = 0; k < files4th.length; k++) {
								File file4 = files4th[k];
								String name4 = file4.getName();
								String path4 = file4.getPath();

								if (0 == flag) {
									if (!name4.endsWith(".xml")) {
										list4th.add(new CVU_BeanNameAndPath(name4, "", path4));
									}
								} else if (1 == flag) {
									if (!name4.endsWith(".pcm")) {
										list4th.add(new CVU_BeanNameAndPath(name4, "", path4));
									}
								}
							}
						}

						if (0 == flag) {
							if (!name3.endsWith(".xml")) {
								list3rd.add(new CVU_BeanThreadStep(name3, "", path3, list4th));
							}
						} else if (1 == flag) {
							// if (!name3.endsWith(".pcm")) {
							if (name3.endsWith(".xml")) { // data2进入的，只加载xml模板文件
								list3rd.add(new CVU_BeanThreadStep(name3, "", path3, list4th));
							}
						}
					}
				}

				String createTime = "";

				if (name2.length() >= 15) { // 默认时间戳为项目名称则长度为15
					createTime = name2.substring(0, 15);
					createTime = createTime.replace("_", "");
					try {
						Long ct = Long.valueOf(createTime);
						createTime = ct + "";
					} catch (Exception e) {
						if (0 == flag) {
							if (!name2.endsWith(".xml")) {
								listMeasurements.add(new CVU_BeanSecondStep(name2, createTime, path2, list3rd));
							}
						} else if (1 == flag) {
							if (!name2.endsWith(".pcm")) {
								listMeasurements.add(new CVU_BeanSecondStep(name2, createTime, path2, list3rd));
							}
						}

						continue;
					}

					if (name2.length() > 15) {
						name2 = name2.substring(15, name2.length()); // 显示用的文件名
					}
				}

				if (0 == flag) {
					if (!name2.endsWith(".xml")) {
						listMeasurements.add(new CVU_BeanSecondStep(name2, createTime, path2, list3rd));
					}
				} else if (1 == flag) {
					if (!name2.endsWith(".pcm")) {
						listMeasurements.add(new CVU_BeanSecondStep(name2, createTime, path2, list3rd));
					}
				}
			}
		}

		// 排序
		Collections.sort(listMeasurements, new CVU_NPCompareMsurementsUtil<CVU_BeanSecondStep>());

		return listMeasurements;
	}

	/** Data,获取 /pro 下 projects 列表 <br/> */
	public static List<CVU_BeanNameAndPath> getFoldersInPro() {
		List<CVU_BeanNameAndPath> list = new ArrayList<CVU_BeanNameAndPath>();

		File proFile = new File(dataPath);
		File[] files = proFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			String path = file.getPath();
			String name = file.getName().replace(" ", ""); // 创建新project时
															// EditText的hint:留空则使用当前时间作为名称，填写则在自定义名称前加时间戳
			String createTime = "";

			if (name.length() >= 15) { // 默认时间戳为项目名称则长度为15
				createTime = name.substring(0, 15);
				createTime = createTime.replace("_", "");
				try {
					Long ct = Long.valueOf(createTime);
					createTime = ct + "";
				} catch (Exception e) {
					list.add(new CVU_BeanNameAndPath(name, "", path));
					continue;
				}

				if (name.length() > 15) {
					name = name.substring(15, name.length()); // 显示用的文件名
				}
			}

			list.add(new CVU_BeanNameAndPath(name, createTime, path));
		}

		// 排序
		Collections.sort(list, new CVU_NPCompareUtil<CVU_BeanNameAndPath>());

		return list;
	}

	/** Template,获取 /Template 下全部的.xml文件列表 <br/> */
	public static List<CVU_BeanNameAndPath> getFoldersInTemplate() {
		List<CVU_BeanNameAndPath> list = new ArrayList<CVU_BeanNameAndPath>();

		File proFile = new File(tempPath);
		File[] files = proFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			String path = file.getPath();
			String name = file.getName(); // 创建模板文件时
											// EditText的hint:留空则使用当前时间作为文件名，填写则在自定义名称前加时间戳
			name = name.replace(" ", ""); // 去除无意义的空格
			/*
			 * 20150527_051132.xml Template模板文件标准名称格式，长度19
			 * 
			 * 20150527_051132ABCD.xml，长度大于19 ABCD20150527_051132.xml，长度大于19
			 * ABCD.xml，长度小于19
			 */
			String createTime = "";
			if (name.length() >= 19) { // 文件名长度有效
				createTime = name.substring(0, 15); // 限定前15个字符（索引0-14）是时间戳
				createTime = createTime.replace("_", ""); // 包括0，不包括15。提取时间戳
				try {
					Long ct = Long.valueOf(createTime);
					createTime = ct + ""; // 有意义的时间戳
				} catch (Exception e) {
					// Log.e("ard", "NFE转换：" + e);
					list.add(new CVU_BeanNameAndPath(name, "", path));
					continue;
				}

				if (name.length() > 19) {
					name = name.substring(15, name.length()); // 显示用的文件名
				}
			}

			list.add(new CVU_BeanNameAndPath(name, createTime, path));
		}

		// 排序
		Collections.sort(list, new CVU_NPCompareUtil<CVU_BeanNameAndPath>());

		// Log.e("ard", "模板数量 " + list.size());

		return list;
	}

	/**
	 * <b>功能</b>: getMeasurementCountInProject，返回 /pro/projectX/
	 * 下measurement文件夹数量 <br/>
	 * <b>说明</b>: <br/>
	 * <b>创建</b>: 2016-3-3_下午2:45:30 <br/>
	 * 
	 * @param path
	 *            project绝对路径
	 * @return : int 数量 <br/>
	 * @author : weiyou.cui@ts-tech.com.cn <br/>
	 * @version 1 <br/>
	 */
	public static int getMeasurementFoldsCountInProject(String path) {
		// Log.e("ard", "measurement文件夹路径：" + path);
		int count = 0;

		File proF = new File(path);
		File[] folds = proF.listFiles();
		for (int i = 0; i < folds.length; i++) {
			if (folds[i].isDirectory())
				count += 1;
		}
		// Log.e("ard", "measurement文件夹数量：" + count);

		return count;
	}

	/**
	 * <b>功能</b>: createProject，创建新的项目 <br/>
	 * <b>说明</b>: TODO 未区分Data1 Data2 <br/>
	 * <b>创建</b>: 2016-2-17_上午10:28:11 <br/>
	 * 
	 * @param name
	 *            : 带有时间戳的项目名称 <br/>
	 * @return boolean : 创建成功？
	 * @author : weiyou.cui@ts-tech.com.cn <br/>
	 * @version 1 <br/>
	 */
	public static boolean createProject(String name) {
		// Log.e("ard", "创建项目,pro可写：" + new File(dataPath).canWrite());
		// TODO 必要的文件
		File file = new File(dataPath + name);
		// Log.e("ard", "创建项目,有了吗：" + file.exists());
		boolean mkdir = file.mkdir();

		// Log.e("ard", "创建项目结果：" + mkdir);
		return mkdir;
	}

	/**
	 * <b>功能</b>: removeFilesOrDirs，删除已有的文件或目录<br/>
	 * 
	 * @param path
	 *            : 目标文件或文件夹 <br/>
	 * @author : weiyou.cui@ts-tech.com.cn <br/>
	 * @version 1 <br/>
	 */
	public static void removeFilesOrDirs(String path) {
		// Log.e("ard", "项目下：" + path);
		File file = new File(path);
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] arrFiles = file.listFiles();
				// 循环删除目录下的子目录
				for (int i = 0; i < arrFiles.length; i++) {
					removeFilesOrDirs(arrFiles[i].getAbsolutePath());
				}
				arrFiles = null;
				// 当目录下的所有删除后, 再把本目录删除
				file.delete();
			} else {
				file.delete();
			}
		}
	}

	/**
	 * 复制文件
	 * 
	 * @param oldFile
	 *            源文件绝对路径
	 * @param newFile
	 *            目标文件绝对路径
	 * @author 来自 {@link DataCollection}
	 */
	public static void copyFile(File oldFile, File newFile) {
		try {
			FileOutputStream output = new FileOutputStream(newFile);
			FileInputStream input = new FileInputStream(oldFile);
			byte[] b = new byte[1024];
			int len;
			try {
				while ((len = input.read(b)) != -1) {
					output.write(b, 0, len);
				}
				output.flush();
				output.close();
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取kb单位的可用存储空间<br/>
	 * @return long 可用空间KB
	 */
	public static long getUsableSDStorageSize() {
		long usableSize = 0;
		
		File sdcardDir = Environment.getExternalStorageDirectory();
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			StatFs sf = new StatFs(sdcardDir.getPath());
			long blockSize = sf.getBlockSize();        // 每个存储块大小
			long blockCount = sf.getBlockCount();      // 存储卡总数
			long availCount = sf.getAvailableBlocks(); // 可用块数量
			long spaceSize = blockSize * blockCount / 1024;
			usableSize = availCount * blockSize / 1024;
		}
		
		return usableSize;
	}
}
