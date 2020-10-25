/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.util;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

/**
 * Quick method to do operation on RSA encrypt or decrypt.
 * <p>
 * <h2>SCENE1</h2><br>
 * <i>Person B</i> sends a message to <i>Person A</i>:<br>
 * STEP1: <i>Person A</i> generate a pair of <tt>RSAKey</tt>, keeps the
 * <tt>RSAPrivateKey</tt> self while the <tt>RSAPublicKey</tt> can be told to
 * anyone.<br>
 * STEP2: <i>Person B</i> requests the <tt>RSAPublicKey</tt> from <i>Person
 * A</i>, encrypts the message in the key and sends it to <i>Person A</i>. <br>
 * STEP3: <i>Person A</i> receives the message from <i>Person B</i>, decrypts
 * the data as message and resolves this message.
 * <p>
 * <h2>SCENE2</h2><br>
 * <i>Person A</i> ensures the message to <i>Person B</i>:<br>
 * STEP1: <i>Person A</i> generate a pair of <tt>RSAKey</tt>, keeps the
 * <tt>RSAPrivateKey</tt> self while the <tt>RSAPublicKey</tt> can be told to
 * anyone.<br>
 * STEP2: <i>Person A</i> makes the ensure message, signs the message as a
 * signature, sends both the message and signature to <i>Person B</i>.<br>
 * STEP3: <i>Person B</i> receives the message and signature, verifys the
 * message and signature with the <tt>RSAPublicKey</tt> from <i>Person A</i> and
 * resolves the message and the verify result.
 *
 * @author XuYanhang
 * @since 2020-10-25
 *
 */
public final class RSAs {

	/**
	 * Returns a new key pair in RSA use direct key size of 1024 bits. The
	 * {@link KeyPair#getPublic()} is an instance of {@link RSAPublicKey} while the
	 * {@link KeyPair#getPrivate()} is an instance of {@link RSAPrivateKey}.
	 * 
	 * @return a random RSA keyPair
	 */
	public static KeyPair newKeyPair() {
		return newKeyPair(1024);
	}

	/**
	 * Returns a new key pair in RSA use direct key size of specified bits. The
	 * {@link KeyPair#getPublic()} is an instance of {@link RSAPublicKey} while the
	 * {@link KeyPair#getPrivate()} is an instance of {@link RSAPrivateKey}.
	 * 
	 * @param keySize an algorithm-specific metric, such as modulus length,
	 *                specified in number of bits
	 * @return a random RSA keyPair
	 * @throws InvalidParameterException if the {@code keysize} is not supported by
	 *                                   this KeyPairGenerator object.
	 */
	public static KeyPair newKeyPair(int keySize) {
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			throw new InternalError(e);
		}
		kpg.initialize(keySize);
		return kpg.generateKeyPair();
	}

	/**
	 * From a public key value, generate the public key instance
	 * 
	 * @param key public key data
	 * @return the public key of the data
	 * @throws IllegalArgumentException if this public can not be encoded as a
	 *                                  public key
	 */
	public static RSAPublicKey toPublicKey(byte[] key) {
		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			throw new InternalError(e);
		}
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
		try {
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (InvalidKeySpecException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * From a public key value, generate the public key instance
	 * 
	 * @param key private key data
	 * @return the public key of the data
	 * @throws IllegalArgumentException if this public can not be encoded as a
	 *                                  public key
	 */
	public static RSAPrivateKey toPrivateKey(byte[] key) {
		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			throw new InternalError(e);
		}
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
		try {
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (InvalidKeySpecException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Encrypt the whole data in a {@link RSAPublicKey} or a {@link RSAPrivateKey}.
	 * 
	 * @param data source data to encrypt
	 * @param key  the RSA key used
	 * @return data after encrypt
	 */
	public static byte[] encrypt(byte[] data, RSAKey key) {
		return encrypt(data, 0, data.length, key);
	}

	/**
	 * Encrypt a part of data in a {@link RSAPublicKey} or a {@link RSAPrivateKey}.
	 * 
	 * @param data source data to encrypt
	 * @param off  offset to encrypt in source data
	 * @param len  length to encrypt in source data
	 * @param key  the RSA key used
	 * @return data after encrypt
	 */
	public static byte[] encrypt(byte[] data, int off, int len, RSAKey key) {
		int to = off + len;
		if (off < 0 || off > to || to > data.length)
			throw new IndexOutOfBoundsException();
		int keySize = key.getModulus().bitLength();
		final int decryptBlock = keySize >> 3;
		final int encryptBlock = decryptBlock - 11;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new InternalError(e);
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, (Key) key);
		} catch (InvalidKeyException e) {
			throw new IllegalStateException(e);
		}
		int paragraphs = len / encryptBlock;
		if ((len - encryptBlock * paragraphs) != 0)
			paragraphs++;
		byte[] result = new byte[paragraphs * decryptBlock];
		int i = off;
		int j = 0;
		while (i < to) {
			int li = (to - i) > encryptBlock ? encryptBlock : (to - i);
			int lj;
			try {
				lj = cipher.doFinal(data, i, li, result, j);
			} catch (ShortBufferException | IllegalBlockSizeException | BadPaddingException e) {
				throw new InternalError(e);
			}
			i += li;
			j += lj;
		}
		if (j < result.length) {
			result = Arrays.copyOf(result, j);
		}
		return result;
	}

	/**
	 * Encrypt the whole data in a {@link RSAPublicKey} or a {@link RSAPrivateKey}.
	 * 
	 * @param data   source data to encrypt
	 * @param key    the RSA key used
	 * @param target target to write encrypt data
	 * @return the length of encrypt data
	 */
	public static int encrypt(byte[] data, RSAKey key, OutputStream target) throws IOException {
		return encrypt(data, 0, data.length, key, target);
	}

	/**
	 * Encrypt a part of data in a {@link RSAPublicKey} or a {@link RSAPrivateKey}.
	 * 
	 * @param data   source data to encrypt
	 * @param off    offset to encrypt in source data
	 * @param len    length to encrypt in source data
	 * @param key    the RSA key used
	 * @param target target to write encrypt data
	 * @return the length of encrypt data
	 */
	public static int encrypt(byte[] data, int off, int len, RSAKey key, OutputStream target) throws IOException {
		int to = off + len;
		if (off < 0 || off > to || to > data.length)
			throw new IndexOutOfBoundsException();
		int keySize = key.getModulus().bitLength();
		final int decryptBlock = keySize >> 3;
		final int encryptBlock = decryptBlock - 11;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new InternalError(e);
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, (Key) key);
		} catch (InvalidKeyException e) {
			throw new IllegalStateException(e);
		}
		byte[] cache = new byte[decryptBlock];
		int i = off;
		int resultLen = 0;
		while (i < to) {
			int li = (to - i) > encryptBlock ? encryptBlock : (to - i);
			int lj;
			try {
				lj = cipher.doFinal(data, i, li, cache);
			} catch (ShortBufferException | IllegalBlockSizeException | BadPaddingException e) {
				throw new InternalError(e);
			}
			target.write(cache, 0, lj);
			i += li;
			resultLen += lj;
		}
		return resultLen;
	}

	/**
	 * Decrypt the whole data in a {@link RSAPublicKey} or a {@link RSAPrivateKey}.
	 * 
	 * @param data source data to decrypt
	 * @param key  the RSA key used
	 * @return data after decrypt
	 */
	public static byte[] decrypt(byte[] data, RSAKey key) {
		return decrypt(data, 0, data.length, key);
	}

	/**
	 * Decrypt a part of data in a {@link RSAPublicKey} or a {@link RSAPrivateKey}.
	 * 
	 * @param data source data to decrypt
	 * @param off  offset to decrypt in source data
	 * @param len  length to decrypt in source data
	 * @param key  the RSA key used
	 * @return data after decrypt
	 */
	public static byte[] decrypt(byte[] data, int off, int len, RSAKey key) {
		int to = off + len;
		if (off < 0 || off > to || to > data.length)
			throw new IndexOutOfBoundsException();
		int keySize = key.getModulus().bitLength();
		final int decryptBlock = keySize >> 3;
		final int encryptBlock = decryptBlock - 11;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new InternalError(e);
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, (Key) key);
		} catch (InvalidKeyException e) {
			throw new IllegalStateException(e);
		}
		int paragraphs = len / decryptBlock;
		if ((len - decryptBlock * paragraphs) != 0)
			throw new IllegalStateException("Padding failed");
		byte[] result = new byte[paragraphs * encryptBlock + 11];
		int i = off;
		int j = 0;
		while (i < to) {
			int li = (to - i) > decryptBlock ? decryptBlock : (to - i);
			int lj;
			try {
				lj = cipher.doFinal(data, i, li, result, j);
			} catch (ShortBufferException | IllegalBlockSizeException | BadPaddingException e) {
				throw new InternalError(e);
			}
			i += li;
			j += lj;
		}
		if (j < result.length) {
			result = Arrays.copyOf(result, j);
		}
		return result;
	}

	/**
	 * Decrypt the whole data in a {@link RSAPublicKey} or a {@link RSAPrivateKey}.
	 * 
	 * @param data   source data to decrypt
	 * @param key    the RSA key used
	 * @param target target to write decrypt data
	 * @return the length of decrypt data
	 */
	public static int decrypt(byte[] data, RSAKey key, OutputStream target) throws IOException {
		return decrypt(data, 0, data.length, key, target);
	}

	/**
	 * Decrypt a part of data in a {@link RSAPublicKey} or a {@link RSAPrivateKey}.
	 * 
	 * @param data   source data to decrypt
	 * @param off    offset to decrypt in source data
	 * @param len    length to decrypt in source data
	 * @param key    the RSA key used
	 * @param target target to write decrypt data
	 * @return the length of decrypt data
	 */
	public static int decrypt(byte[] data, int off, int len, RSAKey key, OutputStream target) throws IOException {
		int to = off + len;
		if (off < 0 || off > to || to > data.length)
			throw new IndexOutOfBoundsException();
		int keySize = key.getModulus().bitLength();
		final int decryptBlock = keySize >> 3;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new InternalError(e);
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, (Key) key);
		} catch (InvalidKeyException e) {
			throw new IllegalStateException(e);
		}
		int paragraphs = len / decryptBlock;
		if ((len - decryptBlock * paragraphs) != 0)
			throw new IllegalStateException("Padding failed");
		byte[] cache = new byte[decryptBlock];
		int i = off;
		int resultLen = 0;
		while (i < to) {
			int li = (to - i) > decryptBlock ? decryptBlock : (to - i);
			int lj;
			try {
				lj = cipher.doFinal(data, i, li, cache);
			} catch (ShortBufferException | IllegalBlockSizeException | BadPaddingException e) {
				throw new InternalError(e);
			}
			target.write(cache, 0, lj);
			i += li;
			resultLen += lj;
		}
		return resultLen;
	}

	/**
	 * Returns sign of the whole data in a {@link RSAPrivateKey}. Use a rule of
	 * <code>MD5withRSA</code>.
	 * 
	 * @param data source data to make sign
	 * @param key  RSA private key to make sign
	 * @return sign result of the data
	 */
	public static byte[] sign(byte[] data, RSAPrivateKey key) {
		return sign(data, 0, data.length, key);
	}

	/**
	 * Returns sign of a part of data in a {@link RSAPrivateKey}. Use a rule of
	 * <code>MD5withRSA</code>.
	 * 
	 * @param data    source data to make sign
	 * @param dataOff offset to make sign of the source data
	 * @param dataLen length to make sign of the source data
	 * @param key     RSA private key to make sign
	 * @return sign result of the data
	 */
	public static byte[] sign(byte[] data, int dataOff, int dataLen, RSAPrivateKey key) {
		if (dataOff < 0 || dataOff + dataLen < dataOff || dataOff + dataLen > data.length)
			throw new IndexOutOfBoundsException();
		Signature signature;
		try {
			signature = Signature.getInstance("MD5withRSA");
		} catch (NoSuchAlgorithmException e) {
			throw new InternalError(e);
		}
		try {
			signature.initSign(key);
		} catch (InvalidKeyException e) {
			throw new IllegalStateException(e);
		}
		try {
			signature.update(data, dataOff, dataLen);
		} catch (SignatureException e) {
			throw new InternalError(e);
		}
		try {
			return signature.sign();
		} catch (SignatureException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Verify a data and sign in a {@link RSAPublicKey}. Returns <code>true</code>
	 * if the sign on the data is match with this public key. Use a rule of
	 * <code>MD5withRSA</code>.
	 * 
	 * @param data source data to verify
	 * @param key  RSA private key to verify
	 * @param sign sign data
	 * @return <code>true</code> if the sign on the data is match
	 */
	public static boolean verify(byte[] data, RSAPublicKey key, byte[] sign) {
		return verify(data, 0, data.length, key, sign, 0, sign.length);
	}

	/**
	 * Verify a data and sign in a {@link RSAPublicKey}. Returns <code>true</code>
	 * if the sign on the data is match with this public key. Use a rule of
	 * <code>MD5withRSA</code>.
	 * 
	 * @param data    source data to verify
	 * @param dataOff offset to verify of the source data
	 * @param dataLen length to verify of the source data
	 * @param key     RSA private key to verify
	 * @param sign    sign data
	 * @return <code>true</code> if the sign on the data is match
	 */
	public static boolean verify(byte[] data, int dataOff, int dataLen, RSAPublicKey key, byte[] sign) {
		return verify(data, dataOff, dataLen, key, sign, 0, sign.length);
	}

	/**
	 * Verify a data and sign in a {@link RSAPublicKey}. Returns <code>true</code>
	 * if the sign on the data is match with this public key. Use a rule of
	 * <code>MD5withRSA</code>.
	 * 
	 * @param data    source data to verify
	 * @param dataOff offset to verify of the source data
	 * @param dataLen length to verify of the source data
	 * @param key     RSA private key to verify
	 * @param sign    sign data
	 * @param signOff offset of the sign data to verify
	 * @param signLen length of the sign data to verify
	 * @return <code>true</code> if the sign on the data is match
	 */
	public static boolean verify(byte[] data, int dataOff, int dataLen, RSAPublicKey key, byte[] sign, int signOff,
			int signLen) {
		if (dataOff < 0 || dataOff + dataLen < dataOff || dataOff + dataLen > data.length)
			throw new IndexOutOfBoundsException();
		if (signOff < 0 || signOff + signLen < signOff || signOff + signLen > sign.length)
			throw new IndexOutOfBoundsException();
		Signature signature;
		try {
			signature = Signature.getInstance("MD5withRSA");
		} catch (NoSuchAlgorithmException e) {
			throw new InternalError(e);
		}
		try {
			signature.initVerify(key);
		} catch (InvalidKeyException e) {
			throw new IllegalStateException(e);
		}
		try {
			signature.update(data, dataOff, dataLen);
		} catch (SignatureException e) {
			throw new InternalError(e);
		}
		try {
			return signature.verify(sign, signOff, signLen);
		} catch (SignatureException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private RSAs() {
		super();
	}

}
