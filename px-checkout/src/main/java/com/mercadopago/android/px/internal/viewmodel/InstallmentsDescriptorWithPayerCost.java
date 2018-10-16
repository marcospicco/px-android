package com.mercadopago.android.px.internal.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.util.textformatter.InstallmentFormatter;
import com.mercadopago.android.px.internal.util.textformatter.TextFormatter;
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
        @NonNull final Context context, @NonNull final CharSequence amount) {
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

            final Spannable totalAmount = TextFormatter.withCurrencyId(getCurrencyId())
                .amount(getPayerCost().getTotalAmount())
                .normalDecimals()
                .apply(R.string.px_total_amount_holder, context);

            final int initialIndex = spannableStringBuilder.length();
            final String separator = " ";
            spannableStringBuilder.append(separator).append(totalAmount);
            final int endIndex = separator.length() + totalAmount.length();
            final ForegroundColorSpan installmentsDescriptionColor =
                new ForegroundColorSpan(ContextCompat.getColor(context, R.color.ui_meli_grey));
            spannableStringBuilder
                .setSpan(installmentsDescriptionColor, initialIndex, initialIndex + endIndex,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
    }

    @Override
    public void updateInterestDescriptionSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context) {
        if (hasMultipleInstallments() && BigDecimal.ZERO.compareTo(getPayerCost().getInstallmentRate()) == 0) {
            final int initialIndex = spannableStringBuilder.length();
            final String separator = " ";
            final String description = context.getString(R.string.px_zero_rate);
            spannableStringBuilder.append(separator).append(description);
            final int endIndex = separator.length() + description.length();

            final ForegroundColorSpan installmentsDescriptionColor =
                new ForegroundColorSpan(ContextCompat.getColor(context, R.color.px_discount_description));
            spannableStringBuilder.setSpan(installmentsDescriptionColor, initialIndex, initialIndex + endIndex,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
    }

    @Override
    public void updateCFTSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context) {
        final int initialIndex = spannableStringBuilder.length();
        final String cftDescription =
            context.getString(R.string.px_installments_cft, getPayerCost().getCFTPercent());
        final String separator = " ";
        spannableStringBuilder.append(separator).append(cftDescription);
        final int endIndex = separator.length() + cftDescription.length();
        final ForegroundColorSpan installmentsDescriptionColor =
            new ForegroundColorSpan(ContextCompat.getColor(context, R.color.ui_meli_grey));
        spannableStringBuilder
            .setSpan(installmentsDescriptionColor, initialIndex, initialIndex + endIndex,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    }
}
