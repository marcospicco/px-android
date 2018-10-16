package com.mercadopago.android.px.internal.viewmodel.mappers;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.viewmodel.drawables.AccountMonetDrawableItem;
import com.mercadopago.android.px.internal.viewmodel.drawables.ChangePaymentMethodDrawableItem;
import com.mercadopago.android.px.internal.viewmodel.drawables.DrawableItem;
import com.mercadopago.android.px.internal.viewmodel.drawables.SavedCardDrawableItem;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.CustomSearchItem;
import com.mercadopago.android.px.model.PaymentMethodSearch;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodDrawableItemMapper extends Mapper<PaymentMethodSearch, List<DrawableItem>> {

    @Override
    public List<DrawableItem> map(@NonNull final PaymentMethodSearch val) {
        //TODO add viewmodel for pager:
        // Plugin if it's account monet
        // change payment method viewmodel
        // Saved cards
        // Onetap selected first.
        final List<DrawableItem> result = new ArrayList<>();

        result.add(new AccountMonetDrawableItem("Total en tu cuenta: $ 5.632"));

        for (final CustomSearchItem item : val.getCustomSearchItems()) {
            final Card card = val.getCardById(item.getId());
            result.add(new SavedCardDrawableItem(card));
        }


        //TODO fix.
        result.add(new ChangePaymentMethodDrawableItem("Agregar nueva tarjeta"));
        return result;
    }
}
