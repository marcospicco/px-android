package com.mercadopago.android.px.internal.features.onetap.components;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.viewmodel.drawables.AccountMonetDrawableItem;

public class AccountMoneyFragment extends Fragment {

    private static final String ARG_MODEL = "ARG_MODEL";

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.px_fragment_account_money, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {

        final Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ARG_MODEL)) {
            final AccountMonetDrawableItem model =
                (AccountMonetDrawableItem) arguments.getSerializable(ARG_MODEL);
            final TextView message = view.findViewById(R.id.label);
            message.setText(model.label);
        } else {
            throw new IllegalStateException("ChangePaymentMethodFragment does not contains model info");
        }
    }

    @NonNull
    public static Fragment getInstance(@NonNull final AccountMonetDrawableItem item) {
        final AccountMoneyFragment accountMoneyFragment = new AccountMoneyFragment();
        final Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_MODEL, item);
        accountMoneyFragment.setArguments(bundle);
        return accountMoneyFragment;
    }
}
