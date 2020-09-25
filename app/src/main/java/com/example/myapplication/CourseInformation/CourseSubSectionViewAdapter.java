package com.example.myapplication.CourseInformation;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.R;
import com.pixplicity.sharp.OnSvgElementListener;
import com.pixplicity.sharp.Sharp;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CourseSubSectionViewAdapter extends RecyclerView.Adapter<CourseSubSectionViewAdapter.CourseSubSectionViewHolder> implements  ActivityCompat.OnRequestPermissionsResultCallback{

    private List<CourseSection.CourseSubSection> courseSubSectionList;
    private Activity activity;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 321:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                else {
                    Toast.makeText(activity,"permission denied",Toast.LENGTH_LONG).show();
                }
        }
    }




    public CourseSubSectionViewAdapter(List<CourseSection.CourseSubSection> courseList, Activity activity) {
        this.courseSubSectionList = new ArrayList<>();
        this.activity = activity;
        for (CourseSection.CourseSubSection courseSubSection : courseList) {
            if ("false".equals(courseSubSection.noviewlink)) {
                this.courseSubSectionList.add(courseSubSection);
            } else {
                if (!courseSubSection.description.contains("src")) {
                    this.courseSubSectionList.add(courseSubSection);
                }
            }
        }
    }

    @NonNull
    @Override
    public CourseSubSectionViewAdapter.CourseSubSectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_sub_section, parent
                , false);
        return new CourseSubSectionViewAdapter.CourseSubSectionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseSubSectionViewHolder holder, int position) {
        final CourseSection.CourseSubSection current = courseSubSectionList.get(position);
        Log.i("HBD", current.name);
        if (current.description != null) {
            if (!current.description.contains("src") && "true".equals(current.noviewlink)) {
                holder.courseTextView.setTextColor(Color.parseColor("#C80DE8"));

            }
        }
        holder.courseTextView.setText(current.name);
        final boolean[] fail = {false};
        if (current.modicon != null) {
            if (!current.modicon.contains("icon") || !"false".equals(current.noviewlink)) {
                Glide.with(activity).load(current.modicon).into(holder.iconImage);

                }
//            else {
//                    Utils.fetchSvg(activity,
//                            current.modicon,
//                            holder.iconImage);
//
//                }

        }
        if (current.contents != null && !current.contents.isEmpty()) {
            final CourseSection.CourseSubSection.CourseSubSectionContents contents =
                    current.contents.get(0);
            if ("file".equals(contents.type)) {
                holder.courseTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_DENIED) {
                                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                activity.requestPermissions(permissions,321);
                            }
                            else{
                                startDownload(contents.fileurl+"&token" +
                                        "=690cb20e0e50c5ffd76cd3ab4e8cd797",current.name,"pdf");
                            }
                        }
                    }
                });
            }
        }

    }

    private void startDownload(String url,String name,String type){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle(name);
        request.setDescription("downloading file... ");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                name + "." + type);
        DownloadManager downloadManager =
                (DownloadManager)activity.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }


    @Override
    public int getItemCount() {
        return courseSubSectionList.size();
    }

    static class Utils {
        private static OkHttpClient httpClient;

        public static void fetchSvg(Context context, String url, final ImageView target) {
            if (httpClient == null) {
                // Use cache for performance and basic offline capability
                httpClient = new OkHttpClient.Builder()
                        .cache(new Cache(context.getCacheDir(), 5 * 1024 * 1024))
                        .build();
            }

            Request request = new Request.Builder().url(url).build();
            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    target.setImageDrawable(R.drawable.);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream stream = response.body().byteStream();
                    Sharp.loadInputStream(stream).into(target);
                    stream.close();
                }
            });
        }
    }

    /**
     * Background Async Task to download file
     */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory()
                        + "/2020.kml");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }


    }




    public static class CourseSubSectionViewHolder extends RecyclerView.ViewHolder {
        public TextView courseTextView;
        public ImageView iconImage;

        public CourseSubSectionViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTextView = itemView.findViewById(R.id.sub_section_textView);
            iconImage = itemView.findViewById(R.id.imageView);
        }
    }

}
