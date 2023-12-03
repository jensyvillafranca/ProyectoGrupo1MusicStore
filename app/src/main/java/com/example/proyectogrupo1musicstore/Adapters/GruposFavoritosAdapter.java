package com.example.proyectogrupo1musicstore.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityGrupoInfo;
import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityGruposBuscar;
import com.example.proyectogrupo1musicstore.Models.informacionGruposFavoritos;
import com.example.proyectogrupo1musicstore.R;

import java.util.List;

public class GruposFavoritosAdapter extends RecyclerView.Adapter<GruposFavoritosAdapter.GruposViewHolder> {
    private List<informacionGruposFavoritos> itemList;
    private Context context;

    public GruposFavoritosAdapter(Context context, List<informacionGruposFavoritos> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public GruposViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item_grupos_perfil_favoritos, parent, false);
        return new GruposViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GruposViewHolder holder, int position) {
        informacionGruposFavoritos item = itemList.get(position);
        holder.groupImage.setImageBitmap(item.getFoto());
        holder.nombre.setText(item.getNombre());
        Integer isMember = item.getIsMember();
        Log.e("IsMember", String.valueOf(isMember));
        if(isMember == 1){
            holder.imageViewUnirse.setVisibility(View.GONE);
            holder.groupImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pantallaInfo = new Intent(v.getContext(), ActivityGrupoInfo.class);
                    pantallaInfo.putExtra("idgrupo", item.getIdgrupo());
                    v.getContext().startActivity(pantallaInfo);
                }
            });
        }else{
            holder.imageViewUnirse.setVisibility(View.VISIBLE);
            holder.imageViewUnirse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ActivityGruposBuscar.class);
                    intent.putExtra("busqueda", item.getNombre());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class GruposViewHolder extends RecyclerView.ViewHolder {
        ImageView groupImage, imageViewUnirse;
        TextView nombre;


        public GruposViewHolder(@NonNull View itemView) {
            super(itemView);
            groupImage = itemView.findViewById(R.id.imageviewListItemImageGruposFavorito);
            nombre = itemView.findViewById(R.id.txtListItemNombreGrupoGruposFavorito);
            imageViewUnirse = itemView.findViewById(R.id.imageviewGruposFavoritoUnirse);
        }
    }

    public void setDataList(List<informacionGruposFavoritos> newDataList) {
        itemList = newDataList;
        notifyDataSetChanged();
    }
}
