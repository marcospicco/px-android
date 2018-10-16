package com.mercadopago.android.px.internal.viewmodel.drawables;

import android.support.annotation.NonNull;

public class AccountMonetDrawableItem implements DrawableItem {

    @NonNull public final String label;

    public AccountMonetDrawableItem(@NonNull final String label) {
        this.label = label;
    }

    @Override
    public Object draw(@NonNull final ItemDrawer drawer) {
        return drawer.draw(this);
    }
}
