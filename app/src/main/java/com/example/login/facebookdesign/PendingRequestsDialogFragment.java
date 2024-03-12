package com.example.login.facebookdesign;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;

import java.util.List;

public class PendingRequestsDialogFragment extends DialogFragment {

    private List<String> pendingRequests;

    public PendingRequestsDialogFragment(List<String> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_pending_requests, null);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        // Create and set adapter for the RecyclerView
        FriendsAdapter adapter = new FriendsAdapter(requireContext(), pendingRequests);
        recyclerView.setAdapter(adapter);

        builder.setView(view)
                .setTitle("Pending Friend Requests")
                .setPositiveButton("Close", (dialog, which) -> dismiss());

        return builder.create();
    }
}
