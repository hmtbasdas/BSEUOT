package com.hmtbasdas.bseuot.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hmtbasdas.bseuot.databinding.ActivityMealListBinding;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class MealListActivity extends BaseActivity {

    private ActivityMealListBinding binding;

    private RequestQueue requestQueue;
    private Response.Listener<String> listener;
    private Response.ErrorListener errorListener;
    private String URL = "http://w3.bilecik.edu.tr/sks/beslenme-hizmetleri-2/yemek-menusu/";
    private StringRequest stringRequest;

    private ArrayList<String> mealList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMealListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
    }

    private void init(){
        requestQueue = Volley.newRequestQueue(this);
        listenerSettings();

        stringRequest = new StringRequest(Request.Method.GET, URL, listener, errorListener);
        requestQueue.add(stringRequest);
    }

    private void setListeners(){
        binding.backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void listenerSettings(){
        listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);
            }
        };

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void parseResponse(String response){
        Document document = Jsoup.parse(response);
        Elements myElements = document.getElementsByClass("entry-content").select("p");
        Element getElementImage = document.getElementsByClass("alignnone").select("img").first();

        if(getElementImage != null){
            String absoluteUrl = getElementImage.absUrl("src");

            if(!absoluteUrl.isEmpty()){
                Picasso.get().load(absoluteUrl).into(binding.mealImage);
            }
            else {
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1200px-No_image_available.svg.png").into(binding.mealImage);
            }
        }
        else {
            Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1200px-No_image_available.svg.png").into(binding.mealImage);
        }


        for(int i=0;i<myElements.size();i++){
            if(!myElements.get(i).text().isEmpty()){
                mealList.add(myElements.get(i).text());
            }
        }
        setMealValues();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setMealValues(){
        binding.mealDate.setText(mealList.get(0));
        binding.meal1.setText(mealList.get(1));
        binding.meal2.setText(mealList.get(2));
        binding.meal3.setText(mealList.get(3));
        binding.meal4.setText(mealList.get(4));
    }
}