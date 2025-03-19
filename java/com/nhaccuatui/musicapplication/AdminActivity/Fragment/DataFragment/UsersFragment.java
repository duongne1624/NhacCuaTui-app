package com.nhaccuatui.musicapplication.AdminActivity.Fragment.DataFragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhaccuatui.musicapplication.ADTO.User;
import com.nhaccuatui.musicapplication.AdminActivity.Fragment.Adapter.UserAdapter;
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

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> users = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Floating Action Button for adding user
        view.findViewById(R.id.fab_add_user).setOnClickListener(v -> showAddUserDialog());

        fetchUsers();

        return view;
    }

    private void fetchUsers() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getAllData().enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    users.clear();
                    List<List<Object>> userList = (List<List<Object>>) response.body().get("Users");
                    for (List<Object> user : userList) {
                        User u = new User();
                        u.setUser_id((String) user.get(0));
                        u.setUsername((String) user.get(1));
                        u.setEmail((String) user.get(3));
                        u.setPhone_number((String) user.get(4));
                        u.setFullname((String) user.get(7));
                        u.setRole((String) user.get(8));
                        users.add(u);
                    }
                    userAdapter = new UserAdapter(users);
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to fetch users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_user, null);
        builder.setView(dialogView);

        EditText editUsername = dialogView.findViewById(R.id.edit_username);
        EditText editEmail = dialogView.findViewById(R.id.edit_email);
        EditText editPhoneNumber = dialogView.findViewById(R.id.edit_phone_number);
        EditText editFullname = dialogView.findViewById(R.id.edit_fullname);
        EditText editPassword = dialogView.findViewById(R.id.edit_password);
        Spinner spinnerRole = dialogView.findViewById(R.id.spinner_role);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String username = editUsername.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String phoneNumber = editPhoneNumber.getText().toString().trim();
            String fullname = editFullname.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            String role = spinnerRole.getSelectedItem().toString();

            if (username.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || fullname.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            addUser(username, password, email, phoneNumber, fullname, role);
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void addUser(String username, String password, String email, String phoneNumber, String fullname, String role) {
        String adminCode = "ASKALSK";

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<ApiResponse> call = apiService.addUser(username, password, email, phoneNumber, fullname, role, adminCode);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        // Optionally, fetch users again to refresh the list
                        fetchUsers();
                    } else {
                        Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to add user. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
