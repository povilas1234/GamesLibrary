package com.example.povilas.gameslibrary;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String ENDPOINT = "https://gameslibrary.000webhostapp.com/Shops.php";
    private RequestQueue requestQueue;
    private Gson gson;
    private List<Shop> shops;
    private ProgressDialog progressDialog;;


    public MapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();


        requestQueue = Volley.newRequestQueue(getContext());



        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        fetchPosts();
    }

    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading markers....");
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
            shops = Arrays.asList(gson.fromJson(response, Shop[].class));

            Log.i("PostActivity: ", shops.size() + " posts loaded.");
            for (Shop shop : shops) {
                Log.i("PostActivity: ", shop.id + " | " + shop.title + " | " + shop.latitude + " | " + shop.longitude);
            }
            progressDialog.dismiss();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.169438,23.881275), 7));
            for(Shop shop: shops){
                LatLng tempMarker = new LatLng(shop.latitude,shop.longitude);
                mMap.addMarker(new MarkerOptions().position(tempMarker).title(shop.title));
            }
        }
    };
}
