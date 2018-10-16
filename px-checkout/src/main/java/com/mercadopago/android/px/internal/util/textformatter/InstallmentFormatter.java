package com.mercadopago.android.px.internal.util.textformatter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import com.mercadopago.android.px.R;

public class InstallmentFormatter extends ChainFormatter {

    private int installment;
    private ForegroundColorSpan textColor;
    private final Context context;
    private final SpannableStringBuilder spannableStringBuilder;

    public InstallmentFormatter(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context) {
        this.spannableStringBuilder = spannableStringBuilder;
        this.context = context;
    }

    public InstallmentFormatter withInstallment(final int installment) {
        this.installment = installment;
        return this;
    }

    public InstallmentFormatter withTextColor(final int color) {
        textColor = new ForegroundColorSpan(color);
        return this;
    }

    public Spannable build(@NonNull final CharSequence amount) {
        return apply(amount);
    }

    @Override
    protected Spannable apply(@NonNull final CharSequence amount) {
        if (installment != 0) {

            final int holder = R.string.px_amount_with_installments_holder;
            final String installmentAmount = String.valueOf(installment);
            final int holderFixedCharactersLength = 2;

            final int length = installmentAmount.length() + holderFixedCharactersLength + amount.length();

            final CharSequence charSequence = context.getResources().getString(holder, installmentAmount, amount);

            spannableStringBuilder.append(charSequence);

            if (textColor != null) {
                //TODO arreglar el style, en esta parte tiene que ser semi bold
//            final StyleSpan installmentsDescriptionStyle = new StyleSpan(android.graphics.Typeface.BOLD);
                spannableStringBuilder
                    .setSpan(textColor, 0, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }

        return spannableStringBuilder;
    }
}
