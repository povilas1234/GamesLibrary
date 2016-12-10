package com.example.povilas.gameslibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

/**
 * Created by povilas on 16.12.9.
 */



public class GamesAdapter extends BaseAdapter{
    Context mContext;
    LayoutInflater inflater;
    List<Game> mGames;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String subscriptions;
    boolean isSubscribed;

    public GamesAdapter(List<Game> mGames, Context mContext) {
        this.mGames = mGames;
        this.mContext = mContext;
        inflater = LayoutInflater.from(this.mContext);


        pref = mContext.getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        editor = pref.edit();
        subscriptions = pref.getString("subscriptions","");
    }

    @Override
    public int getCount() {
        return mGames.size();
    }

    @Override
    public Object getItem(int i) {
        return mGames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private void Subscribe(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://gameslibrary.000webhostapp.com/Subscribe.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("subscribe", ": successful");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("subscribe: ", String.valueOf(error));
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",pref.getString("id",""));
                params.put("subscriptions",subscriptions);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mainViewholder = null;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.game_list_item, null);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.ivThumbnail);
            Picasso.with(mContext).load(mGames.get(position).image).into(viewHolder.thumbnail);

            viewHolder.name = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.name.setText(mGames.get(position).name);

            viewHolder.description = (TextView) convertView.findViewById(R.id.tvDescription);
            viewHolder.description.setText(mGames.get(position).description);

            viewHolder.subscribe = (Button) convertView.findViewById(R.id.bSubscribe);


            isSubscribed = subscriptions.contains(";" + mGames.get(position).id +";");

            if(isSubscribed) viewHolder.subscribe.setText("Unsubscribe");
            else viewHolder.subscribe.setText("Subscribe");
            //Log.i("real: ", String.valueOf(subscribed));

            viewHolder.subscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isSubscribed = subscriptions.contains(";" + mGames.get(position).id +";");

                    Log.i("before: ", subscriptions);



                    if(isSubscribed){ subscriptions = subscriptions.replace(";"+mGames.get(position).id+";",";");Log.i("is: ", " true");}
                    else {subscriptions += mGames.get(position).id+";";Log.i("is: ", " false");}


                    editor.putString("subscriptions",subscriptions);
                    editor.apply();


                    Subscribe();

                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });






            convertView.setTag(viewHolder);
        } else {
            mainViewholder = (ViewHolder) convertView.getTag();
        }



        return convertView;
    }
    private class ViewHolder{
        ImageView thumbnail;
        TextView name, description;
        Button subscribe;

    }




}
