package common;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mode.drive.DriveModeActivity;
import preference.welcome.MyAPP;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import bottom.drawer.BottomOperate;

import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanNameAndPath;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_FileUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_JSONUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_SPUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_TimeUtil;
import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;
import com.example.mobileacquisition.R.string;

/** 录音器 **/
public class DataCollection {
	public static boolean isRecording;
//	public static List<int[]> dataCollectionIntBufferList;
	public byte[] byteBuffer;
//	public static int[] buffer;
	public static int hardType;
	private AudioRecord audioRecord;
	private int recBufSize;
	private File mAudioFile;
	private String oldProjectName;
	private Activity context;
	private boolean notShown;
	private String saveDialogTitle;
	private String saveDialogButton;
	private int AcquiFreq_spinner_values;
	private InputStream input;
	private StringBuffer sendMsg;
	private BufferedWriter writer;
	private OutputStream out;
	private boolean ifReceiveDataSuccessfully = false;//接收数据是否成功
	private boolean ifSendCommandSuccessfully = false;//发送命令是否成功
	private boolean ifServerConnectSuccessfully=false;//服务器端连接成功
	private SharedPreferences sharedPreferences;
	private SharedPreferences preference;
	private File folder;
	private String newProject;
	private boolean saveBeCancel = false;
	private int triggerMode;// 0 Start to Stop,1 Start to Time,2Time to Stop
	private long startTime;
	private long lastTime;
	private float duration;// 5s
	public BottomOperate bottomOperate;

	private Handler myHandler;
	private Handler newProjectHandler;
	private Handler showDialogHandler;
	private RecordTask recordTask;
	/** str 临时录音保存目录 /sdcard/data/stmp_recordtmp/ 每次start时初始化 */
	private String recordTmpPath;
	private FileOutputStream fileOutputStream;
	private LinkedList<Byte> linkedList;
	private int startIndex = 0;
	private ProgressDialog progressDialog;
	private LogThread logThread;
	public static int collectionState = 0;
	public static int collectionOverCalculateCount = 0;
	private boolean ifStillIntheWhileThread=false;
	private int calculateSize=0;
	FileOutputStream log_fos = null;
	private String newProjectName;
	private String projectName;
	private String runName = "measurement";
	private int saveMethod;
	private File projectFolder;
	private File measurementFolder;
	
	
	private Socket client_socket;
	private Socket server_socket;
	private ServerSocket server_serverSocket;
	private ClientTask  clientTask;
	
	public static Map<Integer,List<int[]>> dataListMap;
	private Map<Integer,Integer> channelDataArrayCountList;
	@SuppressLint("HandlerLeak")
	public DataCollection(Handler myHandler, Activity context) {
		if (null != myHandler)
			this.myHandler = myHandler;

		preference = context.getSharedPreferences("hz_5D", 0);
		triggerMode = preference.getInt("Trigger_Type_ID", 0);
		if(!preference.getString("Trigger_Duration", "0").equals("")){
			duration = Float.parseFloat(preference.getString("Trigger_Duration", "0"));
		}
		AcquiFreq_spinner_values = Integer.parseInt(preference.getString("AcquiFreq_spinner_values", "12800"));
		AcquiFreq_spinner_values = 48000;
		recBufSize = AudioRecord.getMinBufferSize(// x
				AcquiFreq_spinner_values, //x
				AudioFormat.CHANNEL_IN_MONO, //x
				AudioFormat.ENCODING_PCM_16BIT);
		this.context = context;

		sharedPreferences = context.getSharedPreferences("displayInfo", Context.MODE_PRIVATE);
		folder = new File("sdcard/data/pro");
		if (!folder.exists()) {
			folder.mkdir();
		}

		if ((oldProjectName = sharedPreferences.getString("oldProject", null)) == null) {// 没有存储过上一次的文件夹就直接使用最近一次使用的文件夹
			oldProjectName = "project" + folder.list().length;
		}

		newProjectHandler = new Handler() {
			@Override
			public void handleMessage(Message age) {
				newProject = (String) age.obj;
			}
		};
		XclSingalTransfer xclSingalTransfer = XclSingalTransfer.getInstance();
		xclSingalTransfer.putValue("newProjectHandler", newProjectHandler);

		showDialogHandler = new Handler() {
			@Override
			public void handleMessage(Message age) {
				
				bottomOperate.startChanged();
				ShowPreferenceDialog();
			}
		};

		// if(context.getClass().getSimpleName()
		// .equals("MainActivity")){
		// rightDrawerFragment=((MainActivity)context).rightDrawerFragment;
		// rightDrawerFragment.setNewProject(oldProjectName);
		// }
	}

	public void setHandler(Handler handler) {
		this.myHandler = handler;
	}

	public void setContext(Activity context) {
		this.context = context;
	}

	/**
	 * 点击录音开始按钮，临时保存录音文件
	 * @param hardType 0是硬件模式，1是手机模式
	 */
	public void Start(int hardType) {
		
		startSaveTmpAudioFile(hardType);
	}

	public void Stop() {
		isRecording = false;
		
		switch (hardType) {
		case 0:// 硬件模式
			if (!ifSendCommandSuccessfully) {
				Toast.makeText(context, R.string.equipment_connection_exception, Toast.LENGTH_SHORT).show();
				return;
			}
			sendMsg.delete(0, sendMsg.length());
			// sendMsg.append("HX 00000000 StopTF DY");
			// sendMsg.append("HX 00000000 StopTF DY");
			sendMsg.append("HX 0000close DY");
			try {	
				writer.write(sendMsg.toString().trim());
				writer.flush();
				input.close();
				out.close();
			} catch (Exception e) {
				Toast.makeText(context, R.string.equipment_connection_exception, Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			
			if (triggerMode != 1) {
				if (newProject == null && folder.list().length > 0) {
					if (!notShown || saveBeCancel) {
						ShowPreferenceDialog();
					}
				}
			} else {
				Message showDialogMessage = showDialogHandler.obtainMessage();
				showDialogHandler.sendMessage(showDialogMessage);
			}
			// if(rightDrawerFragment!=null){
			// FileListAdapter fileListAdapter;
			// if((fileListAdapter=rightDrawerFragment.getFileListAdapter())!=null){
			// fileListAdapter.notifyDataSetChanged();
			// }
			// }
			break;
		case 1:// 本机模式
//			System.out.println("recordTask1");
			if (null == recordTask) {
				return;
			}
//			System.out.println("recordTask0");
			audioRecord.stop();
			if (audioRecord != null) {
				audioRecord.release();
				audioRecord = null;
			}
			if (recordTask != null) {
				recordTask.interrupt();
				recordTask = null;
				byteBuffer = null;
			}
//			 progressDialog = ProgressDialog.show(context, "",
//			 context.getResources().getString(R.string.In_saving));
			break;
		}
	}

	/** 保存文件弹窗 **/
	private void ShowPreferenceDialog() {

		SharedPreferences sp = context.getSharedPreferences("displayInfo", 0);
		int dir = sp.getInt("Dir", -1);
		
		if(0 == dir){
			deleteFolder(new File(recordTmpPath)).delete();
			Toast.makeText(context, context.getResources().getString(R.string.data_saved_in_peripheral), 1).show();
			return ;
		}

		saveFileDialog();
	}

	/** 复制文件file */
	private void copyFile(File oldFile, File newFile) {
		try {
			if (oldFile == null || newFile == null) {
				return;
			}
			
			File folder = newFile.getParentFile();
			if(!folder.exists())
				folder.mkdirs();
			
			if(!newFile.exists())
				newFile.createNewFile();
			
			FileOutputStream output = new FileOutputStream(newFile);
			FileInputStream input = new FileInputStream(oldFile);
			byte[] b = new byte[1024];
			int len;
			
			while ((len = input.read(b)) != -1) {
				output.write(b, 0, len);
			}
			output.flush();
			output.close();
			input.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/** 复制文件<b>夹</b>  **/
	private String copyFolder(File oldFile, File newFile) {
		File[] oldfiles = oldFile.listFiles();
		if (oldfiles == null)
			return null;
		String tempFile = null;
		for (int i = 0; i < oldfiles.length; i++) {
			if (oldfiles[i].isDirectory()) {
				tempFile = oldfiles[i].getName();
				File file = new File(newFile.getPath() + File.separator + tempFile + File.separator);
				file.mkdirs();
				copyFolder(oldfiles[i], file);
			} else {
				tempFile = newFile.getPath() + "/" + oldfiles[i].getName();
				try {
					FileOutputStream output = new FileOutputStream(new File(tempFile));
					FileInputStream input = new FileInputStream(oldfiles[i]);
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
		}
		return tempFile;
	}

	/** 删除文件<b>夹</b> */
	private File deleteFolder(File oldFolder) {
		if (oldFolder.isDirectory()) {
			File[] files = oldFolder.listFiles();
			if (files != null && files.length > 0) {
				for (File f : files) {
					if (f.isDirectory()) {
						deleteFolder(f);
						try {
//							f.delete();
							File to = new File(f.getAbsolutePath() + System.currentTimeMillis());  
							f.renameTo(to);  
							to.delete();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						try {
//							f.delete();
							File to = new File(f.getAbsolutePath() + System.currentTimeMillis());  
							f.renameTo(to);  
							to.delete();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return oldFolder;
	}
/*****************************************************客户端线程（硬件模式专用）*****************************************************************/
/*****************************************************************************************************************************************************/
	class ClientTask extends Thread{
		public void run(){
			if(hardType>0){
				Toast.makeText(context, context.getResources().getString(R.string.equipment_connection_exception), 0).show();
				try {
					if(server_socket!=null) server_socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
			if(client_socket==null){
				if((client_socket= (Socket) XclSingalTransfer.getInstance().getValue("client_socket"))==null) return;
				try{
				// ----------发送命令-------------------------
					out = client_socket.getOutputStream();
					writer = new BufferedWriter(new OutputStreamWriter(out));
					sendMsg = new StringBuffer();
					//-----------发送接收数据命令----------------
					sendMsg.append("HX 00000000 StartTF DY");
					writer.write(sendMsg.toString().trim());
					writer.flush();
					//-----------完毕-----------------------------
					ifSendCommandSuccessfully=true;
				}catch(Exception e){
					ifSendCommandSuccessfully=false;
					e.printStackTrace();
					try {
						writer.close();
						out.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}
/*****************************************************录音线程||服务器端线程*****************************************************************/
/*********************************************************************************************************************************/
	class RecordTask extends Thread {
		DataOutputStream dos;
		BufferedReader bufRead;

		@Override
		public void run() {
			try {
				dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(mAudioFile)));
				logThread = new LogThread();
				if(dataListMap==null){
					dataListMap=new HashMap<Integer,List<int[]>>();
				}
				if(channelDataArrayCountList==null){
					channelDataArrayCountList=new HashMap<Integer,Integer>();
				}
				switch (hardType) {
				case 0:// 硬件模式
					try {
						if(server_serverSocket==null){
							if ((server_serverSocket = (ServerSocket) XclSingalTransfer.getInstance().getValue("server_serverSocket")) == null) {
								return;
							}
						}
						server_socket=server_serverSocket.accept();
						ifServerConnectSuccessfully=true;
						input = server_socket.getInputStream();
						byteBuffer = new byte[1200];

						// --------获取转速----------
						String chan1Str = preference.getString("Digital_Chan1", "false");
						String chan2Str = preference.getString("Digital_Chan2", "false");
						if (!(chan1Str.equals("false") && chan2Str.equals("false"))) {
//							// -------------待真实转速通道出现后删除----------
//							rpmdata = new float[32768];
//							File file = new File("sdcard/data/rpm_order.txt");
//							if(!(file.exists())){
//								Toast.makeText(context, "请从SVN\\源码里寻找G2开头的文件放置在手机目录的data文件夹下", Toast.LENGTH_SHORT).show();
//							}else{
//								 reader = new LineNumberReader(new FileReader(file));
//							}
//							// -------可删除部分------------------------------
						}
						// ---------- over ---------
					} catch (Exception e) {
						ifServerConnectSuccessfully=false;
						// Toast.makeText(context, "connecting failed!",
						// Toast.LENGTH_SHORT).show();
						deleteFolder(new File(recordTmpPath + cvuMesPath)).delete(); 
						e.printStackTrace();
						dos.flush();
						dos.close();
					}
					
					while (isRecording) {
						if (input == null) {
							return;
						}
						if (byteBuffer == null) {
							return;
						}
						if (triggerMode == 1 && duration > 0) {
							if (startTime > 0) {
								if (lastTime >= duration) {
									Stop();
									startTime = 0;
									lastTime = 0;
									break;
								}
							}
							startTime = System.currentTimeMillis();
							input.read(byteBuffer);

							lastTime = lastTime + (System.currentTimeMillis() - startTime);
						} else {
							input.read(byteBuffer);
							ifReceiveDataSuccessfully  = true;
						}
						ByteBuffer tempBuffer = ByteBuffer.wrap(byteBuffer).order(ByteOrder.LITTLE_ENDIAN);	
					
//						int[] databuffer=new int[recBufSize / 2];
						for (int i = 0; tempBuffer.position() < tempBuffer.capacity(); i++) {
//							buffer[xsize] =( tempBuffer.getInt() >> 8)&0xffff-8388608;//8388608是偏移量
							//------------数据解析过程---------------
								byte[] tempByte=new byte[8];
								tempBuffer.get(tempByte, 0,4);
								ByteBuffer buffer=ByteBuffer.wrap(tempByte).order(ByteOrder.LITTLE_ENDIAN);
								long long_buffer=buffer.getLong();
								int a=(int)(long_buffer&0x78000000);
								int b=a>>27;
								int channelIndex=(int)((long_buffer&0x78000000)>>27);
								int channeldata=(int)((long_buffer&0x00FFFFFF)-0x00800000)/10000;
//								String testdate=Long.toHexString(buffer.getLong());
//								int channelIndex=Integer.valueOf(testdate.charAt(1));
//								int channeldata=Integer.valueOf(testdate.substring(2, testdate.length()-1))-800000;
						  //------------初始化各通道的数据存储空间------------------
								if(!dataListMap.containsKey(channelIndex)){
									List<int[]> dataList=new ArrayList<int[]>();
									dataList.add(new int[recBufSize/2]);
									dataListMap.put(channelIndex, dataList);
									channelDataArrayCountList.put(channelIndex,0);
								}
						//-------------数据存储过程---------------------
								List<int[]> channelbuffer=dataListMap.get(channelIndex);//获取该通道数组集合
								int[] databuffer=channelbuffer.get(channelbuffer.size()-1);//获取该通道还未填充数据的数组
								int index=channelDataArrayCountList.get(channelIndex);//获取该通道最新可加载的数组index
								databuffer[index]=channeldata;
								
								dos.writeByte(channeldata);
								index+=1;
								if(index==databuffer.length){//当该数组填充完毕后生成新数组
									channelbuffer.add(new int[recBufSize/2]);
									index=0;
								}
								channelDataArrayCountList.put(channelIndex,index);
				     //---------------完毕--------------------------------
						}
						Message msg = myHandler.obtainMessage();
						msg.what = 1;
						myHandler.sendMessage(msg);
				
						if (isRecording ==false) {
							ifStillIntheWhileThread=true;
							collectionState = 1;
							if(context.getClass().getSimpleName().equals("MainActivity")){
								calculateSize=((MainActivity) context).viewList.size();
							}else if(context.getClass().getSimpleName().equals("DriveModeActivity")){
								calculateSize= ((DriveModeActivity)context).getViewsSize()-1;
							}
							while (collectionOverCalculateCount !=calculateSize) {
//								System.out.println("collectionOverCalculateCount,"+collectionOverCalculateCount);
								if (dataListMap.size() == 0) {
									break;
								}
								Message msg1 = myHandler.obtainMessage();
								msg1.what = 1;
								myHandler.sendMessage(msg1);
							}
							collectionState = 2;
//							 progressDialog.dismiss();
							Message showDialogMessage = showDialogHandler.obtainMessage();
							showDialogHandler.sendMessage(showDialogMessage);
							dataListMap.clear();
						}

					}
					
					if(!ifStillIntheWhileThread){
						collectionState = 1;
						while (collectionOverCalculateCount != ((MainActivity) context).viewList.size()) {
							if (dataListMap.size() == 0) {
								break;
							}
							Message msg1 = myHandler.obtainMessage();
							msg1.what = 1;
							myHandler.sendMessage(msg1);
						}
						collectionState = 2;
//						 progressDialog.dismiss();
						Message showDialogMessage = showDialogHandler.obtainMessage();
						showDialogHandler.sendMessage(showDialogMessage);
						dataListMap.clear();
					}	
					break;
				case 1:// 本机模式
					try { 	
						while (audioRecord.getRecordingState() == 3) {
							// nullpointexception ����ȷ�������龰...
							collectionState = 0;
							if (triggerMode == 1 && duration > 0) {// Start to Time
								if (startTime > 0) {
									if (lastTime >= duration) {
										Stop();
										startTime = 0;
										lastTime = 0;
										break;
									}
								}
								startTime = System.currentTimeMillis();
								audioRecord.read(byteBuffer, 0, recBufSize);
								lastTime = lastTime + (System.currentTimeMillis() - startTime);
								for (int i = 0; byteBuffer != null && i < byteBuffer.length; i++) {
									dos.writeByte(byteBuffer[i]);
								}
							} else if (triggerMode == 2 && duration > 0) {// Time to Stop
								startTime = System.currentTimeMillis();
								audioRecord.read(byteBuffer, 0, 2);
								lastTime = lastTime + (System.currentTimeMillis() - startTime);

								if (startTime > 0) {
									if (lastTime > duration && linkedList.size() > 0) {
										if (fileOutputStream == null) {
											fileOutputStream = new FileOutputStream(mAudioFile);
										}
										for (int i = 0; byteBuffer != null && i < byteBuffer.length; i++) {
											linkedList.removeFirst();
											linkedList.addLast(byteBuffer[0]);
										}
									} else {
										if (linkedList == null) {
											linkedList = new LinkedList<Byte>();
										}
										for (int i = 0; byteBuffer != null && i < byteBuffer.length; i++) {
											dos.writeByte(byteBuffer[0]);
											linkedList.add(byteBuffer[0]);
										}

									}
								}
							} else {// Start to Stop
								audioRecord.read(byteBuffer, 0, recBufSize);
								for (int i = 0; byteBuffer != null && i < byteBuffer.length; i++) {
									dos.writeByte(byteBuffer[i]);
								}
							}
							ByteBuffer tempBuffer = ByteBuffer.wrap(byteBuffer).order(ByteOrder.LITTLE_ENDIAN); 
							int[] buffer = new  int[recBufSize/2];
							for (int i = 0; tempBuffer.position() < tempBuffer.capacity(); i++) {
								buffer[i] = tempBuffer.getShort() << 8; 
							}
							  //------------数据存储------------------
							if(!dataListMap.containsKey(1)){
								dataListMap.put(1, new ArrayList<int[]>());
							}
							List<int[]> dataList=dataListMap.get(1);
							dataList.add(buffer);
							//-------------完毕--------------------------------------
							Message msg = myHandler.obtainMessage();
							msg.obj = buffer;
							msg.what = 1;
							myHandler.sendMessage(msg);

							if (audioRecord.getRecordingState() != 3) {
								ifStillIntheWhileThread=true;
								collectionState = 1;
								if(context.getClass().getSimpleName().equals("MainActivity")){
									calculateSize=((MainActivity) context).viewList.size();
								}else if(context.getClass().getSimpleName().equals("DriveModeActivity")){
									calculateSize= ((DriveModeActivity)context).getViewsSize()-1;
								}
								while (collectionOverCalculateCount !=calculateSize) {
//									System.out.println("collectionOverCalculateCount,"+collectionOverCalculateCount);
									if (dataListMap.get(1).size() == 0) {
										break;
									}
									Message msg1 = myHandler.obtainMessage();
									msg1.what = 1;
									myHandler.sendMessage(msg1);
								}
								collectionState = 2;
//								 progressDialog.dismiss();
								Message showDialogMessage = showDialogHandler.obtainMessage();
								showDialogMessage.obj = "this is record state not 3 , fffffffffffff";
								showDialogHandler.sendMessage(showDialogMessage);
								dataListMap.clear();
							}
							
							if (!logThread.isAlive()) {
								logThread.start();
							}
						}
						if(!ifStillIntheWhileThread){
							collectionState = 1;
							while (collectionOverCalculateCount != ((MainActivity) context).viewList.size()) {
								if (dataListMap.get(1).size() == 0) {
									break;
								}
								Message msg1 = myHandler.obtainMessage();
								msg1.what = 1;
								myHandler.sendMessage(msg1);
							}
							collectionState = 2;
//							 progressDialog.dismiss();
							Message showDialogMessage = showDialogHandler.obtainMessage();
							showDialogHandler.sendMessage(showDialogMessage);
							dataListMap.clear();
						}	
						// �ر�log�ļ�
						log_fos.close();
					} catch (Exception e) {
						return;
					}
					break;
				}

				dos.flush();
				dos.close();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					if (dos != null) {
						dos.flush();
						dos.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			super.run();
		}

	}

	public File getAudioFile() {
		return mAudioFile;
	}

	public void setNewProject(String newProject) {
		this.newProject = newProject;
	}

	public void setTriggerMode(int triggerMode) {
		this.triggerMode = triggerMode;
	}

	public void setDuration(float duration) {
		this.duration = duration * 1000;
	}

	/** 检查当前是否加载项目<br/>
	 * 如果没有，即检查是否加载了模板<br/>
	 * 然后根据模板来源处理 
	 * @return 
	 * -1 未加载项目未加载模板 <br/>
	 * 0 右侧拉中加载了项目 <br/>
	 * 1 右侧拉中ProjectX下加载模板 <br/>
	 * 2 右侧拉中measurementX下加载模板 <br/>
	 * 3 右侧拉中ABC下加载模板 <br/>
	 * 4 开始界面加载模板 <br/>
	 *  **/ // 16.3.28 崔
	private int checkCurrentProjectAndTemplate() {
		// 1. 判断是否加载了项目
		CVU_SPUtil.getCurrentProject(MyAPP.getContext(), "dataFlag"); 
		String spj = CVU_SPUtil.getCurrentProject(MyAPP.getContext(), "currentProject");
		
		if (null != spj && !"".equals(spj)) { // 如果加载了项目
			return 0;
		}
		
		// 2. 判断模板来源
		// 1）abc下的模板
		String tplt = CVU_SPUtil.getTemplateFromWhere(MyAPP.getContext()); 

		if ("prj".equals(tplt)) {

			return 1;
		} else if ("mes".equals(tplt)) {

			return 2;
		} else if ("abc".equals(tplt)) {

			return 3;
		} else if ("wel".equals(tplt)) {

			return 4;
		}

		return -1;
	}

	/** 删除临时录音文件 */
	protected void deleteTmpAudioFile() {
		deleteFolder(new File(recordTmpPath)).delete();
		sharedPreferences.edit().putString("oldProject", oldProjectName);
		saveAudioFileDialog.dismiss();
		saveBeCancel = true;
	}

	public class LogThread extends Thread {
		private int arrayCount;
		public void run() {
			/* 此处判断数据是否过载如果过载，写入log文件 */
			//加载多通道日志后进行二次修改 by刘亦茜
			int dataCount = 0;
			while (isRecording) {
				if(dataListMap!=null&&dataListMap.size()>0){
					try {
						dataCount=saveLog(dataCount);
					} catch (Exception e) {
						dataCount=0;
						arrayCount=0;
					}
				}
			}
			super.run();			
				try {
					saveLog(dataCount);
				} catch (Exception e) {
					dataCount=0;
					arrayCount=0;
				}
		}
		//由于加载多通道的原因，将saveLog(int[] dataArray)修改为saveLog(int[] dtaArray,int channelIndex)，"Channel 1 过载"修改为“Channel”+String.valueOf(channelIndex)+"过载"
		//上条备注by 刘亦茜
		private int saveLog(int dataCount) {
			for(int i=0;i<=dataCount;i++){
				Iterator<Integer> it=dataListMap.keySet().iterator();
				
				while(it.hasNext()){
					int channelIndex=it.next();
					
					List <int[]>dataList=dataListMap.get(channelIndex);
					if(arrayCount==dataList.size())	continue;
					int[] dataArray=dataList.get(arrayCount);
					if(i==dataArray.length)	continue;

					dataCount=dataArray.length-1;
					if (dataArray[i] > 3000000) {
						SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						String date = sDateFormat.format(new java.util.Date());
						String str = "Channel"+channelIndex+"   过载" + "  " + date + "\n";
						try {
							log_fos.write(str.getBytes());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			return dataCount;
		}
	}

	/////////////////////////////// 录音后文件保存 ////////////////////////////

	/** str 临时录音项目下的测量文件夹 */
	String cvuMesPath;
	AlertDialog saveAudioFileDialog;

	/**
	 * 点击录音开始按钮，临时保存录音文件
	 * @param hardType 0是硬件模式，1是手机模式
	 */
	private void startSaveTmpAudioFile(int hardType) {
		recordTmpPath = CVU_FileUtil.dataTmpPath + CVU_TimeUtil.getTimestamp() + "_recordtmp/"; // 防止 IOException: open failed: EBUSY (Device or resource busy)
		
		if (isRecording)
			return;

		String mesRule = CVU_SPUtil.getLeftNamingRule(context, 2); // 命名规则，默认"measurement"
		String mesModl = CVU_SPUtil.getLeftNamingRule(context, 4); // 命名模式，默认"0"-自定义。1-默认

		if ("0".equals(mesModl)) {
			cvuMesPath = CVU_TimeUtil.getTimestamp() + mesRule;
		} else {
			cvuMesPath = CVU_TimeUtil.getTimestamp() + "measurement";
		}

		int max = getMaxFileCount(mesRule);

		cvuMesPath = cvuMesPath + "_" + max;

		if (new File(recordTmpPath).exists()) {
			deleteFolder(new File(recordTmpPath)).delete();
		}
		
		new File(recordTmpPath).mkdirs();

		// 1. 创建临时项目目录
		new File(recordTmpPath).mkdir();
		// 2. 在临时目录创建模板文件
		SharedPreferences ss = context.getSharedPreferences("hz_5D",context.MODE_MULTI_PROCESS);
		String tmplSource = context.getFilesDir().getAbsolutePath() + "/shared_prefs/hz_5D.xml"; // 手动创建的文件必须是 777 权限
		//   /data/data/com.example.ssxx/files/shared_prefs/hz_5D.xml
		if(!new File(tmplSource).exists()){
			SharedPreferences sp = context.getSharedPreferences("Hz_5D", 0);
			//   /data/data/com.example.ssxx/shared_prefs/hz_5D.xml
			tmplSource = "/data/data/" + context.getPackageName() + "/shared_prefs/hz_5D.xml";
		}
		String tmplTarget = recordTmpPath + "template.xml";
		copyFile(new File(tmplSource), new File(tmplTarget));
		// 3. 在项目目录创建测量目录
		new File(recordTmpPath + cvuMesPath).mkdirs();
		// 4. 在测量目录创建信号和结果目录
		new File(recordTmpPath + cvuMesPath + "/Signal").mkdirs();
		new File(recordTmpPath + cvuMesPath + "/Result").mkdirs();
		// 5. 在测量目录创建模板文件
		String tmplTarget2 = recordTmpPath + cvuMesPath + "/template.xml";
		copyFile(new File(tmplSource), new File(tmplTarget2));
		// 6. 在信号目录创建新pcm信号文件
		mAudioFile = new File(recordTmpPath + cvuMesPath + "/Signal/" + "111" + ".pcm");
		// 7. 在结果目录创建日志文件
		try {
			log_fos = new FileOutputStream(recordTmpPath + cvuMesPath + "/Result/" + "/log.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		/////////////////// 以下为原始代码未变化 ///////////////////////////////
		switch (hardType) {
		case 0:// 硬件模式
			recordTask = new RecordTask();
			clientTask=new ClientTask();
			recordTask.start();
			clientTask.start();
			break;
		case 1:// 本机模式
			if (recBufSize <= 0) {
				Toast.makeText(context, R.string.recording_prohibited, Toast.LENGTH_SHORT).show();
				return;
			}

			if (triggerMode == 2 && duration > 0) {
				byteBuffer = new byte[2];
			} else {
				byteBuffer = new byte[recBufSize];
			}
			collectionOverCalculateCount = 0;
			audioRecord = new AudioRecord( // 1
					MediaRecorder.AudioSource.MIC, // 1
					AcquiFreq_spinner_values, // 1
					AudioFormat.CHANNEL_IN_MONO, // 1
					AudioFormat.ENCODING_PCM_16BIT, // 1
					recBufSize);
			audioRecord.startRecording();
			recordTask = new RecordTask();
			recordTask.start();
			break;
		}

		isRecording = true;

		DataCollection.hardType = hardType;
	}

	/**
	 * 根据模板来源，获取目录下子目录总数
	 * @param mesRule
	 */
	private int getMaxFileCount(String mesRule) {
		int max = 0;
		File pf = null;
		int from = checkCurrentProjectAndTemplate(); // 模板来源
		if (0 == from) { // 0 右侧拉中加载了项目
			String cpp = CVU_JSONUtil.json2Bean(CVU_SPUtil.getCurrentProject(context, "currentProject")).getPath(); // 已加载项目路径
			pf = new File(cpp);
		} else if (1 == from) { // 模板来自项目下
			String tmpl = CVU_SPUtil.getTemplate4SaveRecord(context);
			pf = new File(tmpl).getParentFile();
		} else if (2 == from) { // 模板来自measurement下
			String tmpl = CVU_SPUtil.getTemplate4SaveRecord(context);
			pf = new File(tmpl).getParentFile().getParentFile();
		} else {
			return 1;
		}

		File[] fs = pf.listFiles();
		for (int i = 0; i < fs.length; i++) {
			String name = fs[i].getName();
			if (name.toLowerCase().contains("_")) { //规定用户手动输入的名字不能使用下划线，所以有下划线的都是规则的名字，可以读取编号
				int iii = name.lastIndexOf("_");
				String number = name.substring(iii + 1, name.length());
				try {
					int nb = Integer.valueOf(number);
					if (nb > max)
						max = nb;
				} catch (NumberFormatException e) {
					continue;
				}
			} else {
				continue;
			}
		}
		max++;
		return max;
	}
	
	/** 停止录音，确认保存文件弹窗。替换原stop后的ShowPreferenceDialog方法 */
	private void saveFileDialog() {
		int res = checkCurrentProjectAndTemplate();
		// -1 未加载项目未加载模板
		// 0 右侧拉中加载了项目
		// 1 右侧拉中ProjectX下加载模板
		// 2 右侧拉中measurementX下加载模板
		// 3 右侧拉中ABC下加载模板
		// 4 开始界面加载模板
		if (0 == res) { // 0 右侧拉中加载了项目，直接保存
			directSave();
			return;
		}

		saveAudioFileDialog = createDialog();
		RelativeLayout dialog_title_layout=(RelativeLayout) saveAudioFileDialog.findViewById(R.id.dialog_title_layout); // 
		TextView tvTitle = (TextView) saveAudioFileDialog.findViewById(R.id.c_dialog_title);// 标题
		// EditText etDefN = (EditText) saveAudioFileDialog.findViewById(R.id.c_dialog_edit); // 自定义项目名称
		Button btnOK = (Button) saveAudioFileDialog.findViewById(R.id.c_dialog_ok); // 确定按钮
		Button btnCancel = (Button) saveAudioFileDialog.findViewById(R.id.c_dialog_cancel); // 取消按钮
		Button btnAnother = (Button) saveAudioFileDialog.findViewById(R.id.c_dialog_other); // 其他按钮
		
		if(ThemeLogic.themeType==1){
			dialog_title_layout.setBackgroundResource(R.drawable.welcome_title_corners_bg);
			btnOK.setBackgroundResource(R.drawable.btn_gray);
			btnCancel.setBackgroundResource(R.drawable.btn_gray);
			btnAnother.setBackgroundResource(R.drawable.btn_gray);
			tvTitle.setTextColor(Color.argb(255,25,25,112));
			btnAnother.setTextColor(Color.argb(255,255,255,255));
		}else{
			dialog_title_layout.setBackgroundResource(R.drawable.welcome_title_corners_bg1);
			btnOK.setBackgroundResource(R.drawable.bt_blue_selector1);	
			btnCancel.setBackgroundResource(R.drawable.bt_gray_selector2);
			btnAnother.setBackgroundResource(R.drawable.bt_gray_selector3);
			tvTitle.setTextColor(Color.argb(255, 255, 255, 255));
			btnAnother.setTextColor(Color.argb(255,78,78,78));
		}
		
		if (1 == res || 2 == res) {
			tvTitle.setText(R.string.saveNewDialogTitleOrTmplsPrj);
			btnOK.setText(R.string.ok);
			btnCancel.setText(R.string.cancel);
			btnAnother.setText(R.string.saveWhereTheTmplateInter);
		} else if (-1 == res || 0 == res || 3 == res || 4 == res) {
			tvTitle.setText(R.string.saveNewDialogTitle);
			btnOK.setText(R.string.ok);
			btnCancel.setText(R.string.cancel);
			btnAnother.setVisibility(View.GONE);
		}

		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveTmpAudioFile2Prj(); // 新建项目保存
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteTmpAudioFile(); // 取消保存
			}
		});

		btnAnother.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				operateTmpAudioFile(); // 保存到模板所在项目
			}
		});

		/*
		 * //不再询问 
		 * CheckBox checkNotShown = (CheckBox)
		 * saveAudioFileDialog.findViewById(R.id.not_shown);
		 * checkNotShown.setOnCheckedChangeListener(new
		 * CompoundButton.OnCheckedChangeListener() {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton buttonView,
		 * boolean isChecked) { if (isChecked) { notShown = true; } else {
		 * notShown = false; } } });
		 */
	}

	/**
	 * 统计pro目录下的项目数量，然后+1
	 * @param prjRule
	 */
	private int getProjectCount(String prjRule) {
		List<CVU_BeanNameAndPath> fs = CVU_FileUtil.getFoldersInPro();
		int size = fs.size();

		prjRule = CVU_SPUtil.getLeftNamingRule(context, 1);

		int max = 0;
		for (int i = 0; i < size; i++) {
			String name = fs.get(i).getName();
			if (name.toLowerCase().contains("_")) { //规定用户手动输入的名字不能使用下划线，所以有下划线的都是规则的名字，可以读取编号
				int iii = name.lastIndexOf("_");
				String number = name.substring(iii + 1, name.length());
				try {
					int nb = Integer.valueOf(number);
					if (nb > max)
						max = nb;
				} catch (NumberFormatException e) {
					continue;
				}
			} else {
				continue;
			}
		}
		max++;

		return max;
	}

	/** 将临时录音文件保存在模板来源项目目录 */
	protected void operateTmpAudioFile() {
		saveBeCancel = false;
		saveAudioFileDialog.dismiss();

		File pf = new File(CVU_JSONUtil.json2Bean(CVU_SPUtil.getTemplate4SaveRecordParent(context)).getPath());

		copyFolder(new File(recordTmpPath), pf);
		deleteFolder(new File(recordTmpPath)).delete();

		saveCurrentProject(null);

		String pcmFilePath = pf.getAbsolutePath() + "/" + cvuMesPath + "/Signal/" + "111" + ".pcm";
		setReplayPthe(pcmFilePath);
	}

	/**
	 * 设置新建的项目或模板所在的项目 <b>为加载的项目</b>
	 * @param bean 新建项目传人项目bean，否则传入null
	 */
	private void saveCurrentProject(CVU_BeanNameAndPath bean) {
		if (null == bean) {
			String json = CVU_SPUtil.getTemplate4SaveRecordParent(context);
			CVU_SPUtil.saveCurrentProject(context, "dataFlag", 1 + ""); //加载模板是从data2，对应1
			CVU_SPUtil.saveCurrentProject(context, "currentProject", json);

			Toast.makeText(context, context.getResources().getString(R.string.template_be_inside_project_and_load), 0).show();
		} else {
			String json = CVU_JSONUtil.bean2Json(bean);
			CVU_SPUtil.saveCurrentProject(context, "dataFlag", 0 + "");
			CVU_SPUtil.saveCurrentProject(context, "currentProject", json);

			Toast.makeText(context, context.getResources().getString(R.string.create_new_project_and_load), 0).show(); //新项目创建保存，并加载为当前项目
		}
	}

	/** 保存临时录音文件到新项目 */
	protected void saveTmpAudioFile2Prj() {
		saveAudioFileDialog.dismiss();
		saveBeCancel = false;

		CVU_BeanNameAndPath bean = new CVU_BeanNameAndPath();

		String prjRule = CVU_SPUtil.getLeftNamingRule(context, 1); // prj下命名规则，默认"project"
		String prjMode = CVU_SPUtil.getLeftNamingRule(context, 3); // prj下命名模式，默认"0"-自定义。1-默认

		int max = getProjectCount(prjRule);
		String timestamp = CVU_TimeUtil.getTimestamp();
		String cvuPrjPath;
		if ("0".equals(prjMode)) {
			cvuPrjPath = prjRule + "_" + max;
		} else {
			cvuPrjPath = "Project" + "_" + max;
		}

		bean.setName(cvuPrjPath);
		bean.setCreateTime(timestamp);

		cvuPrjPath = timestamp + cvuPrjPath;

		File np = new File(CVU_FileUtil.dataPath + cvuPrjPath);
		np.mkdir();

		bean.setPath(np.getAbsolutePath());

		copyFolder(new File(recordTmpPath), np);
		deleteFolder(new File(recordTmpPath)).delete();

		// 尽管现在是新建的项目，但是如果模板来自其它项目，那么measurement包的编号就须要重置一下
		int from = checkCurrentProjectAndTemplate();
		if (1 == from || 2 == from) {
			File[] fs = np.listFiles();
			for (int i = 0; i < fs.length; i++) {
				if (fs[i].isDirectory()) {
					copyFolder(fs[i].getAbsoluteFile(), new File(np.getAbsolutePath() + "/" + CVU_TimeUtil.getTimestamp() + CVU_SPUtil.getLeftNamingRule(context, 2) + "1"));
					deleteFolder(fs[i].getAbsoluteFile()).delete();
					break;
				}
			}
		}

		// 文件名，创建日期，绝对路径
		saveCurrentProject(bean);

		String pcmFilePath = np.getAbsolutePath() + "/" + cvuMesPath + "/Signal/" + "111" + ".pcm";
		setReplayPthe(pcmFilePath);
	}

	/** 创建用于确认保存文件的对话框弹窗 **/
	private AlertDialog createDialog() {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout preferenceLayout = (RelativeLayout) inflater.inflate(R.layout.cvu_view_dialog_save, null);
		final AlertDialog preferenceDialog = new AlertDialog.Builder(context).create();
		preferenceDialog.setCancelable(false);
		preferenceDialog.show();
		preferenceDialog.getWindow().setContentView(preferenceLayout);
		preferenceDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

		// 更改标题
		if (saveDialogTitle != null && !(saveDialogTitle.equals(""))) {
			((TextView) preferenceLayout.findViewById(R.id.c_dialog_title)).setText(saveDialogTitle);
			((Button) preferenceLayout.findViewById(R.id.c_dialog_other)).setText(saveDialogButton);
		}

		return preferenceDialog;
	}

	/** 当右侧拉加载了项目时，直接保存录音文件 */
	private void directSave() {
		String cp2 = CVU_SPUtil.getCurrentProject(MyAPP.getContext(), "currentProject");
		CVU_BeanNameAndPath bnap2 = CVU_JSONUtil.json2Bean(cp2);

		copyFolder(new File(recordTmpPath + cvuMesPath), new File(bnap2.getPath() + "/" + cvuMesPath));
		deleteFolder(new File(recordTmpPath)).delete();

		String pcmFilePath = bnap2.getPath() + "/" + cvuMesPath + "/Signal/" + "111" + ".pcm";
		setReplayPthe(pcmFilePath);

		Toast.makeText(context, context.getResources().getString(R.string.save_2_loaded_project_pre) + bnap2.getName(), 0).show();
	}

	/**
	 * <b>功能</b>: setReplayPthe，设置pcm回放路径<br/>
	 * @param bnap2 pcm文件全局路径
	 */
	private void setReplayPthe(String pcmFilePath) {
		if (context instanceof MainActivity) // 添加判断，16.3.3 亦茜
			((MainActivity) context).setReplayPath(pcmFilePath); // 录音重播目录
	}

//////////////////////////////////// 原始代码 original code ////////////////////////////////////////////////////////////
//////////////////////////////////// 原始代码 original code ////////////////////////////////////////////////////////////
//////////////////////////////////// 原始代码 original code ////////////////////////////////////////////////////////////
//////////////////////////////////// 原始代码 original code ////////////////////////////////////////////////////////////
// TODO 原始代码 original code
	
	public void Start2(int hardType) {

		if (isRecording)
			return;
		//// 生成log文件
		try {
			log_fos = new FileOutputStream("sdcard/data/log.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		File pt = new File(CVU_FileUtil.dataPath + newProjectName);
		// Log.e("ard", "存在：" + pt.exists() + " - " + newProjectName);
		if (!pt.exists()) {
			newProjectName = createNewProject(); // 更新项目序号
			projectName = newProjectName;
		}

		isRecording = true;
		Log.e("ard", "DC 正在录");
		if ((projectName = newProject) == null) {
			switch (saveMethod) {
			case 0:
				projectName = newProjectName;
				break;
			case 1:
				projectName = oldProjectName;
				break;
			}
			projectFolder = new File("sdcard/data/pro/" + projectName);
			if (!projectFolder.exists()) {
				projectFolder.mkdirs();
				// 刚生成文件夹的时候，保存模板
				// --------------生成模板文件---------------
				String path = context.getApplicationContext().getFilesDir().getAbsolutePath();
				int num = path.lastIndexOf("/");
				String oldPath = path.substring(0, num + 1) + "shared_prefs/hz_5D.xml";

				String newPath = projectFolder.getPath() + "/template.xml";
				copyFile(new File(oldPath), new File(newPath));
			}
		} else {
			projectFolder = new File("sdcard/data/pro/" + projectName);
		}

		// ------------------生成run文件夹
		// String measurementName =
		// projectFolder.list()[projectFolder.list().length - 1];
		// int fileCount =
		// Integer.valueOf(measurementName.substring(runName.length(),
		// measurementName.length()));

		int fileCount = 0;

		/////////////////////// 添加排序防止覆盖 16.3.3 14:55 崔维友 -begin
		fileCount = CVU_FileUtil.getMeasurementFoldsCountInProject(CVU_FileUtil.dataPath + projectName);
		runName = "measurement";
		runName = CVU_TimeUtil.getTimestamp() + runName;
		/////////////////////// -over

		measurementFolder = new File(
				"sdcard/data/pro/" + projectName + "/" + runName + String.valueOf(fileCount + 1) + "/");
		measurementFolder.mkdirs();

		// Log.e("ard", "DataCollection measurementFolder：" +
		// measurementFolder.getAbsolutePath());

		File runFolder = new File(
				"sdcard/data/pro/" + projectName + "/" + runName + String.valueOf(fileCount + 1) + "/Signal/");
		runFolder.mkdirs();
		new File("sdcard/data/pro/" + projectName + "/" + runName + String.valueOf(fileCount + 1) + "/Result/")
				.mkdirs();
		Editor editor = sharedPreferences.edit();
		editor.putString("oldProject", projectName);
		editor.commit();

		// --------------生成模板文件---------------
		String path = context.getApplicationContext().getFilesDir().getAbsolutePath();
		int num = path.lastIndexOf("/");
		String oldPath = path.substring(0, num + 1) + "shared_prefs/hz_5D.xml";

		String newPath = measurementFolder.getPath() + "/template.xml";
		copyFile(new File(oldPath), new File(newPath));
		// --------------完毕---------------
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			mAudioFile = new File(runFolder, "111" + ".pcm");
		}
		switch (hardType) {
		case 0:// 硬件模式
			recordTask = new RecordTask();
			recordTask.start();
			break;
		case 1:// 本机模式
			if (recBufSize <= 0) {
				Toast.makeText(context, R.string.recording_prohibited, Toast.LENGTH_SHORT).show();
				return;
			}
			if (triggerMode == 2 && duration > 0) {
				byteBuffer = new byte[2];
			} else {
				byteBuffer = new byte[recBufSize];
			}
			collectionOverCalculateCount = 0;
			audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, AcquiFreq_spinner_values,
					AudioFormat.CHANNEL_IN_MONO, // 设置双声道
					AudioFormat.ENCODING_PCM_16BIT, recBufSize);
			audioRecord.startRecording();
			recordTask = new RecordTask();
			recordTask.start();
			break;
		}

		DataCollection.hardType = hardType;
		// rightDrawerFragment.setNewProject(projectName);
	}
	
	private String createNewProject() {
		List<CVU_BeanNameAndPath> list = CVU_FileUtil.getFoldersInPro();
		final int size = list.size();
		String name = "Project" + (size + 1);

		for (int i = 0; i < size; i++) {
			String name2 = list.get(i).getName();
			if (name.equalsIgnoreCase(name2)) {
				name = name + "_0";
				break;
			}
		}
		
		return name;
	}
	
	private void ShowPreferenceDialog2() {
		int res = checkCurrentProjectAndTemplate();

		String rule = CVU_SPUtil.getLeftNamingRule(context, 2);
		String mode = CVU_SPUtil.getLeftNamingRule(context, 4);

		Log.e("ard", "rule：" + rule + ",mode：" + mode);
		if ("0".equals(mode))
			runName = rule;
		else
			runName = "measurement";

		String cp = CVU_SPUtil.getCurrentProject(MyAPP.getContext(), "currentProject");
		CVU_BeanNameAndPath bnap = CVU_JSONUtil.json2Bean(cp);

		if (0 == res) { /////////////////// 16.3.31 崔 根据新需求，当右侧拉加载了项目时指出保存
			// 直接保存，不弹窗
			String cp2 = CVU_SPUtil.getCurrentProject(MyAPP.getContext(), "currentProject");
			CVU_BeanNameAndPath bnap2 = CVU_JSONUtil.json2Bean(cp2);

			File newmeasurementFolder = new File(bnap2.getPath() + "/" + CVU_TimeUtil.getTimestamp() + runName);
			newmeasurementFolder.mkdirs();
			copyFolder(measurementFolder, newmeasurementFolder);
			deleteFolder(new File(measurementFolder.getParent())).delete();

			File tempLogFile = new File("sdcard/data/log.txt");
			File logFile = new File(newmeasurementFolder + "/" + "Result" + "/log.txt");
			copyFile(tempLogFile, logFile);
			return;
		}
		// else if (1==res || 2==res || 3==res){
		// // 没有变化，按原流程
		// }
		// else if (4==res || -1==res){
		//
		// // 隐藏“save later project
		// }

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// 1. 布局文件转换为View对象
		RelativeLayout preferenceLayout = (RelativeLayout) inflater.inflate(R.layout.dialog_save, null);

		// 2. 新建对话框对象
		final AlertDialog preferenceDialog = new AlertDialog.Builder(context).create();
		preferenceDialog.setCancelable(false);
		preferenceDialog.show();
		preferenceDialog.getWindow().setContentView(preferenceLayout);
		preferenceDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

		// 3.更改标题
		if (saveDialogTitle != null && !(saveDialogTitle.equals(""))) {
			((TextView) preferenceLayout.findViewById(R.id.dialog_title)).setText(saveDialogTitle);
			((Button) preferenceLayout.findViewById(R.id.dialog_other)).setText(saveDialogButton);
		}

		// 4. 确定按钮
		Button btnOK = (Button) preferenceLayout.findViewById(R.id.dialog_ok);
		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				preferenceDialog.dismiss();
				saveBeCancel = false;
				String currentFileName = measurementFolder + "/" + "Signal" + "/" + "111.pcm";

				File tempLogFile = new File("sdcard/data/log.txt");
				File logFile = new File(measurementFolder + "/" + "Result" + "/log.txt");
				copyFile(tempLogFile, logFile);
				if (context instanceof MainActivity) // 添加判断，16.3.3 亦茜
					((MainActivity) context).setReplayPath(currentFileName);

				// Log.e("ard", "DataCollection currentFileName：" +
				// currentFileName);
			}
		});

		// 5. 取消按钮
		Button btnCancel = (Button) preferenceLayout.findViewById(R.id.dialog_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteFolder(measurementFolder).delete();
				sharedPreferences.edit().putString("oldProject", oldProjectName);
				preferenceDialog.dismiss();
				saveBeCancel = true;
			}
		});

		// 6. 其他按钮
		Button btnAnother = (Button) preferenceLayout.findViewById(R.id.dialog_other);

		TextView dialog_title = (TextView) preferenceLayout.findViewById(R.id.dialog_title);
		RelativeLayout rl_dialog_content = (RelativeLayout) preferenceLayout.findViewById(R.id.rl_dialog_content);
		if (ThemeLogic.themeType == 1) {
			rl_dialog_content.setBackgroundResource(R.drawable.welcome_title_corners_bg);
			btnOK.setBackgroundResource(R.drawable.btn_gray);
			btnCancel.setBackgroundResource(R.drawable.btn_gray);
			btnAnother.setBackgroundResource(R.drawable.btn_gray);
			dialog_title.setTextColor(Color.argb(255, 25, 25, 112));
		} else {
			rl_dialog_content.setBackgroundResource(R.drawable.welcome_title_corners_bg1);
			btnOK.setBackgroundResource(R.drawable.bt_gray_selector2);
			btnCancel.setBackgroundResource(R.drawable.bt_blue_selector1);
			btnAnother.setBackgroundResource(R.drawable.bt_gray_selector3);
			dialog_title.setTextColor(Color.argb(255, 255, 255, 255));
		}

		if (4 == res || -1 == res) { /////////////////// 16.3.31 崔
										/////////////////// 根据新需求，当加载为空模板时隐藏
			// 隐藏“save later project
			btnAnother.setVisibility(View.GONE);
		}

		String oldP = sharedPreferences.getString("oldProject", null);
		if (null != oldP && !"".equals(oldP) && !"null".equals(oldP)) {
			File fileOld = new File("sdcard/data/pro/" + oldProjectName + "/");
			if (!fileOld.exists() || (null == fileOld.list())) {
				btnAnother.setEnabled(false);
				btnAnother.setAlpha(0.5f);
				// btnAnother.setBackgroundResource(R.drawable.btn_gray_false);
				// btnAnother.setTextColor(Color.parseColor("#cccccc"));
			} else {
				btnAnother.setEnabled(true);
				btnAnother.setAlpha(1f);
				// btnAnother.setBackgroundResource(R.drawable.btn_gray);
				// btnAnother.setTextColor(Color.parseColor("#ffffff"));//R.color.cvu_datacollection_btnenable);
			}

			sharedPreferences.edit().putString("oldProject", null);
		} else {
			btnAnother.setEnabled(false);
			btnAnother.setAlpha(0.5f);
			// btnAnother.setBackgroundResource(R.drawable.btn_gray_false);
			// btnAnother.setTextColor(Color.parseColor("#cccccc"));//R.color.cvu_datacollection_btnfalse);
		}

		btnAnother.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveBeCancel = false;
				switch (saveMethod) {
				case 0:// 保存在上一次文件夹里
						// File[] files = new
						// File("sdcard/data/pro").listFiles();
						// if (files.length > 1) {
						// File laterFile = files[files.length - 2];
						// int fileCount = laterFile.list().length;
						// File newFolder = new File(laterFile.getPath() + "/"
						// + runName + String.valueOf(fileCount + 1) + "/");
						// newFolder.mkdirs();
						// copyFolder(runFolder, newFolder);
						// deleteFolder(runFolder).delete();
						// }
					projectName = oldProjectName;
					int oldProjectListSize = new File("sdcard/data/pro/" + projectName + "/").list().length;
					File lastmeasurementFolder = new File("sdcard/data/pro/" + projectName + "/" + runName);
					lastmeasurementFolder.mkdirs();
					copyFolder(measurementFolder, lastmeasurementFolder);
					deleteFolder(measurementFolder).delete();
					sharedPreferences.edit().putString("oldProject", projectName);
					measurementFolder = lastmeasurementFolder;
					break;
				case 1:// 保存在新文件夹里
					projectName = newProjectName;
					// Log.v("POSITION", projectName);
					File newProject = new File("sdcard/data/pro/" + projectName + "/");
					if (!newProject.exists()) {
						newProject.mkdirs();
					}
					int newProjectListSize = newProject.list().length;
					File newmeasurementFolder = new File(
							newProject.getPath() + "/" + runName + (newProjectListSize + 1));
					newmeasurementFolder.mkdirs();
					copyFolder(measurementFolder, newmeasurementFolder);
					deleteFolder(measurementFolder).delete();
					sharedPreferences.edit().putString("oldProject", projectName);
					measurementFolder = newmeasurementFolder;
					break;
				}
				preferenceDialog.dismiss();

				String currentFileName = measurementFolder + "/" + "Signal" + "/" + "111.pcm";

				// Log.v("POSITION", currentFileName);
				((MainActivity) context).setReplayPath(currentFileName);

				// 将log文件保存进result文件夹中
				File tempLogFile = new File("sdcard/data/log.txt");
				File logFile = new File(measurementFolder + "/" + "Result" + "/log.txt");
				copyFile(tempLogFile, logFile);
			}
		});

		// 不再询问
		CheckBox checkNotShown = (CheckBox) preferenceLayout.findViewById(R.id.not_shown);
		checkNotShown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					notShown = true;
				} else {
					notShown = false;
				}
			}
		});
	}
}
