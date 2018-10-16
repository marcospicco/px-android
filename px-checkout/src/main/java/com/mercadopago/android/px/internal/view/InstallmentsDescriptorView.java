package com.mercadopago.android.px.internal.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.textformatter.AmountFormatter;
import com.mercadopago.android.px.internal.util.textformatter.CurrencyFormatter;
import com.mercadopago.android.px.internal.util.textformatter.TextFormatter;
import com.mercadopago.android.px.model.PayerCost;
import java.math.BigDecimal;

public class InstallmentsDescriptorView extends MPTextView {

    public InstallmentsDescriptorView(final Context context) {
        this(context, null);
    }

    public InstallmentsDescriptorView(final Context context,
        @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InstallmentsDescriptorView(final Context context, @Nullable final AttributeSet attrs,
        final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    public void update(@NonNull final Model model) {
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        updateInstallmentsDescription(model, spannableStringBuilder);
        updateTotalAmountDescription(model, spannableStringBuilder);
        updateInterestDescription(model, spannableStringBuilder);
        updateCFT(model, spannableStringBuilder);
        setText(spannableStringBuilder);
    }
    //TODO pasar todo lo del spannable a una factory como la de CurrencyFormatter

    private void updateInstallmentsDescription(@NonNull final Model model,
        @NonNull final SpannableStringBuilder spannableStringBuilder) {
        final CurrencyFormatter currencyFormatter = TextFormatter.withCurrencyId(model.getCurrencyId());

        final AmountFormatter amountFormatter = model.hasMultipleInstallments() ?
            currencyFormatter.amount(model.getPayerCost().getInstallmentAmount()) :
            currencyFormatter.amount(model.getTotalAmount());
        final CharSequence amount = amountFormatter
            .normalDecimals()
            .into(this)
            .toSpannable();
        model.updateInstallmentsRowSpannable(spannableStringBuilder, getContext(), amount);
    }

    private void updateInterestDescription(@NonNull final Model model,
        @NonNull final SpannableStringBuilder spannableStringBuilder) {

        if (model.hasMultipleInstallments() &&
            BigDecimal.ZERO.compareTo(model.getPayerCost().getInstallmentRate()) == 0) {
            final int initialIndex = spannableStringBuilder.length();
            final String separator = " ";
            final String description = getContext().getString(R.string.px_zero_rate);
            spannableStringBuilder.append(separator);
            spannableStringBuilder.append(description);
            final int endIndex = separator.length() + description.length();
            final ForegroundColorSpan installmentsDescriptionColor =
                new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.px_discount_description));
            spannableStringBuilder
                .setSpan(installmentsDescriptionColor, initialIndex, initialIndex + endIndex,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        //TODO preguntar: si hay una única cuota seleccionada, igual se muestra el "sin interes"?

    }

    private void updateTotalAmountDescription(@NonNull final Model model,
        @NonNull final SpannableStringBuilder spannableStringBuilder) {

        if (BigDecimal.ZERO.compareTo(model.getPayerCost().getInstallmentRate()) < 0) {
            //Si hay intereses
            final CharSequence totalAmount = TextFormatter.withCurrencyId(model.getCurrencyId())
                .amount(model.getPayerCost().getTotalAmount())
                .normalDecimals()
                .into(this)
                //TODO no está funcionando el holder, no se muestran los paréntesis
                .holder(R.string.px_total_amount_holder)
                .toSpannable();
            final int initialIndex = spannableStringBuilder.length();
            final String separator = " ";
            spannableStringBuilder.append(separator);
            spannableStringBuilder.append(totalAmount);
            final int endIndex = separator.length() + totalAmount.length();
            final ForegroundColorSpan installmentsDescriptionColor =
                new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.ui_meli_grey));
            spannableStringBuilder
                .setSpan(installmentsDescriptionColor, initialIndex, initialIndex + endIndex,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
    }

    private void updateCFT(@NonNull final Model model,
        @NonNull final SpannableStringBuilder spannableStringBuilder) {
        final int initialIndex = spannableStringBuilder.length();
        final String cftDescription =
            getContext().getString(R.string.px_installments_cft, model.getPayerCost().getCFTPercent());
        final String separator = " ";
        spannableStringBuilder.append(separator);
        spannableStringBuilder.append(cftDescription);
        final int endIndex = separator.length() + cftDescription.length();
        final ForegroundColorSpan installmentsDescriptionColor =
            new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.ui_meli_grey));
        spannableStringBuilder
            .setSpan(installmentsDescriptionColor, initialIndex, initialIndex + endIndex,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    }

    //TODO falta pasar el payment type, o hacer un modelo abstracto, al cual le podamos pedir los spannables
    //TODO que haya dos implementaciones del modelo, uno para debito y otro para credito, entonces devuelven los
    //TODO textos según la logica de cada uno.
    public abstract static class Model {
        private final String currencyId;
        private final PayerCost payerCost;
        private final BigDecimal totalAmount;

        protected Model(@NonNull final String currencyId, @NonNull final PayerCost payerCost,
            @NonNull final BigDecimal totalAmount) {
            this.currencyId = currencyId;
            this.payerCost = payerCost;
            this.totalAmount = totalAmount;
        }

        public String getCurrencyId() {
            return currencyId;
        }

        public PayerCost getPayerCost() {
            payerCost.setInstallmentRate(new BigDecimal(10));
            payerCost.setTotalAmount(new BigDecimal(400.90));
            payerCost.setInstallments(3);
            return payerCost;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        boolean hasMultipleInstallments() {
            return payerCost.getInstallments() > 1;
        }

        public abstract void updateInstallmentsRowSpannable(
            @NonNull final SpannableStringBuilder spannableStringBuilder,
            @NonNull final Context context, @NonNull final CharSequence amount);
    }
}
