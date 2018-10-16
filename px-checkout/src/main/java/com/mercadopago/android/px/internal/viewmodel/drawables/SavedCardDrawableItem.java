package com.mercadopago.android.px.internal.viewmodel.drawables;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.model.Card;
import java.util.Locale;

public class SavedCardDrawableItem implements DrawableItem {

    @NonNull public final String name;
    @NonNull public final String cardNumber;
    @NonNull public final String expDate;

    public SavedCardDrawableItem(@NonNull final Card card) {
        //TODO refactor - cardholder name
        name =
            card.getCardHolder() != null && TextUtil.isNotEmpty(card.getCardHolder().getName()) ? card.getCardHolder()
                .getName() : "***** *****";

        //TODO better formatting - Dates come null.
        expDate = String.format(Locale.getDefault(), "%d/%d", card.getExpirationMonth(), card.getExpirationYear());
        //TODO better digit * amount
        cardNumber = String.format(Locale.getDefault(), "%s %s", "**** **** ****", card.getLastFourDigits());

        //TODO issuer

        //TODO Card brand

        //TODO color
    }

    @Override
    public Object draw(@NonNull final ItemDrawer drawer) {
        return drawer.draw(this);
    }
}
