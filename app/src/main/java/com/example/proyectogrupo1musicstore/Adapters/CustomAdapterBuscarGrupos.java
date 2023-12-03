package com.example.proyectogrupo1musicstore.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityUnirseGrupo;
import com.example.proyectogrupo1musicstore.Models.buscarGrupo;
import com.example.proyectogrupo1musicstore.R;
import com.google.gson.Gson;

import java.util.List;

public class CustomAdapterBuscarGrupos extends RecyclerView.Adapter<CustomAdapterBuscarGrupos.CustomViewHolder> {
    private List<buscarGrupo> dataList;
    private Context context;

    public CustomAdapterBuscarGrupos(Context context, List<buscarGrupo> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_list_item_grupos_buscar, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        buscarGrupo data = dataList.get(position);

        // Vincula los datos a las vistas en tu diseño de elemento de lista personalizado
        holder.nombreGrupo.setText(data.getNombre());
        holder.creadoPor.setText(data.getUsuario());
        holder.integrantes.setText("Integrantes: " + data.getTotalusuarios());
        holder.image.setImageBitmap(data.getImage());

        // Obtiene el ImageView del diseño
        ImageView imgUnirse = holder.itemView.findViewById(R.id.imageviewGruposBuscarUnirse);

        if(data.getIsmember()==1){
            imgUnirse.setVisibility(View.GONE);
        }else{
            // Listener para ir a la pantalla de un grupo especifico y unirse
            imgUnirse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pantallaUnirse = new Intent(v.getContext(), ActivityUnirseGrupo.class);
                    String jsonString = new Gson().toJson(data);
                    pantallaUnirse.putExtra("jsonString", jsonString);
                    v.getContext().startActivity(pantallaUnirse);
                }
            });
        }
    }



    //Devuelve el número total de elementos en la lista de datos.
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView nombreGrupo; // TextView para mostrar el nombre del grupo
        TextView creadoPor; // TextView para mostrar el creador del grupo
        TextView integrantes; // TextView para mostrar la cantidad de integrantes
        ImageView image; // ImageView para mostrar una imagen asociada al grupo

        public CustomViewHolder(View itemView) {
            super(itemView);
            nombreGrupo = itemView.findViewById(R.id.txtListItemNombreGrupoGruposBuscar); // Asocia la vista de nombre del grupo
            creadoPor = itemView.findViewById(R.id.txtListItemCreadoGruposBuscar);  // Asocia la vista del creador del grupo
            integrantes = itemView.findViewById(R.id.txtListItemIntegranteGruposBuscar); // Asocia la vista de la cantidad de integrantes
            image = itemView.findViewById(R.id.imageviewListItemImageGruposBuscar); // Asocia la vista de la imagen asociada al grupo
        }
    }

    public void setDataList(List<buscarGrupo> newDataList) {
        dataList = newDataList;
        notifyDataSetChanged();
    }
}
