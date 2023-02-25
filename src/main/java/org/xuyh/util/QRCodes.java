/*
 * Copyright (c) 2022 XuYanhang
 *
 */
package org.xuyh.util;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Normally, a QR(Quick Response) Code is a pattern of black-and white squares
 * that is printed on something and that can be read by some types of mobile phone
 * to give information to the user of the phone.
 * <p>
 * This is a toolkit as a quick way to resolve the decode operation and encode
 * operation between QR Code and data.
 *
 * @author XuYanhang
 * @since 2022-07-25
 *
 */
public final class QRCodes {

	/**
	 * Encodes a text to QR code as a image in a normal way in a pattern of
	 * black-and white squares. The result image will be as smaller as possible.
	 *
	 * @param text source text to encode, can't be empty
	 * @param charset charset to encode text, Nullable
	 * @param minSize minimum picture size to generate
	 * @return the result QR image
	 * @throws NullPointerException if the text is <code>null</code>
	 * @throws IllegalArgumentException if the text is empty
	 * 			or the size is negative
	 * @throws UnsupportedEncodingException
	 * 			if the text cannot be encoded legally in a format
	 */
	public static BufferedImage encode(String text, String charset, int minSize)
			throws UnsupportedEncodingException {
		return encode(text, charset, minSize, 0Xff000000, 0Xffffffff);
	}

	/**
	 * Encodes a text to QR code as a image. The result image will be as
	 * smaller as possible.
	 *
	 * @param text source text to encode, can't be empty
	 * @param charset charset to encode text, Nullable
	 * @param minSize minimum picture size to generate
	 * @param frontColorRgb the color to replace the original blank
	 * @param bgColorRgb the color to replace the original white
	 * @return the result QR image
	 * @throws NullPointerException if the text is <code>null</code>
	 * @throws IllegalArgumentException if the text is empty
	 * 			or the size is negative
	 * @throws UnsupportedEncodingException
	 * 			if the text cannot be encoded legally in a format
	 */
	public static BufferedImage encode(String text, String charset, int minSize,
			int frontColorRgb, int bgColorRgb)
			throws UnsupportedEncodingException {
		Map<EncodeHintType, String> hints = new HashMap<>();
		if (null != charset)
			hints.put(EncodeHintType.CHARACTER_SET, charset);
		BitMatrix bitMatrix;
		try {
			bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, minSize, minSize, hints);
		} catch (WriterException e) {
			throw (null != e.getCause() && (e.getCause() instanceof UnsupportedEncodingException))
					? (UnsupportedEncodingException) e.getCause()
					: new UnsupportedEncodingException(e.getMessage());
		}
		BufferedImage image = new BufferedImage(bitMatrix.getWidth(), bitMatrix.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < bitMatrix.getWidth(); i++)
			for (int j = 0; j < bitMatrix.getHeight(); j++)
				image.setRGB(i, j, bitMatrix.get(i, j) ? frontColorRgb : bgColorRgb);
		return image;
	}

	/**
	 * Decodes a image with QR code into text.
	 *
	 * @param image source image to decode, NonNull
	 * @param charset charset to decode QR code, Nullable
	 * @return the text of this QR code.
	 * @throws NullPointerException if the image is <code>null</code>
	 * @throws NotFoundException if no potential barcode is found
	 * @throws ChecksumException if a potential barcode is found but does not pass its checksum
	 * @throws FormatException if a potential barcode is found but format is invalid
	 */
	public static String decode(BufferedImage image, String charset)
			throws NotFoundException, ChecksumException, FormatException {
		BinaryBitmap binaryBitmap = new BinaryBitmap(
				new HybridBinarizer(
						new BufferedImageLuminanceSource(image)));
		Map<DecodeHintType, String> hints = new HashMap<>();
		if (null == charset)
			hints.put(DecodeHintType.CHARACTER_SET, charset);
		Result result;
		try {
			result = new QRCodeReader().decode(binaryBitmap, hints);
		} catch (NotFoundException e) {
			throw e;
		} catch (ChecksumException e) {
			throw e;
		} catch (FormatException e) {
			throw e;
		}
		return result.getText();
	}

	/**
	 * Decodes a image with zero to multiple QR codes into texts.
	 *
	 * @param image source image to decode, NonNull
	 * @param charset charset to decode QR code, Nullable
	 * @return a list of texts, not <code>null</code>, no empty element while maybe empty
	 * @throws NullPointerException if the image is <code>null</code>
	 */
	public static List<String> decodes(BufferedImage image, String charset) {
		BinaryBitmap binaryBitmap = new BinaryBitmap(
				new HybridBinarizer(
						new BufferedImageLuminanceSource(image)));
		Map<DecodeHintType, String> hints = new HashMap<>();
		if (null == charset)
			hints.put(DecodeHintType.CHARACTER_SET, charset);
		Result[] results;
		try {
			results = new QRCodeMultiReader().decodeMultiple(binaryBitmap, hints);
		} catch (NotFoundException e) {
			return Collections.emptyList();
		}
		if (results.length == 0) return Collections.emptyList();
		List<String> texts = new ArrayList<>(results.length);
		for (int i = 0; i < results.length; ++i)
			if (null != results[i] && null != results[i].getText() && !results[i].getText().isEmpty())
				texts.add(results[i].getText());
		return Collections.unmodifiableList(texts);
	}

	/**
	 * Shouldn't initialize.
	 */
	private QRCodes() {
		super();
	}
}
