package voice.operation;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class VoiceCollection {

	// 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
	//private final int frequency = 48000;
	// 设置双声道
	//private final int channelConfiguration = AudioFormat.CHANNEL_IN_STEREO;
	// 音频数据格式：每个样本16位
	//private final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	
	private AudioRecord audioRecord;
	private int recBufSize;
	
	public VoiceCollection() {
		// TODO Auto-generated constructor stub
		recBufSize = AudioRecord.getMinBufferSize(48000,  
				  								  AudioFormat.CHANNEL_IN_STEREO,// 设置双声道 
				  								  AudioFormat.ENCODING_PCM_16BIT); 

		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 
									  48000,  
									  AudioFormat.CHANNEL_IN_STEREO,// 设置双声道 
									  AudioFormat.ENCODING_PCM_16BIT, 
									  recBufSize);
	}
}
