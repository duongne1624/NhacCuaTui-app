package com.nhaccuatui.musicapplication.AdminActivity.Fragment.DataFragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhaccuatui.musicapplication.ADTO.Song;  // Giả sử bạn đã tạo model Song
import com.nhaccuatui.musicapplication.AdminActivity.Fragment.Adapter.SongAdapter;  // Adapter cho bài hát
import com.nhaccuatui.musicapplication.DB.ApiResponse;
import com.nhaccuatui.musicapplication.DB.ApiService;
import com.nhaccuatui.musicapplication.DB.RetrofitClient;
import com.nhaccuatui.musicapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private List<Song> songs = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Floating Action Button for adding song
        //view.findViewById(R.id.fab_add_song).setOnClickListener(v -> showAddSongDialog());

        fetchSongs();

        return view;
    }

    private void fetchSongs() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getAllData().enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    songs.clear();
                    List<List<Object>> songList = (List<List<Object>>) response.body().get("Songs");

                    for (List<Object> song : songList) {
                        Song s = new Song();

                        // Safely cast songId from String if needed
                        String songIdStr = (String) song.get(0); // Assume songId is sent as a String
                        try {
                            int songId = Integer.parseInt(songIdStr); // Convert String to Integer
                            s.setSongId(songId);
                        } catch (NumberFormatException e) {
                            s.setSongId(0); // Default value in case of an error
                        }

                        s.setSongName((String) song.get(1));  // Assuming songName is a String
                        s.setArtistName((String) song.get(2)); // Assuming artistName is a String
                        s.setSongCover((String) song.get(7)); // Assuming songCover is a String (URL)

                        songs.add(s);
                    }

                    songAdapter = new SongAdapter(getContext(), songs);
                    recyclerView.setAdapter(songAdapter);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to fetch songs: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    private void showAddSongDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_song, null);
        builder.setView(dialogView);

        EditText editSongName = dialogView.findViewById(R.id.edit_song_name);
        EditText editArtistName = dialogView.findViewById(R.id.edit_artist_name);
        EditText editSongCover = dialogView.findViewById(R.id.edit_song_cover);
        EditText editReleaseDate = dialogView.findViewById(R.id.edit_release_date);
        EditText editFileName = dialogView.findViewById(R.id.edit_file_name);
        EditText editGenreId = dialogView.findViewById(R.id.edit_genre_id);
        EditText editAlbumId = dialogView.findViewById(R.id.edit_album_id);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String songName = editSongName.getText().toString().trim();
            String artistName = editArtistName.getText().toString().trim();
            String songCover = editSongCover.getText().toString().trim();
            String releaseDate = editReleaseDate.getText().toString().trim();
            String fileName = editFileName.getText().toString().trim();
            String genreIdStr = editGenreId.getText().toString().trim();
            String albumIdStr = editAlbumId.getText().toString().trim();

            // Kiểm tra các trường nhập liệu
            if (songName.isEmpty() || artistName.isEmpty() || songCover.isEmpty() || releaseDate.isEmpty() || fileName.isEmpty() || genreIdStr.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            int genreId = Integer.parseInt(genreIdStr);
            Integer albumId = albumIdStr.isEmpty() ? null : Integer.parseInt(albumIdStr);

            // Gửi bài hát mới đến API
            addSong(songName, artistName, songCover, releaseDate, fileName, genreId, albumId);
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void addSong(String songName, String artistName, String songCover, String releaseDate, String fileName, int genreId, Integer albumId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<ApiResponse> call = apiService.addSong(songName, releaseDate, fileName, songCover, genreId, albumId);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        fetchSongs(); // Refresh the list of songs
                    } else {
                        Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to add song. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    */
}
