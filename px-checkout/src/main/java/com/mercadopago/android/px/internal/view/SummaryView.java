package com.mercadopago.android.px.internal.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.mercadopago.android.px.R;

public class SummaryView extends LinearLayout {

    public SummaryView(final Context context) {
        this(context, null);
    }

    public SummaryView(final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SummaryView(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void updateElementDescriptor(@NonNull final ElementDescriptorView.Model elementDescriptorModel) {
        for (int i = 0; i < getChildCount(); i++) {
            final View childAt = getChildAt(i);
            if (childAt instanceof ElementDescriptorView) {
                ((ElementDescriptorView) childAt).update(elementDescriptorModel);
                return;
            }
        }
        addElementDescriptor(elementDescriptorModel);
    }

    private void addElementDescriptor(final ElementDescriptorView.Model elementDescriptorModel) {
        final Context context = getContext();
        final Resources resources = context.getResources();
        final int iconSize = (int) resources.getDimension(R.dimen.px_m_height);
        final ElementDescriptorView elementDescriptorView = new ElementDescriptorView(context);
        elementDescriptorView.update(elementDescriptorModel);
        elementDescriptorView.setIconSize(iconSize, iconSize);
        elementDescriptorView.setOrientation(VERTICAL);
        elementDescriptorView.setTextSize(resources.getDimension(R.dimen.px_xxl_text));
        addView(elementDescriptorView);
    }

    public void updateAmountDescriptor(final AmountDescriptorView.Model amountDescriptorModel) {
        for (int i = 0; i < getChildCount(); i++) {
            final View childAt = getChildAt(i);
            if (childAt instanceof AmountDescriptorView) {
                ((AmountDescriptorView) childAt).update(amountDescriptorModel);
                return;
            }
        }
        addAmountDescriptor(amountDescriptorModel);
    }

    private void addAmountDescriptor(final AmountDescriptorView.Model amountDescriptorModel) {
        final AmountDescriptorView amountDescriptorView = new AmountDescriptorView(getContext());
        amountDescriptorView.setAmountDescriptorStyle(amountDescriptorModel.getAmountStyle());
        amountDescriptorView.update(amountDescriptorModel);
        addView(amountDescriptorView);
    }
}
