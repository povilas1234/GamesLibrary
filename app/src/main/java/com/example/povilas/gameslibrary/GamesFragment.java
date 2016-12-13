package com.example.povilas.gameslibrary;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.AndroidAppUri;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 */
public class GamesFragment extends Fragment {

    String ENDPOINT = "https://gameslibrary.000webhostapp.com/Games.php";
    RequestQueue requestQueue;
    Gson gson;
    List<Game> games;
    ProgressDialog progressDialog;
    ListView listView;


    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading games list....");
        progressDialog.show();
    }


    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            games = Arrays.asList(gson.fromJson(response, Game[].class));

            Log.i("PostActivity", games.size() + " posts loaded.");
            for (Game game : games) {
                Log.i("PostActivity: ", game.id + " | " + game.image + " | " + game.name + " | " + game.description);
            }
            progressDialog.dismiss();

            listView.setAdapter(new GamesAdapter(games, getActivity()));
        }
    };


    public GamesFragment() {
        // Required empty public constructor

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();




        requestQueue = Volley.newRequestQueue(getContext());
        fetchPosts();

        listView = (ListView)view.findViewById(R.id.lvGames);
        // Inflate the layout for this fragment
        return view;

    }
}
