package com.zeedoo.mars.file.transfer;

import java.nio.charset.Charset;
import java.util.zip.CRC32;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class CRC32Test {

	@Test
	public void CRC32Test() {
		HashFunction f= Hashing.crc32();
		String s = "1234";
		HashCode hc = f.newHasher().putString("1234", Charset.forName("UTF-8")).hash();
		
		CRC32 c = new CRC32();
		c.update(s.getBytes());
		
		Assert.assertEquals(hc.padToLong(), c.getValue());
	}

}
