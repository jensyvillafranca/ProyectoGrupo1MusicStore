package com.example.proyectogrupo1musicstore.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.ActivityUnirseGrupo;
import com.example.proyectogrupo1musicstore.Models.vistaDeGrupo;
import com.example.proyectogrupo1musicstore.R;

import java.util.List;

public class CustomAdapterBuscarGrupos extends RecyclerView.Adapter<CustomAdapterBuscarGrupos.CustomViewHolder> {
    private List<vistaDeGrupo> dataList;
    private Context context;

    public CustomAdapterBuscarGrupos(Context context, List<vistaDeGrupo> dataList) {
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
        vistaDeGrupo data = dataList.get(position);

        // Vincula los datos a las vistas en tu diseño de elemento de lista personalizado
        holder.nombreGrupo.setText(data.getText1());
        holder.creadoPor.setText(data.getText2());
        holder.integrantes.setText(data.getText3());
        holder.image.setImageBitmap(data.getImageResource());

        // Obtiene el ImageView del diseño
        ImageView imgUnirse = holder.itemView.findViewById(R.id.imageviewGruposBuscarUnirse);

        // Listener para ir a la pantalla de un grupo especifico y unirse
        imgUnirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pantallaUnirse = new Intent(v.getContext(), ActivityUnirseGrupo.class);
                v.getContext().startActivity(pantallaUnirse);
            }
        });
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
}
