package com.mercadopago.android.px.internal.features.onetap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.mercadolibre.android.ui.widgets.MeliSnackbar;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.di.Session;
import com.mercadopago.android.px.internal.features.CheckoutActivity;
import com.mercadopago.android.px.internal.features.Constants;
import com.mercadopago.android.px.internal.features.explode.ExplodeDecorator;
import com.mercadopago.android.px.internal.features.explode.ExplodeParams;
import com.mercadopago.android.px.internal.features.explode.ExplodingFragment;
import com.mercadopago.android.px.internal.features.onetap.components.PaymentMethodFragmentAdapter;
import com.mercadopago.android.px.internal.features.plugins.PaymentProcessorActivity;
import com.mercadopago.android.px.internal.repository.DiscountRepository;
import com.mercadopago.android.px.internal.tracker.Tracker;
import com.mercadopago.android.px.internal.util.StatusBarDecorator;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.internal.view.AmountDescriptorView;
import com.mercadopago.android.px.internal.view.Button;
import com.mercadopago.android.px.internal.view.ButtonPrimary;
import com.mercadopago.android.px.internal.view.ElementDescriptorView;
import com.mercadopago.android.px.internal.view.ScrollingPagerIndicator;
import com.mercadopago.android.px.internal.view.SummaryView;
import com.mercadopago.android.px.internal.viewmodel.drawables.DrawableItem;
import com.mercadopago.android.px.internal.viewmodel.mappers.ElementDescriptorMapper;
import com.mercadopago.android.px.model.Action;
import com.mercadopago.android.px.model.BusinessPayment;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.Discount;
import com.mercadopago.android.px.model.GenericPayment;
import com.mercadopago.android.px.model.IPayment;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.PaymentRecovery;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.view.View.INVISIBLE;

public class OneTapFragment extends Fragment implements OneTap.View {

    private static final String TAG_EXPLODING_FRAGMENT = "TAG_EXPLODING_FRAGMENT";
    private static final int REQ_CODE_CARD_VAULT = 0x999;
    private static final int REQ_CODE_PAYMENT_PROCESSOR = 0x123;
    private static final float PAGER_VISIBILITY_MULTIPLIER = 1.5f;

    private CallBack callback;

    /* default */ OneTapPresenter presenter;

    private Toolbar toolbar;

    private SummaryView summaryView;

    private ViewGroup linearContainer;
    private View confirmButton;

    public static Fragment getInstance() {
        return new OneTapFragment();
    }

    public interface CallBack {

        void onOneTapCanceled();

        void onChangePaymentMethod();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
        @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.px_onetap_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        final Session session = Session.getSession(view.getContext());
        presenter = new OneTapPresenter(session.getPaymentRepository(),
            session.getConfigurationModule().getPaymentSettings(),
            new ElementDescriptorMapper(getString(R.string.px_review_summary_products)),
            session.getGroupsRepository());
        presenter.attachView(this);
        trackScreen();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onViewResumed();
    }

    @Override
    public void onPause() {
        presenter.onViewPaused();
        super.onPause();
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof CallBack) {
            callback = (CallBack) context;
        }
    }

    @Override
    public void onDetach() {
        callback = null;
        presenter.detachView();
        super.onDetach();
    }

    @Override
    public void configureView(@NonNull final List<DrawableItem> items) {
        final View view = getView();
        linearContainer = view.findViewById(R.id.linear_container);
        final ScrollingPagerIndicator indicator = view.findViewById(R.id.indicator);
        summaryView = view.findViewById(R.id.summary_view);
        final ViewPager paymentMethodPager = view.findViewById(R.id.payment_method_pager);
        paymentMethodPager.setPageMargin(
            ((int) (-getResources().getDimensionPixelSize(R.dimen.px_m_margin) * PAGER_VISIBILITY_MULTIPLIER)));
        paymentMethodPager.setOffscreenPageLimit(2);

        paymentMethodPager.setAdapter(new PaymentMethodFragmentAdapter(getChildFragmentManager(), items));
        indicator.attachToPager(paymentMethodPager);

        toolbar = view.findViewById(R.id.toolbar);
        showToolbar();
        addConfirmButton(Session.getSession(view.getContext()).getDiscountRepository());
    }

    private void addConfirmButton(@NonNull final DiscountRepository discountRepository) {
        final Discount discount = discountRepository.getDiscount();

        final String confirm = getContext().getString(R.string.px_confirm);
        final Button.Actions buttonActions = new Button.Actions() {
            @Override
            public void onClick(final Action action) {
                presenter.confirmPayment();
            }
        };

        final Button button = new ButtonPrimary(new Button.Props(confirm), buttonActions);
        final View view = button.render(linearContainer);
        final int resMargin = discount == null ? R.dimen.px_m_margin : R.dimen.px_zero_height;
        ViewUtils.setMarginTopInView(view, getContext().getResources().getDimensionPixelSize(resMargin));
        linearContainer.addView(view);
        confirmButton = view;
        final int mMargin = getContext().getResources().getDimensionPixelSize(R.dimen.px_m_margin);
        final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.setMargins(mMargin, layoutParams.topMargin, mMargin, mMargin);
    }

    private void trackScreen() {
        if (getActivity() != null) {
            Tracker.trackOneTapScreen(getActivity().getApplicationContext());
        }
    }

    @Override
    public void cancel() {
        if (callback != null) {
            callback.onOneTapCanceled();
        }
    }

    @Override
    public void showItemDescription(@NonNull final ElementDescriptorView.Model model) {
        //Change when all together works
        summaryView.updateElementDescriptor(model);
//        if (toolbarSwitcher.getNextView().equals(toolbarWithElement)) {
//                toolbarSwitcher.showNext();
//        }
    }

    @Override
    public void showAmountDescription(final AmountDescriptorView.Model amountDescriptorModel) {
        summaryView.updateAmountDescriptor(amountDescriptorModel);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQ_CODE_CARD_VAULT && resultCode == RESULT_OK) {
            getActivity().getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    presenter.onTokenResolved();
                }
            });
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void changePaymentMethod() {
        if (callback != null) {
            callback.onChangePaymentMethod();
        }
    }

    @Override
    public void showDetailModal() {
        PaymentDetailInfoDialog.showDialog(getChildFragmentManager());
    }

    @Override
    public void trackConfirm() {
        if (getActivity() != null) {
            Tracker.trackOneTapConfirm(getActivity().getApplicationContext());
        }
    }

    @Override
    public void trackCancel() {
        if (getActivity() != null) {
            Tracker.trackOneTapCancel(getActivity().getApplicationContext());
        }
    }

    @Override
    public void trackModal() {
        if (getActivity() != null) {
            Tracker.trackOneTapSummaryDetail(getActivity().getApplicationContext());
        }
    }

    @Override
    public void showPaymentProcessor() {
        final Intent intent = PaymentProcessorActivity.getIntent(getContext());
        startActivityForResult(intent, REQ_CODE_PAYMENT_PROCESSOR);
    }

    @Override
    public void showErrorScreen(@NonNull final MercadoPagoError error) {
        if (getActivity() != null) {
            ((CheckoutActivity) getActivity()).presenter.onPaymentError(error);
        }
    }

    @Override
    public void showErrorSnackBar(@NonNull final MercadoPagoError error) {
        if (getView() != null && getActivity() != null) {
            MeliSnackbar.make(getView(), error.getMessage(), Snackbar.LENGTH_LONG,
                MeliSnackbar.SnackbarType.ERROR).show();
            Tracker.trackError(getActivity().getApplicationContext(), error);
        }
    }

    @Override
    public void showPaymentResult(@NonNull final IPayment paymentResult) {
        //TODO refactor
        if (getActivity() != null) {
            //TODO refactor
            if (paymentResult instanceof GenericPayment) {
                ((CheckoutActivity) getActivity()).presenter.onPaymentFinished((GenericPayment) paymentResult);
            } else if (paymentResult instanceof Payment) {
                ((CheckoutActivity) getActivity()).presenter.onPaymentFinished((Payment) paymentResult);
            } else {
                ((CheckoutActivity) getActivity()).presenter.onPaymentFinished((BusinessPayment) paymentResult);
            }
        }
    }

    //TODO refactor
    @Override
    public void onRecoverPaymentEscInvalid(final PaymentRecovery recovery) {
        if (getActivity() != null) {
            ((CheckoutActivity) getActivity()).presenter.onRecoverPaymentEscInvalid(recovery);
        }
    }

    @Override
    public void startPayment() {
        presenter.confirmPayment();
    }

    @Override
    public void showLoadingFor(@NonNull final ExplodeDecorator params,
        @NonNull final ExplodingFragment.ExplodingAnimationListener explodingAnimationListener) {

        final FragmentManager childFragmentManager = getChildFragmentManager();
        final Fragment fragment = childFragmentManager.findFragmentByTag(TAG_EXPLODING_FRAGMENT);
        if (fragment != null && fragment.isAdded() && fragment instanceof ExplodingFragment) {
            ((ExplodingFragment) fragment).finishLoading(params, explodingAnimationListener);
        }
    }

    @Override
    public void cancelLoading() {
        showToolbar();
        hideConfirmButton();
        restoreStatusBar();

        final FragmentManager childFragmentManager = getChildFragmentManager();
        final Fragment fragment = childFragmentManager.findFragmentByTag(TAG_EXPLODING_FRAGMENT);
        if (fragment != null && fragment.isAdded()) {
            childFragmentManager.beginTransaction().remove(fragment).commitNow();
        }
    }

    private void restoreStatusBar() {
        if (getActivity() != null) {
            new StatusBarDecorator(getActivity().getWindow())
                .setupStatusBarColor(ContextCompat.getColor(getActivity(), R.color.px_colorPrimaryDark));
        }
    }

    private void showToolbar() {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            activity.setSupportActionBar(toolbar);

            final ActionBar supportActionBar = activity.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setDisplayShowTitleEnabled(false);
                supportActionBar.setDisplayHomeAsUpEnabled(true);
                supportActionBar.setDisplayShowHomeEnabled(true);
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    presenter.cancel();
                }
            });
        }
    }

    @Override
    public void hideConfirmButton() {
        confirmButton.setVisibility(INVISIBLE);
    }

    @Override
    public void hideToolbar() {
        if (getActivity() != null) {
            final ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            supportActionBar.setDisplayHomeAsUpEnabled(false);
            supportActionBar.setDisplayShowHomeEnabled(false);
        }
    }

    @Override
    public void startLoadingButton(final int paymentTimeout) {

        final int[] location = new int[2];
        confirmButton.getLocationOnScreen(location);

        final ExplodeParams explodeParams =
            new ExplodeParams(location[1] - confirmButton.getMeasuredHeight() / 2, confirmButton.getMeasuredHeight(),
                (int) getContext().getResources().getDimension(R.dimen.px_m_margin),
                getContext().getResources().getString(R.string.px_processing_payment_button),
                paymentTimeout);

        final FragmentManager childFragmentManager = getChildFragmentManager();
        childFragmentManager.beginTransaction()
            .replace(R.id.exploding_frame, ExplodingFragment.newInstance(explodeParams), TAG_EXPLODING_FRAGMENT)
            .commitNowAllowingStateLoss();
        childFragmentManager.executePendingTransactions();
    }

    @Override
    public void showCardFlow(@NonNull final Card card) {
        new Constants.Activities.CardVaultActivityBuilder()
            .setCard(card)
            .startActivity(this, REQ_CODE_CARD_VAULT);
    }
}
