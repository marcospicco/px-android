package com.mercadopago.android.px.testcheckout.flows;

import com.mercadopago.android.px.testcheckout.assertions.CheckoutValidator;
import com.mercadopago.android.px.testcheckout.input.Card;
import com.mercadopago.android.px.testcheckout.pages.CongratsPage;


public class OneTapTestFlow extends TestFlow {

    //TODO
    public CongratsPage runSavedCardWithOneTapWithoutESCApprovedPaymentFlow(final Card card,
        final CheckoutValidator validator) {
        startCheckout();

        return new CongratsPage(validator);
    }

    //TODO
    public CongratsPage runSavedCardWithOneTapWithoutESCCallForAuthPaymentFlow(final Card card,
        final CheckoutValidator validator) {
        startCheckout();

        return new CongratsPage(validator);
    }

    //TODO
    public CongratsPage runSavedCardWithOneTapWithoutESCPendingPaymentFlow(final Card card,
        final CheckoutValidator validator) {
        startCheckout();

        return new CongratsPage(validator);
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
    public CongratsPage runAccountMoneyWithOneTapRejectedPaymentFlow(final Card card,
        final CheckoutValidator validator) {
        startCheckout();

        return new CongratsPage(validator);
    }
}
