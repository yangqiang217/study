package com.yq.tjimagegallery;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by rzc on 16/4/21.
 */
public class NotEditTextView extends TextView {
    public NotEditTextView (Context context) {
        super(context);
    }

    public NotEditTextView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotEditTextView (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (isSelected() && isEnabled()) {
            setText(R.string.cancel_not_edit);
        } else {
            setText(R.string.not_edit);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (isSelected() && isEnabled()) {
            setText(R.string.cancel_not_edit);
        } else {
            setText(R.string.not_edit);
        }
    }
}
