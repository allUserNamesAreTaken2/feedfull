package com.shivam.deliveryapp;

import android.content.Context;

import android.graphics.Bitmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;



public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<Bitmap> mtargetImages=new ArrayList<>();
    private ArrayList<String> mnameTarget=new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context mContext,ArrayList<Bitmap> mtargetImages,ArrayList<String> mnameTarget) {
        this.mContext = mContext;
        this.mtargetImages=mtargetImages;
        this.mnameTarget=mnameTarget;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.targetImage.setImageBitmap(mtargetImages.get(position));
        holder.targetText.setText(mnameTarget.get(position));
        holder.targetProgress.setProgress(20);
        holder.parentContext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ON CLICKING THE TAB

            }
        });

    }

    @Override
    public int getItemCount() {
        return mnameTarget.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView targetText;
        ImageView targetImage;
        ProgressBar targetProgress;
        ConstraintLayout parentContext;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            targetImage=itemView.findViewById(R.id.imageOfTargets);
            targetProgress=itemView.findViewById(R.id.targetProgressBar);
            targetText=itemView.findViewById(R.id.targetTextView);
            parentContext=itemView.findViewById(R.id.parent_layout);


        }
    }


}
