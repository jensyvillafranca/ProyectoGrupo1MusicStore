package com.example.proyectogrupo1musicstore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Models.vistaDeNuevoGrupo;
import com.example.proyectogrupo1musicstore.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterNuevoGrupoIntegrantes extends RecyclerView.Adapter<CustomAdapterNuevoGrupoIntegrantes.CustomViewHolder> {
    private List<vistaDeNuevoGrupo> dataList;
    private Context context;
    private List<Integer> selectedUserIds;
    private int idUsuario;

    public CustomAdapterNuevoGrupoIntegrantes(Context context, List<vistaDeNuevoGrupo> dataList, int idUsuario) {
        this.context = context;
        this.dataList = dataList;
        this.selectedUserIds = new ArrayList<>();
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


    }

    public void setDataList(List<vistaDeNuevoGrupo> newDataList) {
        dataList = newDataList;
        notifyDataSetChanged();
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

        public CustomViewHolder(View itemView) {
            super(itemView);
            nombreUsuario = itemView.findViewById(R.id.txtListItemNombreUsuarioNuevoGrupoIntegrantes); // Asocia la vista de nombre del usuario
            image = itemView.findViewById(R.id.imageviewListItemImageNuevoGrupoIntegrantes); // Asocia la vista de la imagen asociada al usuario
            checkbox = itemView.findViewById(R.id.checkboxNuevoGrupoIntegrantes);
        }
    }
}
