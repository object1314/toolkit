/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.io;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

/**
 * Proxy on {@link BufferedImage} where provides some easy methods.
 * 
 * @see BufferedImage
 * @author XuYanhang
 * @since 2020-10-26
 *
 */
public class ProxyBufferedImage {

	/**
	 * Create the {@link ProxyBufferedImage} filled will a text
	 */
	public static ProxyBufferedImage newTextImage(String text, Font font, int textArgb, int backgroundArgb) {
		Rectangle2D rect = font.getStringBounds(text, 0, text.length(), new FontRenderContext(null, true, true));
		int x = (int) Math.round(rect.getX());
		int y = (int) Math.round(rect.getY());
		int w = (int) Math.ceil(rect.getWidth());
		int h = (int) Math.ceil(rect.getHeight());
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		try {
			g.setColor(new Color(backgroundArgb, true));
			g.fillRect(0, 0, w, h);
			g.setColor(new Color(textArgb, true));
			g.setFont(font);
			g.drawString(text, -x, -y);
		} finally {
			g.dispose();
		}
		return new ProxyBufferedImage(image);
	}

	/**
	 * The proxy image
	 */
	private BufferedImage image;

	/**
	 * Create the {@link ProxyBufferedImage} from an array of image data
	 */
	public ProxyBufferedImage(byte[] data) throws IOException {
		super();
		image = ImageIO.read(new ByteArrayInputStream(data));
	}

	/**
	 * Create the {@link ProxyBufferedImage} from an image file
	 */
	public ProxyBufferedImage(File imageFile) throws IOException {
		super();
		try (InputStream inputStream = new BufferedInputStream(new FileInputStream(imageFile))) {
			image = ImageIO.read(inputStream);
		}
	}

	/**
	 * Create the {@link ProxyBufferedImage} from an input stream. This method
	 * <em>does not</em> close the provided <code>InputStream</code> after the read
	 * operation has completed; it is the responsibility of the caller to close the
	 * stream, if desired.
	 */
	public ProxyBufferedImage(InputStream inputStream) throws IOException {
		super();
		image = ImageIO.read(inputStream);
	}

	/**
	 * Create the {@link ProxyBufferedImage} from a {@link BufferedImage} instance.
	 */
	public ProxyBufferedImage(BufferedImage image) {
		super();
		if (null == image)
			throw new NullPointerException();
		this.image = image;
	}

	/**
	 * Create the {@link ProxyBufferedImage} with empty value but in a pure color.
	 */
	public ProxyBufferedImage(int width, int height, int type) {
		super();
		image = new BufferedImage(width, height, type);
	}

	/**
	 * Returns a subImage defined by a specified rectangular region. The result
	 * {@link ProxyBufferedImage} shares the same data array as the original image.
	 * 
	 * @param x the X coordinate of the upper-left corner of the specified
	 *          rectangular region
	 * @param y the Y coordinate of the upper-left corner of the specified
	 *          rectangular region
	 * @param w the width of the specified rectangular region
	 * @param h the height of the specified rectangular region
	 * @return a <code>BufferedImage</code> that is the subimage of this
	 *         <code>BufferedImage</code>.
	 * @throws java.awt.image.RasterFormatException if the specified area is not
	 *         contained within this image.
	 */
	public ProxyBufferedImage subImage(int x, int y, int w, int h) {
		if (x == 0 && y == 0 && w == image.getWidth() && h == image.getHeight()) {
			return this;
		}
		return new ProxyBufferedImage(this.image.getSubimage(x, y, w, h));
	}

	/**
	 * Sure the image size and type otherwise reset the {@link ProxyBufferedImage}
	 * of one of the predefined image types.
	 * 
	 * @param width     width of the image
	 * @param height    height of the image
	 * @param imageType type of the created image
	 * @see BufferedImage#TYPE_INT_RGB
	 * @see BufferedImage#TYPE_INT_ARGB
	 * @see BufferedImage#TYPE_INT_ARGB_PRE
	 * @see BufferedImage#TYPE_INT_BGR
	 * @see BufferedImage#TYPE_3BYTE_BGR
	 * @see BufferedImage#TYPE_4BYTE_ABGR
	 * @see BufferedImage#TYPE_4BYTE_ABGR_PRE
	 * @see BufferedImage#TYPE_BYTE_GRAY
	 * @see BufferedImage#TYPE_USHORT_GRAY
	 * @see BufferedImage#TYPE_BYTE_BINARY
	 * @see BufferedImage#TYPE_BYTE_INDEXED
	 * @see BufferedImage#TYPE_USHORT_565_RGB
	 * @see BufferedImage#TYPE_USHORT_555_RGB
	 */
	public ProxyBufferedImage castFormat(int width, int height, int type) {

		if (image.getWidth() == width && image.getHeight() == height && image.getType() == type)
			return this;

		BufferedImage imageDist = new BufferedImage(width, height, type);
		Graphics2D graphics2d = imageDist.createGraphics();
		try {
			graphics2d.drawImage(image, 0, 0, width, height, null);
		} finally {
			graphics2d.dispose();
		}

		return new ProxyBufferedImage(imageDist);
	}

	/**
	 * Cast the image as the target image type.
	 * 
	 * @see #castFormat(int, int, int)
	 */
	public ProxyBufferedImage castType(int type) {
		return castFormat(image.getWidth(), image.getHeight(), type);
	}

	/**
	 * Cast the image as the target size.
	 * 
	 * @see #castFormat(int, int, int)
	 */
	public ProxyBufferedImage castSize(int width, int height) {
		return castFormat(width, height, image.getType());
	}

	/**
	 * Sets a pixel in the {@link ProxyBufferedImage} to the specified ARGB value.
	 * The pixel is assumed to be in the default RGB color model, TYPE_INT_ARGB, and
	 * default sRGB color space. For images with an <code>IndexColorModel</code>,
	 * the index with the nearest color is chosen.
	 */
	public ProxyBufferedImage setArgb(int x, int y, int argb) {
		image.setRGB(x, y, argb);
		return this;
	}

	/**
	 * Fills a rectangle region in the {@link ProxyBufferedImage} with the specified
	 * ARGB value.
	 */
	public ProxyBufferedImage fillArgb(int x, int y, int w, int h, int argb) {
		Graphics2D g = image.createGraphics();
		try {
			g.setColor(new Color(argb, true));
			g.fillRect(x, y, w, h);
		} finally {
			g.dispose();
		}
		return this;
	}

	/**
	 * Fills a rectangle region in the {@link ProxyBufferedImage} with the specified
	 * image.
	 */
	public ProxyBufferedImage fillImage(int x, int y, int w, int h, BufferedImage image) {
		Graphics2D g = this.image.createGraphics();
		try {
			g.drawImage(image, x, y, w, h, null);
		} finally {
			g.dispose();
		}
		return this;
	}

	/**
	 * Draw in a custom way
	 */
	public ProxyBufferedImage customPaint(Consumer<Graphics2D> consumer) {
		Graphics2D graphics2d = image.createGraphics();
		try {
			consumer.accept(graphics2d);
		} finally {
			graphics2d.dispose();
		}
		return this;
	}

	/**
	 * Get the source {@link BufferedImage}
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Returns the width of the image
	 */
	public int getWidth() {
		return image.getWidth();
	}

	/**
	 * Returns the height of the image
	 */
	public int getHeight() {
		return image.getHeight();
	}

	/**
	 * Returns an integer pixel in the default RGB color model (TYPE_INT_ARGB) and
	 * default sRGB colorspace. There are only 8-bits of precision for each color
	 * component in the returned data when using this method.
	 */
	public int getArgb(int x, int y) {
		return image.getRGB(x, y);
	}

	/**
	 * Returns the image type. If it is not one of the known types, TYPE_CUSTOM is
	 * returned. <br>
	 * 
	 * @see BufferedImage#TYPE_INT_RGB
	 * @see BufferedImage#TYPE_INT_ARGB
	 * @see BufferedImage#TYPE_INT_ARGB_PRE
	 * @see BufferedImage#TYPE_INT_BGR
	 * @see BufferedImage#TYPE_3BYTE_BGR
	 * @see BufferedImage#TYPE_4BYTE_ABGR
	 * @see BufferedImage#TYPE_4BYTE_ABGR_PRE
	 * @see BufferedImage#TYPE_BYTE_GRAY
	 * @see BufferedImage#TYPE_BYTE_BINARY
	 * @see BufferedImage#TYPE_BYTE_INDEXED
	 * @see BufferedImage#TYPE_USHORT_GRAY
	 * @see BufferedImage#TYPE_USHORT_565_RGB
	 * @see BufferedImage#TYPE_USHORT_555_RGB
	 * @see BufferedImage#TYPE_CUSTOM
	 */
	public int getType() {
		return image.getType();
	}

	/**
	 * Returns whether or not alpha is supported in the <code>ColorModel</code>
	 */
	public boolean hasAlpha() {
		ColorModel colorModel = image.getColorModel();
		return colorModel.hasAlpha();
	}

	/**
	 * Write the {@link ProxyBufferedImage} into file
	 */
	public ProxyBufferedImage write(String filePath, String formatName) throws IOException {
		return write(new File(filePath), formatName);
	}

	/**
	 * Write the {@link ProxyBufferedImage} into file
	 */
	public ProxyBufferedImage write(File file, String formatName) throws IOException {
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
		try {
			return write(outputStream, formatName);
		} finally {
			outputStream.close();
		}
	}

	/**
	 * Write the {@link ProxyBufferedImage} into an output stream.
	 * <p>
	 * This method <em>does not</em> close the provided <code>OutputStream</code>
	 * after the write operation has completed; it is the responsibility of the
	 * caller to close the stream, if desired.
	 */
	public ProxyBufferedImage write(OutputStream outputStream, String formatName) throws IOException {
		ProxyBufferedImage operator = this;
		ImageFormat format = ImageFormat.createFrom(formatName);
		int targetType = format.parseType(image.getType());
		if (targetType != operator.image.getType()) {
			operator = operator.castType(targetType);
		}
		if (!ImageIO.write(operator.image, format.format, outputStream)) {
			throw new IOException("Fails find the writer for " + format);
		}
		return operator;
	}

	/**
	 * Create the byte array data from the {@link ProxyBufferedImage} from a format.
	 */
	public byte[] toData(String formatName) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		write(out, formatName);
		return out.toByteArray();
	}

	/**
	 * Parse to bitmap
	 */
	public byte[] toBmpData() throws IOException {
		return toData("bmp");
	}

	/**
	 * Parse to 24 bit bitmap
	 */
	public byte[] toBmp24Data() throws IOException {
		return toData("bmp24");
	}

	/**
	 * Parse to jpeg
	 */
	public byte[] toJpgData() throws IOException {
		return toData("jpeg");
	}

	/**
	 * Parse to png
	 */
	public byte[] toPngData() throws IOException {
		return toData("png");
	}

	/**
	 * Parse to gif
	 */
	public byte[] toGifData() throws IOException {
		return toData("gif");
	}

	/**
	 * The image format name for parse the image. Here support the most image as
	 * bmp, jpeg, png, gif and ensure the image whole. While the gif can't be
	 * support whole
	 * 
	 * @author XuYanhang
	 *
	 */
	private static class ImageFormat {

		static final ImageFormat BMP = new ImageFormat("bmp") {

			int[] supportTypes = { // Types
					BufferedImage.TYPE_INT_RGB, //
					BufferedImage.TYPE_INT_BGR, //
					BufferedImage.TYPE_3BYTE_BGR, //
					BufferedImage.TYPE_BYTE_GRAY, //
					BufferedImage.TYPE_BYTE_BINARY, //
					BufferedImage.TYPE_BYTE_INDEXED, //
					BufferedImage.TYPE_USHORT_565_RGB, //
					BufferedImage.TYPE_USHORT_555_RGB//
			};

			@Override
			int parseType(int type) {
				for (int index = 0; index < supportTypes.length; index++)
					if (type == supportTypes[index])
						return supportTypes[index];
				return BufferedImage.TYPE_INT_BGR;
			}

		};

		static final ImageFormat BMP24 = new ImageFormat("bmp") {

			public int parseType(int type) {
				if (type == BufferedImage.TYPE_INT_RGB)
					return BufferedImage.TYPE_INT_RGB;
				if (type == BufferedImage.TYPE_3BYTE_BGR)
					return BufferedImage.TYPE_3BYTE_BGR;
				return BufferedImage.TYPE_INT_BGR;
			}

		};

		static final ImageFormat JPEG = new ImageFormat("jpeg") {

			int[] supportTypes = { // Types
					BufferedImage.TYPE_INT_RGB, //
					BufferedImage.TYPE_INT_BGR, //
					BufferedImage.TYPE_3BYTE_BGR, //
					BufferedImage.TYPE_4BYTE_ABGR_PRE, //
					BufferedImage.TYPE_BYTE_GRAY, //
					BufferedImage.TYPE_BYTE_BINARY, //
					BufferedImage.TYPE_BYTE_INDEXED, //
					BufferedImage.TYPE_USHORT_565_RGB, //
					BufferedImage.TYPE_USHORT_555_RGB//
			};

			@Override
			int parseType(int type) {
				for (int index = 0; index < supportTypes.length; index++)
					if (type == supportTypes[index])
						return supportTypes[index];
				return BufferedImage.TYPE_INT_BGR;
			}

		};

		static final ImageFormat PNG = new ImageFormat("png");

		static final ImageFormat GIF = new ImageFormat("gif");

		static ImageFormat createFrom(String format) {
			if (format.equalsIgnoreCase("bmp") || format.equalsIgnoreCase("bitmap"))
				return BMP;
			if (format.equalsIgnoreCase("bmp24"))
				return BMP24;
			if (format.equalsIgnoreCase("jpg") || format.equalsIgnoreCase("jpeg"))
				return JPEG;
			if (format.equalsIgnoreCase("png"))
				return PNG;
			if (format.equalsIgnoreCase("gif"))
				return GIF;
			return new ImageFormat(format);
		}

		String format;

		/**
		 * Create the format.
		 */
		private ImageFormat(String format) {
			super();
			this.format = format;
		}

		/**
		 * Filter the type.
		 */
		int parseType(int type) {
			return type;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return format;
		}

	}
}
