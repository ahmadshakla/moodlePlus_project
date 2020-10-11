package com.example.myapplication.CourseInformation.AssignmentSubmissions;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.CourseInformation.CourseEvents.CourseEventsAdapter;
import com.example.myapplication.R;

import java.util.ArrayList;

public class AssignmentPicturesAdapter extends RecyclerView.Adapter<AssignmentPicturesAdapter.AssignmentPicturesHolder> {
    ArrayList<Bitmap> images;
    public AssignmentPicturesAdapter(ArrayList<Bitmap> images) {
        this.images = images;
    }


    @NonNull
    @Override
    public AssignmentPicturesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view_layout,
                parent, false);
        return new AssignmentPicturesHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentPicturesHolder holder, int position) {
        Bitmap image = images.get(position);
        holder.imageView.setImageBitmap(image);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class AssignmentPicturesHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public AssignmentPicturesHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageInView);
        }
    }
}
