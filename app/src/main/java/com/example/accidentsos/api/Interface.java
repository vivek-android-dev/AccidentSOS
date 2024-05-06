package com.example.accidentsos.api;

import com.example.accidentsos.DistanceResponse.GDistanceResponse;
import com.example.accidentsos.GoogleResponseModel;
import com.example.accidentsos.MapResponse.NearByHospitalResponse;
import com.example.accidentsos.ServerResponses.CommonResponse;
import com.example.accidentsos.ServerResponses.FriendsResponse;
import com.example.accidentsos.ServerResponses.HistoryResponse;
import com.example.accidentsos.ServerResponses.SignInResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface Interface {


    @FormUrlEncoded
    @POST("signup.php")
    Call<CommonResponse> signUp(@Field("name")String name, @Field("age")String age,@Field("gender")String gender,
                                @Field("bloodgroup")String bloodgroup,@Field("email")String email,@Field("mobilenumber")String mobilenumber,
                                 @Field("username")String username,@Field("password")String password);

    @FormUrlEncoded
    @POST("signin.php")
    Call<SignInResponse> signIn(@Field("username")String username, @Field("password")String password);

    @FormUrlEncoded
    @POST("alert.php")
    Call<CommonResponse> alert(@Field("id")String id, @Field("address")String address, @Field("latitude")String latitude,
                               @Field("longitude")String longitude, @Field("vehicle")String vehicle);

    @FormUrlEncoded
    @POST("feedback.php")
    Call<CommonResponse> feedback(@Field("user_id")String user_id, @Field("feedback")String feedback, @Field("comments")String comments);



    @POST("friends.php")
    Call<FriendsResponse> friends();

    @FormUrlEncoded
    @POST("json")
    Call<NearByHospitalResponse> nearHospital(@Field("keyword")String keyword,@Field("location")String location,
                                              @Field("radius")String radius,@Field("type")String type,@Field("key")String key);

    @POST("json?keyword=hospital&location=13.0289644,80.0185776&radius=5000&type=hospital&key=AIzaSyB99Ol78n6mm0nKWURPaqrLPm2jtjYFEKw")
        Call<NearByHospitalResponse> nearHospital1();

    @GET
    Call<GoogleResponseModel> getNearByPlaces(@Url String url);

    @GET
    Call<GDistanceResponse> getDistance(@Url String url);

    @FormUrlEncoded
    @POST("accept.php")
    Call<CommonResponse> accept(@Field("id")String id, @Field("uid")String uid);

    @FormUrlEncoded
    @POST("hospital_info.php")
    Call<CommonResponse> hospitalInfo(@Field("hospitalname")String hospitalname, @Field("attendername")String attendername,
                                      @Field("attendernumber")String attendernumber, @Field("patientid")String patientid
            , @Field("attenderid")String attenderid);

    @FormUrlEncoded
    @POST("history.php")
    Call<HistoryResponse> history(@Field("id")String id);

}
