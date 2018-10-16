package com.mercadopago.android.px.internal.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mercadolibre.android.ui.font.Font;
import com.mercadolibre.android.ui.font.TypefaceHelper;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.CurrenciesUtil;
import java.math.BigDecimal;

public class AmountDescriptorView extends LinearLayout {

    private TextView label;
    private TextView amount;

    public AmountDescriptorView(final Context context) {

        this(context, null);
    }

    public AmountDescriptorView(final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AmountDescriptorView(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        final TypedArray a = context.getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.PXAmountDescriptorView,
            0, 0);
        String amountStyle;
        try {
            amountStyle = a.getString(R.styleable.PXAmountDescriptorView_px_amount_descriptor_style);
            if (amountStyle == null) {
                amountStyle = AmountStyle.NORMAL.style;
            }
        } finally {
            a.recycle();
        }
        init(amountStyle);
    }

    public void setAmountDescriptorStyle(@NonNull final AmountStyle amountStyle) {
        if (AmountStyle.BOLD == amountStyle) {
            final Resources resources = getResources();
            final float textSize = resources.getDimensionPixelSize(R.dimen.px_m_text);
            final int textColor = resources.getColor(R.color.ui_meli_black);
            label.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            amount.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            label.setTextColor(textColor);
            amount.setTextColor(textColor);
            //TODO set semibold font to amount
//            amount.setTextAppearance(getContext(), R.style.MLFont);
            TypefaceHelper.setTypeface(amount, Font.SEMI_BOLD);

//            android:textAppearance="@style/MLFont.Bold.Semi"
        }
        //TODO ver si hacemos el else con el normal
    }

    private void init(@NonNull final String amountStyle) {
        inflate(getContext(), R.layout.px_view_amount_descriptor, this);
        label = findViewById(R.id.label);
        amount = findViewById(R.id.amount);
        final AmountStyle style = AmountStyle.BOLD.style.equals(amountStyle) ? AmountStyle.BOLD : AmountStyle.NORMAL;
        setAmountDescriptorStyle(style);
    }

    public void update(@NonNull final AmountDescriptorView.Model model) {
        label.setText(model.getAmountDescription());

        final StringBuilder amountBuilder = new StringBuilder();
        if (AmountType.NEGATIVE == model.getAmountType()) {
            amountBuilder.append("-");
            final int color = getResources().getColor(R.color.px_discount_description);
            label.setTextColor(color);
            amount.setTextColor(color);
            //TODO desde el layout setear el color default gris
            //TODO ver si hacemos el else con el POSITIVE
        }

        final Spanned spannedAmount =
            CurrenciesUtil.getSpannedAmountWithCurrencySymbol(model.getAmount(), model.getCurrencyId());
        amount.setText(TextUtils.concat(amountBuilder, spannedAmount));
    }

    public static class Model {

        @NonNull private final AmountType amountType;
        @NonNull private AmountStyle amountStyle;
        @NonNull private final String amountDescription;
        @NonNull private final String currencyId;
        @NonNull private final BigDecimal amount;

        public Model(@NonNull final AmountType amountType, @NonNull final String amountDescription,
            @NonNull final String currencyId, @NonNull final BigDecimal amount) {
            this.amountType = amountType;
            this.amountDescription = amountDescription;
            this.currencyId = currencyId;
            this.amount = amount;
            amountStyle = AmountStyle.NORMAL;
        }

        public void setAmountStyle(@NonNull final AmountStyle amountStyle) {
            this.amountStyle = amountStyle;
        }

        @NonNull
        protected String getAmountDescription() {
            return amountDescription;
        }

        @NonNull
        protected AmountType getAmountType() {
            return amountType;
        }

        @NonNull
        protected String getCurrencyId() {
            return currencyId;
        }

        @NonNull
        protected BigDecimal getAmount() {
            return amount;
        }

        @NonNull
        public AmountStyle getAmountStyle() {
            return amountStyle;
        }
    }

    public enum AmountType {NEGATIVE, POSITIVE}

    public enum AmountStyle {

        BOLD("bold"), NORMAL("normal");

        public final String style;

        AmountStyle(final String style) {
            this.style = style;
        }
    }
}
