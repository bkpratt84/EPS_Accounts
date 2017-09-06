package net.edmondschools.accounts.beans;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class Util {
    public static void redirect(String url) throws IOException {
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        if (url.startsWith("http://") || url.startsWith("https://")) {
            ctx.redirect(url);
        } else {
            ctx.redirect(ctx.getRequestContextPath() + url);
        }
    }
    
    public static byte[] hashPassword(final char[] password, final byte[] salt, final int iterations, final int keyLength) throws InvalidKeySpecException {
       try {
           SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
           PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
           SecretKey key = skf.generateSecret(spec);
           byte[] res = key.getEncoded();
           return res;
 
       } catch( NoSuchAlgorithmException | InvalidKeySpecException e ) {
           throw new RuntimeException( e );
       }
   }
}