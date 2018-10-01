package com.mercadopago.android.px.internal.features.uicontrollers.paymentmethodsearch;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.drawee.view.DraweeView;
import com.mercadolibre.android.ui.utils.facebook.fresco.FrescoImageController;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.ResourceUtil;
import com.mercadopago.android.px.internal.view.MPTextView;
import com.mercadopago.android.px.model.CustomSearchItem;

/**
 * Created by mreverter on 10/25/16.
 */

public class PaymentMethodSearchCustomOption implements PaymentMethodSearchViewController {
    protected CustomSearchItem mItem;
    protected Context mContext;
    protected View mView;
    protected MPTextView mDescription;
    protected MPTextView mComment;
    protected DraweeView mIcon;
    protected View.OnClickListener mListener;

    public PaymentMethodSearchCustomOption(Context context, CustomSearchItem item) {
        mContext = context;
        mItem = item;
    }

    @Override
    public View inflateInParent(ViewGroup parent, boolean attachToRoot) {
        mView = LayoutInflater.from(mContext)
            .inflate(R.layout.px_row_pm_search_item, parent, attachToRoot);
        if (mListener != null) {
            mView.setOnClickListener(mListener);
        }
        return mView;
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public void initializeControls() {
        mDescription = mView.findViewById(R.id.mpsdkDescription);
        mComment = mView.findViewById(R.id.mpsdkComment);
        mIcon = mView.findViewById(R.id.mpsdkImage);
    }

    @Override
    public void draw() {

        mDescription.setText(mItem.getDescription());

        int resourceId = 0;

        if (!TextUtils.isEmpty(mItem.getPaymentMethodId())) {
            resourceId = ResourceUtil.getIconById(mContext, mItem.getPaymentMethodId());
        }

        if (resourceId != 0) {
            FrescoImageController.create().load(resourceId).into(mIcon);
        } else {
            mIcon.setVisibility(View.GONE);
        }

        mComment.setVisibility(View.GONE);
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        mListener = listener;
        if (mView != null) {
            mView.setOnClickListener(listener);
        }
    }
}
