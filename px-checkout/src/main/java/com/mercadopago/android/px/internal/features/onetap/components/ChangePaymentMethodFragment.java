package com.mercadopago.android.px.internal.features.onetap.components;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mercadolibre.android.ui.widgets.MeliButton;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.features.Constants;
import com.mercadopago.android.px.internal.viewmodel.drawables.ChangePaymentMethodDrawableItem;

public class ChangePaymentMethodFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_MODEL = "ARG_MODEL";

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.px_fragment_change_payment_method, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {

        final Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ARG_MODEL)) {
            final ChangePaymentMethodDrawableItem model =
                (ChangePaymentMethodDrawableItem) arguments.getSerializable(ARG_MODEL);
            final View floating = view.findViewById(R.id.floating_change);
            final MeliButton message = view.findViewById(R.id.message);
            message.setText(model.message);
            floating.setOnClickListener(this);
            message.setOnClickListener(this);
        } else {
            throw new IllegalStateException("ChangePaymentMethodFragment does not contains model info");
        }
    }

    @NonNull
    public static Fragment getInstance(final ChangePaymentMethodDrawableItem drawableItem) {
        final ChangePaymentMethodFragment changePaymentMethodFragment = new ChangePaymentMethodFragment();
        final Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_MODEL, drawableItem);
        changePaymentMethodFragment.setArguments(bundle);
        return changePaymentMethodFragment;
    }

    @Override
    public void onClick(final View v) {
        new Constants.Activities.PaymentVaultActivityBuilder().setActivity(getActivity())
            .startActivity();
    }
}
