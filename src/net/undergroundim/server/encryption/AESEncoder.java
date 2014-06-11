package net.undergroundim.server.encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author Troy
 *
 */
public class AESEncoder {
    private Cipher ecipher;
    private Cipher dcipher;

    /**
     * Construct a new encrypted to encrypt and
     * decrypt packets for the client.
     * 
     * @param key
     */
    public AESEncoder(SecretKeySpec key) {
    	try {
    		ecipher = Cipher.getInstance("AES");
    		dcipher = Cipher.getInstance("AES");
    		ecipher.init(Cipher.ENCRYPT_MODE, key);
    		dcipher.init(Cipher.DECRYPT_MODE, key);
    	}catch (Exception e){}
    }

    public String encrypt(String str) {
        try {
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc = ecipher.doFinal(utf8);

            return Encoder.encode(enc).replaceAll("\r\n", "");
        }catch(Exception e){}
        return null;
    }

    public String decrypt(String str) {
        try {
            byte[] dec = Encoder.decodeB(str);
            byte[] utf8 = dcipher.doFinal(dec);

            return new String(utf8, "UTF8");
        }catch(Exception e){}
        return null;
    }
    
}