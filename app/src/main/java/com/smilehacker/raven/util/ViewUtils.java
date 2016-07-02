package com.smilehacker.raven.util;

import android.content.res.Resources;

/**
 * Created by kleist on 14/10/25.
 */
public class ViewUtils {

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}
