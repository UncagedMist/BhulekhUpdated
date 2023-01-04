package tbc.uncagedmist.bhulekhkheshra.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import tbc.uncagedmist.bhulekhkheshra.Common.Common;
import tbc.uncagedmist.bhulekhkheshra.Common.MyApplicationClass;
import tbc.uncagedmist.bhulekhkheshra.Interface.ItemClickListener;
import tbc.uncagedmist.bhulekhkheshra.Model.State;
import tbc.uncagedmist.bhulekhkheshra.R;
import tbc.uncagedmist.bhulekhkheshra.ResultActivity;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.MyViewHolder> implements MaxAdListener {

    Context context;
    List<State> states;

    private MaxInterstitialAd interstitialAd;

    public StateAdapter(Context context, List<State> states) {
        this.context = context;
        this.states = states;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.my_item_list,parent, false);

        if (MyApplicationClass.getInstance().isShowAds())   {
            interstitialAd = new MaxInterstitialAd(context.getString(R.string.applovin_full), (Activity) context);
            interstitialAd.setListener(this);
            // Load the first ad
            interstitialAd.loadAd();
        }

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {

        Picasso.get()
                .load(states.get(position).image)
                .into(holder.stateImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.stateName.setText(states.get(position).name);
                        holder.stateDesc.setText(states.get(position).description);
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view) {
                if (interstitialAd.isReady())  {
                    interstitialAd.showAd();
                }
                else {
                    Intent intent = new Intent(context, ResultActivity.class);
                    Common.CURRENT_STATE = states.get(position);
                    context.startActivity(intent);
                }

//                Intent intent = new Intent(context, ResultActivity.class);
//                Common.CURRENT_STATE = states.get(position);
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return states.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView stateImage;
        TextView stateName,stateDesc;

        ItemClickListener itemClickListener;

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            stateImage = itemView.findViewById(R.id.stateImage);
            stateName= itemView.findViewById(R.id.stateName);
            stateDesc = itemView.findViewById(R.id.stateDesc);

            stateName.setSelected(true);
            stateDesc.setSelected(true);

            Typeface face = Typeface.createFromAsset(context.getAssets(),
                    "fonts/sport.ttf");
            stateDesc.setTypeface(face);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(view);
        }
    }

    @Override
    public void onAdLoaded(MaxAd ad) {

    }

    @Override
    public void onAdDisplayed(MaxAd ad) {

    }

    @Override
    public void onAdHidden(MaxAd ad) {

    }

    @Override
    public void onAdClicked(MaxAd ad) {

    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {

    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {

    }
}
