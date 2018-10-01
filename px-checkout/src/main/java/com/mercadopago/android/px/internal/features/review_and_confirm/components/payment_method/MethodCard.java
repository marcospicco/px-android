package com.mercadopago.android.px.internal.features.review_and_confirm.components.payment_method;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.view.DraweeView;
import com.mercadolibre.android.ui.utils.facebook.fresco.FrescoImageController;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.features.review_and_confirm.models.PaymentModel;
import com.mercadopago.android.px.internal.util.ResourceUtil;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.internal.view.CompactComponent;
import java.util.Locale;

/* default */ class MethodCard extends CompactComponent<MethodCard.Props, Void> {

    static class Props {

        /* default */ final String id;
        /* default */ final String cardName;
        /* default */ final String lastFourDigits;
        /* default */ final String bankName;

        /* default */ Props(String id,
            String cardName,
            String lastFourDigits,
            String bankName) {
            this.id = id;
            this.cardName = cardName;
            this.lastFourDigits = lastFourDigits;
            this.bankName = bankName;
        }

        /* default */
        static Props createFrom(final PaymentModel props) {
            return new Props(props.paymentMethodId,
                props.paymentMethodName,
                props.lastFourDigits,
                props.issuerName);
        }
    }

    MethodCard(final Props props) {
        super(props);
    }

    @Override
    public View render(@NonNull final ViewGroup parent) {

        final View paymentView = inflate(parent, R.layout.px_payment_method_card);

        final TextView title = paymentView.findViewById(R.id.title);
        title.setText(formatTitle(title.getContext()));

        final TextView subtitle = paymentView.findViewById(R.id.subtitle);
        subtitle.setText(props.bankName);

        subtitle.setVisibility(shouldShowSubtitle() ? View.VISIBLE : View.GONE);

        final DraweeView imageView = paymentView.findViewById(R.id.icon);
        FrescoImageController.create()
            .load(ResourceUtil.getIconResource(imageView.getContext(), props.id))
            .into(imageView);


        return paymentView;
    }

    @VisibleForTesting
    boolean shouldShowSubtitle() {
        return TextUtil.isNotEmpty(props.bankName) && !props.bankName.equals(props.cardName);
    }

    private String formatTitle(final Context context) {
        final String ending = context.getString(R.string.px_ending_in);
        return String.format(Locale.getDefault(), "%s %s %s",
            props.cardName,
            ending,
            props.lastFourDigits);
    }
}
