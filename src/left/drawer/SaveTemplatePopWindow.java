package left.drawer;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileacquisition.R;

public class SaveTemplatePopWindow extends PopupWindow implements OnItemClickListener,OnClickListener,OnGlobalLayoutListener{
	public Activity context = null;
	public Button okButton;
	public Button deleteButton;
	private EditText fileNameEdit;
	private MainFragment mainFragment;
	public TextView template_name_text;
	public View templateView;
	public View view;
	public ViewGroup root;
	private int oldX;
	private int oldY;
	
	public SaveTemplatePopWindow(Activity context,MainFragment mainFragment) {
		this.context = context;
		this.mainFragment = mainFragment;
		root = (ViewGroup) context.findViewById(android.R.id.content);
		view = LayoutInflater.from(context).inflate(R.layout.popwindow_save_template, null,false);
		
		template_name_text=(TextView)view.findViewById(R.id.template_name_text);
		okButton  = (Button) view.findViewById(R.id.saveTemplate_ok);
		deleteButton  = (Button) view.findViewById(R.id.saveTemplate_cancle);
		fileNameEdit = (EditText)view.findViewById(R.id.template_name_edit);
		this.setContentView(view);
		setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
		setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		//闂佽壈浜弲顐﹀箯鐎ｎ喖�?堢憸蹇涘几娣囦骏lectPicPopupWindow闁诲孩顔栭崰姘跺箠閹捐鍚规い鎾跺Х閻滅粯淇婇悙鎻掆挃缂佸銈搁弻娑樷枎濡櫣浠梺绋款儜缂嶄礁鐣峰▎鎾存櫢闁跨噦鎷�	
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		//this.setBackgroundDrawable(new ColorDrawable(0x00000000));
		this.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
		//闂佺厧褰夌划娆忕暦濮樿埖鐓ラ悗锝庡亞閸樻劙姊虹紒姗嗙劸闁糕晜鐗犻弫鎾绘晸閿燂�?	
		this.update();
		
		okButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
		mainFragment.getView().getViewTreeObserver().addOnGlobalLayoutListener(this);
		SimpleDateFormat  formatter  =  new  SimpleDateFormat    ("yyyyMMdd_HHmmss");       
		Date    curDate    =   new    Date(System.currentTimeMillis());//��ȡ��ǰʱ��
		String fileName = formatter.format(curDate);
		
		fileNameEdit.setText(fileName);
		fileNameEdit.requestFocus();
		//闂佺厧褰夌划娆撳箖濞嗘垹椹抽柍鐑樼床k闂傚倷鐒﹂娆撳窗閺嶎厼绠ù鍏兼綑缁�鍌炴煛閸垺鏆╅梺鎯у�块弻娑㈡晜閹屾毉闂佸搫鐬奸崰鎰板箯閸涙潙鍨傛い鏃傚帶缁额厼鈹戦悙鏉戠仴闁绘妫濇俊鐢告晸娴犲鍋ｅù锝囶焾閳锋棃鏌ｉ妶鍛棡缂佸倸绉瑰畷鍗烆潩鏉堚晩鍟囧┑鐐村灦閹尖晜绂嶅┑鍥ㄦ珷闁绘绮崵鍕煕婵犲嫬鏅块柡鍥ュ灩閻鏌熸潏銊х懘Dismisslistener闂佺厧褰夌划娆撳极�?�ュ懐鏆嗛柛鎰劤閺咁厾绱撻崒姘灓闁哥姵鐗犲畷锝夋倷椤掑偆娴勯梺鍝勭Р閸斿本绂掑Ο濂藉綊鎮╅悜妯笺�愰悷婊勬緲椤﹂潧鐣烽悩璇插唨妞ゆ劑鍊栭幆锝夋⒑閻熸壆浠㈢紒澶婄－缁辩偤鏁撻敓锟�?	this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popwindow));
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
		if(arg0.getId() == R.id.saveTemplate_ok){
			if(fileNameEdit.getText().toString().matches("\\s*")){
				Toast.makeText(context, R.string.template_name_null, Toast.LENGTH_SHORT).show();
				return;
			}
			mainFragment.saveTemplate(fileNameEdit.getText().toString());
		//	this.dismiss();
		}
		else if(arg0.getId() == R.id.saveTemplate_cancle){
			this.dismiss();
		}
		else if(arg0.getId() == R.id.template_name_edit){
			
		}
	}
	
	public void SetTemplateXY(int tx, int ty) {
		this.oldX = tx;
		this.oldY = ty;
		
	}

	@Override
	public void onGlobalLayout() {
		// TODO Auto-generated method stub
		Rect r = new Rect();
		root.getWindowVisibleDisplayFrame(r);
		int keyBoardSize = root.getHeight()-r.height(); 
		Log.v("bug11", "X:"+" y:");
		if(oldY*oldX != 0){
			DisplayMetrics metric = new DisplayMetrics();  
			context.getWindowManager().getDefaultDisplay().getMetrics(metric);  
			int height = metric.heightPixels;   // ��Ļ�߶ȣ����أ� 
			int x,y;
			if(keyBoardSize >100 ){
				y = (int) (height - keyBoardSize-this.getContentView().getHeight() );
			}else{
				y = oldY;
			}
			x = oldX;
//			this.showAtLocation(root, Gravity.NO_GRAVITY, x, y);
			this.update(x,y,this.getContentView().getWidth(),this.getContentView().getHeight());

		}
	}

	@Override
	public void dismiss() {
		mainFragment.getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
		super.dismiss();
	}
	public void languageChanged(){
		template_name_text.setText(R.string.template_name);
		okButton.setText(R.string.Save);
		deleteButton.setText(R.string.Cancel);
	}
}
