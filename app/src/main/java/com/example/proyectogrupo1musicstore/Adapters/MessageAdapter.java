package com.example.proyectogrupo1musicstore.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Activities.Reproductores.ActivityReproductoVideo;
import com.example.proyectogrupo1musicstore.Models.mensajeModel;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.Date.DateUtils;
import com.example.proyectogrupo1musicstore.Utilidades.Imagenes.ImageDownloaderAsync;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<mensajeModel> messages;
    private String currentUserId; // You need to set this with the current user's ID
    private OnPlayClickListener onPlayClickListener;
    private static final int VIEW_TYPE_USER_MESSAGE = 1;
    private static final int VIEW_TYPE_OTHER_MESSAGE = 2;

    public MessageAdapter(List<mensajeModel> messages, String currentUserId, OnPlayClickListener listener) {
        this.messages = messages;
        this.currentUserId = currentUserId;
        this.onPlayClickListener = listener;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_USER_MESSAGE) {
            // Inflate layout for messages sent by the user
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_usuario, parent, false);
        } else {
            // Inflate layout for messages sent by others
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        mensajeModel message = messages.get(position);
        if (message.getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_USER_MESSAGE;
        } else {
            return VIEW_TYPE_OTHER_MESSAGE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        mensajeModel message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView messageText;
        private TextView senderName;
        private TextView messageTime;
        private ImageView senderImage;
        private ImageButton btnPlay;
        private TextView messageTextUsuario;
        private TextView senderNameUsuario;
        private TextView messageTimeUsuario;
        private ImageView senderImageUsuario;
        private ImageButton btnPlayUsuario;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            senderName = itemView.findViewById(R.id.senderName);
            messageTime = itemView.findViewById(R.id.messageTime);
            senderImage = itemView.findViewById(R.id.senderImage);
            btnPlay = itemView.findViewById(R.id.imageButtonChatPlay);
            messageTextUsuario = itemView.findViewById(R.id.messageTextUsuario);
            senderNameUsuario = itemView.findViewById(R.id.senderNameUsuario);
            messageTimeUsuario = itemView.findViewById(R.id.messageTimeUsuario);
            senderImageUsuario = itemView.findViewById(R.id.senderImageUsuario);
            btnPlayUsuario = itemView.findViewById(R.id.imageButtonChatPlayUsuario);
        }

        public void bind(mensajeModel message) {
            if (message.getSenderId().equals(currentUserId)){
                messageTextUsuario.setText(message.getText());
                senderNameUsuario.setText(message.getFull_identity());
                messageTimeUsuario.setText(DateUtils.formatTimestamp(message.getTimestamp()));
                String imgResource = message.getEnlacefoto();
                ImageDownloaderAsync imageDownloaderAsync = new ImageDownloaderAsync(senderImageUsuario);
                imageDownloaderAsync.execute(imgResource);
                messageTextUsuario.setTextColor(itemView.getContext().getColor(R.color.green));
                if(message.getMediatype().equals("Audio")){
                    btnPlayUsuario.setVisibility(View.VISIBLE);
                    // Handle play button click
                    btnPlayUsuario.setOnClickListener(v -> {
                        if (onPlayClickListener != null) {
                            onPlayClickListener.onPlayClick(message.getAudioUrl());
                        }
                    });
                }else if(message.getMediatype().equals("Voice")){
                    btnPlayUsuario.setVisibility(View.VISIBLE);
                    // Handle play button click
                    btnPlayUsuario.setOnClickListener(v -> {
                        if (onPlayClickListener != null) {
                            btnPlayUsuario.setVisibility(View.VISIBLE);
                            onPlayClickListener.onPlayClick(message.getAudioUrl());
                        }
                    });
                }else if(message.getMediatype().equals("Video")){
                    btnPlayUsuario.setVisibility(View.VISIBLE);
                    btnPlayUsuario.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), ActivityReproductoVideo.class);
                            intent.putExtra("videoUrl", message.getAudioUrl());
                            intent.putExtra("name", message.getText());
                            v.getContext().startActivity(intent);
                        }
                    });
                }
            }else{
                messageText.setText(message.getText());
                senderName.setText(message.getFull_identity());
                messageTime.setText(DateUtils.formatTimestamp(message.getTimestamp()));
                String imgResource = message.getEnlacefoto();
                ImageDownloaderAsync imageDownloaderAsync = new ImageDownloaderAsync(senderImage);
                imageDownloaderAsync.execute(imgResource);
                messageText.setTextColor(itemView.getContext().getColor(R.color.black));
                if(message.getMediatype().equals("Audio")){
                    btnPlay.setVisibility(View.VISIBLE);
                    // Handle play button click
                    btnPlay.setOnClickListener(v -> {
                        if (onPlayClickListener != null) {
                            onPlayClickListener.onPlayClick(message.getAudioUrl());
                        }
                    });
                }
                else if(message.getMediatype().equals("Voice")){
                    btnPlay.setVisibility(View.VISIBLE);
                    // Handle play button click
                    btnPlay.setOnClickListener(v -> {
                        if (onPlayClickListener != null) {
                            btnPlay.setVisibility(View.VISIBLE);
                            onPlayClickListener.onPlayClick(message.getAudioUrl());
                        }
                    });
                }else if(message.getMediatype().equals("Video")){
                    btnPlay.setVisibility(View.VISIBLE);
                    btnPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), ActivityReproductoVideo.class);
                            intent.putExtra("videoUrl", message.getAudioUrl());
                            intent.putExtra("name", message.getText());
                            v.getContext().startActivity(intent);
                        }
                    });
                }


            }

        }
    }

    // Method to add a new message to the adapter
    public void addMessage(mensajeModel message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    // Define an interface for handling play button clicks
    public interface OnPlayClickListener {
        void onPlayClick(String audioUrl);
    }


}
