package com.example.proyectogrupo1musicstore.Utilidades.Nombramiento;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UniqueFileNameGenerator {

    public static String generateUniqueFileName(String fileExtension) {
        // Obtiene un timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // Genera un identificador unico
        String uniqueId = UUID.randomUUID().toString();

        // Combina timestamp, unique identifier, and file extension
        return "voice_" + timeStamp + "_" + uniqueId + "." + fileExtension;
    }

    public static void main(String[] args) {
        // Example usage
        String fileExtension = "mp4";
        String uniqueFileName = generateUniqueFileName(fileExtension);
        System.out.println("Unique File Name: " + uniqueFileName);
    }
}
