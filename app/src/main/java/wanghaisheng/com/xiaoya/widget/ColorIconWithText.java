package wanghaisheng.com.xiaoya.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import wanghaisheng.com.xiaoya.R;

/**
 * Created by sheng on 2016/1/12.
 */
public class ColorIconWithText extends View {
    private int mColor = 0xFF45C01A;
    private int mTextColor = 0xff7E7E7C;
    //tab的icon，other attrs
    private Bitmap mNormalBitmap;
    private Bitmap mFocusedBitmap;
    private String mText = "微信";
    private int mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,7,getResources().getDisplayMetrics());

    private Paint mPaint;

    private Rect mIconRect;
    private Rect mTextBound;

    private Paint mTextPaint;

    private float mAlpha;

    //存储系统bundle和自定义的alpha值的key
    private static final String INSTANCE_STATUS = "instance_status";
    private static final String STATUS_ALPHA = "status_alpha";

    public ColorIconWithText(Context context) {
        super(context, null);
    }

    public ColorIconWithText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorIconWithText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray datas = context.obtainStyledAttributes(attrs, R.styleable.ColorIconWithText);
        //获取布局文件中的自定义属性
        int count = datas.getIndexCount();
        for(int i=0; i<count; i++) {
            int attr = datas.getIndex(i);
            switch (attr) {
                case R.styleable.ColorIconWithText_normal_icon:
                    BitmapDrawable normalIcon = (BitmapDrawable) datas.getDrawable(attr);
                    mNormalBitmap = normalIcon.getBitmap();
                    break;
                case R.styleable.ColorIconWithText_focused_icon:
                    BitmapDrawable focusedIcon = (BitmapDrawable) datas.getDrawable(attr);
                    mFocusedBitmap = focusedIcon.getBitmap();
                    break;
                case R.styleable.ColorIconWithText_text_color:
                    int color = datas.getColor(attr,mColor);
                    mColor = color;
                    break;
                case R.styleable.ColorIconWithText_text_size:
                    int size = (int) datas.getDimension(attr,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,7,getResources().getDisplayMetrics()));
                    mTextSize = size;
                    break;
                case R.styleable.ColorIconWithText_text:
                    mText = datas.getString(attr);
                    break;
            }
        }

        datas.recycle();

        initDatas();
    }

    /**
     * 获取字体的宽度和高度所在的Rect
     */
    private void initDatas() {
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xff555555);

        mTextBound = new Rect();
        //通过mTextPaint获取TextView字体的宽和高
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
        int height = getMeasuredHeight()-getPaddingTop()-getPaddingBottom()-mTextBound.height();
        //获取icon的宽度（宽度和高度就一样，所以就选宽度和高度的最小值）
        int iconWidth = Math.min(width,height);
        iconWidth = (int) (iconWidth*0.9f);
        //int iconHeight = (int) (iconWidth*(84*1.0f/96));
        int iconHeight = iconWidth;
        iconHeight = (int) (iconHeight*0.9f);

        //获取icon的left和top
        int left = getMeasuredWidth()/2-iconWidth/2;
        int top = (getMeasuredHeight()-mTextBound.height()-iconHeight)/2;

        //获取icon所在的rect
        mIconRect = new Rect(left,top,left+iconWidth,top+iconHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawBitmap(mIconBitmap, null, mIconRect, null);

        int alpha = (int) Math.ceil(255*mAlpha);

        //canvas.drawBitmap(mBitmap, 0, 0, null);
        drawSourceBitmap(canvas,alpha);
        drawTargetBitmap(canvas,alpha);

        //绘制文本的颜色
        //1、绘制源文字
        drawSourceText(canvas, alpha);
        //2、绘制目标文字（带颜色的文字）
        drawTargetText(canvas, alpha);
    }

    /**
     * 绘制源bitmap
     * @param canvas
     * @param alpha
     */
    private void drawSourceBitmap(Canvas canvas,int alpha) {
        mPaint = new Paint();
        //Log.e("TextColor==>","  "+mColor);
        //Log.e("Normal Color==>"," "+0xFF45C01A);
        mPaint.setAntiAlias(true);//设置抗锯齿
        mPaint.setDither(true);
        mPaint.setAlpha(255-alpha);

        canvas.drawBitmap(mNormalBitmap,null,mIconRect,mPaint);
    }

    /**
     * 绘制目标bitmap
     * @param alpha
     */
    private void drawTargetBitmap(Canvas canvas,int alpha) {
        mPaint = new Paint();
        //Log.e("TextColor==>","  "+mColor);
        //Log.e("Normal Color==>"," "+0xFF45C01A);
        mPaint.setAntiAlias(true);//设置抗锯齿
        mPaint.setDither(true);
        mPaint.setAlpha(alpha);

        canvas.drawBitmap(mFocusedBitmap,null,mIconRect,mPaint);
    }

    /**
     * 绘制目标文字（带颜色的文字）
     * @param canvas
     * @param alpha
     */
    private void drawTargetText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);
        int x = getMeasuredWidth()/2-mTextBound.width()/2;
        int y = mIconRect.bottom + mTextBound.height();
        canvas.drawText(mText, x, y, mTextPaint);
    }

    /**
     * 绘制源文本
     * @param canvas
     * @param alpha
     */
    private void drawSourceText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mTextColor);
//        mTextPaint.setColor(0xffADADAD);
        mTextPaint.setAlpha(255-alpha);
        int x = getMeasuredWidth()/2-mTextBound.width()/2;
        int y = mIconRect.bottom + mTextBound.height();
        canvas.drawText(mText, x, y, mTextPaint);
    }

    /**
     * 设置alpha
     * @param alpha
     */
    public void setIconAlpha(float alpha) {
        this.mAlpha = alpha;
        invalidateView();
    }

    /**
     * 重绘UI
     */
    private void invalidateView() {
        if(Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }



    /**
     * 重写父类方法，保存状态，保存mAlpha值
     * @return
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS,super.onSaveInstanceState());
        bundle.putFloat(STATUS_ALPHA, mAlpha);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            mAlpha = bundle.getFloat(STATUS_ALPHA);
            return;
        }

        super.onRestoreInstanceState(state);
    }
}
