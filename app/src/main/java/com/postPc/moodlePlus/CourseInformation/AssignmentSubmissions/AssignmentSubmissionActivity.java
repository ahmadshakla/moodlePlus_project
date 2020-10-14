package com.postPc.moodlePlus.CourseInformation.AssignmentSubmissions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.postPc.moodlePlus.Constants;
import com.postPc.moodlePlus.CourseInformation.CourseSection;
import com.postPc.moodlePlus.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AssignmentSubmissionActivity extends AppCompatActivity {
    public static final int CAMERA_INTENT_REQUEST = 50;
    Button openCamera;
    Button convertToPDF;
    RecyclerView picturesRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    Gson gson;
    CourseSection.CourseSubSection subSection;
    String token;
    ArrayList<Bitmap> images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_submission);
        gson = new Gson();
        images = new ArrayList<>();
        Intent intent = getIntent();
        token = intent.getStringExtra(Constants.TOKEN);
        layoutManager = new LinearLayoutManager(this);
        subSection = gson.fromJson(intent.getStringExtra(Constants.SUB_SECTION_DATA),
                CourseSection.CourseSubSection.class);
        openCamera = findViewById(R.id.openCameraButton);
        convertToPDF = findViewById(R.id.convertToPdfButton);
        picturesRecyclerView = findViewById(R.id.exPicturesRecyclerView);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions();
            }
        });
        convertToPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(AssignmentSubmissionActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},300);
                createPDFWithMultipleImage();
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(contents.fileurl));
//                activity.startActivity(browserIntent);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(subSection.url.contains("?")?subSection.url +
                        "&token=" + token:subSection.url +
                        "?token=" + token));
                startActivity(browserIntent);
            }
        });
    }

    private void askCameraPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},
                    Constants.CAMERA_COMMAND_REQUEST_CODE);

        }
        else{
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.CAMERA_COMMAND_REQUEST_CODE && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            openCamera();
        }
        else if (requestCode == 300){
            createPDFWithMultipleImage();
        }
        else{
            Toast.makeText(this,"You need to allow camera permissions to use this feature!",
                    Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_INTENT_REQUEST && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap)data.getExtras().get("data");
            images.add(image);
            adapter = new AssignmentPicturesAdapter(images,AssignmentSubmissionActivity.this,token);
            picturesRecyclerView.setLayoutManager(layoutManager);
            picturesRecyclerView.setAdapter(adapter);

        }
    }

    private void createpdf(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.book);

        PdfDocument pdfDocument = new PdfDocument();
        Paint myPaint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(250,400,1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(pageInfo);
        Canvas canvas = myPage.getCanvas();
        canvas.drawBitmap(bitmap,40,50,myPaint);
        pdfDocument.finishPage(myPage);
        File file = new File(getFilesDir()+"/pdf1.pdf");
        if (!file.exists()){
            file.mkdir();
        }
//        File file1 = new File(file,"ff1");
        try {
            pdfDocument.writeTo(new FileOutputStream(file  ));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
    }

    private void openCamera(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_INTENT_REQUEST);

    }

    private void createPDFWithMultipleImage(){
        File file = getOutputFile();
        if (file != null){
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                PdfDocument pdfDocument = new PdfDocument();

                for (int i = 0; i < images.size(); i++){
                    Bitmap bitmap = images.get(i);
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), (i + 1)).create();
                    PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                    Canvas canvas = page.getCanvas();
                    Paint myPaint = new Paint();

//                    canvas.drawBitmap(bitmap,bitmap.getHeight(),bitmap.getHeight(),myPaint);

//                    Paint paint = new Paint();
//                    paint.setColor(Color.BLUE);
//                    canvas.drawPaint(paint);
                    canvas.drawBitmap(bitmap, 0f, 0f, null);
                    pdfDocument.finishPage(page);
//                    bitmap.recycle();
                }
                pdfDocument.writeTo(fileOutputStream);
                pdfDocument.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File getOutputFile(){
        File root = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"My Moodle " +
                "Exercise");
        String hbf = this.getExternalFilesDir(null).getAbsolutePath();
        boolean isFolderCreated = true;
        if (!root.exists()){
            isFolderCreated = root.mkdir();
        }

        if (isFolderCreated) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            String imageFileName = "PDF_" + timeStamp;
            return new File(root, imageFileName + ".pdf");
        }
        else {
            Toast.makeText(this, "Folder is not created", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
