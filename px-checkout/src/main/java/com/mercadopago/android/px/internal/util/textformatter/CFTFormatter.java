package com.mercadopago.android.px.internal.util.textformatter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.model.PayerCost;

public class CFTFormatter extends ChainFormatter {

    private PayerCost payerCost;
    private int textColor;
    private final Context context;
    private final SpannableStringBuilder spannableStringBuilder;

    public CFTFormatter(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context, @NonNull final PayerCost payerCost) {
        this.spannableStringBuilder = spannableStringBuilder;
        this.context = context;
        this.payerCost = payerCost;
    }

    public CFTFormatter withTextColor(final int color) {
        textColor = color;
        return this;
    }

    public Spannable build() {
        return apply(payerCost.getCFTPercent());
    }

    @Override
    protected Spannable apply(@NonNull final CharSequence amount) {

        final int initialIndex = spannableStringBuilder.length();
        final String cftDescription = context.getString(R.string.px_installments_cft, amount);
        final String separator = " ";
        spannableStringBuilder.append(separator).append(cftDescription);
        final int endIndex = separator.length() + cftDescription.length();

        updateTextColor(initialIndex, endIndex);
        return spannableStringBuilder;
    }

    private void updateTextColor(final int indexStart, final int indexEnd) {
        if (textColor != 0) {
            ViewUtils.setColorInSpannable(textColor, indexStart, indexStart + indexEnd, spannableStringBuilder);
        }
    }
}
