package com.weekcalendar.utils;

import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by Asi on 19/01/2016.
 */
public class ViewUtils {

    public static void setTextType(int textStyle, TextView... textViews) {
        for (TextView tv : textViews) {
            tv.setTypeface(tv.getTypeface(), textStyle);
        }
    }

    public static void setTextSize(int textSize, TextView... textViews) {
        for (TextView tv : textViews) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }
}
