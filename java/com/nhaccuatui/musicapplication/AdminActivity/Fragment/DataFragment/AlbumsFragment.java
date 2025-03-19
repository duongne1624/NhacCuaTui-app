package com.nhaccuatui.musicapplication.AdminActivity.Fragment.DataFragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.nhaccuatui.musicapplication.ADTO.Album;
import com.nhaccuatui.musicapplication.ADTO.Artist;
import com.nhaccuatui.musicapplication.AdminActivity.Fragment.Adapter.AlbumAdapter;
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

public class AlbumsFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private RecyclerView recyclerView;
    private AlbumAdapter albumAdapter;
    private List<Album> albums = new ArrayList<>();
    private int albumArtistId = -1; // To hold the selected artist ID
    private String albumCoverPath = ""; // To hold the album cover image path

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_albums);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Floating Action Button for adding album
        //view.findViewById(R.id.fab_add_album).setOnClickListener(v -> showAddAlbumDialog());

        fetchAlbums();

        return view;
    }

    private void fetchAlbums() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getAllData().enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    albums.clear();
                    List<List<Object>> albumList = (List<List<Object>>) response.body().get("Albums");

                    for (List<Object> album : albumList) {
                        Album a = new Album();
                        a.setAlbumId(Integer.parseInt((String) album.get(0)));
                        a.setAlbumName((String) album.get(1));
                        a.setArtistName((String) album.get(2));
                        a.setAlbumCover((String) album.get(4));
                        a.setReleaseDate((String) album.get(3));
                        albums.add(a);
                    }

                    albumAdapter = new AlbumAdapter(getContext(), albums);
                    recyclerView.setAdapter(albumAdapter);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to fetch albums: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    private void showAddAlbumDialog() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        // Fetch the artists list first to populate the artist selection dialog
        apiService.getAllData().enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();
                    List<List<Object>> artistList = (List<List<Object>>) data.get("Artists");

                    if (artistList != null && !artistList.isEmpty()) {
                        // Convert the artist data into Artist objects
                        List<Artist> artists = new ArrayList<>();
                        for (List<Object> artist : artistList) {
                            Artist a = new Artist();
                            a.setArtistId((int) artist.get(0)); // artistId
                            a.setArtistName((String) artist.get(1)); // artistName
                            a.setBio((String) artist.get(2)); // bio
                            a.setAvatarImage((String) artist.get(3)); // avatarImage
                            artists.add(a);
                        }
                        // Now display the dialog to select an artist
                        showArtistListDialog(artists);
                    } else {
                        Toast.makeText(getContext(), "Không tìm thấy ca sĩ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Không thể lấy danh sách ca sĩ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showArtistListDialog(List<Artist> artistList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chọn Ca Sĩ");

        // Create a list of artist names
        List<String> artistNames = new ArrayList<>();
        for (Artist artist : artistList) {
            artistNames.add(artist.getArtistName()); // Add the artist names to the list
        }

        builder.setItems(artistNames.toArray(new String[0]), (dialog, which) -> {
            Artist selectedArtist = artistList.get(which); // Get the selected artist
            int selectedArtistId = selectedArtist.getArtistId(); // Get artistId

            // Now, show the dialog to input album details
            showAlbumDetailsDialog(selectedArtistId);
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void showAlbumDetailsDialog(int artistId) {
        // Now, create another dialog for adding album details
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_album, null);
        builder.setView(dialogView);

        EditText editAlbumName = dialogView.findViewById(R.id.edit_album_name);
        EditText editReleaseDate = dialogView.findViewById(R.id.edit_release_date);
        EditText editCoverImage = dialogView.findViewById(R.id.edit_album_cover);  // Cover image URL or file path

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String albumName = editAlbumName.getText().toString().trim();
            String releaseDate = editReleaseDate.getText().toString().trim();
            String coverImage = editCoverImage.getText().toString().trim();

            if (albumName.isEmpty() || releaseDate.isEmpty() || coverImage.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call the addAlbum method to add the album
            addAlbum(albumName, artistId, releaseDate, coverImage);
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void addAlbum(String albumName, int artistId, String releaseDate, String coverImage) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<ApiResponse> call = apiService.addAlbum(albumName, artistId, releaseDate, coverImage);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        fetchAlbums(); // Refresh the album list after adding
                    } else {
                        Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to add album. Please try again.", Toast.LENGTH_SHORT).show();
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
