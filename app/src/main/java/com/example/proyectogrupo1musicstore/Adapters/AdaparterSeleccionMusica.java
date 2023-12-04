package com.example.proyectogrupo1musicstore.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Models.musicItem;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.audioItem;

import java.util.List;

public class AdaparterSeleccionMusica  {
    private List<audioItem> audioItems;
    private OnAudioItemClickListener audioItemClickListener;

    public AdaparterSeleccionMusica(List<audioItem> audioItems, OnAudioItemClickListener listener) {
        this.audioItems = audioItems;
        this.audioItemClickListener = listener;
    }

    @NonNull
    //@Override
   // public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     //   View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subirmusica, parent, false);
       // return new ViewHolder(view, audioItemClickListener);
    //}

    //@Override
    public void onBindViewHolder(@NonNull AudioPersonalAdapter.ViewHolder holder, int position) {

    }

    //@Override
    public int getItemCount() {
        return 0;
    }

    public interface OnAudioItemClickListener {
        void onAudioItemClick(String url, String nombreArchivo);
    }
}
