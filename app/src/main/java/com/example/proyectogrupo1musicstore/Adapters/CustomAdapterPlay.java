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

import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityChat;
import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityEditarGrupo;
import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityGrupoInfo;
import com.example.proyectogrupo1musicstore.ActivityEditarPlayList;
import com.example.proyectogrupo1musicstore.ActivityModificarPlayList;
import com.example.proyectogrupo1musicstore.Models.vistaDeGrupo;
import com.example.proyectogrupo1musicstore.Models.vistadeplaylist;
import com.example.proyectogrupo1musicstore.R;

import java.util.List;

public class CustomAdapterPlay extends RecyclerView.Adapter<CustomAdapterPlay.CustomViewHolder> {
    private final List<vistadeplaylist> dataList;
    private final Context context;
    private final RecyclerView recyclerView;
    boolean isImage1;
    private final int idUsuario;
    private int tipoAccion;

    public CustomAdapterPlay(Context context, List<vistadeplaylist> dataList, int idUsuario, RecyclerView recyclerView) {
        this.context = context;
        this.dataList = dataList;
        this.idUsuario = idUsuario;
        this.recyclerView = recyclerView;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_modificacionplaylist_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        vistadeplaylist data = dataList.get(position);

        // Vincula los datos a las vistas en tu diseño de elemento de lista personalizado
        holder.nombrePlayList.setText(data.getText1());
        holder.creadoPor.setText(data.getText2());
        holder.biogra.setText(data.getText3());
        holder.image.setImageBitmap(data.getImageResource());


        // Obtiene el ImageView del diseño
        ImageView itemEditarplay = holder.itemView.findViewById(R.id.imageviewPlayListIconoEditar);
        ImageView imgplaylist = holder.itemView.findViewById(R.id.imageviewListItemImageplaylist);


        //muestra o esconde boton editar
        if (data.getIdOwner() == idUsuario) {
            itemEditarplay.setVisibility(View.VISIBLE);
        } else {
            itemEditarplay.setVisibility(View.GONE);
        }

        imgplaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pantallaInfo = new Intent(v.getContext(), ActivityModificarPlayList.class);
                pantallaInfo.putExtra("idplaylist", data.getIdplaylist());
                v.getContext().startActivity(pantallaInfo);
            }
        });

        holder.nombrePlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityModificarPlayList.class);
                intent.putExtra("idplaylist", data.getIdplaylist());
                v.getContext().startActivity(intent);
            }
        });

        //Listener para boton editar
        itemEditarplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityEditarPlayList.class);
                intent.putExtra("idplaylist", data.getIdplaylist());
                v.getContext().startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView nombrePlayList; // TextView para mostrar el nombre de la playlist
            TextView creadoPor; // TextView para mostrar el creador de la playList
             TextView biogra; // TextView para mostrar el creador de la playList
            ImageView image; // ImageView para mostrar una imagen asociada a la playlist

            public CustomViewHolder(View itemView) {
                super(itemView);
                nombrePlayList = itemView.findViewById(R.id.txtListItemNombrePlayList); // Asocia la vista de nombre de la playlist
                creadoPor = itemView.findViewById(R.id.txtListItemCreadoplaylist); // Asocia la vista del creador de la playlist
                biogra = itemView.findViewById(R.id.txtListItemBiografia); // Asocia la vista de la biografia de la playlist
                image = itemView.findViewById(R.id.imageviewListItemImageplaylist); // Asocia la vista de la imagen asociada a la playlist

            }
        }




    }

