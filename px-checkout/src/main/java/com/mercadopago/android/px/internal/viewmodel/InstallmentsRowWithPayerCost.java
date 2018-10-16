package com.mercadopago.android.px.internal.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.util.textformatter.InstallmentFormatter;
import com.mercadopago.android.px.internal.view.InstallmentsDescriptorView;
import com.mercadopago.android.px.model.CardPaymentMetadata;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import java.math.BigDecimal;

/**
 * Model used to instanciate InstallmentsDescriptorView
 * For payment methods with payer costs: credit_card only
 */
public final class InstallmentsRowWithPayerCost extends InstallmentsDescriptorView.Model {

    private InstallmentsRowWithPayerCost(@NonNull final String currencyId,
        @NonNull final PayerCost payerCost,
        @NonNull final BigDecimal totalAmount) {
        super(currencyId, payerCost, totalAmount);
    }

    public static InstallmentsDescriptorView.Model createFrom(@NonNull final PaymentSettingRepository configuration,
        @NonNull final CardPaymentMetadata card) {
        final CheckoutPreference checkoutPreference = configuration.getCheckoutPreference();
        final PayerCost payerCost = card.getAutoSelectedInstallment();
        final String currencyId = checkoutPreference.getSite().getCurrencyId();
        final BigDecimal totalAmount = checkoutPreference.getTotalAmount();
        return new InstallmentsRowWithPayerCost(currencyId, payerCost, totalAmount);
    }

    @Override
    public void updateInstallmentsRowSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context, @NonNull final CharSequence amount) {
        final InstallmentFormatter installmentFormatter = new InstallmentFormatter(spannableStringBuilder, context)
            .withInstallment(getPayerCost().getInstallments())
            .withTextColor(ContextCompat.getColor(context, R.color.ui_meli_black));
        installmentFormatter.build(amount);
    }
}
