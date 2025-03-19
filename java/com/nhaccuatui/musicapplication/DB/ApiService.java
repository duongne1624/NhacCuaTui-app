package com.nhaccuatui.musicapplication.DB;

import com.nhaccuatui.musicapplication.ADTO.*;

import java.util.Date;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

// Define API service interface for Retrofit
public interface ApiService {

    @GET("API/MyAPI/GetAllData")
    Call<Map<String, Object>> getAllData();

    // Songs
    @FormUrlEncoded
    @POST("API/MyAPI/AddSong")
    Call<ApiResponse> addSong(
            @Field("songName") String songName,
            @Field("releaseDate") String releaseDate,
            @Field("fileName") String fileName,
            @Field("thumbnailImage") String thumbnailImage,
            @Field("genreId") int genreId,
            @Field("albumId") Integer albumId
    );

    @FormUrlEncoded
    @PUT("API/MyAPI/UpdateSong")
    Call<ApiResponse> updateSong(
            @Field("songId") int songId,
            @Field("songName") String songName,
            @Field("releaseDate") String releaseDate,
            @Field("fileName") String fileName,
            @Field("thumbnailImage") String thumbnailImage,
            @Field("genreId") int genreId,
            @Field("albumId") Integer albumId
    );

    @DELETE("API/MyAPI/DeleteSong")
    Call<ApiResponse> deleteSong(@Query("songId") int songId);

    // Artists
    @FormUrlEncoded
    @POST("API/MyAPI/AddArtist")
    Call<ApiResponse> addArtist(
            @Field("artistName") String artistName,
            @Field("bio") String bio,
            @Field("avatarImage") String avatarImage
    );

    @FormUrlEncoded
    @PUT("API/MyAPI/UpdateArtist")
    Call<ApiResponse> updateArtist(
            @Field("artistId") int artistId,
            @Field("artistName") String artistName,
            @Field("bio") String bio,
            @Field("avatarImage") String avatarImage
    );

    @DELETE("API/MyAPI/DeleteArtist")
    Call<ApiResponse> deleteArtist(@Query("artistId") int artistId);

    // Albums
    @FormUrlEncoded
    @POST("API/MyAPI/AddAlbum")
    Call<ApiResponse> addAlbum(
            @Field("albumName") String albumName,
            @Field("artistId") int artistId,
            @Field("releaseDate") String releaseDate,
            @Field("coverImage") String coverImage
    );

    @FormUrlEncoded
    @PUT("API/MyAPI/UpdateAlbum")
    Call<ApiResponse> updateAlbum(
            @Field("albumId") int albumId,
            @Field("albumName") String albumName,
            @Field("artistId") int artistId,
            @Field("releaseDate") String releaseDate,
            @Field("coverImage") String coverImage
    );

    @DELETE("API/MyAPI/DeleteAlbum")
    Call<ApiResponse> deleteAlbum(@Query("albumId") int albumId);

    // Users
    @FormUrlEncoded
    @POST("API/MyAPI/AddUser")
    Call<ApiResponse> addUser(
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email,
            @Field("phoneNumber") String phoneNumber,
            @Field("fullname") String fullname,
            @Field("role") String role,
            @Field("adminCode") String adminCode
    );

    @FormUrlEncoded
    @PUT("API/MyAPI/UpdateUser")
    Call<ApiResponse> updateUser(
            @Field("userId") int userId,
            @Field("username") String username,
            @Field("email") String email,
            @Field("phoneNumber") String phoneNumber,
            @Field("fullname") String fullname,
            @Field("role") String role,
            @Field("adminCode") String adminCode
    );


    @DELETE("API/MyAPI/DeleteUser")
    Call<ApiResponse> deleteUser(
            @Query("userId") int userId,
            @Query("adminCode") String adminCode
    );

}
