package com.mercadopago.android.px.internal.features.paymentresult.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mercadolibre.android.ui.utils.facebook.fresco.FrescoImageController;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.internal.view.Renderer;

public class IconRenderer extends Renderer<Icon> {

    @Override
    public View render(@NonNull final Icon component, @NonNull final Context context,
        @Nullable final ViewGroup parent) {

        final View iconView = inflate(R.layout.px_icon, parent);
        final SimpleDraweeView iconImageView = iconView.findViewById(R.id.mpsdkIconProduct);
        final SimpleDraweeView iconBadgeView = iconView.findViewById(R.id.mpsdkIconBadge);

        //Render icon
        if (component.hasIconFromUrl()) {
            ViewUtils.loadIntoCircle(iconImageView, component.props.iconImage, component.props.iconUrl);
        } else {
            ViewUtils.loadIntoCircle(iconImageView, component.props.iconImage);
        }

        //Render badge
        if (component.props.badgeImage == 0) {
            iconBadgeView.setVisibility(View.INVISIBLE);
        } else {
            FrescoImageController.create().load(component.props.badgeImage).into(iconBadgeView);
            iconBadgeView.setVisibility(View.VISIBLE);
        }

        return iconView;
    }
}
