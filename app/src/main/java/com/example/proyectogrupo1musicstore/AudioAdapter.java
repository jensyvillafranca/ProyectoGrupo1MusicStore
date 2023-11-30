package com.example.proyectogrupo1musicstore;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Models.integrantesItem;

import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {
    private List<audioItem> itemList;
    private Context context;

    public AudioAdapter(Context context, List<audioItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subirmusica, parent, false);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        audioItem item = itemList.get(position);
        //holder.audioImage.setImageBitmap(item.getImageResId());
        holder.audioName.setText(item.getItemName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder {
        ImageView audioImage;
        TextView audioName;
        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            audioImage = itemView.findViewById(R.id.itemPortadaAudio);
            audioName = itemView.findViewById(R.id.textviewNombreCancion);
        }
    }

    public void setDataList(List<audioItem> newDataList) {
        itemList = newDataList;
        notifyDataSetChanged();
    }
}
