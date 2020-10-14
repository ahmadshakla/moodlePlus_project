package com.postPc.moodlePlus;

import com.postPc.moodlePlus.CalenderData.CoursesAssignmentsInfo;
import com.postPc.moodlePlus.CourseInformation.AssignmentSubmissions.SubmissionInfo;
import com.postPc.moodlePlus.CourseInformation.CourseForums.AddPostReturnInfo;
import com.postPc.moodlePlus.CourseInformation.CourseForums.DeletePostReturnInfo;
import com.postPc.moodlePlus.CourseInformation.CourseForums.DiscussionInfo;
import com.postPc.moodlePlus.CourseInformation.CourseForums.ForumInfo;
import com.postPc.moodlePlus.CourseInformation.CourseForums.PostDiscussionReturnInfo;
import com.postPc.moodlePlus.CourseInformation.CourseForums.PostsInfo;
import com.postPc.moodlePlus.CourseInformation.CourseGrades.GradesTable;
import com.postPc.moodlePlus.CourseInformation.CourseSection;
import com.postPc.moodlePlus.MainMenu.UserCourse;
import com.postPc.moodlePlus.UserInformation.LoginReturn;
import com.postPc.moodlePlus.UserInformation.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MoodleApi {
    String REST_API_LINK = "nu19/webservice/rest/server.php";

    @GET("nu19/login/token.php")
    Call<LoginReturn> getToken(@Query("service") String service,
                               @Query("username") String username,
                               @Query("password") String password);

    @GET(REST_API_LINK)
    Call<List<UserInfo>> getUserInfo(@Query("moodlewsrestformat") String moodlewsrestformat,
                                     @Query("wstoken") String wstoken,
                                     @Query("wsfunction") String wsfunction,
                                     @Query("field") String field,
                                     @Query("values[0]") String values);


    @GET(REST_API_LINK)
    Call<List<UserCourse>> getUserCourses(@Query("moodlewsrestformat") String moodlewsrestformat,
                                          @Query("wstoken") String wstoken,
                                          @Query("wsfunction") String wsfunction,
                                          @Query("userid") String userid);

    @GET(REST_API_LINK)
    Call<List<CourseSection>> getCourseInfo(@Query("moodlewsrestformat") String moodlewsrestformat,
                                            @Query("wstoken") String wstoken,
                                            @Query("wsfunction") String wsfunction,
                                            @Query("courseid") String courseid);


    @GET(REST_API_LINK)
    Call<GradesTable> getGradesInfo(@Query("moodlewsrestformat") String moodlewsrestformat,
                                    @Query("wstoken") String wstoken,
                                    @Query("wsfunction") String wsfunction,
                                    @Query("courseid") String courseid,
                                    @Query("userid") String userid);

    @GET(REST_API_LINK)
    Call<CoursesAssignmentsInfo> getAssignmentsForCourses(@Query("moodlewsrestformat") String moodlewsrestformat,
                                                          @Query("wstoken") String wstoken,
                                                          @Query("wsfunction") String wsfunction);

    @GET(REST_API_LINK)
    Call<CoursesAssignmentsInfo> getAssignmentsForOneCourse(@Query("moodlewsrestformat") String moodlewsrestformat,
                                                          @Query("wstoken") String wstoken,
                                                          @Query("wsfunction") String wsfunction,
                                                            @Query("courseids[0]") String courseid );

    @GET(REST_API_LINK)
    Call<List<ForumInfo>> getForumsForCourses(@Query("moodlewsrestformat") String moodlewsrestformat,
                                              @Query("wstoken") String wstoken,
                                              @Query("wsfunction") String wsfunction,
                                              @Query("courseids[0]") String coursesid);

    @GET(REST_API_LINK)
    Call<DiscussionInfo> getDiscussionsOfForum(@Query("moodlewsrestformat") String moodlewsrestformat,
                                               @Query("wstoken") String wstoken,
                                               @Query("wsfunction") String wsfunction,
                                               @Query("forumid") String forumid);

    @GET(REST_API_LINK)
    Call<PostsInfo> getPostsOfDiscussion(@Query("moodlewsrestformat") String moodlewsrestformat,
                                         @Query("wstoken") String wstoken,
                                         @Query("wsfunction") String wsfunction,
                                         @Query("discussionid") String discussionid);

    @POST(REST_API_LINK)
    Call<PostDiscussionReturnInfo> getReturnResOfNewDiscussion(@Body DiscussionInfo.Discussion discussion,
                                                               @Query("moodlewsrestformat") String moodlewsrestformat,
                                                               @Query("wstoken") String wstoken,
                                                               @Query("wsfunction") String wsfunction,
                                                               @Query("forumid") String forumid,
                                                               @Query("subject") String subject,
                                                               @Query("message") String message);

    @POST(REST_API_LINK)
    Call<AddPostReturnInfo> getReturnResOfNewPost(@Query("moodlewsrestformat") String moodlewsrestformat,
                                                  @Query("wstoken") String wstoken,
                                                  @Query("wsfunction") String wsfunction,
                                                  @Query("postid") String postid,
                                                  @Query("subject") String subject,
                                                  @Query("message") String message);

    @POST(REST_API_LINK)
    Call<DeletePostReturnInfo> getReturnResOfDeletePost(@Query("moodlewsrestformat") String moodlewsrestformat,
                                                        @Query("wstoken") String wstoken,
                                                        @Query("wsfunction") String wsfunction,
                                                        @Query("postid") String postid);

    @POST(REST_API_LINK)
    Call<SubmissionInfo> getReturnResOfViewSubmissions(@Query("moodlewsrestformat") String moodlewsrestformat,
                                                  @Query("wstoken") String wstoken,
                                                  @Query("wsfunction") String wsfunction,
                                                  @Query("assignid") String assignid);
}
