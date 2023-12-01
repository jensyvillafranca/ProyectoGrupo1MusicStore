package com.example.proyectogrupo1musicstore.Models;

public class mensajeModel {
    private String senderId;
    private String text;
    private long timestamp;
    private String groupId;
    private String full_identity;
    private String enlacefoto;
    private String audioUrl;
    private String mediatype;

    public mensajeModel(String audioUrl, String enlacefoto, String full_identity, String groupId, String mediatype, String senderId, String text, long timestamp) {
        this.senderId = senderId;
        this.text = text;
        this.timestamp = timestamp;
        this.groupId = groupId;
        this.enlacefoto = enlacefoto;
        this.full_identity = full_identity;
        this.audioUrl = audioUrl;
        this.mediatype = mediatype;
    }

    public mensajeModel() {
        // Default constructor required for calls to DataSnapshot.getValue(mensajeModel.class)
    }

    public String getSenderId() {
        return senderId;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getFull_identity() {
        return full_identity;
    }

    public String getEnlacefoto() {return enlacefoto;}

    public String getAudioUrl() {return audioUrl;}

    public String getMediatype() {return mediatype;}
}
