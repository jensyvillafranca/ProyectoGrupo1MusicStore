package com.example.proyectogrupo1musicstore.Adapters;

import androidx.annotation.NonNull;

import com.example.proyectogrupo1musicstore.Models.audioItem;

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
