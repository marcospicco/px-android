package com.mercadopago.android.px.internal.viewmodel.mappers;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.view.ElementDescriptorView;
import com.mercadopago.android.px.model.Item;
import com.mercadopago.android.px.preferences.CheckoutPreference;

public class ElementDescriptorMapper extends Mapper<CheckoutPreference, ElementDescriptorView.Model> {

    private final String defaultLabel;
    private final int defaultIcon;

    public ElementDescriptorMapper(@NonNull final String defaultLabel) {
        this.defaultLabel = defaultLabel;
        defaultIcon = R.drawable.px_review_item_default;
    }

    @Override
    public ElementDescriptorView.Model map(@NonNull final CheckoutPreference checkoutPreference) {
        final String title = Item.getItemsTitle(checkoutPreference.getItems(), defaultLabel);
        final Item firstItem = checkoutPreference.getItems().get(0);
        return new ElementDescriptorView.Model(title, firstItem.getPictureUrl(), defaultIcon);
    }
}
