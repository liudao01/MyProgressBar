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

public class MyProgressBar extends ProgressBar {
    private float RATIO = 0;
    private float OFFSET_LEFT = 0;
    private float OFFSET_TOP = 0;
    private String leftText = "信息完成度";
    private Paint mPaint;
    private int mWidth;
    private int textsize = 40;
    private int textpaddingleft;
    private String mText;
    public static int TEXT_SIZE = 0;
    private Context mContext;
    private int textpaddingtop;

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

    }

    private void initDraw() {
//        DisplayMetrics displayMetrics = new DisplayMetrics();


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
//        2.计算与你开发时设定的屏幕大小的纵横比(这里假设你开发时定的屏幕大小是480*800)

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        float ratioWidth = (float) screenWidth / 1080;
        float ratioHeight = (float) screenHeight / 1920;

        RATIO = Math.min(ratioWidth, ratioHeight);
        if (ratioWidth != ratioHeight) {
            if (RATIO == ratioWidth) {
                OFFSET_LEFT = 0;
                OFFSET_TOP = Math.round((screenHeight - 1920 * RATIO) / 2);
            } else {
                OFFSET_LEFT = Math.round((screenWidth - 1080 * RATIO) / 2);
                OFFSET_TOP = 0;
            }
        }
        //3.根据上一步计算出来的最小纵横比来确定字体的大小(假定在1080*1920屏幕下字体大小设定为35)
        TEXT_SIZE = Math.round(textsize * RATIO);

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

    /**
     * 调整进度字体的位置 初始位置为图片的正中央
     *
     * @param top
     * @param left
     */
    public void setTextPadding(int top, int left) {
        this.textpaddingleft = left;
        this.textpaddingtop = top;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        mText = (getProgress() * 100 / getMax()) + "%";
        Rect bounds = this.getProgressDrawable().getBounds();

        Rect rect = new Rect();
//        mPaint.getTextBounds(leftText, 0, leftText.length(), rect);
        //
        float v = (mPaint.descent() - mPaint.ascent())/2;
        //基线上方为负
        int y = (getHeight() / 2)-(int)(mPaint.ascent()/2);
        canvas.drawText(leftText, 5, y, mPaint);//在进度条上画上自定义文本

    }
}
