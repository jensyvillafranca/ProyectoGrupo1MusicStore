package com.example.proyectogrupo1musicstore;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Activities.Reproductores.ActivityReproductoVideo;
import com.example.proyectogrupo1musicstore.Models.videoItem;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<videoItem> itemListVideo;
    private Context context;

    public VideoAdapter(Context context, List<videoItem> itemList) {
        this.context = context;
        this.itemListVideo = itemList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subirvideo, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        videoItem item = itemListVideo.get(position);
        holder.videoImage.setImageBitmap(item.getImageResId());
        holder.videoName.setText(item.getItemName());

        //parte fue modificado por JM
        ImageView imgVideo = holder.itemView.findViewById(R.id.itemPortadaAudio);

        holder.videoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityReproductoVideo.class);
                intent.putExtra("videoUrl", item.getUrl());
                intent.putExtra("name", item.getItemName());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemListVideo.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView videoImage;
        TextView videoName;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoImage = itemView.findViewById(R.id.itemPortadaAudio);
            videoName = itemView.findViewById(R.id.textviewNombreCancion);
        }
    }

    public void setDataList(List<videoItem> newDataList) {
        itemListVideo = newDataList;
        notifyDataSetChanged();
    }
}
