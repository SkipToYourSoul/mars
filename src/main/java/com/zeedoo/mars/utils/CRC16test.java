package com.zeedoo.mars.utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.StringUtils;

public class CRC16test {
	
    /** CRC16 polynomial 0x8005 */
    private static final int poly = 0x8005;
    private static final int[] table = new int[256];
    private int value = 0;

    static { // initialize static lookup table
            for (int i = 0; i < 256; i++) {
                    int crc = i << 8;
                    for (int j = 0; j < 8; j++) {
                            if ((crc & 0x8000) == 0x8000) {
                                    crc = (crc << 1) ^ poly;
                            } else {
                                    crc = (crc << 1);
                            }
                    }
                    table[i] = crc & 0xffff;
            }
    }

    /**
     * Update 16-bit CRC.
     * 
     * @param crc starting CRC value
     * @param bytes input byte array
     * @param off start offset to data
     * @param len number of bytes to process
     * @return 16-bit unsigned CRC
     */
    private int update(int crc, byte[] bytes, int off, int len) {
            for (int i = off; i < (off + len); i++) {
                    int b = (bytes[i] & 0xff);
                    crc = (table[((crc >> 8) & 0xff) ^ b] ^ (crc << 8)) & 0xffff;
            }
            return crc;
    }

    /**
     * Return lookup table
     */
    public static int[] getTable() {
            return table;
    }

    public int getValue() {
            return value;
    }

    public void reset() {
            value = 0;
    }

    /**
     * Update 16-bit CRC.
     * 
     * @param b input byte
     */
    public void update(int b) {
            byte[] ba = { (byte) (b & 0xff) };
            value = update(value, ba, 0, 1);
    }

    /**
     * Update 16-bit CRC.
     * 
     * @param b input byte array
     */
    public void update(byte[] b) {
            value = update(value, b, 0, b.length);
    }

    /**
     * Update 16-bit CRC.
     * 
     * @param b input byte array
     * @param off starting offset to data
     * @param len number of bytes to process
     */
    public void update(byte[] b, int off, int len) {
            value = update(value, b, off, len);
    }
    
    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(x);
        return buffer.array();
    }
	
	public static void main(String[] args) throws NoSuchAlgorithmException { 
		
		/*CRC16test crc16 = new CRC16test();
        String test = "abc";
        byte[] bytes = test.getBytes();
		crc16.update(bytes);
        byte[] buffer = ByteBuffer.allocate(bytes.length + 4).order(ByteOrder.LITTLE_ENDIAN).put(bytes).putInt(crc16.getValue()).array();

		int crc = crc16.getValue();
		System.out.println(crc);
		
		CRC16test crcNew = new CRC16test();		
		crcNew.update(buffer);
		//crcNew.update(crc);

		System.out.println(crcNew.getValue());*/
		String s = "hello world";
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(s.getBytes());
		byte byteData[] = md.digest();
		StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
 
        System.out.println("Digest(in hex format):: " + sb.toString());
		
		
    }
}
