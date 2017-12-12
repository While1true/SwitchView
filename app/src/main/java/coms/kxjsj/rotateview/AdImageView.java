package coms.kxjsj.rotateview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * Created by vange on 2017/12/5.
 */

public class AdImageView extends android.support.v7.widget.AppCompatImageView {
    private int mdy;
    public AdImageView(Context context) {
        this(context,null);
    }

    public AdImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AdImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDy(int dy){
        Drawable drawable = getDrawable();
        if(drawable==null){
            return;
        }
        mdy=dy-getHeight();
        if(mdy<0){
            mdy=0;
        }
        if(mdy>drawable.getBounds().height()-getHeight()){
            mdy=drawable.getBounds().height()-getHeight();
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        int width = getWidth();
        int h= (int) (width*1.0f/drawable.getIntrinsicWidth()*drawable.getIntrinsicHeight());
        drawable.setBounds(0,0,width,h);
        canvas.save();
        canvas.translate(0,-mdy);
        super.onDraw(canvas);
        canvas.restore();
    }

    public int getMdy() {
        return mdy;
    }
}
