package xyz.osamusasa.pdf.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Password {

    /**
     * パディング文字列
     */
    final byte[] padding = {
            (byte)0x28, (byte)0xBF, (byte)0x4E, (byte)0x5E,
            (byte)0x4E, (byte)0x75, (byte)0x8A, (byte)0x41,
            (byte)0x64, (byte)0x00, (byte)0x4E, (byte)0x56,
            (byte)0xFF, (byte)0xFA, (byte)0x01, (byte)0x08,
            (byte)0x2E, (byte)0x2E, (byte)0x00, (byte)0xB6,
            (byte)0xD0, (byte)0x68, (byte)0x3E, (byte)0x80,
            (byte)0x2F, (byte)0x0C, (byte)0xA9, (byte)0xFE,
            (byte)0x64, (byte)0x53, (byte)0x69, (byte)0x7A
    };

    /**
     * O値(オーナー値)
     *
     * PdfTrainerのEncryptキーに含まれるO
     */
    private byte[] o;

    /**
     * Pエントリ
     *
     * PdfTrainerのEncryptキーに含まれるP
     */
    private int p;

    /**
     * ファイル識別子
     *
     * PdfTrainerのIDキーの１つ目の要素
     */
    private byte[] id;

    /**
     * コンストラクタ
     *
     * @param o O値(オーナー値)
     * @param p Pエントリ
     * @param id ファイル識別子
     */
    public Password(byte[] o, int p, byte[] id) {
        this.o = o;
        this.p = p;
        this.id = id;
    }

    public byte[] getEncryptionKey(int objectNumber, int generation) {
        byte[] basePass = getPassword();
        byte[] encryptionKey = new byte[10];
        for (int i = 0; i < 5; i++) {
            encryptionKey[i] = basePass[i];
        }

        encryptionKey[5] = (byte) objectNumber;
        encryptionKey[6] = (byte) (objectNumber >>> 8);
        encryptionKey[7] = (byte) (objectNumber >>> 16);
        encryptionKey[8] = (byte) generation;
        encryptionKey[9] = (byte) (generation >>> 8);

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            encryptionKey = messageDigest.digest(encryptionKey);
//            System.out.println(objectNumber + ":" + Arrays.toString(encryptionKey));
            return Arrays.copyOf(encryptionKey, 10);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("cant get MD5 instance");
        }
    }

    public byte[] getPassword() {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            List<Byte> passwordSource = new ArrayList<>();
            for (byte b: padding)passwordSource.add(b);
            for (byte b: o)passwordSource.add(b);
            passwordSource.add((byte) p);
            passwordSource.add((byte) (p >>>  8));
            passwordSource.add((byte) (p >>> 16));
            passwordSource.add((byte) (p >>> 24));
            for (byte b: id)passwordSource.add(b);

            byte[] passwordSource_b = new byte[passwordSource.size()];
            for (int i = 0; i < passwordSource.size(); i++) {
                passwordSource_b[i] = passwordSource.get(i);
            }
            byte[] digest = md5.digest(passwordSource_b);

            for (int i = 0; i < 50; i++) {
                md5.update(digest, 0, 5);
                digest = md5.digest();
            }

            return Arrays.copyOf(digest, 5);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("cant get MD5 instance");
        }

    }

    @Override
    public String toString() {
        return "Password{" +
                "o=" + Arrays.toString(o) +
                ", p=" + p +
                ", id=" + Arrays.toString(id) +
                '}';
    }
}
