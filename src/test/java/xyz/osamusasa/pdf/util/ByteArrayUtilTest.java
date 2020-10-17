package xyz.osamusasa.pdf.util;

import static org.junit.jupiter.api.Assertions.*;

class ByteArrayUtilTest {

    @org.junit.jupiter.api.Test
    void subAry() {
        Byte[] q = {97, 98, 99, 10, 100, 101, 102, 103};
        byte[] a = {98, 99};
        assertArrayEquals(a, ByteArrayUtil.subAry(q, 1, 3));
    }

    @org.junit.jupiter.api.Test
    void subString() {
        Byte[] q = {97, 98, 99, 10, 100, 101, 102, 103};
        String a = "bc";
        assertEquals(a, ByteArrayUtil.subString(q, 1, 3));
    }

    @org.junit.jupiter.api.Test
    void subAryUntilReturn() {
        Byte[] q = {97, 98, 99, 10, 100, 101, 102, 103};
        byte[] a = {98, 99};
        assertArrayEquals(a, ByteArrayUtil.subAryUntilReturn(q, 1));
    }
}