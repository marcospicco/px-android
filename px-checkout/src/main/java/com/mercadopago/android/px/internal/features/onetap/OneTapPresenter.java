package com.mercadopago.android.px.internal.features.onetap;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.base.MvpPresenter;
import com.mercadopago.android.px.internal.base.ResourcesProvider;
import com.mercadopago.android.px.internal.features.explode.ExplodeDecoratorMapper;
import com.mercadopago.android.px.internal.features.explode.ExplodingFragment;
import com.mercadopago.android.px.internal.repository.GroupsRepository;
import com.mercadopago.android.px.internal.repository.PaymentRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.view.InstallmentsDescriptorView;
import com.mercadopago.android.px.internal.viewmodel.InstallmentsDescriptorNoPayerCost;
import com.mercadopago.android.px.internal.viewmodel.InstallmentsDescriptorWithPayerCost;
import com.mercadopago.android.px.model.BusinessPayment;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.GenericPayment;
import com.mercadopago.android.px.model.OneTapMetadata;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.PaymentMethodSearch;
import com.mercadopago.android.px.model.PaymentRecovery;
import com.mercadopago.android.px.model.PaymentTypes;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.mercadopago.android.px.services.Callback;
import com.mercadopago.android.px.internal.view.AmountDescriptorView;
import com.mercadopago.android.px.internal.view.ElementDescriptorView;
import com.mercadopago.android.px.internal.viewmodel.mappers.ElementDescriptorMapper;
import com.mercadopago.android.px.internal.viewmodel.mappers.PaymentMethodDrawableItemMapper;
import com.mercadopago.android.px.model.Sites;
import java.math.BigDecimal;

/* default */ class OneTapPresenter extends MvpPresenter<OneTap.View, ResourcesProvider>
    implements OneTap.Actions {

    @NonNull private final PaymentRepository paymentRepository;
    @NonNull private final GroupsRepository groupsRepository;
    @NonNull private final PaymentSettingRepository configuration;
    @NonNull private final ExplodeDecoratorMapper explodeDecoratorMapper;
    @NonNull private final PaymentMethodDrawableItemMapper paymentMethodDrawableItemMapper;
    @NonNull private final ElementDescriptorMapper elementDescriptorMapper;
    private OneTapMetadata oneTapMetadata;

    /* default */ OneTapPresenter(@NonNull final PaymentRepository paymentRepository,
        @NonNull final PaymentSettingRepository configuration,
        @NonNull final ElementDescriptorMapper elementDescriptorMapper,
        @NonNull final GroupsRepository groupsRepository) {
        this.paymentRepository = paymentRepository;
        this.configuration = configuration;
        this.groupsRepository = groupsRepository;
        explodeDecoratorMapper = new ExplodeDecoratorMapper();
        this.elementDescriptorMapper = elementDescriptorMapper;
        paymentMethodDrawableItemMapper = new PaymentMethodDrawableItemMapper();

            groupsRepository.getGroups().execute(new Callback<PaymentMethodSearch>() {
                @Override
                public void success(final PaymentMethodSearch paymentMethodSearch) {
                    oneTapMetadata = paymentMethodSearch.getOneTapMetadata();
                }

                @Override
                public void failure(final ApiException apiException) {
                    throw new IllegalStateException("groups missing rendering one tap");
                }
            });
    }

    @Override
    public void confirmPayment() {
        getView().trackConfirm();
        getView().hideToolbar();

        if (paymentRepository.isExplodingAnimationCompatible()) {
            getView().startLoadingButton(paymentRepository.getPaymentTimeout());
            getView().hideConfirmButton();
        }

        // TODO improve: This was added because onetap can detach this listener on its OnDestroy
        paymentRepository.attach(this);
        paymentRepository.startOneTapPayment();
    }

    @Override
    public void changePaymentMethod() {
        getView().changePaymentMethod();
    }

    @Override
    public void onAmountShowMore() {
        getView().trackModal();
        getView().showDetailModal();
    }

    public void cancel() {
        getView().cancel();
        getView().trackCancel();
    }

    @Override
    public void onTokenResolved() {
        getView().cancelLoading();
        confirmPayment();
    }

    @Override
    public void onPaymentFinished(@NonNull final Payment payment) {
        getView().showLoadingFor(explodeDecoratorMapper.map(payment),
            new ExplodingFragment.ExplodingAnimationListener() {
                @Override
                public void onAnimationFinished() {
                    getView().showPaymentResult(payment);
                }
            });
    }

    /**
     * When there is no visual interaction needed this callback is called.
     *
     * @param genericPayment plugin payment.
     */
    @Override
    public void onPaymentFinished(@NonNull final GenericPayment genericPayment) {
        getView().showLoadingFor(explodeDecoratorMapper.map(genericPayment),
            new ExplodingFragment.ExplodingAnimationListener() {
                @Override
                public void onAnimationFinished() {
                    getView().showPaymentResult(genericPayment);
                }
            });
    }

    /**
     * When there is no visual interaction needed this callback is called.
     *
     * @param businessPayment plugin payment.
     */
    @Override
    public void onPaymentFinished(@NonNull final BusinessPayment businessPayment) {
        getView().showLoadingFor(explodeDecoratorMapper.map(businessPayment),
            new ExplodingFragment.ExplodingAnimationListener() {
                @Override
                public void onAnimationFinished() {
                    getView().showPaymentResult(businessPayment);
                }
            });
    }

    @Override
    public void onPaymentError(@NonNull final MercadoPagoError error) {
        getView().cancelLoading();

        if (error.isInternalServerError() || error.isNoConnectivityError()) {
            getView().showErrorSnackBar(error);
        } else {
            getView().showErrorScreen(error);
        }
    }

    @Override
    public void onVisualPayment() {
        getView().showPaymentProcessor();
    }

    @Override
    public void onCvvRequired(@NonNull final Card card) {
        getView().cancelLoading();
        getView().showCardFlow(card);
    }

    @Override
    public void onRecoverPaymentEscInvalid(final PaymentRecovery recovery) {
        getView().onRecoverPaymentEscInvalid(recovery);
    }

    @Override
    public void onViewResumed() {
        final ElementDescriptorView.Model elementDescriptorModel =
            elementDescriptorMapper.map(configuration.getCheckoutPreference());
        //TODO armar el modelo en serio
        final AmountDescriptorView.Model amountDescriptorModel = new AmountDescriptorView.Model(
            AmountDescriptorView.AmountType.POSITIVE, "Total", Sites.ARGENTINA.getCurrencyId(), new BigDecimal(100));
        amountDescriptorModel.setAmountStyle(AmountDescriptorView.AmountStyle.BOLD);

        getView().showAmountRow(createInstallmentsDescriptorModel());
        getView().showItemDescription(elementDescriptorModel);
        getView().showAmountDescription(amountDescriptorModel);

        //If a payment was attempted, the exploding fragment is still visible when we go back to one tap fragment.
        //Example: call for authorize, after asking for cvv and pressing back, we go back to one tap and need to
        //remove the exploding fragment we had before.
        if (paymentRepository.hasPayment()) {
            getView().cancelLoading();
        }
        paymentRepository.attach(this);
    }

    private InstallmentsDescriptorView.Model createInstallmentsDescriptorModel() {
        final String paymentTypeId = oneTapMetadata.getPaymentTypeId();
        if (PaymentTypes.isCreditCardPaymentType(paymentTypeId)) {
            return InstallmentsDescriptorWithPayerCost.createFrom(configuration, oneTapMetadata.getCard());
        } else {
            //TODO fix account money case
            return InstallmentsDescriptorNoPayerCost.createFrom(configuration, oneTapMetadata.getCard());
        }
    }

    @Override
    public void attachView(final OneTap.View view) {
        super.attachView(view);

        groupsRepository.getGroups().execute(new Callback<PaymentMethodSearch>() {
            @Override
            public void success(final PaymentMethodSearch paymentMethodSearch) {
                getView().configureView(paymentMethodDrawableItemMapper.map(paymentMethodSearch));
            }

            @Override
            public void failure(final ApiException apiException) {
                throw new IllegalStateException("OneTapFragment - attachView - getting groups error");
            }
        });

        paymentRepository.attach(this);
    }

    @Override
    public void onViewPaused() {
        paymentRepository.detach(this);
    }

    @Override
    public void detachView() {
        onViewPaused();
        super.detachView();
    }
}