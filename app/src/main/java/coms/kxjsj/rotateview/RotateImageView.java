package coms.kxjsj.rotateview;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vange on 2017/12/4.
 */

public class RotateImageView extends FrameLayout implements Animation.AnimationListener {

    private Rotation3DAnimation rotation3DAnimation;
    private Rotation3DAnimation rotation3DAnimation2;
    private Runnable action;
    private int measuredWidth;
    private int measuredHeight;

    public RotateImageView(@NonNull Context context) {
        this(context, null);
    }

    public RotateImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        ((LayoutParams)params).gravity= Gravity.CENTER;
        super.addView(child, index, params);
    }

    public void init3DRotate() {
        rotation3DAnimation = new Rotation3DAnimation(0, -90, 50, 0, measuredHeight / 2, true);
        rotation3DAnimation.setDuration(adapter.getSwitchTime());
        rotation3DAnimation.setInterpolator(new DecelerateInterpolator(0.9f-0.15f*(getResources().getDisplayMetrics().density*150f)/measuredWidth));
        rotation3DAnimation.setAnimationListener(this);

        rotation3DAnimation2 = new Rotation3DAnimation(90, 0, 50, measuredWidth, measuredHeight / 2, false);
//        rotation3DAnimation2.setInterpolator(new AccelerateInterpolator(0.1f+0.15f*measuredWidth/(getResources().getDisplayMetrics().density*150f)));
        rotation3DAnimation2.setDuration(adapter.getSwitchTime());
    }

    private void doLayoutView() {
        currentNum++;
        View view = attachedView.get(attachedView.size() - 1);
//        removeView(view);
        attachedView.get(attachedView.size() - 2).bringToFront();
        attachedView.remove(view);
        view = adapter.getView((currentNum + attachedView.size()) % adapter.getCount(), view, RotateImageView.this);
//        addView(view, 0);
        attachedView.add(0, view);

    }

    private void startAnimator() {
        int childCount = getChildCount();
        if (childCount <= 1) {
            return;
        }
       if(action==null) {
           action = new Runnable() {
               @Override
               public void run() {
                   View view = attachedView.get(attachedView.size() - 1);
                   View view2 = attachedView.get(attachedView.size() - 2);
                   view.startAnimation(rotation3DAnimation);
                   view2.startAnimation(rotation3DAnimation2);
               }
           };
       }
        attachedView.get(attachedView.size() - 1).postDelayed(action,adapter.getSwitchPeriod());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(rotation3DAnimation!=null&&!rotation3DAnimation.hasStarted()){
            startAnimator();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            attachedView.get(attachedView.size() - 2).clearAnimation();
            attachedView.get(attachedView.size() - 1).clearAnimation();
            attachedView.get(attachedView.size() - 1).removeCallbacks(action);
            attachedView.get(attachedView.size() - 2).removeCallbacks(action);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
        setLayerType(LAYER_TYPE_HARDWARE,null);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        setLayerType(LAYER_TYPE_NONE,null);
        doLayoutView();
        startAnimator();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(measuredWidth==0){
            measuredWidth=getMeasuredWidth();
        }

        if(measuredHeight==0){
            measuredHeight = getMeasuredHeight();
        }
    }

    /**
     * --------------------------模板代码----------------------------------
     */
    private void handleViews() {
        if (adapter == null || adapter.getCount() == 0) {
            return;
        }
        removeAllViews();
        attachedView.clear();

        int cachedSize = adapter.getCacheSize();
        for (int i = currentNum + cachedSize - 1; i >= currentNum; i--) {
            View view = adapter.getView(i % adapter.getCount(), null, this);
            attachedView.add(view);
            addView(view);
        }
    }

    private int currentNum;

    private ViewAdapter adapter;

    private List<View> attachedView = new ArrayList<>();

    private final DataSetObserver observer = new DataSetObserver() {
        @Override
        public void onChanged() {
            handleViews();
            post(new Runnable() {
                @Override
                public void run() {
                    init3DRotate();
                    startAnimator();
                }
            });

        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    };

    public void setAdapter(ViewAdapter adapter) {
        this.adapter = adapter;
        adapter.registerDataSetObserver(observer);
        adapter.notifyDataSetChanged();
    }

    public static abstract class ViewAdapter<T> extends BaseAdapter {
        List<T> list;
        int cacheSize = 2;
        long switchPeriod=5000;
        long switchTime=1500;

        public ViewAdapter(List<T> list) {
            this.list = list;
        }

        public void setList(List<T> list) {
            this.list = list;
        }

        public int getCacheSize() {
            return cacheSize;
        }

        public ViewAdapter setCacheSize(int cacheSize) {
            this.cacheSize = cacheSize;
            return this;
        }

        public List<T> getList() {
            return list;
        }

        public long getSwitchTime() {
            return switchTime;
        }

        public void setSwitchTime(long switchTime) {
            this.switchTime = switchTime;
        }

        public long getSwitchPeriod() {
            return switchPeriod;
        }

        public void setSwitchPeriod(long switchPeriod) {
            this.switchPeriod = switchPeriod;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View conventView, ViewGroup viewGroup) {
            return getView(i, list.get(i), conventView, viewGroup);
        }

        public abstract View getView(int i, T t, View conventView, ViewGroup viewGroup);
    }
}
