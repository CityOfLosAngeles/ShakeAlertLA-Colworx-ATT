package com.app.shakealertla.Utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.animation.OvershootInterpolator;

import com.github.clans.fab.FloatingActionMenu;

public class AnimUtils {
    // Colworx : Floating menu button Animation in Recent Earthquakes screen
    public static AnimatorSet getFloatingMenuAnimation(FloatingActionMenu floatingActionMenu){
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(floatingActionMenu.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(floatingActionMenu.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(floatingActionMenu.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(floatingActionMenu.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        return set;
    }
}
