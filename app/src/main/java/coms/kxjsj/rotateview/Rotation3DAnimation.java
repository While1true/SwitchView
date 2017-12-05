package coms.kxjsj.rotateview;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by vange on 2017/12/4.
 */

public class Rotation3DAnimation extends Animation {
    final float startDegree;
    final float endDegree;
    final float deep;
    final float centreX;
    final float centreY;
    final boolean reverse;
    final private Camera camera;

    public Rotation3DAnimation(float startDegree,float endDegree,float deep,float centreX,float centreY,boolean reverse){
        this.startDegree=startDegree;
        this.endDegree=endDegree;
        this.deep=deep;
        this.centreX=centreX;
        this.centreY=centreY;
        this.reverse=reverse;
        this.camera=new Camera();

    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        if(centreY==0){
            return;
        }
        float currentDegree=startDegree+(endDegree-startDegree)*interpolatedTime;
        Matrix matrix = t.getMatrix();
        camera.save();
//        camera.translate(0,0,deep);
        camera.rotateY(currentDegree);
        camera.getMatrix(matrix);
        matrix.postScale(!reverse?interpolatedTime:(1-interpolatedTime),!reverse?interpolatedTime:(1-interpolatedTime));
        camera.restore();
        System.out.println(centreX+"ppp"+centreY);
        matrix.preTranslate(-centreX,-centreY);
        matrix.postTranslate(centreX,centreY);
    }
}
