package com.getbewarned.connectinterpreter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Outline;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import javax.annotation.Nullable;

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

    public static void showAnimated(final View v, Techniques technique, Long duration) {
        YoYo.with(technique)
                .duration(duration)
                .onStart(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        v.setVisibility(View.VISIBLE);

                    }
                })
                .playOn(v);
    }

    public static void hideAnimated(final View v, Techniques technique, Long duration) {
        YoYo.with(technique)
                .duration(duration)
                .onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        v.setVisibility(View.GONE);

                    }
                })
                .playOn(v);
    }

    public static void rotateAnimated(final View v, int fromAngle, int toAngle, Long duration) {
        int centerX = v.getMeasuredWidth() / 2;
        int centerY = v.getMeasuredHeight() / 2;
        ObjectAnimator animation = ObjectAnimator.ofFloat(v, "rotation", fromAngle, toAngle);
        animation.setDuration(duration);
        v.setPivotX(centerX);
        v.setPivotY(centerY);
        animation.setInterpolator(new LinearInterpolator());
        animation.start();

    }

    public static void scaleToCenterAnimated(final View v, float fromScale, float toScale, Long duration) {
        Animation anim = new ScaleAnimation(
                fromScale, toScale, // Start and end values for the X axis scaling
                fromScale, toScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(duration);
        v.startAnimation(anim);
    }

    public static void hideKeyboard(@Nullable View currentFocus) {
        if (currentFocus != null) {
            ((InputMethodManager) currentFocus.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    public static void clickSubText(TextView view, final String clickableText, final ClickableSpan listener) {

        CharSequence text = view.getText();
        String string = text.toString();

        int start = string.indexOf(clickableText);
        int end = start + clickableText.length();
        if (start == -1) return;

        if (text instanceof Spannable) {
            ((Spannable)text).setSpan(listener, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            SpannableString s = SpannableString.valueOf(text);
            s.setSpan(listener, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            view.setText(s);
        }

        MovementMethod m = view.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            view.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}