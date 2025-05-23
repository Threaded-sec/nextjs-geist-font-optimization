package com.example.moviewatcher.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class CreditsManager {
    private static final String PREFS_NAME = "MovieWatcherPrefs";
    private static final String CREDITS_KEY = "user_credits";
    private static final int DEFAULT_CREDITS = 10;
    private static final int CREDITS_PER_AD = 5;

    private SharedPreferences prefs;
    private static CreditsManager instance;

    private CreditsManager(Context context) {
        prefs = context.getApplicationContext()
                      .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized CreditsManager getInstance(Context context) {
        if (instance == null) {
            instance = new CreditsManager(context);
        }
        return instance;
    }

    public int getCredits() {
        return prefs.getInt(CREDITS_KEY, DEFAULT_CREDITS);
    }

    public boolean hasEnoughCredits(int required) {
        return getCredits() >= required;
    }

    public boolean deductCredits(int amount) {
        int currentCredits = getCredits();
        if (currentCredits >= amount) {
            setCredits(currentCredits - amount);
            return true;
        }
        return false;
    }

    public void addCreditsForAd() {
        addCredits(CREDITS_PER_AD);
    }

    public void addCredits(int amount) {
        if (amount > 0) {
            int currentCredits = getCredits();
            setCredits(currentCredits + amount);
        }
    }

    private void setCredits(int credits) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(CREDITS_KEY, credits);
        editor.apply();
    }

    public void resetCredits() {
        setCredits(DEFAULT_CREDITS);
    }

    public boolean needsMoreCredits(int required) {
        return getCredits() < required;
    }

    public int getCreditsNeeded(int required) {
        return Math.max(0, required - getCredits());
    }
}
