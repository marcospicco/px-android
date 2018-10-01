package com.mercadopago.android.px.internal.util;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.core.PaymentMethodPlugin;
import com.mercadopago.android.px.internal.di.Session;
import com.mercadopago.android.px.model.Bin;
import com.mercadopago.android.px.model.Issuer;
import com.mercadopago.android.px.model.PaymentMethod;
import com.mercadopago.android.px.model.exceptions.BinException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ResourceUtil {

    private static final String LIB_PREFIX = "px_";
    private static final String DEF_TYPE_DRAWABLE = "drawable";
    public static final String TINT_PREFIX = "grey_";
    private static final String CARD_IMAGE_PREFIX = "px_issuer_";
    private static final String CARD_ICO = "px_ico_card_";

    /**
     * This object represents mapped resources.
     * The reason behind this map is to simplify resource
     * mapping that already exists. Also it is necessary because webP resources
     * can be accessed through xml resources like this:
     * px_banamex == px_banamex_atm == px_banamex_bank_transfer
     * but is not possible to load resources like px_banamex_atm.xml:
     * <bitmap xmlns:android="http://schemas.android.com/apk/res/android" android:src="@drawable/px_banamex"/>
     */
    private static final Map<String, Integer> RESOURCE_MAP = new HashMap<>();

    private static final String BANAMEX = "banamex";
    private static final String BANCOMER = "bancomer";
    private static final String REDLINK = "redlink";
    private static final String SERFIN = "serfin";
    private static final String PREPAID_CARD = "prepaid_card";  // TODO CHECK USAGE

    private static final String CABAL = "cabal";
    private static final String CENCOSUD = "cencosud";

    private static final String ATM_BBVA = "pagoefectivo_atm_bbva_continental";
    private static final String ATM_BCP = "pagoefectivo_atm_bcp";
    private static final String ATM_INTERBANK = "pagoefectivo_atm_interbank";
    private static final String ATM_SCOTIABANK = "pagoefectivo_atm_scotiabank";

    private static final String BOLBRADESCO = "bolbradesco";
    private static final String CREDIT_CARD = "credit_card";

    private static final String GREY_BOLBRADESCO = "grey_bolbradesco";  // TODO CHECK USAGE
    private static final String GREY_CREDIT_CARD = "grey_credit_card";  // TODO CHECK USAGE
    private static final String GREY_PREPAID_CARD = "grey_prepaid_card";  // TODO CHECK USAGE

    //TODO add issuers
    //TODO ICO tarjetas

    static {
        RESOURCE_MAP.put(BANAMEX, R.drawable.px_banamex);
        RESOURCE_MAP.put(BANCOMER, R.drawable.px_bancomer);
        RESOURCE_MAP.put(BOLBRADESCO, R.drawable.px_bolbradesco);
        RESOURCE_MAP.put(REDLINK, R.drawable.px_redlink);
        RESOURCE_MAP.put(SERFIN, R.drawable.px_serfin);
        RESOURCE_MAP.put(CREDIT_CARD, R.drawable.px_cards);
        RESOURCE_MAP.put(PREPAID_CARD, R.drawable.px_debit_card);

        RESOURCE_MAP.put(GREY_BOLBRADESCO, R.drawable.px_grey_bolbradesco);
        RESOURCE_MAP.put(GREY_CREDIT_CARD, R.drawable.px_grey_cards);
        RESOURCE_MAP.put(GREY_PREPAID_CARD, R.drawable.px_grey_debit_card);

        RESOURCE_MAP.put(ATM_BBVA, R.drawable.px_pagoefectivo_atm_bbva_continental);
        RESOURCE_MAP.put(ATM_BCP, R.drawable.px_pagoefectivo_atm_bcp);
        RESOURCE_MAP.put(ATM_INTERBANK, R.drawable.px_pagoefectivo_atm_interbank);
        RESOURCE_MAP.put(ATM_SCOTIABANK, R.drawable.px_pagoefectivo_atm_scotiabank);

        RESOURCE_MAP.put(CABAL, R.drawable.px_cabal);
        RESOURCE_MAP.put(CENCOSUD, R.drawable.px_cencosud);
    }

    private ResourceUtil() {
        // do nothing
    }

    @DrawableRes
    public static int getCardImage(@NonNull final Context context, @NonNull final PaymentMethod paymentMethod) {
        final String imageName = CARD_ICO + paymentMethod.getId().toLowerCase();
        return context.getResources().getIdentifier(imageName, DEF_TYPE_DRAWABLE, context.getPackageName());
    }

    @DrawableRes
    public static int getCardImage(@NonNull final Context context, @NonNull final Issuer issuer) {
        final String imageName = CARD_IMAGE_PREFIX + String.valueOf(issuer.getId());
        return context.getResources().getIdentifier(imageName, DEF_TYPE_DRAWABLE, context.getPackageName());
    }

    @DrawableRes
    public static int getIconById(final Context context, final String id) {
        if (TextUtil.isNotEmpty(id)) {
            final int resource =
                context.getResources().getIdentifier(LIB_PREFIX + id, DEF_TYPE_DRAWABLE, context.getPackageName());

            // or by bits, if 0 then new value != 0 will be placed.
            if (resource != 0) {
                return resource;
            } else {
                //TODO improve to access in O(1) - can be done knowing all types and have them mapped.
                final Set<Map.Entry<String, Integer>> entries = RESOURCE_MAP.entrySet();
                for (final Map.Entry<String, Integer> entry : entries) {
                    if (id.startsWith(entry.getKey())) {
                        return entry.getValue();
                    }
                }
            }
        }

        return R.drawable.px_none;
    }

    @DrawableRes
    public static int getIconResource(final Context context, final String id) {
        try {
            final PaymentMethodPlugin paymentMethodPlugin =
                Session.getSession(context).getPluginRepository().getPlugin(id);
            return paymentMethodPlugin.getPaymentMethodInfo(context).icon;
        } catch (final Exception e) {
            return getIconById(context, id);
        }
    }

    public static String getAccreditationTimeMessage(final Context context, final int milliseconds) {

        String accreditationMessage;

        if (milliseconds == 0) {
            accreditationMessage = context.getString(R.string.px_instant_accreditation_time);
        } else {
            StringBuilder accreditationTimeMessageBuilder = new StringBuilder();
            if (milliseconds > 1440 && milliseconds < 2880) {

                accreditationTimeMessageBuilder.append(context.getString(R.string.px_accreditation_time));
                accreditationTimeMessageBuilder.append(" 1 ");
                accreditationTimeMessageBuilder.append(context.getString(R.string.px_working_day));
            } else if (milliseconds < 1440) {

                accreditationTimeMessageBuilder.append(context.getString(R.string.px_accreditation_time));
                accreditationTimeMessageBuilder.append(" ");
                accreditationTimeMessageBuilder.append(milliseconds / 60);
                accreditationTimeMessageBuilder.append(" ");
                accreditationTimeMessageBuilder.append(context.getString(R.string.px_hour));
            } else {

                accreditationTimeMessageBuilder.append(context.getString(R.string.px_accreditation_time));
                accreditationTimeMessageBuilder.append(" ");
                accreditationTimeMessageBuilder.append(milliseconds / (60 * 24));
                accreditationTimeMessageBuilder.append(" ");
                accreditationTimeMessageBuilder.append(context.getString(R.string.px_working_days));
            }
            accreditationMessage = accreditationTimeMessageBuilder.toString();
        }
        return accreditationMessage;
    }

    public static List<PaymentMethod> getValidPaymentMethodsForBin(final String bin,
        final Iterable<PaymentMethod> paymentMethods) {
        if (bin.length() == Bin.BIN_LENGTH) {
            final List<PaymentMethod> validPaymentMethods = new ArrayList<>();
            for (final PaymentMethod pm : paymentMethods) {
                if (pm.isValidForBin(bin)) {
                    validPaymentMethods.add(pm);
                }
            }
            return validPaymentMethods;
        }

        throw new BinException(bin.length());
    }
}
