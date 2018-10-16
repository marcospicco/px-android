package com.mercadopago.android.px.internal.viewmodel.drawables;

import android.support.annotation.NonNull;

public interface ItemDrawer<T> {

    T draw(@NonNull final DrawableItem drawableItem);

    T draw(@NonNull final ChangePaymentMethodDrawableItem drawableItem);

    T draw(@NonNull final SavedCardDrawableItem drawableItem);

    T draw(@NonNull final AccountMonetDrawableItem drawableItem);
}
