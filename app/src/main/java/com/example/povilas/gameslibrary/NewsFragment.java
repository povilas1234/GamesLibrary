package com.example.povilas.gameslibrary;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    String ENDPOINT = "http://gameslibrary.16mb.com/News.php";
    RequestQueue requestQueue;
    Gson gson;
    List<News> news;
    ProgressDialog progressDialog;
    FragmentManager ft;
    ListView listView;


    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading news....");
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
            news = Arrays.asList(gson.fromJson(response, News[].class));

//            Log.i("PostActivity", news.size() + " posts loaded.");
//            for (News current : news) {
//                Log.i("PostActivity: ", current.id + " | " + current.game_id + " | " + current.title + " | " + current.date
//                        + " | " + current.header_img + " | " + current.content);
//            }
            progressDialog.dismiss();

            listView.setAdapter(new NewsAdapter(news, getActivity()));
        }
    };


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();




        requestQueue = Volley.newRequestQueue(getContext());
        fetchPosts();

        listView = (ListView)view.findViewById(R.id.lvNews);
        // Inflate the layout for this fragment
        return view;
    }

}
