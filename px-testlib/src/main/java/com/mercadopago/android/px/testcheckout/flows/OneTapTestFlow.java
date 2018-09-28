package com.mercadopago.android.px.testcheckout.flows;

import android.content.Context;
import android.support.annotation.NonNull;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.testcheckout.assertions.CheckoutValidator;
import com.mercadopago.android.px.testcheckout.input.Card;
import com.mercadopago.android.px.testcheckout.pages.CallForAuthPage;
import com.mercadopago.android.px.testcheckout.pages.CongratsPage;
import com.mercadopago.android.px.testcheckout.pages.OneTapPage;
import com.mercadopago.android.px.testcheckout.pages.PendingPage;
import com.mercadopago.android.px.testcheckout.pages.RejectedPage;

public class OneTapTestFlow extends TestFlow {

    public OneTapTestFlow() {
        // This constructor is intentionally empty. Nothing special is needed here.
    }

    public OneTapTestFlow(@NonNull final MercadoPagoCheckout mercadoPagoCheckout, @NonNull final Context context) {
        super(mercadoPagoCheckout, context);
    }

    public CongratsPage runCardWithOneTapWithoutESCApprovedPaymentFlow(final Card card,
        final CheckoutValidator validator) {
        startCheckout();
        return new OneTapPage(validator)
            .pressConfirmButton()
            .enterSecurityCodeToCongratsPage(card.escNumber());
    }

    public CallForAuthPage runSavedCardWithOneTapWithoutESCCallForAuthPaymentFlow(final Card card,
        final CheckoutValidator validator) {
        startCheckout();

        return new OneTapPage(validator)
            .pressConfirmButton()
            .enterSecurityCodeToCallForAuthPage(card.escNumber());
    }

    public PendingPage runSavedCardWithOneTapWithoutESCPendingPaymentFlow(final Card card,
        final CheckoutValidator validator) {
        startCheckout();

        return new OneTapPage(validator)
            .pressConfirmButton()
            .enterSecurityCodeToPendingPage(card.escNumber());
    }

    //TODO
    public CongratsPage runSavedCardWithOneTapWithESCApprovedPaymentFlow(final Card card,
        final CheckoutValidator validator) {
        startCheckout();

        return new CongratsPage(validator);
    }

    //TODO
    public CongratsPage runAccountMoneyWithOneTapApprovedPaymentFlow(final Card card,
        final CheckoutValidator validator) {
        startCheckout();

        return new CongratsPage(validator);
    }

    //TODO
    public RejectedPage runAccountMoneyWithOneTapRejectedPaymentFlow(final Card card,
        final CheckoutValidator validator) {
        startCheckout();

        return new RejectedPage(validator);
    }
}
