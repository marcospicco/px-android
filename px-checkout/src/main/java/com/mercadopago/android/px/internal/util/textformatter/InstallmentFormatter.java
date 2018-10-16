package com.mercadopago.android.px.internal.util.textformatter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import com.mercadolibre.android.ui.font.Font;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.ViewUtils;

public class InstallmentFormatter extends ChainFormatter {

    private int installment;
    private int textColor;
    private final Context context;
    private final SpannableStringBuilder spannableStringBuilder;
    private Font fontStyle;
    private TextView textView;

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
        textColor = color;
        return this;
    }

    public InstallmentFormatter withTextStyle(@NonNull final Font fontStyle, @NonNull final TextView textView) {
        this.fontStyle = fontStyle;
        this.textView = textView;
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

            updateTextColor(0, length);
        } else {
            final int holder = R.string.px_string_holder;
            final CharSequence charSequence = context.getResources().getString(holder, amount);
            spannableStringBuilder.append(charSequence);

            updateTextColor(0, charSequence.length());
        }

        return spannableStringBuilder;
    }

    private void updateTextColor(final int indexStart, final int indexEnd) {
        if (textColor != 0) {
            ViewUtils.setColorInSpannable(textColor, indexStart, indexStart + indexEnd, spannableStringBuilder);
        }
    }

    private void updateTextStyle(final int indexStart, final int indexEnd) {

        //TODO arreglar el style, en esta parte tiene que ser semi bold
//        final StyleSpan installmentsDescriptionStyle =
//            new StyleSpan(TypefaceHelper.setTypeface(textView, Font.SEMI_BOLD));
//
//        if (fontStyle != null && textView != null) {
//            spannableStringBuilder.setSpan(TypefaceHelper.setTypeface(textView, fontStyle), indexStart, indexEnd,
//                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        }
    }
}
