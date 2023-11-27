package com.example.proyectogrupo1musicstore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Models.vistaDeNuevoGrupo;
import com.example.proyectogrupo1musicstore.R;

import java.util.List;

public class CustomAdapterNuevoGrupoDetalles extends RecyclerView.Adapter<CustomAdapterNuevoGrupoDetalles.CustomViewHolder> {
    private List<vistaDeNuevoGrupo> dataList;
    private Context context;

    public CustomAdapterNuevoGrupoDetalles(Context context, List<vistaDeNuevoGrupo> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_list_item_nuevo_grupo_detalles, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        vistaDeNuevoGrupo data = dataList.get(position);

        // Vincula los datos a las vistas en tu diseño de elemento de lista personalizado
        holder.nombreUsuario.setText(data.getText1());
        holder.image.setImageBitmap(data.getImageResource());
    }

    //Devuelve el número total de elementos en la lista de datos.
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView nombreUsuario; // TextView para mostrar el nombre del usuario
        ImageView image; // ImageView para mostrar una imagen asociada al usuario

        public CustomViewHolder(View itemView) {
            super(itemView);
            nombreUsuario = itemView.findViewById(R.id.txtListItemNombreUsuarioNuevoGrupoDetalles); // Asocia la vista de nombre del usuario
            image = itemView.findViewById(R.id.imageviewListItemImageNuevoGrupoDetalles); // Asocia la vista de la imagen asociada al usuario
        }
    }
}
