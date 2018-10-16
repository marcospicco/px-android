package com.mercadopago.android.px.internal.util.textformatter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.ViewUtils;

public class InterestFormatter {

    private int textColor;
    private final Context context;
    private final SpannableStringBuilder spannableStringBuilder;

    public InterestFormatter(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context) {
        this.spannableStringBuilder = spannableStringBuilder;
        this.context = context;
    }

    public InterestFormatter withTextColor(final int color) {
        textColor = color;
        return this;
    }

    public Spannable apply() {

        final int initialIndex = spannableStringBuilder.length();
        final String separator = " ";
        final String description = context.getString(R.string.px_zero_rate);
        spannableStringBuilder.append(separator).append(description);
        final int endIndex = separator.length() + description.length();

        updateTextColor(initialIndex, endIndex);
        return spannableStringBuilder;
    }

    private void updateTextColor(final int indexStart, final int indexEnd) {
        if (textColor != 0) {
            ViewUtils.setColorInSpannable(textColor, indexStart, indexStart + indexEnd, spannableStringBuilder);
        }
    }
}
