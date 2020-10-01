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
    public static final String USERNAME = "ahmad.shakla";
    public static final String PASSWORD = "0932ahs";
    public static final String ID_NUMBER = "31529455";
    public static final String TAG = "moodlePlus";
    public static final String ON_FAILURE_COURSE_CONTENTS = "failed to get the content of the " +
            "course";
    public final static String COURSE_SECTION_ARR = "sectionArray";
    public final static String COURSE_SECTION = "course section";
    public final static String USER_INFO = "user information";
    public final static String FORUM = "forum";
    public final static String DISCUSSION_ID = "discussion id";
    public final static String DISCUSSION_INFO = "discussion info";
    public final static int ADD_POST_REQUEST_CODE = 11;


    public static Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.MOODLE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public static MoodleApi moodleApi = retrofit.create(MoodleApi.class);
}
