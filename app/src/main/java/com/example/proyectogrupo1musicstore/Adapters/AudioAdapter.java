package com.example.proyectogrupo1musicstore.Adapters;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Activities.Reproductores.ActivityReproductor;
import com.example.proyectogrupo1musicstore.Models.audioItem;
import com.example.proyectogrupo1musicstore.NetworkTasks.Multimedia.UpdateCancionFavoritaAsyncTask;
import com.example.proyectogrupo1musicstore.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {

    private List<audioItem> itemList;
    private Context context;
    private int estadofavorito;


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
        holder.audioImage.setImageBitmap(item.getImageResId());
        holder.audioName.setText(item.getItemName());

        if(item.getTipoVista()==2){
            holder.checkBoxFavorito.setVisibility(View.GONE);
        }

        holder.checkBoxFavorito.setChecked(item.getEstadofavorito() == 1);

        holder.checkBoxFavorito.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    estadofavorito = 1;
                    updateFavoritoState(item.getId(), estadofavorito);
                    Log.e("audioid: ", String.valueOf(item.getId()));
                } else {
                    estadofavorito = 0;
                    updateFavoritoState(item.getId(), estadofavorito);
                }
            }
        });


        holder.audioImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityReproductor.class);
               // intent.putExtra("imagen", item.getImageResId());
                intent.putExtra("musicaUrl", item.getUrl());
                intent.putExtra("name", item.getItemName());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Check if the list is null
        if (itemList == null) {
            return 0;
        }

        return itemList.size();
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder {
        ImageView audioImage;
        TextView audioName;
        CheckBox checkBoxFavorito;
        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            audioImage = itemView.findViewById(R.id.itemPortadaAudios);
            audioName = itemView.findViewById(R.id.textviewNombreCancion);
            checkBoxFavorito = itemView.findViewById(R.id.checkboxCancion);
        }
    }

    public void setDataList(List<audioItem> newDataList) {
        itemList = newDataList;
        notifyDataSetChanged();
    }

    private void updateFavoritoState(int idAudio, int newFavoritoState) {

        // Construye el JSON
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("estadofavorito", newFavoritoState);
            jsonData.put("idaudio", idAudio);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new UpdateCancionFavoritaAsyncTask().execute(jsonData.toString());
    }
}
