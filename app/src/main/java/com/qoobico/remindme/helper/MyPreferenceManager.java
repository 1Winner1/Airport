package com.qoobico.remindme.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.qoobico.remindme.model.User;

/**
 * Created by Lincoln on 07/01/16.
 */
public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME_USER = "airport_user";
    private static final String PREF_NAME_CODE = "code_user";

    // All Shared Preferences Keys
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NAME = "name";
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_PHONE = "phone";
    public static final String KEY_USER_IMAGE = "user_image";
    public static final String KEY_USER_POSITION = "position_name";
    public static final String KEY_USER_COST_PER = "cost_per_hour";
    public static final String KEY_USER_CODE_ID = "code_id";

    private static final String KEY_NOTIFICATIONS = "notifications";

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME_USER, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void storeUser(User user) {
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_PHONE, user.getPhone());
        editor.putString(KEY_USER_POSITION, user.getPosition());
        editor.putString(KEY_USER_IMAGE, user.getImageUser());
        editor.putString(KEY_USER_COST_PER, user.getCost());
        editor.putString(KEY_USER_CODE_ID, user.getCodeId());

        editor.commit();

        Log.e(TAG, "Пользователь сохранен. " + user.getName() + ", " + user.getEmail() + ", " + user.getPhone() + ", " + user.getPosition() + ", " + user.getImageUser());
    }

    public void storeCode(User user) {
        editor.putString(KEY_USER_CODE_ID, user.getCodeId());

        editor.commit();

        Log.e(TAG, "Пользователь сохранен." + user.getCodeId());
    }

    public User getCodeUser() {
        if (pref.getString(KEY_USER_CODE_ID, null) != null) {
            String codeId;

            codeId = pref.getString(KEY_USER_CODE_ID, null);

            User user = new User(codeId);
            return user;
        }
        return null;
    }

    public User getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id, name, email, phone, user_image, position, cost, codeId;
            id = pref.getString(KEY_USER_ID, null);
            name = pref.getString(KEY_USER_NAME, null);
            email = pref.getString(KEY_USER_EMAIL, null);
            phone = pref.getString(KEY_USER_PHONE, null);
            position = pref.getString(KEY_USER_POSITION, null);
            user_image = pref.getString(KEY_USER_IMAGE, null);
            cost = pref.getString(KEY_USER_COST_PER, null);
            codeId = pref.getString(KEY_USER_CODE_ID, null);

            User user = new User(id, name, email, phone, position, user_image, cost, codeId);
            return user;
        }
        return null;
    }

    public void addNotification(String notification) {

        // get old notifications
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}
