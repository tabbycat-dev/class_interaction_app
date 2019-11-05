package com.example.classinteraction.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.classinteraction.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TextDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TextDialog extends DialogFragment {

    private static final String PROMPT_KEY = "prompt_key";
    private static final String MSG_KEY = "msg_key";


    private TextDialog.TextDialogListener mListener;
    private TextView message;

    public static TextDialog newInstance(String prompt, String msg) {

        Bundle args = new Bundle();
        args.putString(PROMPT_KEY, prompt);
        args.putString(MSG_KEY, msg);


        TextDialog fragment = new TextDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TextDialog.TextDialogListener) {
            mListener = (TextDialog.TextDialogListener) context;
        } else {
            throw new ClassCastException("Caller must implement Listener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_text_dialog, container, false);

        String prompt = getArguments().getString(PROMPT_KEY);
        String msg = getArguments().getString(MSG_KEY);


        TextView tvPrompt = (TextView) view.findViewById(R.id.tvPrompt);
        tvPrompt.setText(prompt);

        TextView message = (TextView) view.findViewById(R.id.messageTV);
        message.setText(msg);

        Button btnOk = (Button) view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTextDialogOK(true);
                dismiss();
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTextDialogOK(false);
                dismiss();
            }
        });

        return view;
    }

    public interface TextDialogListener {
        void onTextDialogOK(boolean agree);
    }

}

