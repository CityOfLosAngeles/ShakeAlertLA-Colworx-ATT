package com.app.shakealertla.Utils;

import android.content.SharedPreferences;

import com.app.shakealertla.ShakeAlertLA;

//
//
//import android.content.SharedPreferences;
//import android.support.annotation.Nullable;
//
//import com.app.eatyoulike.EatYouLike;
//import com.app.eatyoulike.Models.User;
//

// Colworx : Shared Preference Manager class for String data locally based on Key, Value pairs
public class SharedPreferenceManager {
    private static String AppConfig = "ShakeAlertConfig";
    private static SharedPreferences.Editor preferences = ShakeAlertLA.getContext().getSharedPreferences(AppConfig, 0).edit();
    private static SharedPreferences.Editor locationPrefs = ShakeAlertLA.getContext().getSharedPreferences("Location", 0).edit();
    private static SharedPreferenceManager manager;

    ////    private static String name, email, phone, address, image, city, postcode, country;
////    private static long userId;
////    private static double latitude,longitude;
//
    public static SharedPreferenceManager getInstance() {
        if (manager == null)
            manager = new SharedPreferenceManager();
        return manager;
    }

    private static String showTour = "ShowTour";

    public void showTour(boolean On) {
        preferences.putBoolean(showTour, On);
        preferences.commit();
    }

    public static boolean shouldShowTour() {
        SharedPreferences sharedPreferences = ShakeAlertLA.getContext().getSharedPreferences(AppConfig, 0);
        return sharedPreferences.getBoolean(showTour, true);
    }

    private static String language = "Language";

    public void setLanguage(String languageConstant) {
        preferences.putString(language, languageConstant);
        preferences.commit();
    }

    public static String getLanguage() {
        SharedPreferences sharedPreferences = ShakeAlertLA.getContext().getSharedPreferences(AppConfig, 0);
        return sharedPreferences.getString(language, null);
    }

    private static String showNotification = "ShowNotification";

    public void setShowNotification(boolean On) {
        preferences.putBoolean(SharedPreferenceManager.showNotification, On);
        preferences.commit();
    }

    public static boolean shouldShowNotification() {
        SharedPreferences sharedPreferences = ShakeAlertLA.getContext().getSharedPreferences(AppConfig, 0);
        return sharedPreferences.getBoolean(showNotification, true);
//        return false;//taodo disable notification temporary
    }

//    public void clearDeliveryPrefs() {
//        locationPrefs.remove(Constants.ISDELIVERYALLOWED);
//        locationPrefs.commit();
//    }

    public static double getLatitude() {
        SharedPreferences sharedPreferences = ShakeAlertLA.getContext().getSharedPreferences("Location", 0);
        return Double.valueOf(sharedPreferences.getString(Constants.LATITUDE, "0"));
    }

    public static double getLongitude() {
        SharedPreferences sharedPreferences = ShakeAlertLA.getContext().getSharedPreferences("Location", 0);
        return Double.valueOf(sharedPreferences.getString(Constants.LONGITUDE, "0"));
    }

    public void saveLocation(double latitude, double longitude) {
        locationPrefs.putString(Constants.LATITUDE, String.valueOf(latitude));
        locationPrefs.putString(Constants.LONGITUDE, String.valueOf(longitude));
        locationPrefs.commit();
    }

    public void clearLocation() {
        locationPrefs.remove(Constants.LATITUDE);
        locationPrefs.remove(Constants.LONGITUDE);
        locationPrefs.commit();
    }

    //    public void save(User user) {
//        preferences.putLong(Constants.USER_ID,user.getUserID());
//        preferences.putString(Constants.EMAIL, user.getEmail());
//        preferences.putString(Constants.NAME, user.getName());
//        preferences.putString(Constants.PHONE, user.getPhone());
//        preferences.putString(Constants.ADDRESS, user.getAddress());
//        preferences.putString(Constants.PROFILE_IMAGE, user.getProfileImg());
//        preferences.putString(Constants.CITY, user.getCity());
//        preferences.putString(Constants.POST_CODE, user.getPostcode());
//        preferences.putString(Constants.COUNTRY, user.getCountry_name());
//        preferences.commit();
//    }
//
//    //    clear user data
//    public void clearUser(){
//        preferences.remove(Constants.USER_ID);
//        preferences.remove(Constants.EMAIL);
//        preferences.remove(Constants.NAME);
//        preferences.remove(Constants.PHONE);
//        preferences.remove(Constants.ADDRESS);
//        preferences.remove(Constants.PROFILE_IMAGE);
//        preferences.remove(Constants.CITY);
//        preferences.remove(Constants.POST_CODE);
//        preferences.remove(Constants.COUNTRY);
//
//        locationPrefs.remove(Constants.LATITUDE);
//        locationPrefs.remove(Constants.LONGITUDE);
//        preferences.commit();
//        locationPrefs.commit();
//    }
//
//    public void clear() {
//        preferences.clear();
//        preferences.commit();
//        locationPrefs.clear();
//        locationPrefs.commit();
//    }
//
//    public User getUser() {
//        User user = new User();
//        SharedPreferences sharedPreferences = EatYouLike.getContext().getSharedPreferences("USER", 0);
//
//        long userID = sharedPreferences.getLong(Constants.USER_ID,0);
//        String email = sharedPreferences.getString(Constants.EMAIL, "");
//        String name = sharedPreferences.getString(Constants.NAME, "");
//        String phone = sharedPreferences.getString(Constants.PHONE, "");
//        String address = sharedPreferences.getString(Constants.ADDRESS, "");
//        String image = sharedPreferences.getString(Constants.PROFILE_IMAGE, "");
//        String city = sharedPreferences.getString(Constants.CITY, "");
//        String postcode = sharedPreferences.getString(Constants.POST_CODE, "");
//        String country = sharedPreferences.getString(Constants.COUNTRY, "");
//
////        if (name.length() == 0
////                || email.length() == 0
////                || name.length() == 0
////                || phone.length() == 0
////                || address.length() == 0
////                || image.length() == 0) {
////            return null;
////        }
//
////        SharedPreferenceManager.userId = userID;
////        SharedPreferenceManager.email = email;
////        SharedPreferenceManager.name = name;
////        SharedPreferenceManager.phone = phone;
////        SharedPreferenceManager.address = address;
////        SharedPreferenceManager.image = image;
////        SharedPreferenceManager.city = city;
////        SharedPreferenceManager.postcode = postcode;
////        SharedPreferenceManager.country = country;
//
//        user.setUserID(userID);
//        user.setEmail(email);
//        user.setName(name);
//        user.setPhone(phone);
//        user.setAddress(address);
//        user.setProfileImg(image);
//        user.setCity(city);
//        user.setPostcode(postcode);
//        user.setCountry_name(country);
//
//        return user;
//    }
//
    private static final class Constants {
        static final String CART_TUT = "cartTut",
                EMAIL = "email",
                NAME = "firstName",
                PHONE = "lastName",
                ADDRESS = "lastLogin",
                USER_ID = "userId",
                PROFILE_IMAGE = "image",
                CITY = "city",
                POST_CODE = "postcode",
                COUNTRY = "country",
                LATITUDE = "latitude",
                LONGITUDE = "longitude",
                ISDELIVERYALLOWED = "deliveryoption";
    }
}
