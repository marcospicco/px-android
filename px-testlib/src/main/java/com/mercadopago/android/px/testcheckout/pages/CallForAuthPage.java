package com.mercadopago.android.px.testcheckout.pages;

import com.mercadopago.android.px.testcheckout.assertions.CheckoutValidator;
import com.mercadopago.android.testlib.pages.PageObject;

public class CallForAuthPage extends PageObject<CheckoutValidator> {

    public CallForAuthPage() {
        // This constructor is intentionally empty. Nothing special is needed here.
    }

    public CallForAuthPage(CheckoutValidator validator) {
        super(validator);
    }

    @Override
    public CallForAuthPage validate(CheckoutValidator validator) {
        validator.validate(this);
        return this;
    }
}
