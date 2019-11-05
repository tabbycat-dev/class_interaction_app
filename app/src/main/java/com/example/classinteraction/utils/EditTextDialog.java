package com.example.classinteraction.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.classinteraction.R;

public class EditTextDialog extends DialogFragment {

    private static final String PROMPT_KEY = "prompt_key";
    private static final String CODE_KEY = "class_code_key";
    private static final String NAME_KEY = "class_name_key";


    private EditTextDialogListener mListener;
    private EditText etClassCode, etClassName;

    public static EditTextDialog newInstance(String prompt) {

        Bundle args = new Bundle();
        args.putString(PROMPT_KEY, prompt);

        EditTextDialog fragment = new EditTextDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditTextDialogListener) {
            mListener = (EditTextDialogListener) context;
        } else {
            throw new ClassCastException("Caller must implement Listener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_text_dialog, container, false);

        String prompt = getArguments().getString(PROMPT_KEY);

        TextView tvPrompt = (TextView) view.findViewById(R.id.tvPrompt);
        tvPrompt.setText(prompt);

        etClassCode = (EditText) view.findViewById(R.id.etClassCode);
        etClassName = (EditText) view.findViewById(R.id.etClassName);
        Button btnOk = (Button) view.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classcode = etClassCode.getText().toString();
                String classname = etClassName.getText().toString();
                mListener.onEditTextDialogOK(classcode , classname, getTag());
                dismiss();
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    public interface EditTextDialogListener {
        void onEditTextDialogOK(String classcode, String classname, String tag);
    }

}
