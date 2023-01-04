package tbc.uncagedmist.bhulekhkheshra;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.nativeAds.adPlacer.MaxAdPlacer;
import com.applovin.mediation.nativeAds.adPlacer.MaxAdPlacerSettings;
import com.applovin.mediation.nativeAds.adPlacer.MaxRecyclerAdapter;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.Icon;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import tbc.uncagedmist.bhulekhkheshra.Adapter.StateAdapter;
import tbc.uncagedmist.bhulekhkheshra.Common.Common;
import tbc.uncagedmist.bhulekhkheshra.Common.MyApplicationClass;
import tbc.uncagedmist.bhulekhkheshra.Model.State;
import tbc.uncagedmist.bhulekhkheshra.Remote.IMyAPI;

public class MainActivity extends AppCompatActivity implements MaxAdPlacer.Listener {

    RecyclerView recyclerView;
    EditText edtState;

    IMyAPI iMyAPI;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    NoInternetDialog noInternetDialog;

    List<State> localDataSource = new ArrayList<>();

    StateAdapter searchAdapter, adapter;

    ProgressDialog dialog;

    private MaxAdView adView;

    MaxRecyclerAdapter adAdapter;

    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setContentView(R.layout.activity_main);


        noInternetDialog = new NoInternetDialog.Builder(this).build();

        dialog = new ProgressDialog(this);

        iMyAPI = Common.getAPI();

        recyclerView = findViewById(R.id.recyclerState);
        edtState = findViewById(R.id.edtState);

        adView = findViewById(R.id.max_banner);

        if (MyApplicationClass.getInstance().isShowAds())   {
            adView.loadAd();
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.shareApp(MainActivity.this);
            }
        });

        loadStates();

        edtState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                startSearch(text);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void startSearch(CharSequence text) {
        List<State> result = new ArrayList<>();

        String name = text.toString();

        for (State state : localDataSource)   {
            if (state.name.toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT)))    {
                result.add(state);
            }
        }
        searchAdapter = new StateAdapter(this, result);
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadStates() {
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        compositeDisposable.add(
                iMyAPI.getStates()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<State>>() {
                                       @Override
                                       public void accept(List<State> stateList) throws Exception {
                                           displayStates(stateList);
                                           dialog.dismiss();
                                       }
                                   }
                        )
        );
    }


    private void displayStates(List<State> stateList) {
        localDataSource = stateList;
        adapter = new StateAdapter(this,stateList);

        if (MyApplicationClass.getInstance().isShowAds())   {
            MaxAdPlacerSettings settings = new MaxAdPlacerSettings( getString(R.string.applovin_native) );
            settings.setRepeatingInterval(4);
            adAdapter = new MaxRecyclerAdapter( settings, adapter, this );

            adAdapter.setListener(this);

            recyclerView.setAdapter(adAdapter);
            adAdapter.loadAds();
        }
        else {
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed() {
        new FancyAlertDialog.Builder(MainActivity.this)
                .setTitle("Exit App")
                .setBackgroundColor(Color.parseColor("#303F9F"))  //Don't pass R.color.colorvalue
                .setMessage("You're exiting this app!")
                .setNegativeBtnText("Exit")
                .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("Rate")
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(R.drawable.ic_star_border_black_24dp, Icon.Visible)
                .OnPositiveClicked(() ->
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName()))))
                .OnNegativeClicked(() -> {
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                })
                .build();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        compositeDisposable.dispose();
        adAdapter.destroy();
        super.onDestroy();
    }

    @Override
    public void onAdLoaded(int i) {

    }

    @Override
    public void onAdRemoved(int i) {

    }

    @Override
    public void onAdClicked(MaxAd maxAd) {

    }

    @Override
    public void onAdRevenuePaid(MaxAd maxAd) {

    }
}