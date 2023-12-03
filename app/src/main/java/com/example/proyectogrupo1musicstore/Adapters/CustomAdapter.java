package com.example.proyectogrupo1musicstore.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityChat;
import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityEditarGrupo;
import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityGrupoInfo;
import com.example.proyectogrupo1musicstore.Models.vistaDeGrupo;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.DeleteGrupoAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.UpdateFavoritoAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.salirGrupoAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.UI.ConfirmationDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    private final List<vistaDeGrupo> dataList;
    private final Context context;
    private final RecyclerView recyclerView;
    boolean isImage1;
    private final int idUsuario;
    private int tipoAccion;

    public CustomAdapter(Context context, List<vistaDeGrupo> dataList, int idUsuario, RecyclerView recyclerView) {
        this.context = context;
        this.dataList = dataList;
        this.idUsuario = idUsuario;
        this.recyclerView = recyclerView;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_list_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        vistaDeGrupo data = dataList.get(position);

        // Vincula los datos a las vistas en tu diseño de elemento de lista personalizado
        holder.nombreGrupo.setText(data.getText1());
        holder.creadoPor.setText(data.getText2());
        holder.integrantes.setText(data.getText3());
        holder.image.setImageBitmap(data.getImageResource());

        // Obtiene el ImageView del diseño
        ImageView itemImageView = holder.itemView.findViewById(R.id.imageviewGruposIconoFavorito);
        ImageView itemEditar = holder.itemView.findViewById(R.id.imageviewGrupoIconoEditar);
        ImageView imgGrupoInfo = holder.itemView.findViewById(R.id.imageviewListItemImage);
        ImageView itemEliminar = holder.itemView.findViewById(R.id.imageviewGruposIconoEliminal);

        //muestra o esconde boton editar
        if(data.getIdOwner()==idUsuario){
            itemEditar.setVisibility(View.VISIBLE);
        }else{
            itemEditar.setVisibility(View.GONE);
        }

        // Revisa si el estado favorito esta seleccionado
        itemImageView.setImageResource(data.getEstadofavorito() == 0 ? R.drawable.favoritodesmarcado : R.drawable.favoritomarcado);

        // Establece el OnClickListener para alternar la imagen
        itemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cambia el estado favorito
                int newFavoritoState = isImage1 ? 1 : 0;
                data.setEstadofavorito(newFavoritoState);

                //Actualiza la base de datos
                updateFavoritoState(data.getIdgrupo(), data.getEstadofavorito());

                // Actualiza la imagen localmente
                isImage1 = !isImage1;
                itemImageView.setImageResource(isImage1 ? R.drawable.favoritodesmarcado : R.drawable.favoritomarcado);


            }
        });

        //Listener para el la foto del grupo que lleva a la informacion del grupo
        imgGrupoInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pantallaInfo = new Intent(v.getContext(), ActivityGrupoInfo.class);
                pantallaInfo.putExtra("idgrupo", data.getIdgrupo());
                v.getContext().startActivity(pantallaInfo);
            }
        });

        //Listener para boton editar
        itemEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityEditarGrupo.class);
                intent.putExtra("idgrupo", data.getIdgrupo());
                v.getContext().startActivity(intent);
            }
        });

        itemEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.getIdOwner()==idUsuario){
                    //tipoAccion: 1, para eliminar grupo cuando es el dueño
                    tipoAccion = 1;
                }else{
                    //tipoAccion = 0, para salir del grupo cuando no es el dueño
                    tipoAccion = 0;
                }

                if(tipoAccion==0){
                    // Muestra dialogo de confirmacion
                    ConfirmationDialog.showConfirmationDialog(
                            context, // Replace with your activity or fragment context
                            "Confirmación",
                            "¿Está seguro de que desea abandonar este grupo?, ¡Esta acción es definitiva!",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Si el usuario da click en si
                                    deleteIntegrante(idUsuario, data.getIdgrupo(), dataList);
                                }
                            },
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Si el usuario da click en no
                                    dialog.dismiss();
                                }
                            }
                    );
                }else{
                    // Muestra dialogo de confirmacion
                    ConfirmationDialog.showConfirmationDialog(
                            context, // Replace with your activity or fragment context
                            "Confirmación",
                            "¿Está seguro de que desea ElIMINAR este grupo?, ¡Esta acción es definitiva!",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Si el usuario da click en si
                                    deleteGrupo(data.getIdgrupo(), dataList);
                                }
                            },
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Si el usuario da click en no
                                    dialog.dismiss();
                                }
                            }
                    );
                }
            }
        });

        holder.nombreGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityChat.class);
                intent.putExtra("idgrupo", data.getIdgrupo());
                intent.putExtra("nombregrupo", data.getText1());
                intent.putExtra("image", data.getUrl());
                v.getContext().startActivity(intent);
            }
        });

    }

    //Devuelve el número total de elementos en la lista de datos.
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void updateFavoritoState(int idGrupo, int newFavoritoState) {

        // Construye el JSON
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("estadofavorito", newFavoritoState);
            jsonData.put("idgrupo", idGrupo);
            jsonData.put("idusuario", idUsuario);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new UpdateFavoritoAsyncTask().execute(jsonData.toString());
    }

    //Funcion para salir de un grupo
    private void deleteIntegrante(int idUsuario, int idGrupo, List<vistaDeGrupo> dataList) {
        Integer index = null;

        for (int i = 0; i < dataList.size(); i++) {
            vistaDeGrupo item = dataList.get(i);
            if (item.getIdgrupo() == idGrupo) {
                index = i;
            }
        }

        //crea el json
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("idgrupo", idGrupo);
            jsonData.put("idusuario", idUsuario);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new salirGrupoAsyncTask(context, recyclerView, index, dataList).execute(jsonData.toString());

    }

    //Funcion para salir de un grupo
    private void deleteGrupo(int idGrupo, List<vistaDeGrupo> dataList) {
        Integer index = null;

        for (int i = 0; i < dataList.size(); i++) {
            vistaDeGrupo item = dataList.get(i);
            if (item.getIdgrupo() == idGrupo) {
                index = i;
            }
        }

        //crea el json
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("idgrupo", idGrupo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new DeleteGrupoAsyncTask(context, recyclerView, index, dataList).execute(jsonData.toString());
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView nombreGrupo; // TextView para mostrar el nombre del grupo
        TextView creadoPor; // TextView para mostrar el creador del grupo
        TextView integrantes; // TextView para mostrar la cantidad de integrantes
        ImageView image; // ImageView para mostrar una imagen asociada al grupo

        public CustomViewHolder(View itemView) {
            super(itemView);
            nombreGrupo = itemView.findViewById(R.id.txtListItemNombreGrupo); // Asocia la vista de nombre del grupo
            creadoPor = itemView.findViewById(R.id.txtListItemCreado); // Asocia la vista del creador del grupo
            integrantes = itemView.findViewById(R.id.txtListItemIntegrante); // Asocia la vista de la cantidad de integrantes
            image = itemView.findViewById(R.id.imageviewListItemImage); // Asocia la vista de la imagen asociada al grupo
        }
    }
}
