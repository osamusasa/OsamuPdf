package xyz.osamusasa.pdf.encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * データの復号を行うクラス
 */
public class Decrypt {
    public static byte[] decrypt(byte[] source, Password password, int objectNumber, int generation) {
        try {
            Cipher cipher = Cipher.getInstance("RC4");
            Key k = new SecretKeySpec(password.getEncryptionKey(objectNumber, generation), "RC4");
            cipher.init(Cipher.DECRYPT_MODE, k);
            return cipher.doFinal(source);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("cant get RC4 instance");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException("cant use padding");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("cant use password");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new RuntimeException("IllegalBlockSizeException");
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException("BadPaddingException");
        }
    }
}
