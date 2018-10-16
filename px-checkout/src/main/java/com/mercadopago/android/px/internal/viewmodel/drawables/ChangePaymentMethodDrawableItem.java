package com.mercadopago.android.px.internal.viewmodel.drawables;

import android.support.annotation.NonNull;

public class ChangePaymentMethodDrawableItem implements DrawableItem {

    @NonNull public final String message;

    public ChangePaymentMethodDrawableItem(@NonNull final String message) {
        this.message = message;
    }

    @Override
    public Object draw(@NonNull final ItemDrawer drawer) {
        return drawer.draw(this);
    }
}
