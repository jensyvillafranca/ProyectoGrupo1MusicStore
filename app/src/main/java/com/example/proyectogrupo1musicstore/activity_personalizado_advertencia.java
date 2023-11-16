package com.example.proyectogrupo1musicstore;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class activity_personalizado_advertencia extends DialogFragment {

    private static final String ARG_TEXT = "text_arg";

    public static activity_personalizado_advertencia newInstance(String text) {
        activity_personalizado_advertencia fragment = new activity_personalizado_advertencia();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflar el layout personalizado
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_personalizado_advertencia, null);

        // Asignar texto al TextView del layout inflado
        TextView textViewAdvertencia = view.findViewById(R.id.textViewMensajeExitoso);
        if (textViewAdvertencia != null) {
            // Obtener el texto desde los argumentos, con "" como valor por defecto si es nulo
            Bundle arguments = getArguments();
            String text = (arguments != null) ? arguments.getString(ARG_TEXT, "") : "";
            textViewAdvertencia.setText(text);
        }

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