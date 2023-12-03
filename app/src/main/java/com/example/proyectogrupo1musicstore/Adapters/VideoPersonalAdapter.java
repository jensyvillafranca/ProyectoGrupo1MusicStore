package com.example.proyectogrupo1musicstore.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Models.videoItem;
import com.example.proyectogrupo1musicstore.R;

import java.util.List;

public class VideoPersonalAdapter extends RecyclerView.Adapter<VideoPersonalAdapter.ViewHolder> {
    private List<videoItem> videoItems;
    private OnVideoItemClickListener videoItemClickListener;

    public VideoPersonalAdapter(List<videoItem> videoItems, OnVideoItemClickListener listener) {
        this.videoItems = videoItems;
        this.videoItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item_audios_personal, parent, false);
        return new ViewHolder(view, videoItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        videoItem videoItem = videoItems.get(position);
        holder.nombreAudio.setText(videoItem.getItemName());
        holder.image.setImageBitmap(videoItem.getImageResId());

        holder.itemView.setOnClickListener(v -> videoItemClickListener.onVideoItemClick(videoItem.getUrl(), videoItem.getItemName()));
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    public void setDataList(List<videoItem> newDataList) {
        videoItems = newDataList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreAudio;
        ImageView image;
        String url, nombreArchivo;

        public ViewHolder(@NonNull View itemView, OnVideoItemClickListener clickListener) {
            super(itemView);
            nombreAudio = itemView.findViewById(R.id.txtListItemNombreAudiosPersonal);
            image = itemView.findViewById(R.id.imageviewListItemImageAudiosPersonal);

            // Set an onClickListener to handle item click
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Get the selected audio item and handle it as needed
                    videoItem selectedVideo = videoItems.get(position);
                    url = selectedVideo.getUrl();
                    nombreArchivo = selectedVideo.getItemName();
                    // Notify the listener about the click
                    clickListener.onVideoItemClick(url, nombreArchivo);
                }
            });
        }
    }

    public interface OnVideoItemClickListener {
        void onVideoItemClick(String url, String nombreArchivo);
    }

}

