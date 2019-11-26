package com.app.shakealertla.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtils {

    private static final String TAG = "FragmentUtils";

    public static void commitFragment(FragmentManager fragmentManager, int containerId,
                                      Fragment fragment, boolean addToBackStack) {
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        final String tag = fragment.getClass().getSimpleName();
        transaction.replace(containerId, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
        showStackLog(fragmentManager);
    }

    public static void addFragment(FragmentManager fragmentManager, int containerId,
                                   Fragment fragment, boolean addToBackStack) {
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        final String tag = fragment.getClass().getSimpleName();
        transaction.add(containerId, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
        showStackLog(fragmentManager);
    }


    public static Fragment getFragmentByTag(FragmentManager fragmentManager, Class<?> fragment) {
        return fragmentManager.findFragmentByTag(fragment.getSimpleName());
    }

    public static void showStackLog(FragmentManager fragmentManager){
        String logMsg = "BackStack was changed. Count " + fragmentManager.getBackStackEntryCount() + "\n";
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); i++){
            logMsg += fragmentManager.getBackStackEntryAt(i).getName() + "\n";
        }
        logMsg +="---------------------------------------------------------- \n\n";
        AppLog.v(TAG,logMsg);

    }
}
