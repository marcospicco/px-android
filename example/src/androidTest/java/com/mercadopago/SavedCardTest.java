package com.mercadopago;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.mercadopago.android.px.configuration.PaymentConfiguration;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.internal.features.plugins.SamplePaymentProcessorNoView;
import com.mercadopago.android.px.model.GenericPayment;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.PaymentMethods;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import com.mercadopago.android.px.testcheckout.flows.SavedCardTestFlow;
import com.mercadopago.android.px.testcheckout.idleresources.CheckoutResource;
import com.mercadopago.android.px.testcheckout.pages.CongratsPage;
import com.mercadopago.android.px.utils.OneTapSamples;
import com.mercadopago.android.testlib.HttpResource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SavedCardTest {
    @Rule
    public HttpResource httpResource = new CheckoutResource();

    @Rule
    public ActivityTestRule<CheckoutExampleActivity> activityRule =
        new ActivityTestRule<>(CheckoutExampleActivity.class);

    @Test
    public void withValidVisaDefaultCardIdWithSavedDebitCardFlowIsOk() {

        final SavedCardTestFlow savedCardTestFlow =
            new SavedCardTestFlow("260077840", "debcabal", activityRule.getActivity());
        CongratsPage congratsPageSavedCard = savedCardTestFlow.runDefaultCardIdPaymentFlow();
        assertNotNull(congratsPageSavedCard);
    }

    @Test
    public void withInvalidVisaDefaultCardIdWithPaymentVaultFlowIsOk() {
        final SavedCardTestFlow savedCardTestFlow =
            new SavedCardTestFlow("01010", "debcabal", activityRule.getActivity());
        CongratsPage congratsPageSavedCard = savedCardTestFlow.runInvalidDefaultCardIdPaymentFlow();
        assertNotNull(congratsPageSavedCard);
    }

}
