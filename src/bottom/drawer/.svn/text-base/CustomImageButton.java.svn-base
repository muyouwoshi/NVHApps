package bottom.drawer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * 自定义ImageButton
 * 可以在ImageButton上面设置文字
 * @author SJR
 */
public class CustomImageButton extends ImageButton {
    private String _text = "";
    private int _color = 0;
    private float _textsize = 0f;
    private boolean _b=false;
    private boolean ifActivity;
    public CustomImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
       // setBackgroundResource(R.drawable.ico_channel_noactive_jiashi);
    }
    
    public void setText(String text){
        this._text = text;
    }
    
    public void setColor(int color){
        this._color = color;
    }
    
    public void setTextSize(float textsize){
        this._textsize = textsize;
    }
    public boolean setFakeBoldText(boolean b){
    	this._b=b;
    	return _b;
    }

    //----------获取按钮激活状态---------
    public void setIfChannelActivity(boolean ifActivity){
    	this.ifActivity=ifActivity;
    }
    public boolean ifChannelActivity(){
    	return ifActivity;
    }
    //--------------完毕-------------
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setTextAlign(Align.CENTER);
        paint.setColor(_color);
        paint.setTextSize(_textsize);
        paint.setFakeBoldText(_b);
        canvas.drawText(_text, canvas.getWidth()/2, (canvas.getHeight()/2)+8, paint);
    }
}