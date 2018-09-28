package com.mercadopago.android.px.internal.features.onetap.components;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.view.DraweeView;
import com.mercadolibre.android.ui.utils.facebook.fresco.FrescoImageController;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.view.CompactComponent;
import javax.annotation.Nonnull;

class CollapsedItem extends CompactComponent<CollapsedItem.Props, Void> {

    /* default */ static class Props {

        @DrawableRes final int icon;
        @NonNull final String itemTitle;

        /* default */ Props(final int icon, @NonNull final String itemTitle) {
            this.icon = icon;
            this.itemTitle = itemTitle;
        }
    }

    /* default */ CollapsedItem(final Props props) {
        super(props);
    }

    @Override
    public View render(@Nonnull final ViewGroup parent) {
        final View layout = inflate(parent, R.layout.px_view_onetap_item);
        final TextView itemTitle = layout.findViewById(R.id.item_title);
        final DraweeView itemImage = layout.findViewById(R.id.item_image);
        FrescoImageController.create().load(props.icon).into(itemImage);
        itemTitle.setText(props.itemTitle);
        return layout;
    }
}
