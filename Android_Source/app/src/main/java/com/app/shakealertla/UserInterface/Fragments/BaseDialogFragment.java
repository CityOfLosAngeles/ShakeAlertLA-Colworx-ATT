package com.app.shakealertla.UserInterface.Fragments;

import android.support.v4.app.DialogFragment;
import android.view.View;


public abstract class BaseDialogFragment extends DialogFragment {

    private static final String TAG = "BaseDialogFragment";

    public void setupComponents(View rootView) {

        initializeComponents(rootView);
        setupListeners(rootView);
    }

    public abstract void initializeComponents(View rootView);

    public abstract void setupListeners(View rootView);

}
