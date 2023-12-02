package com.example.proyectogrupo1musicstore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Models.buscarAudioMusica;
import com.example.proyectogrupo1musicstore.Models.buscarGrupo;
import com.example.proyectogrupo1musicstore.Models.vistaMusicaVideo;
import com.example.proyectogrupo1musicstore.R;

import java.util.List;

public class CustomAdapterMusicaVideos extends RecyclerView.Adapter<CustomAdapterMusicaVideos.CustomViewHolder> {
    private List<buscarAudioMusica> dataList;
    private Context context;
    boolean isImage1 = true;

    ImageView images; // ImageView para mostrar una imagen asociada al grupo

    public CustomAdapterMusicaVideos(Context context, List<buscarAudioMusica> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public CustomAdapterMusicaVideos.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_list_item_musica, parent, false);
        return new CustomAdapterMusicaVideos.CustomViewHolder(view);
    }


    public void onBindViewHolder(CustomAdapterMusicaVideos.CustomViewHolder holder, int position) {
        buscarAudioMusica data = dataList.get(position);

        // Vincula los datos a las vistas en tu diseño de elemento de lista personalizado
        holder.nombreCancion.setText(data.getNombre());
        holder.creadoPor.setText(data.getAutor());
        holder.images.setImageBitmap(data.getImage());
        holder.genero.setText(data.getGenero());
    }


    public int getItemCount() {
        return dataList.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView nombreCancion; // TextView para mostrar el nombre del grupo
        TextView creadoPor; // TextView para mostrar el creador del grupo
        TextView genero; // TextView para mostrar el creador del grupo

        ImageView images; // ImageView para mostrar una imagen asociada al grupo

        public CustomViewHolder(View itemView) {
            super(itemView);
            nombreCancion = itemView.findViewById(R.id.txtListItemNombreGrupo); // Asocia la vista de nombre del grupo
            creadoPor = itemView.findViewById(R.id.txtListItemCreadoMusica); // Asocia la vista del creador del grupo
            images = itemView.findViewById(R.id.imageviewListItemImagess); // Asocia la vista de la imagen asociada al grupo
            genero = itemView.findViewById(R.id.txtListtGenero); // Asocia la vista del creador del grupo
        }
    }

    public void setDataList(List<buscarAudioMusica> newDataList) {
        dataList = newDataList;
//        notifyDataSetChanged();


    }
}