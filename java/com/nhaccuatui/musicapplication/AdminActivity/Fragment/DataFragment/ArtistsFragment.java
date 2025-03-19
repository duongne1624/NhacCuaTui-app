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

import com.nhaccuatui.musicapplication.ADTO.Artist;
import com.nhaccuatui.musicapplication.AdminActivity.Fragment.Adapter.ArtistAdapter;
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

public class ArtistsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArtistAdapter artistAdapter;
    private List<Artist> artists = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_artists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Floating Action Button for adding artist
        //view.findViewById(R.id.fab_add_artist).setOnClickListener(v -> showAddArtistDialog());

        fetchArtists();

        return view;
    }

    private void fetchArtists() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getAllData().enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    artists.clear();
                    List<List<Object>> artistList = (List<List<Object>>) response.body().get("Artists");

                    for (List<Object> artist : artistList) {
                        Artist a = new Artist();

                        // Safely cast artistId from String if needed
                        String artistIdStr = (String) artist.get(0); // Assume artistId is sent as a String
                        try {
                            int artistId = Integer.parseInt(artistIdStr); // Convert String to Integer
                            a.setArtistId(artistId);
                        } catch (NumberFormatException e) {
                            // Handle the case where artistId is not a valid number
                            a.setArtistId(0); // Default value in case of an error
                        }

                        a.setArtistName((String) artist.get(1)); // Assuming artistName is a String
                        a.setBio((String) artist.get(2)); // Assuming bio is a String
                        a.setAvatarImage((String) artist.get(3)); // Assuming avatarImage is a String (URL)

                        artists.add(a);
                    }

                    artistAdapter = new ArtistAdapter(getContext(), artists);
                    recyclerView.setAdapter(artistAdapter);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to fetch artists: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    private void showAddArtistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_artist, null);
        builder.setView(dialogView);

        EditText editArtistName = dialogView.findViewById(R.id.edit_artist_name);
        EditText editBio = dialogView.findViewById(R.id.edit_bio);
        EditText editAvatarImage = dialogView.findViewById(R.id.edit_avatar_image);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String artistName = editArtistName.getText().toString().trim();
            String bio = editBio.getText().toString().trim();
            String avatarImage = editAvatarImage.getText().toString().trim();

            if (artistName.isEmpty() || bio.isEmpty() || avatarImage.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            addArtist(artistName, bio, avatarImage);
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void addArtist(String artistName, String bio, String avatarImage) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<ApiResponse> call = apiService.addArtist(artistName, bio, avatarImage);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        fetchArtists(); // Refresh the list of artists
                    } else {
                        Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to add artist. Please try again.", Toast.LENGTH_SHORT).show();
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