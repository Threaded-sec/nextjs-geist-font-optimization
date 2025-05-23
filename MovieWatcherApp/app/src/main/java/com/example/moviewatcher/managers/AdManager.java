package com.example.moviewatcher.managers;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;

public class AdManager {
    private static final String REWARDED_AD_UNIT_ID = "ca-app-pub-xxxxxxxxxxxxxxxx/yyyyyyyyyy"; // Replace with actual ad unit ID
    private static AdManager instance;
    private RewardedAd rewardedAd;
    private boolean isLoading = false;

    public interface AdCallback {
        void onAdRewarded();
        void onAdFailedToLoad(String error);
        void onAdDismissed();
    }

    private AdManager() {}

    public static synchronized AdManager getInstance() {
        if (instance == null) {
            instance = new AdManager();
        }
        return instance;
    }

    public void initialize(Context context) {
        MobileAds.initialize(context, initializationStatus -> {
            // Initialization completed
            loadRewardedAd(context);
        });
    }

    public void loadRewardedAd(Context context) {
        if (isLoading) return;
        
        isLoading = true;
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(context, REWARDED_AD_UNIT_ID, adRequest, 
            new RewardedAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    rewardedAd = null;
                    isLoading = false;
                }

                @Override
                public void onAdLoaded(@NonNull RewardedAd ad) {
                    rewardedAd = ad;
                    isLoading = false;
                }
            });
    }

    public boolean isAdAvailable() {
        return rewardedAd != null;
    }

    public void showRewardedAd(Activity activity, final AdCallback callback) {
        if (rewardedAd == null) {
            callback.onAdFailedToLoad("Ad not loaded");
            loadRewardedAd(activity);
            return;
        }

        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                rewardedAd = null;
                loadRewardedAd(activity);
                callback.onAdDismissed();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                rewardedAd = null;
                callback.onAdFailedToLoad(adError.getMessage());
            }
        });

        rewardedAd.show(activity, new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                callback.onAdRewarded();
            }
        });
    }

    public void preloadAd(Context context) {
        if (!isAdAvailable() && !isLoading) {
            loadRewardedAd(context);
        }
    }
}
