package com.example.myapplication.MainMenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Constants;
import com.example.myapplication.CourseInformation.CourseInfoActivity;
import com.example.myapplication.CourseInformation.CourseSection;
import com.example.myapplication.MoodleApi;
import com.example.myapplication.R;
import com.example.myapplication.UserInformation.UserInfo;
import com.google.gson.Gson;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CoursesViewAdapter extends RecyclerView.Adapter<CoursesViewAdapter.CoursesViewHolder> {
    private List<UserCourse> courseList;
    private Context context;
    private String token;
    private UserInfo userInfo;
    private Gson gson = new Gson();
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.MOODLE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private MoodleApi moodleApi = retrofit.create(MoodleApi.class);
    // ----------------------------- holder class -----------------------------
    public static class CoursesViewHolder extends RecyclerView.ViewHolder {
        public TextView courseTextView;

        public CoursesViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTextView = itemView.findViewById(R.id.course_in_view);
        }
    }
    //---------------------------------------------------------------------------

    public CoursesViewAdapter(List<UserCourse> courseList, Context context, String token,
                              UserInfo userInfo) {
        this.courseList = courseList;
        this.context = context;
        this.token = token;
        this.userInfo = userInfo;
    }

    @NonNull
    @Override
    public CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.course_fill);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#c5e2ea"));
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_single_line_view, parent, false);

        return new CoursesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesViewHolder holder, int position) {
        final UserCourse current = courseList.get(position);
        holder.courseTextView.setText(Html.fromHtml(Html.fromHtml(current.getDisplayname(),
                HtmlCompat.FROM_HTML_MODE_LEGACY).toString(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        holder.courseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<CourseSection>> call = moodleApi.getCourseInfo(Constants.MOODLE_W_REST_FORMAT,
                        token,
                        "core_course_get_contents", current.getId());
                call.enqueue(new Callback<List<CourseSection>>() {
                    @Override
                    public void onResponse(Call<List<CourseSection>> call,
                                           Response<List<CourseSection>> response) {
                        List<CourseSection> sections = response.body();
                        Intent intent = new Intent(context, CourseInfoActivity.class);
                        intent.putExtra(Constants.TOKEN, token);
                        intent.putExtra(Constants.COURSE_SECTION_ARR, gson.toJson(sections));
                        intent.putExtra(Constants.COURSE_SECTION, gson.toJson(current));
                        intent.putExtra(Constants.USER_INFO,gson.toJson(userInfo));
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


