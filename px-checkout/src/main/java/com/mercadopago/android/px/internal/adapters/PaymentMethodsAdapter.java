package com.mercadopago.android.px.internal.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.view.DraweeView;
import com.mercadolibre.android.ui.utils.facebook.fresco.FrescoImageController;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.MercadoPagoUtil;
import com.mercadopago.android.px.model.PaymentMethod;
import java.util.List;

public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsAdapter.ViewHolder> {

    private final Activity mActivity;
    private final List<PaymentMethod> mData;
    private View.OnClickListener mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public DraweeView mPaymentMethodImage;
        public TextView mPaymentMethodName;

        public ViewHolder(View v, View.OnClickListener listener) {

            super(v);
            mPaymentMethodImage = v.findViewById(R.id.mpsdkPmImage);
            mPaymentMethodName = v.findViewById(R.id.mpsdkPmName);
            if (listener != null) {
                v.setOnClickListener(listener);
            }
        }
    }

    public PaymentMethodsAdapter(Activity activity, List<PaymentMethod> data, View.OnClickListener listener) {

        mActivity = activity;
        mData = data;
        mListener = listener;
    }

    @Override
    public PaymentMethodsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
        int viewType) {

        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.px_row_simple_list, parent, false);

        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        PaymentMethod paymentMethod = mData.get(position);

        FrescoImageController.create().load(MercadoPagoUtil.getPaymentMethodIcon(mActivity, paymentMethod.getId()))
            .into(holder.mPaymentMethodImage);

        holder.mPaymentMethodName.setText(paymentMethod.getName());
        // Set view tag item
        holder.itemView.setTag(paymentMethod);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public View.OnClickListener getListener() {
        return mListener;
    }
}