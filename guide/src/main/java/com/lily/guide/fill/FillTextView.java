package com.lily.guide.fill;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import com.lily.guide.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 参考：
 * https://github.com/ChenLittlePing/FillTextView
 */
public class FillTextView extends View implements ExInputConnection.InputListener, View.OnKeyListener {

    //编辑字段标记
    private String EDIT_TAG = "<fill>";
    //可编辑空白
    private String BLANKS = "        ";
    //可编辑开始符
    private String mEditStartTag = "【";
    //可编辑结束符
    private String mEditEndTag = "】";
    //文本
    private StringBuffer mText;
    //存放文字段的列表，根据<fill>分割为多个字段
    private List<AText> mTextList;
    //正在输入的字段
    private AText mEditingText = null;
    //当前正在编辑的文本行数
    private int mEditTextRow = 1;
    //光标，[0]：x坐标,[1]：文字的基准线
    private float[] mCursor = {-1f, -1f};
    //光标所在文字索引
    private int mCursorIndex = 0;
    //光标闪烁标志
    private boolean mHideCursor = true;
    //控件宽度
    private int mWidth = 0;
    //文字画笔
    private Paint mNormalPaint;
    //普通文字颜色
    private int mNormalColor = Color.BLACK;
    //文字画笔
    private Paint mFillPaint;
    //填写文字颜色
    private int mFillColor = Color.BLACK;
    //光标画笔
    private Paint mCursorPain;
    //光标宽度1dp
    private float mCursorWidth = 1f;
    //一个汉字的宽度
    private float mOneWordWidth = 0f;
    //一行最大的文字数
    private int mMaxSizeOneLine = 0;
    //字体大小
    private float mTextSize;
    //当前绘制到第几行
    private int mCurDrawRow = 1;
    //获取文字的起始位置
    private int mStartIndex = 0;
    //获取文字的结束位置
    private int mEndIndex = 0;
    //存放每行的文字，用于计算文字长度
    private StringBuffer mOneRowText;
    //一行字包含的字段：普通字段，可编辑字段
    private List<AText> mOneRowList;
    //默认行距2dp，也是最小行距（用户设置的行距在此基础上叠加，即：2 + cst）
    private float mRowSpace;
    //是否显示下划线
    private boolean mUnderlineVisible = false;
    //下划线画笔
    private Paint mUnderlinePaint;

    private Context mContext;
    private final XHandler mHandler = new XHandler(FillTextView.this);

    public FillTextView(Context context) {
        super(context);
        init(context, null);
    }

    public FillTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FillTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        initPartVariablesAtFirst(context);
        initAttribute(context, attrs);

        initCursorPaint(context);
        initTextPaint();
        initFillPaint();
        initUnderlinePaint(context);
        // 拆分文字
        if (mText.length() > 0) {
            splitTexts();
        }
        initHandler();
        setOnKeyListener(this);
    }

    private void initPartVariablesAtFirst(Context context) {
        mTextSize = sp2px(context, 16f);
        mRowSpace = dp2px(context, 2f);
        mText = new StringBuffer();
        mTextList = new ArrayList<>();
        mOneRowText = new StringBuffer();
        mOneRowList = new ArrayList<>();
    }

    private void initAttribute(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.filled_text);
        if (ta == null) {
            return;
        }
        if (ta.hasValue(R.styleable.filled_text_fillTextSize)) {
            mTextSize = ta.getDimension(R.styleable.filled_text_fillTextSize, mTextSize);
        }
        if (ta.hasValue(R.styleable.filled_text_filledText)) {
            mText = mText.append(ta.getText(R.styleable.filled_text_filledText));
        }
        if (ta.hasValue(R.styleable.filled_text_normalColor)) {
            mNormalColor = ta.getColor(R.styleable.filled_text_normalColor, Color.BLACK);
        }
        if (ta.hasValue(R.styleable.filled_text_fillColor)) {
            mFillColor = ta.getColor(R.styleable.filled_text_fillColor, Color.BLACK);
        }
        if (ta.hasValue(R.styleable.filled_text_rowSpace)) {
            mRowSpace += ta.getDimension(R.styleable.filled_text_rowSpace, 0f);
        }

        ta.recycle();
    }

    /**
     * 初始化光标画笔
     */
    private void initCursorPaint(Context context) {
        mCursorPain = new Paint();
        mCursorWidth = dp2px(context, mCursorWidth);
        mCursorPain.setStrokeWidth(mCursorWidth);
        mCursorPain.setColor(mFillColor);
        mCursorPain.setAntiAlias(true);
    }

    /**
     * 初始化文字画笔
     */
    private void initTextPaint() {
        mNormalPaint = new Paint();
        mNormalPaint.setColor(mNormalColor);
        mNormalPaint.setTextSize(mTextSize);
        mNormalPaint.setAntiAlias(true);

        mOneWordWidth = measureTextLength("填");
    }

    private void initFillPaint() {
        mFillPaint = new Paint();
        mFillPaint.setColor(mFillColor);
        mFillPaint.setTextSize(mTextSize);
        mFillPaint.setAntiAlias(true);
    }

    private void initUnderlinePaint(Context context) {
        mUnderlinePaint = new Paint();
        mUnderlinePaint.setStrokeWidth(dp2px(context, 1f));
        mUnderlinePaint.setColor(Color.GRAY);
        mUnderlinePaint.setAntiAlias(true);
    }

    /**
     * 拆分文字，普通文字和可编辑文字
     * 1. 文字开始或结束是 EDIT_TAG
     * 2. 文字中间有 EDIT_TAG
     */
    private void splitTexts() {
        mTextList.clear();
        String content = mText.toString().trim();
        // 文字是以 EDIT_TAG 开始
        if (content.startsWith(EDIT_TAG)) {
            addStartOrEndFillInBlank();
        }
//        android.util.Log.w("@@@", "content = " + content);
        String[] texts = content.split(EDIT_TAG);
        List<String> textList = new ArrayList<>();
        // 去除 texts 中的空字符串
        for (int i = 0; i < texts.length; i++) {
            String text = texts[i];
            if (TextUtils.isEmpty(text)) {
                continue;
            }
            textList.add(text);
        }
        int size = textList.size();
        for (int i = 0; i < size; i++) {
            String text = textList.get(i);
//            android.util.Log.w("@@@", "text = " + text);
            if (i > 0 && !TextUtils.isEmpty(mEditEndTag)) {
                text = mEditEndTag + text;
            }
            if (!TextUtils.isEmpty(mEditStartTag) && i != size - 1) {
                text += mEditStartTag;
            }
            mTextList.add(new AText(text));
            if (i != size - 1) {
                addBlanks();
            }
        }
        // 文字是以 EDIT_TAG 结尾
        if (content.endsWith(EDIT_TAG)) {
            addStartOrEndFillInBlank();
        }
    }

    /**
     * 添加可编辑开始符
     */
    private void addEditStartTag() {
        if (!TextUtils.isEmpty(mEditStartTag)) {
            mTextList.add(new AText(mEditStartTag));
        }
    }

    /**
     * 添加可编辑结束符
     */
    private void addEditEndTag() {
        if (!TextUtils.isEmpty(mEditEndTag)) {
            mTextList.add(new AText(mEditEndTag));
        }
    }

    /**
     * 添加可编辑空白
     */
    private void addBlanks() {
        mTextList.add(new AText(BLANKS, true));
    }

    /**
     * 添加开始或结尾的填空
     */
    private void addStartOrEndFillInBlank() {
        addEditStartTag();
        addBlanks();
        addEditEndTag();
    }

    /**
     * 光标闪烁定时
     */
    private void initHandler() {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(1, 500);
        }
    }

    /**
     * Handler
     */
    private static final class XHandler extends Handler {
        private final WeakReference<FillTextView> weakReference;

        public XHandler(FillTextView fillTextView) {
            weakReference = new WeakReference<>(fillTextView);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    FillTextView fill = weakReference.get();
                    if (fill != null) {
                        fill.handleMessage();
                    }
                    break;
            }
        }
    }

    /**
     * 光标闪烁定时的Message
     */
    private void handleMessage() {
        mHideCursor = !mHideCursor;
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(1, 500);
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = widthSize;
        int height = heightSize;

        StringBuffer realText = new StringBuffer();
        for (AText aText : mTextList) {
            realText.append(aText.text);
        }

        switch (widthMode) {
            case MeasureSpec.EXACTLY: {
                width = widthSize;
                //用户指定宽高
                mWidth = width;
                mMaxSizeOneLine = (int) (width / mOneWordWidth);
                break;
            }
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST: {
                //绘制宽高为文字最大长度，如果长度超过，则使用父布局可用的最大长度
                if (mText.length() == 0) {
                    width = 0;
                } else {
                    width = (int) Math.min(widthSize, measureTextLength(realText.toString()));
                }

                //设置最大宽高
                mWidth = widthSize;
                mMaxSizeOneLine = (int) (widthSize / mOneWordWidth);
            }
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY: {
                height = heightSize;
                break;
            }
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST: {
                if (realText.length() == 0) {
                    height = 0;
                } else {
                    //其中mRowSpace + mNormalPaint.fontMetrics.descent是最后一行距离底部的间距
                    height = (int) (getRowHeight() * (mCurDrawRow - 1) + mRowSpace + mNormalPaint.getFontMetrics().descent);
                }
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    public void draw(Canvas canvas) {
        clear();
        canvas.save();
        mStartIndex = 0;
        mEndIndex = mMaxSizeOneLine;

        for (int i = 0; i < mTextList.size(); i++) {
            AText aText = mTextList.get(i);
            String text = aText.text;

            while (true) {
                if (mEndIndex > text.length()) {
                    mEndIndex = text.length();
                }
                //记录编辑初始位置
                addEditStartPos(aText);

                CharSequence cs = text.subSequence(mStartIndex, mEndIndex);
                mOneRowList.add(new AText(cs.toString(), aText.isFill));
                mOneRowText.append(cs);

                int textWidth = (int) measureTextLength(mOneRowText.toString());
                if (textWidth <= mWidth) {
                    int left = mWidth - textWidth;
                    int textCount = (int) (left / mOneWordWidth);
                    if (mEndIndex < text.length()) {
                        mStartIndex = mEndIndex;
                        mEndIndex += textCount;
                        if (mStartIndex == mEndIndex) {
                            int one = (int) measureTextLength(text.substring(mEndIndex, mEndIndex + 1));
                            if (one + textWidth < mWidth) {
                                //可以放多一个字
                                mEndIndex++;
                            } else {
                                //绘制文字
                                addEditEndPos(aText);
                                drawOneRow(canvas);
                                //编辑的段落可能进入下一行
                                addEditStartPosFromZero(aText, mStartIndex);
                            }
                        }
                    } else {
                        //进入下一段文字

                        //记录编辑结束位置
                        addEditEndPos(aText);
                        if (i < mTextList.size() - 1) {
                            mStartIndex = 0;
                            mEndIndex = textCount;
                            if (mStartIndex == mEndIndex) {
                                int one = (int) measureTextLength(mTextList.get(i + 1).text.substring(0, 1));
                                if (one + textWidth < mWidth) {
                                    //可以放多一个字
                                    //只读下一段文字第一个字
                                    mEndIndex = 1;
                                } else {
                                    //绘制文字
                                    drawOneRow(canvas);
                                }
                            }
                        } else {
                            //绘制文字
                            drawOneRow(canvas);
                        }
                        break;
                    }
                } else {
                    //绘制文字
                    drawOneRow(canvas);
                }
            }
        }
        if (isFocused()) {
            drawCursor(canvas);
        }
        super.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            if (mHandler != null) {
                mHandler.removeMessages(1);
                mHandler.sendEmptyMessageDelayed(1, 500);
            }
        } else {
            //失去焦点时，停止刷新光标
            if (mHandler != null) {
                mHandler.removeMessages(1);
            }
        }
    }

    /**
     * 清除过期状态
     */
    private void clear() {
        mCurDrawRow = 1;
        mStartIndex = 0;
        mEndIndex = 0;
        mOneRowText.delete(0, mOneRowText.length());
        mOneRowList.clear();
        if (mEditingText != null && mEditingText.postInfoes != null) {
            mEditingText.postInfoes.clear();
        }
    }

    /**
     * 绘制一行文字
     * <code>
     *     float blanksWidth = measureTextLength(BLANKS);
     *
     *     if (x < lineStartX + blanksWidth) {
     *         lineStopX = lineStartX + blanksWidth;
     *         android.util.Log.w("@@@", "lineStartX = " + lineStartX);
     *         android.util.Log.w("@@@", "x = " + x);
     *     }
     * </code>
     */
    private void drawOneRow(Canvas canvas) {
        //drawText中的y坐标为文字基线
        //文字基准线问题
        Paint.FontMetrics fm = mNormalPaint.getFontMetrics();

        float x = 0f;
        for (AText aText : mOneRowList) {
            canvas.drawText(aText.text, x, getRowHeight() * mCurDrawRow, aText.isFill ? mFillPaint : mNormalPaint);

            float lineStartX = x;
            x += measureTextLength(aText.text);

            // 绘制下划线
            if (aText.isFill && mUnderlineVisible) {
                float lineStopX = x;
                canvas.drawLine(lineStartX, getRowHeight() * mCurDrawRow + fm.descent, lineStopX, (getRowHeight() * mCurDrawRow + fm.descent), mUnderlinePaint);
            }
        }

        mCurDrawRow++;
        mEndIndex += mMaxSizeOneLine;
        mOneRowText.delete(0, mOneRowText.length());
        mOneRowList.clear();
    }

    /**
     * 绘制光标
     */
    private void drawCursor(Canvas canvas) {
        if (mHideCursor) {
            mCursorPain.setAlpha(0);
        } else {
            mCursorPain.setAlpha(255);
        }

        if (mCursor[0] >= 0 && mCursor[1] >= 0) {
            //光标可能需要换到上一行
            if (mEditingText != null) {
                if (mEditingText.text == BLANKS && (mCursor[0] == 0f || (mCursor[0] == mCursorWidth && mEditingText.postInfoes.size() > 1))) {
                    if (mEditingText.postInfoes.size() > 1) {
                        //得到可编辑字段最上面一行的起始位置
                        mEditTextRow = mEditingText.getStartPos();
                        EditPostInfo posInfo = mEditingText.postInfoes.get(mEditTextRow);
                        mCursor[0] = (float) posInfo.rect.left;
                        mCursor[1] = (float) posInfo.rect.bottom;
                        //矫正光标X轴坐标
                        if (mCursor[0] <= 0) {
                            mCursor[0] = mCursorWidth;
                        }
                    }
                }
            }

            //文字基准线问题
            Paint.FontMetrics fm = mNormalPaint.getFontMetrics();
            canvas.drawLine(mCursor[0], mCursor[1] + fm.ascent, mCursor[0], (mCursor[1] + fm.descent), mCursorPain);
        }
    }

    /**
     * 添加编辑字段起始位置
     */
    private void addEditStartPos(AText aText) {
        if (aText.isFill && mStartIndex == 0) {
            aText.postInfoes.clear();
            int width = (int) measureTextLength(mOneRowText.toString());
            //加上行距
            Rect rect = new Rect(width, (int) (getRowHeight() * (mCurDrawRow - 1) + mRowSpace), 0, 0);
            EditPostInfo info = new EditPostInfo(mStartIndex, rect);
            aText.postInfoes.put(mCurDrawRow, info);
        }
    }

    /**
     * 添加编辑字段起始位置（换行的情况）
     */
    private void addEditStartPosFromZero(AText aText, int index) {
        if (aText.isFill) {
            // 加上行距
            Rect rect = new Rect(0, (int) (getRowHeight() * (mCurDrawRow - 1) + mRowSpace), 0, 0);
            EditPostInfo info = new EditPostInfo(index, rect);
            aText.postInfoes.put(mCurDrawRow, info);
        }
    }

    /**
     * 添加编辑字段结束位置
     */
    private void addEditEndPos(AText aText) {
        if (aText.isFill) {
            int width = (int) measureTextLength(mOneRowText.toString());
            if (aText.postInfoes.get(mCurDrawRow) != null) {
                if (aText.postInfoes.get(mCurDrawRow).rect != null) {
                    aText.postInfoes.get(mCurDrawRow).rect.right = width;
                    aText.postInfoes.get(mCurDrawRow).rect.bottom = (int) (getRowHeight() * mCurDrawRow);
                }
            }
        }
    }

    private int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    private int sp2px(Context context, float sp) {
        float density = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * density + 0.5);
    }

    /**
     * 计算文字长度：px
     *
     * @param text
     * @return
     */
    private float measureTextLength(String text) {
        return mNormalPaint.measureText(text);
    }

    /**
     * 获取一行高度
     */
    private float getRowHeight() {
        return mTextSize + mRowSpace;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (touchCollision(event)) {
                    setFocusableInTouchMode(true);
                    setFocusable(true);
                    requestFocus();
                    try {
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(this, InputMethodManager.RESULT_SHOWN);
                        imm.restartInput(this);
                    } catch (Exception e) {

                    }
                    return true;
                }
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 检测点击碰撞
     */
    private boolean touchCollision(MotionEvent event) {
        for (AText aText : mTextList) {
            if (aText.isFill) {
                for (Map.Entry<Integer, EditPostInfo> entry : aText.postInfoes.entrySet()) {
                    int row = entry.getKey();
                    EditPostInfo posInfo = entry.getValue();

                    if (event.getX() > posInfo.rect.left && event.getX() < posInfo.rect.right &&
                            event.getY() > posInfo.rect.top && event.getY() < posInfo.rect.bottom) {

                        mEditTextRow = row;
                        if (aText.text == BLANKS) {
                            int firstRow = aText.getStartPos();
                            if (firstRow >= 0) {
                                //可能存在换行
                                mEditTextRow = firstRow;
                            }
                        }
                        mEditingText = aText;
                        calculateCursorPos(event, aText.postInfoes.get(mEditTextRow), aText.text);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 计算光标位置
     */
    private void calculateCursorPos(MotionEvent event, EditPostInfo posInfo, String text) {
        float eX = event.getX();
        float innerWidth = eX - posInfo.rect.left;
        int nWord = (int) (innerWidth / mOneWordWidth);
        int wordsWidth = 0;

        if (nWord <= 0) {
            nWord = 1;
        }
        if (text == BLANKS) {
            mCursor[0] = posInfo.rect.left;
            mCursor[1] = posInfo.rect.bottom;
            mCursorIndex = 0;
        } else {
            //循环计算，直到最后一个真正超过显示范围的文字（因为汉字和英文数字占位不一样，这里以汉字作为初始占位）
            do {
                wordsWidth = (int) measureTextLength(text.substring(posInfo.index, posInfo.index + nWord));
                nWord++;
            } while (wordsWidth < innerWidth && posInfo.index + nWord <= text.length());

            mCursorIndex = posInfo.index + nWord - 1;
            //计算点击位置是否超过所点击文字的一半
            float leftWidth = wordsWidth - innerWidth;
            if (leftWidth > measureTextLength(text.substring(mCursorIndex - 1, mCursorIndex)) / 2) {
                mCursorIndex--;
            }

            mCursor[0] = (float) posInfo.rect.left + measureTextLength(text.substring(posInfo.index, mCursorIndex));
            mCursor[1] = (float) posInfo.rect.bottom;
        }
        invalidate();
    }

    @Override
    public void onTextInput(CharSequence text) {
        if (mEditingText != null) {
            StringBuffer filledText = new StringBuffer(mEditingText.text.replace(BLANKS, ""));
            if (filledText.length() == 0) {
                filledText.append(text);
                mCursorIndex = text.length();
            } else {
                filledText.insert(mCursorIndex, text);
                mCursorIndex += text.length();
            }
            mEditingText.text = filledText.toString();
            if (mCursor[0] + measureTextLength(text.toString()) > mWidth) {//计算实际可以放多少字
                int restCount = (int) ((mWidth - mCursor[0]) / mOneWordWidth);
                int realWidth = (int) (mCursor[0] + measureTextLength(text.toString().substring(0, restCount)));

                //循环计算，直到最后一个真正超过显示范围的文字（因为汉字和英文数字占位不一样，这里以汉字作为初始占位）
                while (realWidth <= mWidth && restCount < text.length()) {
                    restCount++;
                    realWidth = (int) (mCursor[0] + measureTextLength(text.toString().substring(0, restCount)));
                }
                mEditTextRow += ((mCursor[0] + measureTextLength(text.toString())) / mWidth);
                if (mEditTextRow < 1) mEditTextRow = 1;
                int realCount = restCount - 1 > 0 ? restCount - 1 : 0;
                mCursor[0] = measureTextLength(text.toString().substring(realCount, text.length()));
                mCursor[1] = getRowHeight() * (mEditTextRow);
            } else {
                mCursor[0] += measureTextLength(text.toString());
            }
            invalidate();
        }
    }

    @Override
    public void onDeleteWord() {
        if (mEditingText != null) {
            StringBuffer text = new StringBuffer(mEditingText.text);
            if (text != null && text.length() != 0 && !BLANKS.equals(text.toString()) && mCursorIndex >= 1) {
                int cursorPos = (int) (mCursor[0] - measureTextLength(text.substring(mCursorIndex - 1, mCursorIndex)));
                //光标仍然在同一行
                if (cursorPos > 0 || (cursorPos == 0 && mEditingText.postInfoes.size() == 1)) {
                    mCursor[0] = cursorPos;
                } else {
                    //光标回到上一行
                    mEditTextRow--;
                    EditPostInfo posInfo = mEditingText.postInfoes.get(mEditTextRow);
                    if (posInfo != null) {
                        mCursor[0] = posInfo.rect.left + measureTextLength(text.substring(posInfo.index, mCursorIndex - 1));
                        mCursor[1] = getRowHeight() * (mEditTextRow);
                    }
                }

                mEditingText.text = text.replace(mCursorIndex - 1, mCursorIndex, "").toString();
                mCursorIndex--;

                if (mEditingText.text.length() <= 0) {
                    if (text.toString() != BLANKS) {
                        mEditingText.text = BLANKS;
                        mCursorIndex = 1;
                        int firstRow = mEditingText.getStartPos();
                        if (firstRow > 0) {
                            //可能存在换行
                            mEditTextRow = firstRow;
                        }
                        if (mEditingText.postInfoes.get(mEditTextRow) != null) {
                            mCursor[0] = mEditingText.postInfoes.get(mEditTextRow).rect.left;
                            mCursor[1] = getRowHeight() * (mEditTextRow);
                        }
                    }
                }

                invalidate();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            hideInput();
        }
    }

    /**
     * 隐藏输入法
     */
    private void hideInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = InputType.TYPE_CLASS_TEXT;
        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;
        return new ExInputConnection(this, false, this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
            onDeleteWord();
            return true;
        }
        return false;
    }

    /**
     * 设置文本
     */
    public void setText(String text) {
        mText = new StringBuffer(text);
        splitTexts();
        invalidate();
    }

    /**
     * 设置字体大小，单位sp
     */
    public void setTextSize(Context context, float sp) {
        mTextSize = sp2px(context, sp);
        initTextPaint();
        initFillPaint();
        invalidate();
    }

    /**
     * 设置行距，单位dp
     */
    public void setRowSpace(Context context, float dp) {
        mRowSpace = (float) dp2px(context, 2 + dp);
        invalidate();
    }

    /**
     * 设置可编辑标记的开始和结束符
     */
    public void setEditTag(String startTag, String endTag) {
        mEditStartTag = startTag;
        mEditEndTag = endTag;
        invalidate();
    }

    /**
     * 设置是否显示可编辑字段下划线
     */
    public void displayUnderline(boolean visible) {
        mUnderlineVisible = visible;
    }

    /**
     * 设置下划线颜色
     */
    public void setUnderlineColor(int color) {
        mUnderlinePaint.setColor(color);
        invalidate();
    }

    /**
     * 获取填写的文本内容
     */
    public List<String> getFillTexts() {
        List<String> list = new ArrayList<>();
        for (AText aText : mTextList) {
            if (aText.isFill) {
                list.add(aText.text);
            }
        }
        return list;
    }
}
