package com.mercadopago.android.px.internal.features.onetap;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.base.MvpView;
import com.mercadopago.android.px.internal.callbacks.PaymentServiceHandler;
import com.mercadopago.android.px.internal.features.explode.ExplodeDecorator;
import com.mercadopago.android.px.internal.features.explode.ExplodingFragment;
import com.mercadopago.android.px.internal.view.InstallmentsDescriptorView;
import com.mercadopago.android.px.internal.viewmodel.drawables.DrawableItem;
import com.mercadopago.android.px.internal.view.AmountDescriptorView;
import com.mercadopago.android.px.internal.view.ElementDescriptorView;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.IPayment;
import com.mercadopago.android.px.model.PaymentRecovery;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import java.util.List;

public interface OneTap {

    interface View extends MvpView {

        void configureView(@NonNull List<DrawableItem> items);

        void cancel();

        void showItemDescription(@NonNull ElementDescriptorView.Model model);

        void changePaymentMethod();

        void showCardFlow(@NonNull final Card card);

        void showDetailModal();

        void trackConfirm();

        void trackCancel();

        void trackModal();

        void showPaymentProcessor();

        void showLoadingFor(@NonNull final ExplodeDecorator params,
            @NonNull final ExplodingFragment.ExplodingAnimationListener explodingAnimationListener);

        void cancelLoading();

        void startLoadingButton(final int paymentTimeout);

        //TODO shared with Checkout activity

        void showErrorScreen(@NonNull final MercadoPagoError error);

        void showPaymentResult(@NonNull final IPayment paymentResult);

        void onRecoverPaymentEscInvalid(final PaymentRecovery recovery);

        void startPayment();

        void hideToolbar();

        void hideConfirmButton();

        void showErrorSnackBar(@NonNull final MercadoPagoError error);

        void showAmountRow(@NonNull final InstallmentsDescriptorView.Model installmentsModel);

        void showAmountDescription(AmountDescriptorView.Model amountDescriptorModel);
    }

    interface Actions extends PaymentServiceHandler {

        void confirmPayment();

        void onTokenResolved();

        void changePaymentMethod();

        void onAmountShowMore();

        void onViewResumed();

        void onViewPaused();
    }
}