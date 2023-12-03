package com.example.proyectogrupo1musicstore.Adapters;

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

public class AudioPersonalAdapter extends RecyclerView.Adapter<AudioPersonalAdapter.ViewHolder> {
    private List<musicItem> audioItems;
    private OnAudioItemClickListener audioItemClickListener;

    public AudioPersonalAdapter(List<musicItem> audioItems, OnAudioItemClickListener listener) {
        this.audioItems = audioItems;
        this.audioItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item_audios_personal, parent, false);
        return new ViewHolder(view, audioItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        musicItem audioItem = audioItems.get(position);
        holder.nombreAudio.setText(audioItem.getItemName());
        holder.image.setImageBitmap(audioItem.getImageResId());

        holder.itemView.setOnClickListener(v -> audioItemClickListener.onAudioItemClick(audioItem.getUrl(), audioItem.getItemName()));
    }

    @Override
    public int getItemCount() {
        return audioItems.size();
    }

    public void setDataList(List<musicItem> newDataList) {
        audioItems = newDataList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreAudio;
        ImageView image;
        String url, nombreArchivo;

        public ViewHolder(@NonNull View itemView, OnAudioItemClickListener clickListener) {
            super(itemView);
            nombreAudio = itemView.findViewById(R.id.txtListItemNombreAudiosPersonal);
            image = itemView.findViewById(R.id.imageviewListItemImageAudiosPersonal);

            // Set an onClickListener to handle item click
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Get the selected audio item and handle it as needed
                    musicItem selectedAudio = audioItems.get(position);
                    url = selectedAudio.getUrl();
                    nombreArchivo = selectedAudio.getItemName();
                    // Notify the listener about the click
                    clickListener.onAudioItemClick(url, nombreArchivo);
                }
            });
        }
    }

    public interface OnAudioItemClickListener {
        void onAudioItemClick(String url, String nombreArchivo);
    }

}

