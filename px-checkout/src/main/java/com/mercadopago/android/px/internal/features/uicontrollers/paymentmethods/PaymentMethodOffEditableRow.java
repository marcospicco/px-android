package com.mercadopago.android.px.internal.features.uicontrollers.paymentmethods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.drawee.view.DraweeView;
import com.mercadolibre.android.ui.utils.facebook.fresco.FrescoImageController;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.ResourceUtil;
import com.mercadopago.android.px.internal.view.MPTextView;
import com.mercadopago.android.px.model.PaymentMethod;
import com.mercadopago.android.px.model.PaymentMethodSearchItem;

/**
 * Created by mreverter on 29/4/16.
 */
public class PaymentMethodOffEditableRow implements PaymentMethodViewController {

    private PaymentMethod mPaymentMethod;
    private PaymentMethodSearchItem mItem;
    private final Context mContext;
    private View mSeparator;
    private View mView;
    private MPTextView mDescription;
    private MPTextView mComment;
    private DraweeView mIcon;
    private View mEditHint;

    public PaymentMethodOffEditableRow(final Context context, final PaymentMethodSearchItem item) {
        mContext = context;
        mItem = item;
    }

    public PaymentMethodOffEditableRow(final Context context, final PaymentMethod paymentMethod) {
        mContext = context;
        mPaymentMethod = paymentMethod;
    }

    @Override
    public void draw() {
        if (mItem != null) {
            drawWithSearchItem();
        } else if (mPaymentMethod != null) {
            drawWithPaymentMethod();
        }
    }

    private void drawWithPaymentMethod() {

        if (mPaymentMethod.getAccreditationTime() == null) {
            mComment.setVisibility(View.GONE);
        } else {
            mComment
                .setText(ResourceUtil.getAccreditationTimeMessage(mContext, mPaymentMethod.getAccreditationTime()));
        }

        final int resourceId = ResourceUtil.getIconById(mContext, mPaymentMethod.getId());

        if (resourceId != 0) {
            FrescoImageController.create().load(resourceId).into(mIcon);
        } else {
            mIcon.setVisibility(View.GONE);
        }
    }

    private void drawWithSearchItem() {
        if (mItem.hasDescription()) {
            mDescription.setText(mItem.getDescription());
        }
        if (mItem.hasComment()) {
            mComment.setText(mItem.getComment());
        }
        int resourceId = 0;

        if (mItem.isIconRecommended()) {
            resourceId = ResourceUtil.getIconById(mContext, mItem.getId());
        }

        if (resourceId != 0) {
            FrescoImageController.create().load(resourceId).into(mIcon);
        } else {
            mIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        mEditHint.setVisibility(View.VISIBLE);
        mView.setOnClickListener(listener);
    }

    @Override
    public void initializeControls() {
        mComment = mView.findViewById(R.id.mpsdkComment);
        mDescription = mView.findViewById(R.id.mpsdkDescription);
        mIcon = mView.findViewById(R.id.mpsdkImage);
        mEditHint = mView.findViewById(R.id.mpsdkEditHint);
        mSeparator = mView.findViewById(R.id.mpsdkSeparator);

        mEditHint.setVisibility(View.GONE);
        mSeparator.setVisibility(View.GONE);
    }

    @Override
    public View inflateInParent(final ViewGroup parent, final boolean attachToRoot) {
        mView = LayoutInflater.from(mContext)
            .inflate(R.layout.px_row_payment_method_edit_large, parent, attachToRoot);
        return mView;
    }

    @Override
    public void showSeparator() {
        mSeparator.setVisibility(View.VISIBLE);
    }

    @Override
    public View getView() {
        return mView;
    }
}
