package com.yq.tjimagegallery.imagetag;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.AbsoluteLayout;
import android.widget.TextView;

import com.yq.tjimagegallery.R;

import java.io.Serializable;

@SuppressWarnings("deprecation")
public class TagView extends TextView implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int MAX_TEXT_LEN = 8;
    private static final int VERTICAL_PADDING = 4;
    private static final int ANCHOR_PADDING = 20;
    private static final int REVESE_ANCHOR_PADDING = 8;
    private float mDensity;
    // 下面这两个变量是保存相对位置的
    public float mXRate;
    public float mYRate;

    public TagView(Context context) {
        this(context, null);
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDensity = getResources().getDisplayMetrics().density;
        setSingleLine(true);
        setGravity(Gravity.CENTER_VERTICAL);
        mVerticalPadding = (int) (VERTICAL_PADDING * mDensity);
        mAnchorPadding = (int) (ANCHOR_PADDING * mDensity);
        mReveseAnchorPadding = (int) (REVESE_ANCHOR_PADDING * mDensity);
        setHint("输入商铺名称");
        setHintTextColor(0);
        setTextColor(getResources().getColorStateList(
                R.color.image_tag_text_selector));
        int maxW = (int) (getTextSize() * MAX_TEXT_LEN + mAnchorPadding + mReveseAnchorPadding);
        setMaxWidth(maxW);
        setEllipsize(TruncateAt.END);
    }

    public void textToRightOfCircle(boolean yes) {
        mTextToRightOfCircle = yes;
        if (mTextToRightOfCircle) {
            if (mShowDotBg) {
                setBackgroundResource(R.drawable.xml_tag_left_dot);
            } else {
                setBackgroundResource(R.drawable.xml_tag_left);
            }
            setPadding(mAnchorPadding, mVerticalPadding, mReveseAnchorPadding,
                    mVerticalPadding);
        } else {
            if (mShowDotBg) {
                setBackgroundResource(R.drawable.xml_tag_right_dot);
            } else {
                setBackgroundResource(R.drawable.xml_tag_right);
            }
            setPadding(mReveseAnchorPadding, mVerticalPadding, mAnchorPadding,
                    mVerticalPadding);
        }
    }

    public int getDotSize() {
        return (int) (mDensity * 8);
    }

    public int getCurrWidth() {
        int textCount = getText().length();
        if (textCount == 0) {
            textCount = getHint().length();
        }
        if (textCount > MAX_TEXT_LEN) {
            textCount = MAX_TEXT_LEN;
        }
        return (int) (getTextSize() * textCount + mAnchorPadding + mReveseAnchorPadding);
    }

    public int getCurrHeight() {
        return (int) (getTextSize() + mVerticalPadding * 2);
    }

    public void toDotState() {
        mShowDotBg = true;
        textToRightOfCircle(mTextToRightOfCircle);
        setTextColor(0);
    }

    public void forceWrapContent(String text) {
        if (mShowDotBg) {
            mShowDotBg = false;
            textToRightOfCircle(mTextToRightOfCircle);
            setTextColor(getResources().getColorStateList(
                    R.color.image_tag_text_selector));
        }

        int len = text.length();
        if (len > MAX_TEXT_LEN) {
            len = MAX_TEXT_LEN;
        }
        AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) getLayoutParams();
        int oldWidth = getCurrWidth();
        lp.width = (int) (getTextSize() * len + mAnchorPadding + mReveseAnchorPadding);
        if (!mTextToRightOfCircle) {
            lp.x += oldWidth;
            lp.x -= lp.width;
        }
        setLayoutParams(lp);
    }

    public boolean mShowDotBg;
    public boolean mTextToRightOfCircle;// 文字是否显示在图片背景里圆点的右边
    private int mVerticalPadding;// 文字距离顶部或底部的边距
    // 文字距离圆点边距，如果文字在圆点右边，代表左边距；反之代表右边距
    private int mAnchorPadding;
    private int mReveseAnchorPadding;// 与mAnchorPadding相反的另一边边距
}
