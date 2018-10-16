package com.mercadopago.android.px.internal.features.onetap.components;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.mercadopago.android.px.internal.viewmodel.drawables.AccountMonetDrawableItem;
import com.mercadopago.android.px.internal.viewmodel.drawables.ChangePaymentMethodDrawableItem;
import com.mercadopago.android.px.internal.viewmodel.drawables.DrawableItem;
import com.mercadopago.android.px.internal.viewmodel.drawables.ItemDrawer;
import com.mercadopago.android.px.internal.viewmodel.drawables.SavedCardDrawableItem;
import java.util.List;

public class PaymentMethodFragmentAdapter extends FragmentStatePagerAdapter implements ItemDrawer<Fragment> {

    @NonNull final List<DrawableItem> items;

    public PaymentMethodFragmentAdapter(@NonNull final FragmentManager fm,
        @NonNull final List<DrawableItem> drawableItems) {
        super(fm);
        items = drawableItems;
    }

    @Override
    public Fragment getItem(final int position) {
        return (Fragment) items.get(position).draw(this);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Fragment draw(@NonNull final DrawableItem drawableItem) {
        throw new IllegalStateException("Unknown type - PaymentMethodFragmentAdapter");
    }

    @Override
    public Fragment draw(@NonNull final ChangePaymentMethodDrawableItem drawableItem) {
        return ChangePaymentMethodFragment.getInstance(drawableItem);
    }

    @Override
    public Fragment draw(@NonNull final SavedCardDrawableItem drawableItem) {
        return SavedCardFragment.getInstance(drawableItem);
    }

    @Override
    public Fragment draw(@NonNull final AccountMonetDrawableItem drawableItem) {
        return AccountMoneyFragment.getInstance(drawableItem);
    }
}
