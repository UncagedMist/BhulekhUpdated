package tbc.uncagedmist.bhulekhkheshra.Common;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import tbc.uncagedmist.bhulekhkheshra.Model.State;
import tbc.uncagedmist.bhulekhkheshra.R;
import tbc.uncagedmist.bhulekhkheshra.Remote.IMyAPI;
import tbc.uncagedmist.bhulekhkheshra.Remote.RetrofitClient;


public class Common {
    public static final String WIN_URL = "https://894.win.qureka.com";

    public static final String BASE_URL = "https://app.ihuntech.com/bhulekh/";

    public static State CURRENT_STATE;


    public static final String PRIVACY_URL = "https://docs.google.com/document/d/1N9dzhvQlIAV3v3357SOrFBwZ3Y3k_A_K_mFbkEoQOqA/edit?usp=sharing";

    public static IMyAPI getAPI() {
        return RetrofitClient.getClient(BASE_URL).create(IMyAPI.class);
    }


    public static boolean isConnectedToInternet(Context context)    {

        ConnectivityManager connectivityManager = (
                ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null)    {

            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();

            if (info != null)   {

                for (int i = 0; i <info.length;i++)   {

                    if (info[i].getState() == NetworkInfo.State.CONNECTED)  {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void shareApp(Context context)    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String message = "Never Miss A Thing About Land Record. Install "+context.getString(R.string.app_name)+" App and Stay Updated! \n " +
                "https://play.google.com/store/apps/details?id="+context.getPackageName();
        intent.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(intent, "Share "+context.getString(R.string.app_name)+" App Using"));
    }
}