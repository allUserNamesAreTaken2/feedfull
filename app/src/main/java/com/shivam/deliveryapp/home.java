package com.shivam.deliveryapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;


public class home extends Fragment {
    ArrayList<Bitmap> mtargetImages=new ArrayList<>();
    ArrayList<String> mnameTarget=new ArrayList<>();
    RecyclerView recyclerView;
    ProgressBar progressBar;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";








    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View v= inflater.inflate(R.layout.fragment_home, container, false);
           recyclerView=v.findViewById(R.id.homePageRecyclerView);
           progressBar=v.findViewById(R.id.progressBar3);
           return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        thread.start();
    }

    public void intitArrays(){

        Bitmap foodImage= BitmapFactory.decodeResource(getResources(), R.drawable.food);
        Bitmap largeIcon1 = BitmapFactory.decodeResource(getResources(), R.drawable.smile);
        Bitmap largeIcon2 = BitmapFactory.decodeResource(getResources(), R.drawable.clothing);
        Bitmap largeIcon3 = BitmapFactory.decodeResource(getResources(), R.drawable.education);





        mnameTarget.add("10,000 meals every month");
        mtargetImages.add(foodImage);

        mnameTarget.add("Clothing to 1000 people every month");
        mtargetImages.add(largeIcon1);

        mnameTarget.add("Shelter to 100 families");
        mtargetImages.add(largeIcon2);

        mnameTarget.add("Education to 1000 children");
        mtargetImages.add(largeIcon3);




        initRecyclerView();
    }

    public void initRecyclerView(){
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(getContext(),mtargetImages,mnameTarget);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar.setVisibility(View.INVISIBLE);
    }

    Thread thread=new Thread(){
        @Override
        public void run() {
            super.run();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    intitArrays();
                }
            });

        }




    };

}
