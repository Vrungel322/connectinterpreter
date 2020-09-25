package com.getbewarned.connectinterpreter;

import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

public class UiUtils {
    public static void actionMainScreen(View v) {
        UiUtils.shadow(v, 24, 1.2f, 0.5f);
    }

    public static void actionActionsScreen(View v) {
        UiUtils.shadow(v, 16, 1.1f, 0.5f);
    }

    /**
     * [v] need to have background some like
     * <shape xmlns:android="http://schemas.android.com/apk/res/android"
     * android:shape="oval"
     * >
     * <p>
     * <size
     * android:width="72dp"
     * android:height="72dp"
     * />
     * <solid android:color="@android:color/black" />
     *
     * </shape>
     *
     * @param v
     * @param elevation
     * @param shadowScaleFactor
     */
    public static void shadow(View v, int elevation, final float shadowScaleFactor, final float shadowAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            v.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int shadowW = (int) (view.getWidth() * shadowScaleFactor);
                    int shadowH = (int) (view.getHeight() * shadowScaleFactor);
                    int shadowX = (int) (view.getWidth() - shadowW);
                    int shadowY = (int) (view.getHeight() - shadowH);
                    outline.setOval(shadowX / 2, 0, shadowW, shadowH);
                    outline.setAlpha(shadowAlpha);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//                        if (startOffsetInPercent > 0) {
////                            outline.offset(shadowX, shadowY);
//                        }
//                    }
                }
            });
            v.setElevation(elevation);
        }
    }
}
