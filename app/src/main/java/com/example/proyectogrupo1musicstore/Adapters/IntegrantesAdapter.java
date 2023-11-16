package com.example.proyectogrupo1musicstore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Models.integrantesItem;
import com.example.proyectogrupo1musicstore.Models.vistaDeNuevoGrupo;
import com.example.proyectogrupo1musicstore.R;

import java.util.List;

public class IntegrantesAdapter extends RecyclerView.Adapter<IntegrantesAdapter.IntegrantesViewHolder> {
    private List<integrantesItem> itemList;
    private Context context;

    public IntegrantesAdapter(Context context, List<integrantesItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public IntegrantesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_item_integrantes, parent, false);
        return new IntegrantesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IntegrantesViewHolder holder, int position) {
        integrantesItem item = itemList.get(position);
        holder.integranteImage.setImageBitmap(item.getImageResId());
        holder.integranteName.setText(item.getItemName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class IntegrantesViewHolder extends RecyclerView.ViewHolder {
        ImageView integranteImage;
        TextView integranteName;

        public IntegrantesViewHolder(@NonNull View itemView) {
            super(itemView);
            integranteImage = itemView.findViewById(R.id.imageviewCarouselItemIntegrantes);
            integranteName = itemView.findViewById(R.id.textviewCarouselItemIntegrantes);
        }
    }

    public void setDataList(List<integrantesItem> newDataList) {
        itemList = newDataList;
        notifyDataSetChanged();
    }
}
