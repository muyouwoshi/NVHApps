package voice.operation;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class VoiceCollection {

	// ������Ƶ�����ʣ�44100��Ŀǰ�ı�׼������ĳЩ�豸��Ȼ֧��22050��16000��11025
	//private final int frequency = 48000;
	// ����˫����
	//private final int channelConfiguration = AudioFormat.CHANNEL_IN_STEREO;
	// ��Ƶ���ݸ�ʽ��ÿ������16λ
	//private final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	
	private AudioRecord audioRecord;
	private int recBufSize;
	
	public VoiceCollection() {
		// TODO Auto-generated constructor stub
		recBufSize = AudioRecord.getMinBufferSize(48000,  
				  								  AudioFormat.CHANNEL_IN_STEREO,// ����˫���� 
				  								  AudioFormat.ENCODING_PCM_16BIT); 

		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 
									  48000,  
									  AudioFormat.CHANNEL_IN_STEREO,// ����˫���� 
									  AudioFormat.ENCODING_PCM_16BIT, 
									  recBufSize);
	}
}
