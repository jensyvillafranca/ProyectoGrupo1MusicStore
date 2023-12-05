package com.example.proyectogrupo1musicstore.Activities.Auth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.proyectogrupo1musicstore.R;

public class activity_personalizada_huella extends DialogFragment {

    private static final String ARG_TEXT = "text_arg";

    public static activity_personalizada_huella newInstance(String text) {
        activity_personalizada_huella fragment = new activity_personalizada_huella();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflar el layout personalizado
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_personalizada_huella, null);

        // Crear el AlertDialog y devolverlo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel(); // Esto cerrará el diálogo sin condiciones
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
