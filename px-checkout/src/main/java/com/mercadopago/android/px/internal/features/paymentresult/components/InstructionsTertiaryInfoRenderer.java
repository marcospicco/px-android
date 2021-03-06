package com.mercadopago.android.px.internal.features.paymentresult.components;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.view.MPTextView;
import com.mercadopago.android.px.internal.view.Renderer;
import java.util.List;

/**
 * Created by vaserber on 11/14/17.
 */

public class InstructionsTertiaryInfoRenderer extends Renderer<InstructionsTertiaryInfo> {

    @Override
    public View render(final InstructionsTertiaryInfo component, final Context context, final ViewGroup parent) {
        final View secondaryInfoView = inflate(R.layout.px_payment_result_instructions_tertiary_info, parent);
        final MPTextView secondaryInfoTextView = secondaryInfoView.findViewById(R.id.msdpkTertiaryInfo);

        setText(secondaryInfoTextView, getTertiaryInfoText(component.props.tertiaryInfo));
        return secondaryInfoView;
    }

    private String getTertiaryInfoText(List<String> tertiaryInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < tertiaryInfo.size(); i++) {
            stringBuilder.append(tertiaryInfo.get(i));
            if (i != tertiaryInfo.size() - 1) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
