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
import com.mercadopago.android.px.internal.viewmodel.drawables.SavedCardDrawableItem;

public class SavedCardFragment extends PaymentMethodFragment {

    private static final String ARG_CARD = "ARG_CARD";

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.px_fragment_saved_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        final Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ARG_CARD)) {
            draw(view, (SavedCardDrawableItem) arguments.getSerializable(ARG_CARD));
        } else {
            throw new IllegalStateException("SavedCardFragment does not contains card information");
        }
    }

    private void draw(@NonNull final View view, @NonNull final SavedCardDrawableItem card) {
        final TextView cardHolderName = view.findViewById(R.id.card_holder_name);
        final TextView cardNumber = view.findViewById(R.id.card_number);
        final TextView expDate = view.findViewById(R.id.exp_date);
        cardHolderName.setText(card.name);
        cardNumber.setText(card.cardNumber);
        expDate.setText(card.expDate);
    }

    @NonNull
    public static Fragment getInstance(final SavedCardDrawableItem savedCard) {
        final SavedCardFragment savedCardFragment = new SavedCardFragment();
        final Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CARD, savedCard);
        savedCardFragment.setArguments(bundle);
        return savedCardFragment;
    }
}
