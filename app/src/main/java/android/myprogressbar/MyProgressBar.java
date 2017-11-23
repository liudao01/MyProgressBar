package android.myprogressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ProgressBar;

/**
 * @author liuml.
 * @explain 自定义progressbar
 * @time 2017/11/23 17:57
 */
public class MyProgressBar extends ProgressBar {
    private float ratio = 0;
    private float offsetLeft = 0;
    private float offsetTop = 0;
    private String leftText = "信息完成度";
    private Paint mPaint;
    private int mWidth;
    private int textsize = 40;
    private String mText;
    public static int TEXT_SIZE = 0;
    private Context mContext;
    protected int mRealWidth;

    public MyProgressBar(Context context) {
        this(context, null);
        mContext = context;
        initDraw();
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
        initDraw();
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initDraw();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
                                          int heightMeasureSpec) {
        int mWidth = 200;
        int mHeight = 800;
        // TODO Auto-generated method stub
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST
                && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, mHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        mRealWidth = getMeasuredWidth() - getPaddingRight() - getPaddingLeft();
    }

    private void initDraw() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setColor(Color.GRAY);
        //设置基线上那个点究竟是left,center,还是right  这里我设置为center
        mPaint.setTextAlign(Paint.Align.LEFT);
        setTextSize(textsize);
    }

    private void textSizeAdaptive() {
        //1.获取当前设备的屏幕大小
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        //2.计算与你开发时设定的屏幕大小的纵横比(这里假设你开发时定的屏幕大小是480*800)
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        float ratioWidth = (float) screenWidth / 1080;
        float ratioHeight = (float) screenHeight / 1920;

        ratio = Math.min(ratioWidth, ratioHeight);
        if (ratioWidth != ratioHeight) {
            if (ratio == ratioWidth) {
                offsetLeft = 0;
                offsetTop = Math.round((screenHeight - 1920 * ratio) / 2);
            } else {
                offsetLeft = Math.round((screenWidth - 1080 * ratio) / 2);
                offsetTop = 0;
            }
        }
        //3.根据上一步计算出来的最小纵横比来确定字体的大小(假定在1080*1920屏幕下字体大小设定为35)
        TEXT_SIZE = Math.round(textsize * ratio);

    }


    /**
     * 设置进度字体大小
     *
     * @param textsize
     */
    public void setTextSize(int textsize) {
        this.textsize = textsize;
        textSizeAdaptive();
        mPaint.setTextSize(TEXT_SIZE);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        canvas.save();
        mText = (getProgress() * 100 / getMax()) + "%";
        Rect rect = new Rect();
        mPaint.getTextBounds(leftText, 0, leftText.length(), rect);
        int y = (getHeight() / 2) - rect.centerY();

        //在进度条上画上自定义文本
        canvas.drawText(leftText, 5, y, mPaint);

        int width = rect.width();

        //进度
        float radio = getProgress() * 1.0f / getMax();
        float progressPosX = (int) (mRealWidth * radio);

        //起始点
        int beginX = 10 + width;
        //结束点
        float textWidth = mPaint.measureText(mText);
        float endX = mRealWidth - textWidth;
        if (beginX > progressPosX- textWidth) {
            canvas.drawText(mText, beginX, y, mPaint);
        } else if (progressPosX- textWidth > endX) {
            canvas.drawText(mText, endX, y, mPaint);
        } else {
            canvas.drawText(mText, progressPosX - textWidth, y, mPaint);
        }
        canvas.restore();
    }
}
