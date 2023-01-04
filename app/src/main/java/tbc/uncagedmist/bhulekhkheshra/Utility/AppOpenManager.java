package tbc.uncagedmist.bhulekhkheshra.Utility;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;



import java.util.Date;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAppOpenAd;
import com.applovin.sdk.AppLovinSdk;

import tbc.uncagedmist.bhulekhkheshra.Common.MyApplicationClass;
import tbc.uncagedmist.bhulekhkheshra.R;

public class AppOpenManager implements LifecycleObserver, MaxAdListener
{
    private final MaxAppOpenAd appOpenAd;
    private final Context context;

    public static final String APP_OPEN_ID = String.valueOf(R.string.applovin_full);

    public AppOpenManager(final Context context)
    {
        ProcessLifecycleOwner.get().getLifecycle().addObserver( this );

        this.context = context;

        appOpenAd = new MaxAppOpenAd( APP_OPEN_ID, context);
        appOpenAd.setListener( this );
        appOpenAd.loadAd();
    }

    private void showAdIfReady()
    {
        if ( appOpenAd == null || !AppLovinSdk.getInstance( context ).isInitialized() ) return;

        if ( appOpenAd.isReady() )
        {
            appOpenAd.showAd(APP_OPEN_ID);
        }
        else
        {
            appOpenAd.loadAd();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart()
    {
        showAdIfReady();
    }

    @Override
    public void onAdLoaded(final MaxAd ad) {}
    @Override
    public void onAdLoadFailed(final String adUnitId, final MaxError error) {}
    @Override
    public void onAdDisplayed(final MaxAd ad) {}
    @Override
    public void onAdClicked(final MaxAd ad) {}

    @Override
    public void onAdHidden(final MaxAd ad)
    {
        appOpenAd.loadAd();
    }

    @Override
    public void onAdDisplayFailed(final MaxAd ad, final MaxError error)
    {
        appOpenAd.loadAd();
    }
}