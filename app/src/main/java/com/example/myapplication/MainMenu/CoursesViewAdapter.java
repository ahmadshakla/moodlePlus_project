package com.example.myapplication.MainMenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.bumptech.glide.Glide;
import com.example.myapplication.Constants;
import com.example.myapplication.CourseInformation.CourseInfo;
import com.example.myapplication.CourseInformation.CourseSection;
import com.example.myapplication.MoodleApi;
import com.example.myapplication.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CoursesViewAdapter extends RecyclerView.Adapter<CoursesViewAdapter.CoursesViewHolder> {
    private List<UserCourses> courseList;
    private Context context;
    private String token;
    private Gson gson = new Gson();

    public static class CoursesViewHolder extends RecyclerView.ViewHolder {
        public TextView courseTextView;

        public CoursesViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTextView = itemView.findViewById(R.id.course_in_view);
        }
    }

    public CoursesViewAdapter(List<UserCourses> courseList, Context context, String token) {
        this.courseList = courseList;
        this.context = context;
        this.token = token;
    }

    @NonNull
    @Override
    public CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_single_line_view, parent, false);
        return new CoursesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesViewHolder holder, int position) {
        final UserCourses current = courseList.get(position);
        holder.courseTextView.setText(current.getDisplayname());
        holder.courseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.MOODLE_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                MoodleApi moodleApi = retrofit.create(MoodleApi.class);
                Call<List<CourseSection>> call = moodleApi.getCourseInfo(Constants.MOODLE_W_REST_FORMAT,
                        token,
                        "core_course_get_contents", current.getId());
                call.enqueue(new Callback<List<CourseSection>>() {
                    @Override
                    public void onResponse(Call<List<CourseSection>> call,
                                           Response<List<CourseSection>> response) {
                        List<CourseSection> sections = response.body();
                        Intent intent = new Intent(context, CourseInfo.class);
                        intent.putExtra(Constants.TOKEN, token);
                        intent.putExtra(Constants.COURSE_SECTION_ARR, gson.toJson(sections));
                        intent.putExtra(Constants.COURSE_SECTION, gson.toJson(current));
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<List<CourseSection>> call, Throwable t) {
                        Log.e(Constants.TAG, Constants.ON_FAILURE_COURSE_CONTENTS);
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }




//    private class HttpImageRequestTask extends AsyncTask<Void, Void, Drawable> {
//        @Override
//        protected Drawable doInBackground(Void... params) {
//            try {
//
//
//                final URL url = new URL("http://upload.wikimedia.org/wikipedia/commons/e/e8/Svg_example3.svg");
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                InputStream inputStream = urlConnection.getInputStream();
//                SVG svg = SVGParser.getSVGFromInputStream(inputStream);
//                Drawable drawable = svg.createPictureDrawable();
//                return drawable;
//            } catch (Exception e) {
//                Log.e("MainActivity", e.getMessage(), e);
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Drawable drawable) {
//            // Update the view
//            updateImageView(drawable);
//        }
//    }
}


