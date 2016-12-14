package mediumph.beam;


import com.kosalgeek.android.md5simply.MD5;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by josephgenio on 30/05/16.
 */
public class AES {

    //static String initVector = "AAAAAAAAAAAAAAAA"; TODO change this later if d gumana md5
    static String initVector = MD5.encrypt("hold").substring(0,16);
    private String encryptionKey; //TODO Add database shit

    public AES(){
        this.encryptionKey = MD5.encrypt("LOL").substring(0,16);//TODO CHANGE TO Database input for key
        //this.encryptionKey = "lol";
    }

    public byte[] encrypt(String plainText) throws Exception {
        try {
            IvParameterSpec iv = new IvParameterSpec(this.initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(this.encryptionKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return encrypted;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public String decrypt(byte[] cipherText) throws Exception{
        try {
            IvParameterSpec iv = new IvParameterSpec(this.initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(this.encryptionKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);


            byte[] original = null;

            if(!cipherText.equals(null)) {
                cipher.doFinal(cipherText);
                System.out.println("NOT NULL");
            }
            else
            System.out.println("IS NULL BITCH");

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
