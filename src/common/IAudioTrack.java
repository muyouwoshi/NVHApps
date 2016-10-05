package common;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class IAudioTrack {

	public byte[] mAudioTrackShortBuffer;
	public static int[] mAudioTrackIntBuffer;
	private AudioTrack mAudioTrack;
	private File mAudioFile;
	private Handler myHandler;
	private MainActivity activity;
	public static int hardType;
	private int channelNum = 1;// �ط��ļ�����ͨ����
	private ArrayList<Integer> channel;
	public static List<int[]> mAudioTrackIntBufferList;
	public static int shortBufferSize = 0;
	private int trackBufferSizeInBytes;
	private List<byte[]> mAudioTrackByteBufferList;
	public static float[] rpmdata;// ��δ��ֵ
	public static int readState = -1;
	public static int audioOverCalculateCount = 0;// ����طŵ��㷨���� by������

	public IAudioTrack(MainActivity activity, Handler myHandler, ArrayList<Integer> channel) {
		// TODO Auto-generated constructor stub
		this.myHandler = myHandler;
		this.activity = activity;
		this.channel = channel;
		create_mAudioTrack();
	}

	public void start(int hardType) {
		if (mAudioTrack == null) {
			create_mAudioTrack();
		}
		audioOverCalculateCount = 0;
		this.hardType = hardType;
		mAudioFile = new File(activity.getReplayPath());
		if(!mAudioFile.exists()){
			Toast.makeText(activity,  activity.getResources().getString(R.string.save_failed), Toast.LENGTH_SHORT).show();
			return;
		}
		new AudioTrackTask().start();
	}

	public void stop() {

		if (mAudioTrack != null) {
			try {
				mAudioTrack.stop();
				mAudioTrack.release();
				mAudioTrack = null;
			} catch (Exception e) {

			}
		}

	}

	private void create_mAudioTrack() {
		int sound = channel.size() == 1 ? AudioFormat.CHANNEL_OUT_MONO : AudioFormat.CHANNEL_OUT_STEREO;
		trackBufferSizeInBytes = AudioRecord.getMinBufferSize(48000, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		mAudioTrackShortBuffer = new byte[trackBufferSizeInBytes];
		// ------------------������Ͽ���ɾ��----------
		mAudioTrackIntBufferList = new ArrayList<int[]>();
		mAudioTrackByteBufferList = new LinkedList<byte[]>();
		// ------------------���-----------------------
		mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 48000, sound, AudioFormat.ENCODING_PCM_16BIT,
				trackBufferSizeInBytes, AudioTrack.MODE_STREAM);
	}

	class AudioTrackTask extends Thread {

		@Override
		public void run() {
			try {
				mAudioTrack.play();
				readState = 0;
				DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(mAudioFile)));
				while (activity.getPlayBackState() == true && dis.available() > 0) {
					mAudioTrackIntBuffer = new int[trackBufferSizeInBytes / 2];
					int i = 0;
					while (dis.available() > 0 && i < mAudioTrackShortBuffer.length) {
						for (int j = 0; j < channelNum; j++) {
							if (channel.contains(j + 1)) {
								mAudioTrackShortBuffer[i] = dis.readByte();
								mAudioTrackShortBuffer[i + 1] = dis.readByte();
							} else {
								dis.readShort();
							}
						}
						i += 2;
					}
					ByteBuffer tempBuffer = ByteBuffer.wrap(mAudioTrackShortBuffer).order(ByteOrder.LITTLE_ENDIAN);
					mAudioTrackByteBufferList.add(mAudioTrackShortBuffer);
					for (int j = 0; tempBuffer.position() < tempBuffer.capacity(); j++) {
						mAudioTrackIntBuffer[j] = tempBuffer.getShort() << 8;
					}
					mAudioTrackIntBufferList.add(mAudioTrackIntBuffer);
					Message msg = myHandler.obtainMessage();
					msg.what = 1;
					myHandler.sendMessage(msg);
					// Ȼ�����д�뵽AudioTrack��
					if (mAudioTrack == null) {
						break;
					}
					if (mAudioTrack == null || mAudioTrack.getPlayState() == mAudioTrack.PLAYSTATE_STOPPED) {
						break;
					}
					// try{
					mAudioTrack.write(mAudioTrackShortBuffer, 0, mAudioTrackShortBuffer.length);
					// }catch(Exception e){
					// if(mAudioTrack==null||mAudioTrack.getPlayState()==mAudioTrack.PLAYSTATE_STOPPED){
					// break;
					// }
					// }

				}
				readState = 1;
				int viewListSize = activity.viewList.size();
				while (audioOverCalculateCount != activity.viewList.size()) {
					if (mAudioTrackIntBufferList.size() == 0) {
						break;
					}
					if (mAudioTrack == null || mAudioTrack.getPlayState() == mAudioTrack.PLAYSTATE_STOPPED) {
						mAudioTrack = null;
						readState = 3;
					}
					Message msg1 = myHandler.obtainMessage();
					msg1.what = 1;
					myHandler.sendMessage(msg1);
					if (mAudioTrack != null) {
						if (mAudioTrack.getPlayState() != mAudioTrack.PLAYSTATE_STOPPED) {
							if (shortBufferSize < mAudioTrackByteBufferList.size() - 1) {
								byte[] mAudioByteBuffer = mAudioTrackByteBufferList.get(shortBufferSize);
								mAudioTrack.write(mAudioByteBuffer, 0, mAudioByteBuffer.length);
							}
						}
					}
				}
				readState = 2;
				mAudioTrackByteBufferList.removeAll(mAudioTrackByteBufferList);
				mAudioTrackIntBufferList.removeAll(mAudioTrackIntBufferList);
				IAudioTrack.this.stop();
				dis.close();
				Message msg = myHandler.obtainMessage();
				msg.what = 0;
				myHandler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
			super.run();
		}
	}
}
