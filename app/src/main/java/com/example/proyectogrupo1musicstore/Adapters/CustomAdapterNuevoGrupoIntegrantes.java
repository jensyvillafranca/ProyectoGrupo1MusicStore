package com.example.proyectogrupo1musicstore.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.example.proyectogrupo1musicstore.Models.vistaDeNuevoGrupo;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.deleteIntegranteAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.UI.ConfirmationDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterNuevoGrupoIntegrantes extends RecyclerView.Adapter<CustomAdapterNuevoGrupoIntegrantes.CustomViewHolder> {
    private List<vistaDeNuevoGrupo> dataList;
    private final RecyclerView recyclerView;
    private final Context context;
    private final List<Integer> selectedUserIds;
    private final int idUsuario;
    private final int idgrupo;

    public CustomAdapterNuevoGrupoIntegrantes(Context context, List<vistaDeNuevoGrupo> dataList, int idUsuario, int idgrupo, RecyclerView recyclerView) {
        this.context = context;
        this.dataList = dataList;
        this.selectedUserIds = new ArrayList<>();
        this.idUsuario = idUsuario;
        this.idgrupo = idgrupo;
        this.recyclerView = recyclerView;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_list_item_nuevo_grupo_integrantes, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        vistaDeNuevoGrupo data = dataList.get(position);

        // Vincula los datos a las vistas en tu diseño de elemento de lista personalizado
        holder.nombreUsuario.setText(data.getText1());
        holder.image.setImageBitmap(data.getImageResource());

        //controla la vista del checkbox y imagebutton eliminar
        if (data.getVersion() == 1) {
            holder.checkbox.setVisibility(View.VISIBLE);
            holder.btnElimiar.setVisibility(View.GONE);
        } else if ((data.getIdSeguidor()) == idUsuario) {
            holder.checkbox.setVisibility(View.GONE);
            holder.btnElimiar.setVisibility(View.GONE);
        } else {
            holder.btnElimiar.setVisibility(View.VISIBLE);
            holder.checkbox.setVisibility(View.GONE);
        }

        // Aplica el estado del checkbox basado en la seleccion
        holder.checkbox.setChecked(selectedUserIds.contains(data.getIdSeguidor()));

        // Aplica un listener al checkbox
        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Si el checkbox esta seleccionado, agrega el idusuario a la lista
                selectedUserIds.add(data.getIdSeguidor());
            } else {
                // Si el checkbox no esta seleccionado, remueve el idusuario de la lista
                selectedUserIds.remove(Integer.valueOf(data.getIdSeguidor()));
            }
        });

        //Listener para boton de eliminar integrantes de un grupo
        holder.btnElimiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show confirmation dialog
                ConfirmationDialog.showConfirmationDialog(
                        context, // Replace with your activity or fragment context
                        "Confirmación",
                        "¿Está seguro de que desea eliminar este usuario?, ¡Esta acción es definitiva!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Si el usuario da click en si
                                deleteIntegrante(data.getIdSeguidor(), idgrupo, dataList);
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
        });
    }

    private void deleteIntegrante(int idSeguidor, int idGrupo, List<vistaDeNuevoGrupo> dataList) {
        Integer index = null;

        for (int i = 0; i < dataList.size(); i++) {
            vistaDeNuevoGrupo item = dataList.get(i);
            if (item.getIdSeguidor() == idSeguidor) {
                index = i;
            }
        }

        //crea el json
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("idgrupo", idGrupo);
            jsonData.put("idusuario", idSeguidor);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new deleteIntegranteAsyncTask(context, recyclerView, index, dataList).execute(jsonData.toString());

    }

    public void setDataList(List<vistaDeNuevoGrupo> newDataList) {
        Log.d("Adapter", "Before Update: " + dataList.size());
        dataList = newDataList;
        notifyDataSetChanged();
        Log.d("Adapter", "After Update: " + dataList.size());
    }

    // Metodo para obtener la lista de integrantes
    public List<Integer> getSelectedUserIds() {
        return selectedUserIds;
    }

    //Devuelve el número total de elementos en la lista de datos.
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView nombreUsuario; // TextView para mostrar el nombre del usuario
        ImageView image; // ImageView para mostrar una imagen asociada al usuario
        CheckBox checkbox;  // Checkbox for user selection
        ImageButton btnElimiar;

        public CustomViewHolder(View itemView) {
            super(itemView);
            nombreUsuario = itemView.findViewById(R.id.txtListItemNombreUsuarioNuevoGrupoIntegrantes); // Asocia la vista de nombre del usuario
            image = itemView.findViewById(R.id.imageviewListItemImageNuevoGrupoIntegrantes); // Asocia la vista de la imagen asociada al usuario
            checkbox = itemView.findViewById(R.id.checkboxNuevoGrupoIntegrantes);
            btnElimiar = itemView.findViewById(R.id.btnEliminarNuevoGrupoIntegrantes);

        }
    }
}
