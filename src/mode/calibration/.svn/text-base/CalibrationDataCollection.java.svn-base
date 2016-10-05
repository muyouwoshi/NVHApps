package mode.calibration;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

import right.drawer.FileListAdapter;

import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class CalibrationDataCollection {

	private byte[] byteBuffer;
	public static int[] buffer;
	private AudioRecord audioRecord;
	private int recBufSize;
	private int channelType;
	private Handler myHandler;
//	private File mAudioFile;
	private String projectName;
	private Handler projectNameChangeHandler;
	private int equipment_Num;
	private Socket socket;
	private boolean ifConnectSuccessfully = false;
	private StringBuffer sendMsg;
	private BufferedWriter writer;
	private OutputStream out;
	private InputStream input;
	private int hardType;
	public CalibrationDataCollection(Handler myHandler) {
		this.myHandler = myHandler;
		recBufSize = AudioRecord.getMinBufferSize(48000,
				AudioFormat.CHANNEL_IN_STEREO,// 设置双声道
				AudioFormat.ENCODING_PCM_16BIT);

		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 48000,
				AudioFormat.CHANNEL_IN_STEREO,// 设置双声道
				AudioFormat.ENCODING_PCM_16BIT, recBufSize);
	}
	public void Start(int equipment_Num) {
		this.equipment_Num=equipment_Num;
		switch(equipment_Num){
		case 2://硬件模式
			new RecordTask().start();
			break;
		case 1://麦克风模式
			byteBuffer = new byte[recBufSize];
			buffer=new int[recBufSize/2];
			audioRecord.startRecording();
			new RecordTask().start();
			break;
		}
		this.equipment_Num=equipment_Num;
	}

	public void Stop() {
		switch(equipment_Num){
		case 2://硬件模式
			if (!ifConnectSuccessfully) {
				return;
			}
			sendMsg.delete(0, sendMsg.length());
			sendMsg.append("HX 0000close DY");
			try {
				writer.write(sendMsg.toString().trim());
				writer.flush();
				input.close();
				out.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 1://手机麦克模式
			if(audioRecord==null){
				return;
			}
			if (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_STOPPED) {
				return;
			}
			audioRecord.stop();
			audioRecord.release();
			audioRecord = null;
			buffer = null;
			break;
		}
	}
	public void setChannelType(int channelType){
		this.channelType=channelType;
	}

	class RecordTask extends Thread {
		@Override
		public void run() {
			switch(equipment_Num){
			case 2://硬件模式
				// ------------建立连接-------
				final SocketAddress address = new InetSocketAddress("192.168.2.8", 7);
				socket = new Socket();
				try {
					// --------连接硬件---------
					socket.connect(address);
					ifConnectSuccessfully = true;
					// ----------发送命令-------
					out = socket.getOutputStream();
					writer = new BufferedWriter(new OutputStreamWriter(out));
					sendMsg = new StringBuffer();
					sendMsg.append("HX 0000open DY");
					writer.write(sendMsg.toString().trim());
					writer.flush();
					// -------初始化数据源--------
					input = socket.getInputStream();
					byteBuffer = new byte[1024];
				} catch (Exception e) {
					// TODO Auto-generated catch block
					ifConnectSuccessfully = false;
					e.printStackTrace();
				}				
				while (!socket.isClosed()) {				
					try {
						input.read(byteBuffer);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Message msg = myHandler.obtainMessage();
					msg.arg1 = channelType;
					myHandler.sendMessage(msg);;
				}
				break;
			case 1://麦克模式
				if(byteBuffer==null){
					byteBuffer=new byte[recBufSize];
					buffer=new int[recBufSize/2];
				}
				while (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
					audioRecord.read(byteBuffer, 0, recBufSize);
					ByteBuffer tempBuffer = ByteBuffer.wrap(byteBuffer); // NullPointerException: Attempt to get length of null array
					for (int i = 0; tempBuffer.position() < tempBuffer.capacity(); i++) {
						buffer[i] = tempBuffer.getShort() << 8;
					}

						Message msg = myHandler.obtainMessage();
						msg.arg1 = channelType;
						myHandler.sendMessage(msg);;
				}
				break;
			}
			super.run();
		}
	}
}