package com.mercadopago.android.px.internal.util.textformatter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.model.PayerCost;
import java.math.BigDecimal;

public class PayerCostFormatter {

    private PayerCost payerCost;
    private String currencyId;
    private int textColor;
    private final Context context;
    private final SpannableStringBuilder spannableStringBuilder;

    public PayerCostFormatter(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context, @NonNull final PayerCost payerCost, @NonNull final String currencyId) {
        this.spannableStringBuilder = spannableStringBuilder;
        this.context = context;
        this.payerCost = payerCost;
        this.currencyId = currencyId;
    }

    public PayerCostFormatter withTextColor(final int color) {
        textColor = color;
        return this;
    }

    public Spannable apply() {

        final Spannable totalAmount = TextFormatter.withCurrencyId(currencyId)
            .amount(payerCost.getTotalAmount())
            .normalDecimals()
            .apply(R.string.px_total_amount_holder, context);

        final int initialIndex = spannableStringBuilder.length();
        final String separator = " ";
        spannableStringBuilder.append(separator).append(totalAmount);
        final int endIndex = separator.length() + totalAmount.length();

        updateTextColor(initialIndex, endIndex);
        return spannableStringBuilder;
    }

    private void updateTextColor(final int indexStart, final int indexEnd) {
        if (textColor != 0) {
            ViewUtils.setColorInSpannable(textColor, indexStart, indexStart + indexEnd, spannableStringBuilder);
        }
    }
}
