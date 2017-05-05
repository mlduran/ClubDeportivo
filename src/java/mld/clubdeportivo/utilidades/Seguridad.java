
package mld.clubdeportivo.utilidades;

/**
 *
 * @author java1
 */

import java.security.*;

public final class Seguridad extends Object {

    private Seguridad(){}

    private static String encriptar(String message, String tipo){

        if (message == null)
            throw new NullPointerException("Valor sin referencia");

        String encriptado = "";

        try {
            MessageDigest hash = MessageDigest.getInstance(tipo);

            byte[] messageBytes = message.getBytes();

            hash.update(messageBytes);
            byte[] cifrado = hash.digest();
            encriptado = StringUtil.toHexString(cifrado);
        } catch (NoSuchAlgorithmException ex) {
            encriptado = message;
        }

        return encriptado;

    }

    public static String MD5Digest(String message){

       return encriptar(message, "MD5");
    }
    public static String SHA1Digest(String message){

       return encriptar(message, "SHA1");
    }
    public static String encodeBase64(String message){

        return "";
    }
    public static String decodeBase64(String message){

        return "";
    }
    public static boolean isMD5Digest(String message){

        return (message.matches("^[A-Fa-f0-9]{32}$"));
    }
    public static boolean isSHA1Digest(String message){

        return (message.matches("^[A-Fa-f0-9]{40}$"));
    }

}
