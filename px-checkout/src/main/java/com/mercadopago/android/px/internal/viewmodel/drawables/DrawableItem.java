package com.mercadopago.android.px.internal.viewmodel.drawables;

import android.support.annotation.NonNull;
import java.io.Serializable;

public interface DrawableItem extends Serializable {

    Object draw(@NonNull final ItemDrawer drawer);
}
