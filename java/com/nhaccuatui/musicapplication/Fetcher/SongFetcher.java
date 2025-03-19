package com.nhaccuatui.musicapplication.Fetcher;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nhaccuatui.musicapplication.DTO.Song;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SongFetcher {

    // Callback interface to handle fetch result
    public interface FetchSongsCallback {
        void onSuccess(List<Song> songs);
        void onFailure(String errorMessage);
    }

    // Fetch songs from API
    public static void fetchSongs(String apiUrl, FetchSongsCallback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onFailure("Failed to fetch data: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure("Request failed with status code: " + response.code());
                    return;
                }

                String jsonString = response.body().string();
                try {
                    if (jsonString != null) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<List<String>>>() {}.getType();
                        List<List<String>> rawSongs = gson.fromJson(jsonString, type);

                        // Parse raw JSON into a list of Song objects
                        List<Song> songs = new ArrayList<>();
                        for (List<String> songData : rawSongs) {
                            Song song = new Song(
                                    songData.get(0), // id
                                    songData.get(1), // name
                                    songData.get(2), // releaseDate
                                    songData.get(3), // audioFile
                                    songData.get(4), // imageFile
                                    Long.parseLong(songData.get(5)), // views
                                    Integer.parseInt(songData.get(6)), // likes
                                    songData.size() > 7 ? songData.get(7) : null, // album
                                    songData.get(8), // genre
                                    songData.get(9).replaceAll("&44;", ",")  // artist
                            );
                            songs.add(song);
                        }
                        // Success callback on the main thread
                        callback.onSuccess(songs);
                    } else {
                        callback.onFailure("Response body is null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure("Failed to parse data: " + e.getMessage());
                } finally {
                    // Close the response body to avoid leaks
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            }
        });
    }

    public interface DeleteCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public static void executeDelete(String apiUrl, DeleteCallback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .delete() // DELETE request
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onFailure("Failed to remove song: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure("Error: " + response.code());
                }
            }
        });
    }
}
