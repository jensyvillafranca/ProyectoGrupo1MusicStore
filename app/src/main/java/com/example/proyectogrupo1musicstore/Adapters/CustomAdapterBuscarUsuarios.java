package com.example.proyectogrupo1musicstore.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Activities.Perfil.Activity_PerfilPersonal;
import com.example.proyectogrupo1musicstore.Models.buscarUsuario;
import com.example.proyectogrupo1musicstore.NetworkTasks.PerfilNetworkTasks.InformacionPerfilAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.PerfilNetworkTasks.InsertarSeguidorAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.PerfilNetworkTasks.deleteSeguidorAsyncTask;
import com.example.proyectogrupo1musicstore.R;

import java.util.List;

public class CustomAdapterBuscarUsuarios extends RecyclerView.Adapter<CustomAdapterBuscarUsuarios.CustomViewHolder> {
    private List<buscarUsuario> dataList;
    private Context context;
    private int idUsuario;

    public CustomAdapterBuscarUsuarios(Context context, List<buscarUsuario> dataList, int idUsuario) {
        this.context = context;
        this.dataList = dataList;
        this.idUsuario = idUsuario;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_list_item_usuarios_buscar, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        buscarUsuario data = dataList.get(position);

        // Vincula los datos a las vistas en tu diseño de elemento de lista personalizado
        holder.nombreUsuario.setText(data.getNombre());
        holder.usuario.setText(data.getUsuario());
        holder.image.setImageBitmap(data.getImage());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Activity_PerfilPersonal.class);
                intent.putExtra("idusuarioVista", data.getIdUsuario());
                v.getContext().startActivity(intent);
            }
        });

        holder.nombreUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Activity_PerfilPersonal.class);
                intent.putExtra("idusuarioVista", data.getIdUsuario());
                v.getContext().startActivity(intent);
            }
        });

        holder.usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Activity_PerfilPersonal.class);
                intent.putExtra("idusuarioVista", data.getIdUsuario());
                v.getContext().startActivity(intent);
            }
        });

        if(data.getIdUsuario()==idUsuario){
            holder.btnSeguir.setVisibility(View.GONE);
        } else if(data.getFollows()==1){
            holder.btnSeguir.setText("Dejar de Seguir");
            holder.btnSeguir.setTextSize(TypedValue.COMPLEX_UNIT_PX, 27);
            holder.btnSeguir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new deleteSeguidorAsyncTask(context).execute(String.valueOf(data.getIdUsuario()), String.valueOf(idUsuario));
                }
            });
        }else{
            holder.btnSeguir.setText("Seguir");
            // Listener para ir a la pantalla de un grupo especifico y unirse
            holder.btnSeguir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new InsertarSeguidorAsyncTask(context).execute(String.valueOf(data.getIdUsuario()), String.valueOf(idUsuario));
                }
            });
        }
    }



    //Devuelve el número total de elementos en la lista de datos.
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView nombreUsuario; // TextView para mostrar el nombre del grupo
        TextView usuario; // TextView para mostrar el creador del grupo
        ImageView image; // ImageView para mostrar una imagen asociada al grupo
        Button btnSeguir;

        public CustomViewHolder(View itemView) {
            super(itemView);
            nombreUsuario = itemView.findViewById(R.id.txtListItemNombreUsuarioBuscar);
            usuario = itemView.findViewById(R.id.txtListItemCreadoUsuarioBuscar);
            image = itemView.findViewById(R.id.imageviewListItemImageUsuarioBuscar);
            btnSeguir = itemView.findViewById(R.id.btnSeguirBuscar);
        }
    }

    public void setDataList(List<buscarUsuario> newDataList) {
        dataList = newDataList;
        notifyDataSetChanged();
    }
}
