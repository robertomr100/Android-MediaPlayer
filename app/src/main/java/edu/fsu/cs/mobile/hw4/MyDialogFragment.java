package edu.fsu.cs.mobile.hw4;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class MyDialogFragment extends DialogFragment {
    private EditText  EditTextSong;
    private String song;
    private MyListener myDialogListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.layout_dialog,null);
         EditTextSong= (EditText) view.findViewById(R.id.edit_stream);

        builder.setView(view)
                .setTitle("Stream")
                .setPositiveButton("stream", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        song=EditTextSong.getText().toString();
                        myDialogListener.SendSong(song);

                    }
                });
        return builder.create();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            myDialogListener = (MyListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "test");
        }
    }

    public  interface MyListener
    {
        void SendSong(String song);

    }



}
