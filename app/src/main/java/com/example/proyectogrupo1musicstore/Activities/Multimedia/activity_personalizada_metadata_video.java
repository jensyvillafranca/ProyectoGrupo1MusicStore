package com.example.proyectogrupo1musicstore.Activities.Multimedia;
import static com.example.proyectogrupo1musicstore.Activities.Multimedia.Activity_SubirVideo.autor;
import static com.example.proyectogrupo1musicstore.Activities.Multimedia.Activity_SubirVideo.duracion;
import static com.example.proyectogrupo1musicstore.Activities.Multimedia.Activity_SubirVideo.fechaLanzamiento;
import static com.example.proyectogrupo1musicstore.Activities.Multimedia.Activity_SubirVideo.genero;
import static com.example.proyectogrupo1musicstore.Activities.Multimedia.Activity_SubirVideo.idFavorito;
import static com.example.proyectogrupo1musicstore.Activities.Multimedia.Activity_SubirVideo.idPlayList;
import static com.example.proyectogrupo1musicstore.Activities.Multimedia.Activity_SubirVideo.idUsuario;
import static com.example.proyectogrupo1musicstore.Activities.Multimedia.Activity_SubirVideo.imagenPortadaBase64;
import static com.example.proyectogrupo1musicstore.Activities.Multimedia.Activity_SubirVideo.nombreVideo;
import static com.example.proyectogrupo1musicstore.Activities.Multimedia.Activity_SubirVideo.videoBase64;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.example.proyectogrupo1musicstore.NetworkTasks.Multimedia.Activity_SubirVideoAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.Imagenes.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class activity_personalizada_metadata_video extends DialogFragment {
    //ArrayList para almacenar el valor de cada id de los Edittexts
    private final ArrayList<Integer> editTextIds = new ArrayList<>();
    private final ArrayList<String> metadataExistente = new ArrayList<>(Arrays.asList(null, null, null, null));
    private View dialogView;
    String nombreVideo_aux, autor_aux, genero_aux,duracion_aux;
    private Context contextMain;
    private Uri videoUriMain;



    /*Expresion*/
    String[] expresiones_regulares = new String[]{
            "[A-Za-z0-9À-ÖØ-öø-ÿ!,. ]+",      //edittext 1 (Autor del video)
            "[A-Za-z0-9 ]+",                      //edittext 2 (Nombre del video)
            "[A-Za-zÀ-ÖØ-öø-ÿ/:,!$., ]+"  //edittext 3 (Genero)
    };

    public static activity_personalizada_metadata_video newInstance(Context context, Uri videoUri) {
        activity_personalizada_metadata_video fragment = new activity_personalizada_metadata_video();
        Bundle args = new Bundle();
        fragment.setArguments(args, context, videoUri);
        return fragment;
    }

    private void setArguments(Bundle args, Context context, Uri videoUri) {
        contextMain = context;
        videoUriMain = videoUri;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Tipo de fuente para el layout
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.fuente_jost_global);

        // Arreglo para los labels de los EditText en la pantalla modal
        String[] labels = {"Autor:", "Nombre del video:", "Género del video:"};

        // Inflar el layout personalizado
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_personalizada_metadata, null);

        //Acceder al RelativeLayout
        RelativeLayout relativeLayout = view.findViewById(R.id.relativeLayout);

        // Crear ImageView para mostrar la imagen
        ImageView imageView = new ImageView(getActivity());
        imageView.setId(View.generateViewId());
        imageView.setImageResource(R.drawable.iconodecadaarchivovideo);

        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        imageParams.addRule(RelativeLayout.CENTER_HORIZONTAL); // Centrar horizontalmente en el RelativeLayout
        imageParams.setMargins(0, 0, 0, 20); // Margen superior e inferior para la imagen

        imageView.setLayoutParams(imageParams);
        relativeLayout.addView(imageView);


        //Crear TextView para el título de la ventana modal
        TextView textViewTitulo = new TextView(getActivity());
        textViewTitulo.setId(View.generateViewId());
        textViewTitulo.setText("Nos gustaría que compartieras más información a cerca de este video en particular:");
        textViewTitulo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // Tamaño de texto para el título
        textViewTitulo.setTextColor(Color.WHITE); // Color del texto del título

        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        titleParams.setMargins(0, 200, 0, 0);

        textViewTitulo.setLayoutParams(titleParams);
        textViewTitulo.setGravity(Gravity.CENTER_HORIZONTAL);
        relativeLayout.addView(textViewTitulo);
        textViewTitulo.setTypeface(typeface);

        // ID del último elemento añadido al layout para colocar los siguientes elementos debajo de este
        int lastElementId = R.id.relativeLayout;

        // Crear los labels y EditTexts dinámicamente
        for (int i = 0; i < labels.length; i++) {
            // Crear TextView para el label
            TextView textViewLabel = new TextView(getActivity());
            textViewLabel.setId(View.generateViewId());
            textViewLabel.setText(labels[i]);
            textViewLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // Tamaño de texto para el label
            textViewLabel.setTextColor(Color.WHITE); // Color del texto del label

            RelativeLayout.LayoutParams labelParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);

            labelParams.addRule(RelativeLayout.BELOW, lastElementId);

            if (i == 0) {
                labelParams.addRule(RelativeLayout.BELOW, textViewTitulo.getId());
                labelParams.setMargins(0, 50, 0, 0); // Aplica un margen superior más grande para el primer label
            }

            textViewLabel.setLayoutParams(labelParams);
            relativeLayout.addView(textViewLabel);
            textViewLabel.setTypeface(typeface);

            // Crear EditText para la entrada de datos
            EditText editText = new EditText(getActivity());
            int editTextId = View.generateViewId(); // Generar un ID y guardarlo
            editText.setId(editTextId); // Asignar el ID al EditText

            //editText.setHint(labels[i]);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16); // Tamaño de texto para el edit text
            editText.setTextColor(Color.WHITE); // Color del texto para el edit text

            RelativeLayout.LayoutParams editTextParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            editTextParams.addRule(RelativeLayout.BELOW, textViewLabel.getId());

            // Crear un InputFilter que use la expresión regular
            String expresion = expresiones_regulares[i];
            InputFilter filter = new InputFilter() {
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    if (source == null || source.toString().matches(expresion)) {
                        return null;
                    }
                    return "";
                }
            };
            // Aplicar el InputFilter al EditText
            editText.setFilters(new InputFilter[]{filter});


            editText.setLayoutParams(editTextParams);
            relativeLayout.addView(editText);
            editText.setTypeface(typeface);

            //Guarda el ID del edittext en el ArrayList que declaramos arriba
            editTextIds.add(editTextId);
            Log.d("Arreglo",""+editTextIds);

            // El ID del último elemento se actualiza al del EditText
            lastElementId = editText.getId();
        }
        this.dialogView = view;

        // Llamada al método para establecer datos por defecto si existen
        dataPorDefecto();

        // Crear el AlertDialog y devolverlo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        obtenerMetadataUser();
                    }
                });
        return builder.create();
    }

    //Método para obtener el valor de cada EditText
    // Método para recoger los valores de los EditTexts usando sus IDs
    private void obtenerMetadataUser() {
        int contadorAux = 0;
        EditText editText = null;
        String inputText = null;
        //Obtener los valores que el usuario ingresa en los EditText
        for (int id : editTextIds) {
            editText = (EditText) dialogView.findViewById(id);
            inputText = editText.getText().toString();

            if(contadorAux == 0){
                if(inputText.isEmpty()){
                    //Si el usuario no conoce el nombre del autor y decide dejarlo como vacío
                    autor_aux = null;
                }else{
                    //Mandar el valor de nombre del autor en caso de que el usuario si lo llene
                    autor_aux = inputText;
                }
            }
            if(contadorAux == 1){
                if(inputText.isEmpty()){
                    //Si el usuario no conoce el nombre del video y decide dejarlo como vacío
                    nombreVideo_aux = new FileUtils(contextMain).getFileName(videoUriMain);
                }else{
                    //Mandar el valor de nombre del video en caso de que el usuario si lo llene
                    nombreVideo_aux = inputText;
                }
            }
            if(contadorAux == 2){
                if(inputText.isEmpty()){
                    //Si el usuario no conoce el género del video y decide dejarlo como vacío
                    genero_aux = null;
                }else{
                    //Mandar el género del video en caso de que el usuario si lo llene
                    genero_aux = inputText;
                }
            }
            contadorAux++;
        }
        //Invocar el método que manda todos los datos al PHP.
        subirAudioFirebase();

        if (getActivity() instanceof Activity_SubirVideo) {
            ((Activity_SubirVideo) getActivity()).videoItems();
        }
    }

    //Mandar todos los valores al PHP mediante el llamado del siguiente metódo
    public void subirAudioFirebase(){
        Log.d("Mensaje", "se mandaron los datos");
        new Activity_SubirVideoAsyncTask(getContext(), autor_aux, idUsuario, videoBase64 , imagenPortadaBase64, idPlayList, idFavorito, nombreVideo_aux, genero_aux, duracion, fechaLanzamiento).execute();
    }


    //Método para poner los valores que ya existen en la metadata del video y que no serán necesarios capturar del usuario
    public void dataPorDefecto() {
        // Asignar valores existentes a metadataExistente
        if(autor != null) {
            metadataExistente.set(0, autor);
        }
        if(nombreVideo != null) {
            metadataExistente.set(1, nombreVideo);
        }
        if(genero != null) {
            metadataExistente.set(2, genero);
        }

        // Colocar valores en los EditText y cambiar color del texto a gris
        for(int i = 0; i < editTextIds.size(); i++) {
            EditText editText = dialogView.findViewById(editTextIds.get(i));
            if(metadataExistente.size() > i && metadataExistente.get(i) != null) {
                editText.setText(metadataExistente.get(i));
                editText.setTextColor(Color.GRAY); // Cambiar color del texto a gris
                editText.setEnabled(false);
            }
        }
    }


}