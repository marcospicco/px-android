package com.mercadopago.android.px.internal.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;
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
        if (model.isEmpty()) {
            spannableStringBuilder.append(" ");
        } else {
            updateInstallmentsDescription(model, spannableStringBuilder);
            updateTotalAmountDescription(model, spannableStringBuilder);
            updateInterestDescription(model, spannableStringBuilder);
            updateCFT(model, spannableStringBuilder);
        }
        setText(spannableStringBuilder);
    }

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
        model.updateInstallmentsDescriptionSpannable(spannableStringBuilder, getContext(), amount, this);
    }

    private void updateInterestDescription(@NonNull final Model model,
        @NonNull final SpannableStringBuilder spannableStringBuilder) {

        model.updateInterestDescriptionSpannable(spannableStringBuilder, getContext());

    }

    private void updateTotalAmountDescription(@NonNull final Model model,
        @NonNull final SpannableStringBuilder spannableStringBuilder) {

        model.updateTotalAmountDescriptionSpannable(spannableStringBuilder, getContext());
    }

    private void updateCFT(@NonNull final Model model,
        @NonNull final SpannableStringBuilder spannableStringBuilder) {

        model.updateCFTSpannable(spannableStringBuilder, getContext());
    }

    public abstract static class Model {
        private String currencyId;
        private PayerCost payerCost;
        private BigDecimal totalAmount;

        protected Model(@NonNull final String currencyId, @Nullable final PayerCost payerCost,
            @NonNull final BigDecimal totalAmount) {
            this.currencyId = currencyId;
            this.payerCost = payerCost;
            this.totalAmount = totalAmount;
        }

        protected Model() {

        }

        public boolean isEmpty() {
            return currencyId == null && payerCost == null && totalAmount == null;
        }

        public String getCurrencyId() {
            return currencyId;
        }

        @Nullable
        public PayerCost getPayerCost() {
            return payerCost;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        protected boolean hasMultipleInstallments() {
            return payerCost != null && payerCost.getInstallments() > 1;
        }

        public abstract void updateInstallmentsDescriptionSpannable(
            @NonNull final SpannableStringBuilder spannableStringBuilder,
            @NonNull final Context context, @NonNull final CharSequence amount, @NonNull final TextView textView);

        public abstract void updateInterestDescriptionSpannable(
            @NonNull final SpannableStringBuilder spannableStringBuilder,
            @NonNull final Context context);

        public abstract void updateTotalAmountDescriptionSpannable(
            @NonNull final SpannableStringBuilder spannableStringBuilder,
            @NonNull final Context context);

        public abstract void updateCFTSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
            @NonNull final Context context);
    }
}
