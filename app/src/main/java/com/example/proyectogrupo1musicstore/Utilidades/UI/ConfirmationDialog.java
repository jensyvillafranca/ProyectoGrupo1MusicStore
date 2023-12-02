package com.example.proyectogrupo1musicstore.Utilidades.UI;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

public class ConfirmationDialog {

    public static void showConfirmationDialog(Context context, String title, String message, DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener) {
        // Crea un AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Coloca el titulo y mensaje
        builder.setTitle(title);
        builder.setMessage(message);

        // Pone la opcion positiva Si
        builder.setPositiveButton("Si", positiveClickListener);

        // Pone la opcion negativa
        builder.setNegativeButton("No", negativeClickListener);

        // Crea y muestra la alerta
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
