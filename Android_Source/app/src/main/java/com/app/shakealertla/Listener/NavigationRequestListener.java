package com.app.shakealertla.Listener;

import android.content.Intent;
import android.support.v4.app.Fragment;

public interface NavigationRequestListener {

    void onReplaceFragment(int containerId, Fragment fragment, boolean addToBackStack);

    void onAddFragment(int containerId, Fragment fragment, boolean addToBackStack);

    void onStartActivity(Intent intent);

    void onGoBack();

    //void showDialogFragment(BaseDialogFragment dialogFragment);
}
