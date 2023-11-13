package com.example.proyectogrupo1musicstore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
}
