package com.mercadopago.android.px.internal.features.uicontrollers.paymentmethods.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.drawee.view.DraweeView;
import com.mercadolibre.android.ui.utils.facebook.fresco.FrescoImageController;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.features.uicontrollers.paymentmethods.PaymentMethodViewController;
import com.mercadopago.android.px.internal.util.ResourceUtil;
import com.mercadopago.android.px.internal.view.MPTextView;
import com.mercadopago.android.px.model.PaymentMethod;

public abstract class PaymentMethodOnView implements PaymentMethodViewController {

    protected PaymentMethod mPaymentMethod;
    protected Context mContext;
    protected View mSeparator;
    protected View mView;
    protected MPTextView mDescription;
    protected DraweeView mIcon;
    protected DraweeView mEditHint;

    @Override
    public void draw() {
        if (getLastFourDigits() == null || getLastFourDigits().isEmpty()) {
            mDescription.setText(mPaymentMethod.getName());
        } else {
            mDescription.setText(
                new StringBuilder().append(mContext.getString(R.string.px_last_digits_label)).append(" ")
                    .append(getLastFourDigits()).toString());
        }
        final int resourceId = ResourceUtil.getIconById(mContext, mPaymentMethod.getId());
        if (resourceId != 0) {
            FrescoImageController.create().load(resourceId).into(mIcon);
        } else {
            mIcon.setVisibility(View.GONE);
        }
    }

    protected abstract String getLastFourDigits();

    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        mEditHint.setVisibility(View.VISIBLE);
        mView.setOnClickListener(listener);
    }

    @Override
    public void initializeControls() {
        mDescription = mView.findViewById(R.id.mpsdkDescription);
        mIcon = mView.findViewById(R.id.mpsdkImage);
        mEditHint = mView.findViewById(R.id.mpsdkEditHint);
        mSeparator = mView.findViewById(R.id.mpsdkSeparator);
    }

    @Override
    public abstract View inflateInParent(ViewGroup parent, boolean attachToRoot);

    @Override
    public void showSeparator() {
        mSeparator.setVisibility(View.VISIBLE);
    }

    @Override
    public View getView() {
        return mView;
    }
}
