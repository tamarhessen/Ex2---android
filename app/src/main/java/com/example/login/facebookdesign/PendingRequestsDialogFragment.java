package com.example.login.facebookdesign;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.login.R;
import com.example.login.viewModels.UsersViewModel;

import java.util.List;

public class PendingRequestsDialogFragment extends DialogFragment {

//    private UsersViewModel usersViewModel;
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
//        LayoutInflater inflater = requireActivity().getLayoutInflater();
//
//        // Initialize ViewModel
//        usersViewModel = new ViewModelProvider(requireActivity()).get(UsersViewModel.class);
//
//        View view = inflater.inflate(R.layout.dialog_pending_requests, null);
//
//        // Retrieve username and token from Intent
//        Intent activityIntent = getActivity().getIntent();
//        String token = null;
//        String username = null;
//        if (activityIntent != null) {
//            token = activityIntent.getStringExtra("Token");
//            username = activityIntent.getStringExtra("Username");
//            usersViewModel.setToken(token);
//            usersViewModel.setUserid(username);
//
//
//
//                usersViewModel.getFriends().observe(this, users -> {
//                    if (users != null && !users.isEmpty()) {
//                       ;
//                    } else {
//                        Toast.makeText(requireContext(), "No friends found", Toast.LENGTH_SHORT).show();
//                    }
//                });
//        }
//        }
//
//        builder.setView(view)
//                .setTitle("Pending Friend Requests")
//                .setPositiveButton("Close", (dialog, which) -> {
//                    // Close the dialog
//                    dismiss();
//                });
//
//        return builder.create();
//    }
}
