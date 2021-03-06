package com.mercadopago.android.px.testcheckout.pages;

import android.support.test.espresso.action.ViewActions;
import android.view.View;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.testcheckout.assertions.CheckoutValidator;
import com.mercadopago.android.testlib.pages.PageObject;
import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class CreditCardPage extends PageObject<CheckoutValidator> {

    public CreditCardPage() {
        // This constructor is intentionally empty. Nothing special is needed here.
    }

    public CreditCardPage(CheckoutValidator validator) {
        super(validator);
    }

    @Override
    public CreditCardPage validate(CheckoutValidator validator) {
        validator.validate(this);
        return this;
    }

    public NamePage enterCreditCardNumber(final String cardNumber) {
        final Matcher<View> cardNextButtonTextMatcher = enterCardNumber(cardNumber);
        onView(cardNextButtonTextMatcher).perform(click());
        return new NamePage(validator);
    }

    public NoCheckoutPage pressBackWithExclusion() {
        onView(isRoot()).perform(ViewActions.pressBack());
        return new NoCheckoutPage(validator);
    }

    public PaymentMethodPage pressBack() {
        onView(isRoot()).perform(ViewActions.pressBack());
        return new PaymentMethodPage(validator);
    }

    public CreditCardPage enterExcludedCreditCardNumber(final String cardNumber) {
        enterCardNumber(cardNumber);
        return new CreditCardPage(validator);
    }

    public ReviewPaymentMethodsPage clickPaymentMethodNotSupportedSnackbar() {
        final Matcher<View> notSupportedSnackbarTextMatcher = withId(R.id.mpsdkRedErrorContainer);
        onView(notSupportedSnackbarTextMatcher).perform(click());
        return new ReviewPaymentMethodsPage(validator);
    }

    private Matcher<View> enterCardNumber(final String cardNumber) {
        final Matcher<View> cardNumberEditTextMatcher = withId(R.id.mpsdkCardNumber);
        final Matcher<View> cardNextButtonTextMatcher = withId(R.id.mpsdkNextButton);
        onView(cardNumberEditTextMatcher).perform(typeText(cardNumber));
        return cardNextButtonTextMatcher;
    }
}
