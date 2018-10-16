package com.mercadopago.android.px.internal.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.view.InstallmentsDescriptorView;
import com.mercadopago.android.px.model.CardPaymentMetadata;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import java.math.BigDecimal;

/**
 * Model used to instanciate InstallmentsDescriptorView
 * For payment methods without payer costs: debit_card, account_money, prepaid_card
 */
public final class InstallmentsRowNoPayerCost extends InstallmentsDescriptorView.Model {

    private InstallmentsRowNoPayerCost(@NonNull final String currencyId,
        @NonNull final PayerCost payerCost,
        @NonNull final BigDecimal totalAmount) {
        super(currencyId, payerCost, totalAmount);
    }

    @Override
    public void updateInstallmentsRowSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context, @NonNull final CharSequence amount) {
        //TODO implement
    }

    public static InstallmentsDescriptorView.Model createFrom(@NonNull final PaymentSettingRepository configuration,
        @NonNull final CardPaymentMetadata card) {
        final CheckoutPreference checkoutPreference = configuration.getCheckoutPreference();
        final PayerCost payerCost = card.getAutoSelectedInstallment();
        final String currencyId = checkoutPreference.getSite().getCurrencyId();
        final BigDecimal totalAmount = checkoutPreference.getTotalAmount();
        return new InstallmentsRowNoPayerCost(currencyId, payerCost, totalAmount);
    }
}
