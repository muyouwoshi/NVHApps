package preference.welcome;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.example.mobileacquisition.WelcomeActivity.MyHandler;

/**
 * 闂備胶鍘ч〃搴㈢濠婂懏宕插ù锝囧皑閹风兘鏌ｉ幇鐗堟锭缂佺姵鐫攑plication闂備焦瀵х粙鎴︽嚐椤栫偞鍋柛鏇ㄥ灡閸嬫繈鏌ｅΔ锟藉婵炲牆鐖奸弻鐔烘嫚閳ヨ櫕鐏堥梺鍛婄憿閸撴繄绮嬪澶嬫櫢闁跨噦鎷?
 * @author mark
 *
 */
public class MyAPP extends Application {
	// 闂備胶顭堢换鎰亹婢舵劖顥婇柍鍝勬噹閻鎱ㄥΔ瀣婵炲拑鎷?private MyHandler handler = null;
	private MyHandler handler = null;
	// set闂備礁鎼崐浠嬶綖婢跺本鍏滈柨鐕傛嫹
	public void setHandler(MyHandler hdl) {
		handler = hdl;
	}
	
	// get闂備礁鎼崐浠嬶綖婢跺本鍏滈柨鐕傛嫹
	public MyHandler getHandler() {
		return handler;
	}
	
	private static Handler cvu_handler;
	
	/**从上下文获取一个全局的临时型 Handler */ // 16.3.7 崔维友添加 静态Handler
	public static Handler getCvu_handler() {
		return cvu_handler;
	}
	
	/**将一个Handler设为全局的 临时的 */ // 16.3.7 崔维友添加 静态Handler
	public static void setCvu_handler(Handler cvu_handler) {
		MyAPP.cvu_handler = cvu_handler;
	}

	private static Context ctx;
	/** 获取全局Context */ // 16.3.28 崔
	public static Context getContext() {
		return ctx;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		ctx = getApplicationContext();
	}
}
