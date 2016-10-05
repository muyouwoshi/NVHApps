package left.drawer;
import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.mobileacquisition.R;

public class EditPopWindow extends PopupWindow implements OnItemClickListener,OnClickListener {
	public View view = null;
	public Activity context = null;
	private Display_Common common;
	public Button okButton;
	public Button deleteButton; 


	public EditPopWindow(Activity context,View v,Display_Common common) {
		this.view = v;
		this.context = context;
		this.common= common;
		ViewGroup root = (ViewGroup) context.findViewById(android.R.id.content);
		View view = LayoutInflater.from(context).inflate(R.layout.popwindow_common, root,false);
		okButton  = (Button) view.findViewById(R.id.ok);
		deleteButton  = (Button) view.findViewById(R.id.delete);
		
		this.setContentView(view);
		setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
//		setWidth(dip2px(v.getWidth()/2));
		setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		//闂傚倷娴囨竟鍫熺椤掑嫬鐤炬い鎰剁畱缁狀垶鎮楅敐搴℃灈缂侊拷鐗忛幉姝岀疀濞戞ê鍤戞繛锝呮矗妤犲紗ectPicPopupWindow闂備浇顕х�涒晠顢欓弽顓炵獥婵娉涚粻鐘绘煙閹规劦鍤欓柛姘愁潐閵囧嫰骞樼捄鐩掋儵鏌ｅ鍛笡濞ｅ洤锕幃娆撳箵閹哄棙瀵栫紓鍌欑椤︻垶濡堕幖浣歌摕婵炴垶菤閺嬪孩淇婇婵囶仩濞寸媭鍨跺铏圭矙濞嗘儳鍔�缂傚倸绉崇粈渚�鎮惧畡鎵虫瀻闁规儳鐡ㄥ▍銏ゆ⒑鐠恒劌娅愰柟鍑ゆ嫹	
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		//this.setBackgroundDrawable(new ColorDrawable(0x00000000));
		this.setBackgroundDrawable(new ColorDrawable(0x000000ff));
		//闂傚倷鑳堕崢褑銇愭径灞藉灊婵炲棗绻掗弳锔芥叏濡灝鐓愰柣鎾卞劦閹鏁愭惔鈥茬敖闂佸憡蓱閸旀瑥顫忛搹鍦＜婵妫涢崝鎼佹⒑缁洘娅嗛柣妤冨█瀵偊骞樼紒妯绘闂佽法鍣﹂幏锟�
		this.update();
		
		okButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
		
		//闂傚倷鑳堕崢褑銇愭径灞藉灊婵炲棙鎸哥粻鏍ㄧ箾閸℃ê鐏╁瑙勫▕閺屽秹鎮滃Ο鐓庣哎k闂傚倸鍊搁崐鐑芥倿閿曞偆鏁勬繛鍡樻尭缁愭鏌″搴″箹缂佺媭鍨伴湁闁稿繐鍚嬬紞鎴犵磼閿熶粙宕掗悙瀵稿幐闂佸壊鍋侀崹娲汲閳哄懏鈷戦柟顖嗗喛鎷烽崸妤�钃熸繛鎴欏灪閺呮粓鏌熼鍡楁湰濮ｅ姊绘担鍛婃儓闁活剙銈稿畷浼村箛閺夎法顔嗛梺鍛婄♁濞兼瑩宕归崒娑栦簻闁哄啫鍊哥敮鍓佺磼妫版繂骞栭柍瑙勫灴閹瑩寮堕幋鐘辨樊闂備胶绮〃鍛涘┑鍥︾箚闁汇垹鎲￠弲绋棵归悩宸剱闁稿绲介湁闁挎繂娴傞悞楣冩煃闁垮顥堥柡宀嬬秮婵″爼宕ㄩ鍛紒缂傚倷绀侀崐鍝ョ矓閻熸壆鏆﹂柛妤冨剱濞尖晠寮堕崼姘珦闁哥喎娲ら埞鎴︽倷閺夋垹浠搁梺鐟扮毞閺呮粎绮嬪鍛斀闁搞儯鍔嶉悵鐑芥⒑缂佹﹩鐒界紒顕呭灦瀹曠敻宕熼娑氬幈婵犵數濮寸�氼剟寮查崸妤佺厸闁搞儯鍎遍悘鈺呮煟椤撶噥娈滈柡宀�鍠愬蹇涘Ω瑜忛幊妤smisslistener闂傚倷鑳堕崢褑銇愭径灞藉灊婵炲棙鎸搁弸浣猴拷閵夈儲鍣介柡鍡楁閺屾盯骞囬鐐插Б闂佸搫鎷嬮崢鍓ф閹捐纾兼慨姗嗗墯閻忔捇姊洪崫銉バｉ柣妤冨Т閻ｇ兘鏁愭径瀣拷濡炪倖甯掗崑鍡椕归崟顖涒拺闁告繂瀚ⅹ闂佸憡鏌ㄩ張顒傜矉閹烘嚦鐔哥附閽樺绉洪柟顔规櫊閹粌螣缁楃尨鎷烽幇鐗堝仭婵犲﹤瀚欢鍙夈亜閿曞倹娑ч柣锝囧厴閹晝鎷犻幓鎺戞暏婵＄偑鍊栭崝鎴﹀磹閺嶎厼绠氶柨婵嗩槹閳锋垿鏌ｉ悢绋款棆濞寸姰鍨荤槐鎺撳緞婵犲嫸绱炵紓浣戒含閸嬨倝寮幘缁樻櫢闁跨噦鎷�	this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popwindow));
	}
	
	public void showPopupWindow() {
		if (!this.isShowing()) {
			//闂傚倷娴囨竟鍫㈠垝閻愵剚鍙忛柕鍫濇川閻鏌熼悜姗嗘當閸烆垶姊洪棃娑氬婵炲眰鍔戦獮澶愵敋閿熻棄顫忓ú顏呭仭闁哄瀵т簺婵＄偑鍊х紓姘跺础閸愬樊鍤曞┑鐘崇閸嬪嫬鈹戦崒姘兼Ц婵炲牄鍔戝Λ鍛搭敃閵忊�愁槱濠殿喖锕ょ紞濠囧春閵忋倕绫嶉柛灞剧矌閿涙粓姊虹捄銊ユ灁闁哄拋鍋勯埢鎾惰姳閸楃灝pwindow
			this.showAsDropDown(view);
		} else {
			this.dismiss();
		}
	}
	public int dip2px(float dpValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		this.dismiss();
	}




	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(arg0.getId() == R.id.ok){
			common.aplay(view);
			this.dismiss();
		}
		else if(arg0.getId() == R.id.delete){
			common.delete(view);
			this.dismiss();
		}
	}

}
