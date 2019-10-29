package online.postboard.android.util.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.widget.TextView;

import online.postboard.android.R;
import online.postboard.android.util.Constants;

/**
 * Created by Android SD-1 on 03-05-2017.
 */

public class ExpandableTextView extends AppCompatTextView {
    private static final int DEFAULT_TRIM_LENGTH = Constants.INSTANCE.getMAX_LENGTH_CONTENT_TEXT();
    private static final String COLLAPSED_ELLIPSIS = "…show more";
    private static final String EXPANDED_ELLIPSIS = " (show less) ";

    private CharSequence originalText;
    private CharSequence trimmedText;
    private TextView.BufferType bufferType;
    private boolean trim = true;
    private int trimLength;

    /* Listener for callback */
    private OnExpandStateChangeListener mListener;

    public interface OnExpandStateChangeListener {
        /**
         * Called when the expand/collapse animation has been finished
         *
         * @param textView   - TextView being expanded/collapsed
         * @param isExpanded - true if the TextView has been expanded
         */
        void onExpandStateChanged(TextView textView, boolean isExpanded);
    }

    public void setOnExpandStateChangeListener(@Nullable OnExpandStateChangeListener listener) {
        mListener = listener;
    }

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        this.trimLength = typedArray.getInt(R.styleable.ExpandableTextView_trimLength, DEFAULT_TRIM_LENGTH);
        typedArray.recycle();

        setOnClickListener(v -> {
            trim = !trim;
            setText();
            requestFocusFromTouch();

            // notify the listener
            if (mListener != null && !originalText.toString().equalsIgnoreCase(trimmedText.toString())) {
                mListener.onExpandStateChanged(ExpandableTextView.this, !trim);
            }
        });
    }

    private void setText() {
        super.setText(getDisplayableText(), bufferType);
    }

    private CharSequence getDisplayableText() {
        return trim ? trimmedText : getExpandedText(getOriginalText());
    }

    @Override
    public void setText(CharSequence text, TextView.BufferType type) {
        originalText = text;
        trimmedText = getTrimmedText(text);
        bufferType = type;
        setText();
    }

    private CharSequence getTrimmedText(CharSequence text) {
        if (originalText != null && originalText.length() > trimLength) {
            return new SpannableStringBuilder(originalText, 0, trimLength + 1).append(" ").append(Html.fromHtml("<font color=\"#b2b2b2\">" + COLLAPSED_ELLIPSIS + "</font>"));
        } else {
            return originalText;
        }
    }

    private CharSequence getExpandedText(CharSequence text) {
        if (text.length() > trimLength) {
            return new SpannableStringBuilder(originalText).append(" ").append(Html.fromHtml("<br/><font color=\"#b2b2b2\">" + EXPANDED_ELLIPSIS + "</font>"));
        } else {
            return text;
        }
    }

    public CharSequence getOriginalText() {
        return originalText;
    }

    public void setTrimLength(int trimLength) {
        this.trimLength = trimLength;
        trimmedText = getTrimmedText(originalText);
        setText();
    }

    public int getTrimLength() {
        return trimLength;
    }
}