package com.example.androidassignments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class MessageFragment extends Fragment {

    public static final String KEY_MESSAGE = "message";
    public static final String KEY_ID = "id";

    // Not null when running on a tablet, null when running on a phone
    private final ChatWindow chatWindow;

    public MessageFragment(ChatWindow chatWindow) {
        this.chatWindow = chatWindow;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        Bundle args = getArguments();
        String message = args != null ? args.getString(KEY_MESSAGE) : "";
        long id = args != null ? args.getLong(KEY_ID) : -1;

        TextView messageText = view.findViewById(R.id.messageText);
        TextView messageIdText = view.findViewById(R.id.messageIdText);
        messageText.setText(getString(R.string.message_label, message));
        messageIdText.setText(getString(R.string.id_label, id));

        Button deleteButton = view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            if (chatWindow != null) {
                // Tablet: tell the ChatWindow to delete the message and remove this fragment
                chatWindow.deleteMessage(id, this);
            } else {
                // Phone: return the id to ChatWindow and finish the details activity
                Intent result = new Intent();
                result.putExtra(KEY_ID, id);
                getActivity().setResult(Activity.RESULT_OK, result);
                getActivity().finish();
            }
        });

        return view;
    }
}
