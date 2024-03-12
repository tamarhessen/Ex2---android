package com.example.login.facebookdesign;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.login.R;
import com.example.login.facebookdesign.UserCreatePost;
import com.example.login.viewModels.UsersViewModel;

import java.util.List;

public class PendingRequestsDialogFragment extends DialogFragment {

    private UsersViewModel usersViewModel;
    private ArrayAdapter<String> adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Initialize ViewModel
        usersViewModel = new ViewModelProvider(requireActivity()).get(UsersViewModel.class);

        View view = inflater.inflate(R.layout.dialog_pending_requests, null);

        // Retrieve username and token from Intent
        Intent activityIntent = getActivity().getIntent();
        String token = null;
        String username = null;
        if (activityIntent != null) {
            token = activityIntent.getStringExtra("Token");
            username = activityIntent.getStringExtra("Username");
            usersViewModel.setToken(token);
            usersViewModel.setUserid(username);

            // Initialize adapter
            adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1);
            ListView pendingListView = view.findViewById(R.id.pending_requests_list);
            pendingListView.setAdapter(adapter);

            usersViewModel.getCurrentUser(username, token).observe(this, new Observer<UserCreatePost>() {
                @Override
                public void onChanged(UserCreatePost userCreatePost) {
                    // Retrieve pending friend requests list
                    List<String> pendingList = userCreatePost.getPendingList();
                    if (pendingList != null && !pendingList.isEmpty()) {
                        // Update the adapter with the pending friend requests list
                        adapter.clear();
                        adapter.addAll(pendingList);
                    } else {
                        Toast.makeText(requireContext(), "No pending friend requests found", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        Button closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the dialog
                dismiss();
            }
        });

        builder.setView(view)
                .setTitle("Pending Friend Requests");

        return builder.create();
    }
}
