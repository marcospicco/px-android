package com.mercadopago.android.px.internal.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.repository.DiscountRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.util.textformatter.AmountFormatter;
import com.mercadopago.android.px.internal.util.textformatter.CurrencyFormatter;
import com.mercadopago.android.px.internal.util.textformatter.TextFormatter;
import com.mercadopago.android.px.model.CardPaymentMetadata;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import java.math.BigDecimal;

public class InstallmentsDescriptorView extends MPTextView {

    public InstallmentsDescriptorView(final Context context) {
        this(context, null);
    }

    public InstallmentsDescriptorView(final Context context,
        @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InstallmentsDescriptorView(final Context context, @Nullable final AttributeSet attrs,
        final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    public void update(@NonNull final Model model) {
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        updateInstallmentsDescription(model, spannableStringBuilder);
        updateTotalAmountDescription(model, spannableStringBuilder);
        updateInterestDescription(model, spannableStringBuilder);
        updateCFT(model, spannableStringBuilder);
        setText(spannableStringBuilder);
    }
    //TODO pasar todo lo del spannable a una factory como la de CurrencyFormatter

    private void updateInstallmentsDescription(@NonNull final Model model,
        @NonNull final SpannableStringBuilder spannableStringBuilder) {
        final CurrencyFormatter currencyFormatter = TextFormatter.withCurrencyId(model.getCurrencyId());

        final AmountFormatter amountFormatter = model.hasMultipleInstallments() ?
            currencyFormatter.amount(model.getPayerCost().getInstallmentAmount()) :
            currencyFormatter.amount(model.getTotalAmount());

        final String installmentAmount = model.getPayerCost().getInstallments().toString();
        final String x = getContext().getString(R.string.px_installments_by);
        final String separator = " ";
        final CharSequence amount = amountFormatter
            .smallDecimals()
            .into(this)
            .toSpannable();
        final int firstElementLength = installmentAmount.length() + x.length() + separator.length() + amount.length();
        spannableStringBuilder.append(installmentAmount).append(x).append(separator).append(amount);

        final ForegroundColorSpan installmentsDescriptionColor =
            new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.ui_meli_black));
        //TODO arreglar el style, en esta parte tiene que ser semi bold
        final StyleSpan installmentsDescriptionStyle = new StyleSpan(android.graphics.Typeface.BOLD);
        spannableStringBuilder
            .setSpan(installmentsDescriptionColor, 0, firstElementLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    }

    private void updateInterestDescription(@NonNull final Model model,
        @NonNull final SpannableStringBuilder spannableStringBuilder) {

        if (model.hasMultipleInstallments() &&
            BigDecimal.ZERO.compareTo(model.getPayerCost().getInstallmentRate()) == 0) {
            final int initialIndex = spannableStringBuilder.length();
            final String separator = " ";
            final String description = getContext().getString(R.string.px_zero_rate);
            spannableStringBuilder.append(separator);
            spannableStringBuilder.append(description);
            final int endIndex = separator.length() + description.length();
            final ForegroundColorSpan installmentsDescriptionColor =
                new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.px_discount_description));
            spannableStringBuilder
                .setSpan(installmentsDescriptionColor, initialIndex, initialIndex + endIndex,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        //TODO preguntar: si hay una única cuota seleccionada, igual se muestra el "sin interes"?

    }

    private void updateTotalAmountDescription(@NonNull final Model model,
        @NonNull final SpannableStringBuilder spannableStringBuilder) {

        if (BigDecimal.ZERO.compareTo(model.getPayerCost().getInstallmentRate()) < 0) {
            //Si hay intereses
            final CharSequence totalAmount = TextFormatter.withCurrencyId(model.getCurrencyId())
                .amount(model.getPayerCost().getTotalAmount())
                .normalDecimals()
                .into(this)
                //TODO no está funcionando el holder, no se muestran los paréntesis
                .holder(R.string.px_total_amount_holder)
                .toSpannable();
            final int initialIndex = spannableStringBuilder.length();
            final String separator = " ";
            spannableStringBuilder.append(separator);
            spannableStringBuilder.append(totalAmount);
            final int endIndex = separator.length() + totalAmount.length();
            final ForegroundColorSpan installmentsDescriptionColor =
                new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.ui_meli_grey));
            spannableStringBuilder
                .setSpan(installmentsDescriptionColor, initialIndex, initialIndex + endIndex,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
    }

    private void updateCFT(@NonNull final Model model,
        @NonNull final SpannableStringBuilder spannableStringBuilder) {
        final int initialIndex = spannableStringBuilder.length();
        final String cftDescription =
            getContext().getString(R.string.px_installments_cft, model.getPayerCost().getCFTPercent());
        final String separator = " ";
        spannableStringBuilder.append(separator);
        spannableStringBuilder.append(cftDescription);
        final int endIndex = separator.length() + cftDescription.length();
        final ForegroundColorSpan installmentsDescriptionColor =
            new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.ui_meli_grey));
        spannableStringBuilder
            .setSpan(installmentsDescriptionColor, initialIndex, initialIndex + endIndex,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    }

    //TODO falta pasar el payment type, o hacer un modelo abstracto, al cual le podamos pedir los spannables
    //TODO que haya dos implementaciones del modelo, uno para debito y otro para credito, entonces devuelven los
    //TODO textos según la logica de cada uno.
    public static final class Model {
        private final String currencyId;
        private final PayerCost payerCost;
        private final BigDecimal totalAmount;

        public static Model createFrom(@NonNull final PaymentSettingRepository configuration,
            @NonNull final CardPaymentMetadata card) {
            final CheckoutPreference checkoutPreference = configuration.getCheckoutPreference();
            final PayerCost payerCost = card.getAutoSelectedInstallment();
            final String currencyId = checkoutPreference.getSite().getCurrencyId();
            final BigDecimal totalAmount = checkoutPreference.getTotalAmount();
            return new Model(currencyId, payerCost, totalAmount);
        }

        private Model(@NonNull final String currencyId, @NonNull final PayerCost payerCost,
            @NonNull final BigDecimal totalAmount) {
            this.currencyId = currencyId;
            this.payerCost = payerCost;
            this.totalAmount = totalAmount;
        }

        public String getCurrencyId() {
            return currencyId;
        }

        public PayerCost getPayerCost() {
            payerCost.setInstallmentRate(new BigDecimal(10));
            payerCost.setTotalAmount(new BigDecimal(400.90));
            payerCost.setInstallments(3);
            return payerCost;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        boolean hasMultipleInstallments() {
            return payerCost.getInstallments() > 1;
        }
    }
}
