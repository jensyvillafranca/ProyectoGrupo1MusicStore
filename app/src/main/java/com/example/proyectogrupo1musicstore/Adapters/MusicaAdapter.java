package com.example.proyectogrupo1musicstore.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Activities.Reproductores.ActivityReproductor;
import com.example.proyectogrupo1musicstore.Models.musicItem;
import com.example.proyectogrupo1musicstore.R;

import java.util.List;

public class MusicaAdapter extends RecyclerView.Adapter<MusicaAdapter.MusicaViewHolder> {
    private List<musicItem> itemList;
    private Context context;

    public MusicaAdapter(Context context, List<musicItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MusicaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_item_musica, parent, false);
        return new MusicaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicaViewHolder holder, int position) {
        musicItem item = itemList.get(position);
        holder.musicaImage.setImageBitmap(item.getImageResId());
        holder.musicaName.setText(item.getItemName());
        holder.musicaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityReproductor.class);
                intent.putExtra("musicaUrl", item.getUrl());
                intent.putExtra("name", item.getItemName());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MusicaViewHolder extends RecyclerView.ViewHolder {
        ImageView musicaImage;
        TextView musicaName;

        public MusicaViewHolder(@NonNull View itemView) {
            super(itemView);
            musicaImage = itemView.findViewById(R.id.imageviewCarouselItemMusica);
            musicaName = itemView.findViewById(R.id.textviewCarouselItemMusica);
        }
    }

    public void setDataList(List<musicItem> newDataList) {
        itemList = newDataList;
        notifyDataSetChanged();
    }
}
