package com.narmware.vvmexam.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.narmware.vvmexam.R;
import com.squareup.picasso.Picasso;

public class TnCDialogFragment extends DialogFragment {

    Button mBtnOk;

    @SuppressLint("ValidFragment")
    private TnCDialogFragment() { /*empty*/ }

    /** creates a new instance of PropDialogFragment */
    public static TnCDialogFragment newInstance() {
        return new TnCDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //getting proper access to LayoutInflater is the trick. getLayoutInflater is a                   //Function
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_terms, null);
        mBtnOk=view.findViewById(R.id.btn_ok);

        //name = getArguments().getString("name");

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle("Terms and Conditions");
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return builder.create();
    }
}