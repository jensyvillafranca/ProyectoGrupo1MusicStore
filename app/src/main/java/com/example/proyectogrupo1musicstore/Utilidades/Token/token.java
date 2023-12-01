package com.example.proyectogrupo1musicstore.Utilidades.Token;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class token {
    private static final String KEY_ALIAS = "keystore";
    private static final String SHARED_PREFS_NAME = "sesiones";
    private static final String TOKEN_KEY = "token";
    private static final String IV_KEY = "iv"; // Clave para almacenar/recuperar el IV
    private static final int GCM_IV_LENGTH = 12; // Longitud estándar del IV para GCM

    private KeyStore keyStore; // Manejar las claves
    private Cipher cipher; // Encriptar y desencriptar el token
    private Context context;

    public token(Context context) { // Constructor
        this.context = context;
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore"); // Solicita una instancia del keystore.
            keyStore.load(null); // Aquí se carga el keyStore

            cipher = Cipher.getInstance(  // Se solicita una instancia de la clase Cipher, que se utilizará para la encriptación y desencriptación.
                    KeyProperties.KEY_ALGORITHM_AES + "/" + // algoritmo de cifrado es AES
                            KeyProperties.BLOCK_MODE_GCM + "/" +
                            KeyProperties.ENCRYPTION_PADDING_NONE // Sin padding el cifrado
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Almacenar el token en el keyStore */
    public void guardarTokenToKeystore(String token) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance( // Crea una instancia de KeyGenerator para el algoritmo AES
                    KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
            );

            keyGenerator.init(new KeyGenParameterSpec.Builder( // Se utiliza para encriptar y desencriptar utilizando el alias especifico
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
            )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setUserAuthenticationRequired(false)
                    .build());

            SecretKey claveSecreta = keyGenerator.generateKey(); // Genera la clave secreta

            cipher.init(Cipher.ENCRYPT_MODE, claveSecreta); // Inicializa el Cipher para el modo de encriptación con la clave secreta generada

            byte[] iv = cipher.getIV(); // Obtener el IV que se usará para el cifrado

            byte[] tokenEncriptado = cipher.doFinal(token.getBytes(StandardCharsets.UTF_8)); // Cifra el token y lo devuelve en un array de byte

            /* Almacenar el IV junto con el token encriptado en las preferencias compartidas */
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE); // datos accesibles solo para esta aplicación
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(TOKEN_KEY, android.util.Base64.encodeToString(tokenEncriptado, android.util.Base64.DEFAULT));
            editor.putString(IV_KEY, android.util.Base64.encodeToString(iv, android.util.Base64.DEFAULT)); // Guardar el IV en Base64
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Recuperar y descifrar el token
    public String recuperarTokenFromKeystore() {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE); // Instancia para acceder al archivo de preferencias
            String base64Token = sharedPreferences.getString(TOKEN_KEY, null); // Recupera el token almacenado, si no existe devuelve null
            String base64IV = sharedPreferences.getString(IV_KEY, null); // Recupera el IV almacenado

            if (base64Token != null && base64IV != null) { // Comprueba si se recuperó un token y un IV
                byte[] encryptedToken = android.util.Base64.decode(base64Token, android.util.Base64.DEFAULT); // Decodifica el token a un arreglo de bytes
                byte[] iv = android.util.Base64.decode(base64IV, android.util.Base64.DEFAULT); // Decodifica el IV

                cipher.init(Cipher.DECRYPT_MODE, keyStore.getKey(KEY_ALIAS, null), new GCMParameterSpec(128, iv)); // Inicializa el objeto de Cipher para el modo de descifrado con el IV recuperado

                byte[] decryptedToken = cipher.doFinal(encryptedToken); // Descifrado final del token cifrado y devuelve el resultado como un arreglo de bytes.

                return new String(decryptedToken, StandardCharsets.UTF_8); // Ese arreglo de bytes lo convierte nuevamente a una cadena utilizando UTF-8
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para borrar el token en el cierre de sesión
    public void borrarToken() {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(TOKEN_KEY); // Elimina el token
            editor.remove(IV_KEY); // Elimina el IV
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

