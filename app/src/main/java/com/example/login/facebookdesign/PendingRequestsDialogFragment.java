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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.login.R;

import java.util.List;

public class PendingRequestsDialogFragment extends DialogFragment {

    private List<String> pendingRequests;
    String token;
    String username;

    public PendingRequestsDialogFragment(List<String> pendingRequests,String token,String username) {
        this.pendingRequests = pendingRequests;
        this.token=token;
        this.username=username;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_pending_requests, null);

        ListView pendingRequestsListView = view.findViewById(R.id.pending_requests_list);
        Button closeButton = view.findViewById(R.id.close_button);

        // Create and set adapter for the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, pendingRequests);
        pendingRequestsListView.setAdapter(adapter);

        // Set item click listener for the ListView
        pendingRequestsListView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedUsername = pendingRequests.get(position);

            // Navigate to the profile page of the selected user
            Intent intent = new Intent(requireContext(), ProfileActivity.class);

            intent.putExtra("Username",selectedUsername);
            intent.putExtra("myUsername",username);
            intent.putExtra("Token",token);


            startActivity(intent);
        });

        closeButton.setOnClickListener(v -> dismiss());

        builder.setView(view);

        return builder.create();

    }
}
