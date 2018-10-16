package com.mercadopago.android.px.internal.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.widget.TextView;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.util.textformatter.CFTFormatter;
import com.mercadopago.android.px.internal.util.textformatter.InstallmentFormatter;
import com.mercadopago.android.px.internal.util.textformatter.InterestFormatter;
import com.mercadopago.android.px.internal.util.textformatter.PayerCostFormatter;
import com.mercadopago.android.px.internal.view.InstallmentsDescriptorView;
import com.mercadopago.android.px.model.CardPaymentMetadata;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import java.math.BigDecimal;

/**
 * Model used to instanciate InstallmentsDescriptorView
 * For payment methods with payer costs: credit_card only
 */
public final class InstallmentsDescriptorWithPayerCost extends InstallmentsDescriptorView.Model {

    private InstallmentsDescriptorWithPayerCost(@NonNull final String currencyId,
        @Nullable final PayerCost payerCost,
        @NonNull final BigDecimal totalAmount) {
        super(currencyId, payerCost, totalAmount);
    }

    public static InstallmentsDescriptorView.Model createFrom(@NonNull final PaymentSettingRepository configuration,
        @NonNull final CardPaymentMetadata card) {
        final CheckoutPreference checkoutPreference = configuration.getCheckoutPreference();
        final PayerCost payerCost = card.getAutoSelectedInstallment();
        final String currencyId = checkoutPreference.getSite().getCurrencyId();
        final BigDecimal totalAmount = checkoutPreference.getTotalAmount();
        return new InstallmentsDescriptorWithPayerCost(currencyId, payerCost, totalAmount);
    }

    @Override
    public void updateInstallmentsDescriptionSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context, @NonNull final CharSequence amount, @NonNull final TextView textView) {
        final InstallmentFormatter installmentFormatter = new InstallmentFormatter(spannableStringBuilder, context)
            .withInstallment(getPayerCost().getInstallments())
            .withTextColor(ContextCompat.getColor(context, R.color.ui_meli_black));
        installmentFormatter.build(amount);
    }

    /**
     * Updates total amount the user will pay with credit card, only if there are interests involved.
     */
    @Override
    public void updateTotalAmountDescriptionSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context) {
        if (BigDecimal.ZERO.compareTo(getPayerCost().getInstallmentRate()) < 0) {

            final PayerCostFormatter payerCostFormatter =
                new PayerCostFormatter(spannableStringBuilder, context, getPayerCost(), getCurrencyId())
                    .withTextColor(ContextCompat.getColor(context, R.color.ui_meli_grey));
            payerCostFormatter.apply();
        }
    }

    @Override
    public void updateInterestDescriptionSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context) {
        if (hasMultipleInstallments() && BigDecimal.ZERO.compareTo(getPayerCost().getInstallmentRate()) == 0) {

            final InterestFormatter interestFormatter = new InterestFormatter(spannableStringBuilder, context)
                .withTextColor(ContextCompat.getColor(context, R.color.px_discount_description));
            interestFormatter.apply();
        }
    }

    @Override
    public void updateCFTSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context) {

        final CFTFormatter cftFormatter = new CFTFormatter(spannableStringBuilder, context, getPayerCost())
            .withTextColor(ContextCompat.getColor(context, R.color.ui_meli_grey));
        cftFormatter.build();
    }
}
