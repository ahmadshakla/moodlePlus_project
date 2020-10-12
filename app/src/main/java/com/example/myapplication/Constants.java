package com.example.myapplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constants {
    public final static String SHARED_PREFERENCE = "shaaared pppreference";
    public final static String COURSE_ARR = "course array";
    public final static String TOKEN = "token";
    public static final String MOODLE_BASE_URL = "https://moodle2.cs.huji.ac.il/";
    public static final String MOODLE_SERVICE = "moodle_mobile_app";
    public static final String WRONG_LOGIN = "wrong username or password";
    public static final String MOODLE_W_REST_FORMAT = "json";
    public static final String ID_NUMBER = "password";
    public static final String TAG = "moodlePlus";
    public static final String ON_FAILURE_COURSE_CONTENTS = "failed to get the content of the " +
            "course";
    public final static String COURSE_SECTION_ARR = "sectionArray";
    public final static String COURSE_SECTION = "course section";
    public final static String USER_INFO = "user information";
    public final static String FORUM = "forum";
    public final static String CONTENTS_ARR = "contents array";
    public final static String FOLDER_NAME = "folder name";
    public final static String DISCUSSION_ID = "discussion id";
    public final static String DISCUSSION_INFO = "discussion info";
    public final static String POST_INFO = "post information";
    public final static String POST_INFO_ARR = "post information array";
    public final static String WORK_MANAGER = "work manager";
    public final static String SUB_SECTION_DATA = "sub section info";
    public final static String COURSE_ID = "course id";
    public static final int VOICE_COMMAND_REQUEST_CODE = 1;
    public static final int CAMERA_COMMAND_REQUEST_CODE = 3;
    public final static int ADD_DISCUSSION_REQUEST_CODE = 11;
    public final static int ADD_POST_REQUEST_CODE = 22;


    public static Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.MOODLE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public static MoodleApi moodleApi = retrofit.create(MoodleApi.class);
}
