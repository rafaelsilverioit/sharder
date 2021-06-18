package br.com.rafaelsilverio.sharder.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import br.com.rafaelsilverio.sharder.HashAlgorithm;

public class DigestAlgorithm
	implements HashAlgorithm {
	private MessageDigest instance;

	public DigestAlgorithm(Algorithms algorithm)
		throws NoSuchAlgorithmException {
		String value;
		
		switch(algorithm) {
			case MD5:
				value = "MD5";
				break;
			case SHA1:
				value = "SHA-1";
				break;
			case SHA256:
				value = "SHA-256";
				break;
			default:
				value = "MD5";
		}
		
		this.instance = MessageDigest.getInstance(value);
	}

	@Override
	public Long hash(String key) {
		instance.reset();
		instance.update(key.getBytes());
		
		byte[] digest = instance.digest();

		long hash = 0;
		for (int i = 0; i < 4; i++) {
			hash <<= 8;
			hash |= ((int) digest[i]) & 0xFF;
		}
		
		return hash;
	}
	
	public enum Algorithms {
		MD5, SHA1, SHA256;
	}
}