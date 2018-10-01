package com.mercadopago.android.px.internal.features.uicontrollers.issuers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.drawee.view.DraweeView;
import com.mercadolibre.android.ui.utils.facebook.fresco.FrescoImageController;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.ResourceUtil;
import com.mercadopago.android.px.internal.view.MPTextView;
import com.mercadopago.android.px.model.Issuer;

/**
 * Created by vaserber on 10/11/16.
 */

public class IssuersView implements IssuersViewController {



    private final Context mContext;
    private View mView;
    private DraweeView mIssuerImageView;
    private MPTextView mIssuerTextView;

    public IssuersView(final Context context) {
        mContext = context;
    }

    @Override
    public void initializeControls() {
        mIssuerImageView = mView.findViewById(R.id.mpsdkIssuerImageView);
        mIssuerTextView = mView.findViewById(R.id.mpsdkIssuerTextView);
    }

    @Override
    public View inflateInParent(final ViewGroup parent, final boolean attachToRoot) {
        mView = LayoutInflater.from(mContext)
            .inflate(R.layout.px_view_issuer, parent, attachToRoot);
        return mView;
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public void setOnClickListener(final View.OnClickListener listener) {
        mView.setOnClickListener(listener);
    }

    @Override
    public void drawIssuer(final Issuer issuer) {
        final int image = ResourceUtil.getCardImage(mContext, issuer);
        if (image == 0) {
            mIssuerImageView.setVisibility(View.GONE);
            mIssuerTextView.setVisibility(View.VISIBLE);
            mIssuerTextView.setText(issuer.getName());
        } else {
            mIssuerImageView.setVisibility(View.VISIBLE);
            mIssuerTextView.setVisibility(View.GONE);
            FrescoImageController.create().load(image).into(mIssuerImageView);
        }
    }
}
