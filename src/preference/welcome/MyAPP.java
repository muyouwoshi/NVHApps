package preference.welcome;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.example.mobileacquisition.WelcomeActivity.MyHandler;

/**
 * 闂佺厧顨庢禍婊呮崲娴ｇ尨鎷烽柣鎰版涧缁犳睔pplication闂佹寧绋戦懟顖炴偪閸曨垱鍋濋柣妤�墛濞堝爼鏌熺拠鈥虫灈闁告瑢鍓濈粋宥夋晸閿燂�?
 * @author mark
 *
 */
public class MyAPP extends Application {
	// 闂佺绻愯ぐ澶愭閳哄懎鐭楁慨妤嬫嫹濞咃�?private MyHandler handler = null;
	private MyHandler handler = null;
	// set闂佸搫鍊介～澶屾兜閿燂拷
	public void setHandler(MyHandler hdl) {
		handler = hdl;
	}
	
	// get闂佸搫鍊介～澶屾兜閿燂拷
	public MyHandler getHandler() {
		return handler;
	}
	
	private static Handler cvu_handler;
	
	/**�������Ļ�ȡһ��ȫ�ֵ���ʱ�� Handler */ // 16.3.7 ��ά����� ��̬Handler
	public static Handler getCvu_handler() {
		return cvu_handler;
	}
	
	/**��һ��Handler��Ϊȫ�ֵ� ��ʱ�� */ // 16.3.7 ��ά����� ��̬Handler
	public static void setCvu_handler(Handler cvu_handler) {
		MyAPP.cvu_handler = cvu_handler;
	}

	private static Context ctx;
	/** ��ȡȫ��Context */ // 16.3.28 ��
	public static Context getContext() {
		return ctx;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		ctx = getApplicationContext();
	}
}
