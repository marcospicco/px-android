package com.mercadopago.android.px.internal.util.textformatter;

import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;

abstract class ChainFormatter {

    protected abstract Spannable apply(CharSequence charSequence);

}
