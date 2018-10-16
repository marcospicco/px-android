package com.mercadopago.android.px.internal.viewmodel.drawables;

import android.support.annotation.NonNull;

public class AccountMonetDrawableItem implements DrawableItem {

    @Override
    public Object draw(@NonNull final ItemDrawer drawer) {
        return drawer.draw(this);
    }
}
