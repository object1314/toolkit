package org.xuyh.io;

/**
 * A professional class to resolve standard bitmap image data on 24 bits or 32
 * bits. Specializes in bitmap image's query, cut out, rotate and flip
 * operation.
 * 
 * @author XuYanhang
 * @since 2020-10-22
 *
 */
public class BmpImage {

	/**
	 * Create a 24 bitmap {@link BmpImage} in specified pure color and size
	 */
	public static BmpImage createPure24(int width, int height, int color) {

		BmpImage image = new BmpImage(24, new byte[height][width * 3]);

		color &= 0X00ffffff;
		if (color == 0)
			return image;

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				image.datamap[row][col * 3] = (byte) (color & 0Xff);
				image.datamap[row][col * 3 + 1] = (byte) ((color >> 8) & 0Xff);
				image.datamap[row][col * 3 + 2] = (byte) ((color >> 16) & 0Xff);
			}
		}
		return image;
	}

	/**
	 * Create a 32 bitmap {@link BmpImage} in specified pure color and size
	 */
	public static BmpImage createPure32(int width, int height, int color) {

		BmpImage image = new BmpImage(32, new byte[height][width * 4]);

		if (color == 0)
			return image;

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				image.datamap[row][col * 4] = (byte) (color & 0Xff);
				image.datamap[row][col * 4 + 1] = (byte) ((color >> 8) & 0Xff);
				image.datamap[row][col * 4 + 2] = (byte) ((color >> 16) & 0Xff);
				image.datamap[row][col * 4 + 3] = (byte) ((color >> 24) & 0Xff);
			}
		}
		return image;
	}

	/**
	 * Data map contains bitmap image's data as a top to bottom direction.
	 */
	private byte[][] datamap;

	/**
	 * Bit count of the image. It's bit space to hold a pixel.
	 */
	private int bitcount;

	/**
	 * Create a {@link BmpImage} in its bitCount{@link #bitcount} but uninitialized
	 * {@link #datamap}.
	 * 
	 * @param bitcount The {@link #bitcount} of the image.
	 */
	private BmpImage(int bitcount, byte[][] datamap) {
		super();
		this.bitcount = bitcount;
		this.datamap = datamap;
	}

	/**
	 * Create a {@link BmpImage} in a bitmap image data.
	 * 
	 * @param data Bitmap image's full byte array, including headers
	 * @throws IllegalStateException When fails to resolve the bitmap data
	 */
	public BmpImage(byte[] data) {

		super();

		BitReader reader = new BitReader(data);

		// Analyze the file header
		if (reader.nextUnsignedShort(true) != 0X424d)
			throw new IllegalStateException("HEADER_NOT_RECORGNIZED");
		if (reader.nextInt(false) > data.length)
			throw new IllegalStateException("FILE_DESTROYED");
		reader.skip(4);
		int dataOffset = reader.nextInt(false);
		if (dataOffset < 54)
			throw new IllegalStateException("UNSUPPORT DATA BEGIN AT " + dataOffset);
		if (data.length < dataOffset)
			throw new IllegalStateException("FILE_DESTROYED");

		// Analyze the info header
		if (reader.nextInt(false) != 40)
			throw new IllegalStateException("FILE_DESTROYED");

		int width = reader.nextInt(false);
		if (width < 0)
			throw new IllegalStateException("FILE_DESTROYED");

		int realheight = reader.nextInt(false);
		int height = realheight < 0 ? -realheight : realheight;

		reader.skip(2);

		bitcount = reader.nextUnsignedShort(false);
		if (bitcount != 24 && bitcount != 32)
			throw new IllegalStateException("SUPPORT 24,32 BUT NOT " + bitcount);

		reader.position(dataOffset);

		// Analyze the data map

		int realDataSizePerline = (bitcount >> 3) * width;
		int dataSizePerLine = (realDataSizePerline + 3) >> 2 << 2;
		int unusedDataSizePerline = dataSizePerLine - realDataSizePerline;
		int dataRegionSize = height * dataSizePerLine;
		if (dataRegionSize > reader.available())
			throw new IllegalStateException("FILE_DESTROYED");

		datamap = new byte[height][];

		if (realheight >= 0) {
			for (int i = height - 1; i >= 0; i--) {
				datamap[i] = reader.nextBytes(realDataSizePerline);
				reader.skip(unusedDataSizePerline);
			}
		} else {
			for (int i = 0; i < height; i++) {
				datamap[i] = reader.nextBytes(realDataSizePerline);
				reader.skip(unusedDataSizePerline);
			}
		}
	}

	/**
	 * Get the bitmap image data of the {@link BmpImage}.
	 * 
	 * @return Bitmap image data.
	 */
	public byte[] toByteArray() {

		int height = datamap.length;

		int width = 0;

		int fileSize = 54;

		int unusedDataSizePerline = 0;

		if (height > 0) {

			int realDataSizePerline = datamap[0].length;

			int dataSizePerLine = (realDataSizePerline + 3) >> 2 << 2;

			unusedDataSizePerline = dataSizePerLine - realDataSizePerline;

			width = realDataSizePerline / (bitcount >> 3);

			fileSize += (height * dataSizePerLine);
		}

		BitWriter writer = new BitWriter(size());

		// Write file header: BITMAPFILEHEADER
		writer.putShort(0X424d, true) // 2 bytes of type
				.putInt(fileSize, false) // 2 bytes of size
				.skip(4) // 4 bytes Reserved
				.putInt(54, false); // 4 bytes OffBits

		// Write info header: BITMAPINFOHEADER
		writer.putInt(40, false) // 4 bytes size
				.putInt(width, false) // 4 bytes width
				.putInt(height, false) // 4 bytes height
				.putShort(1, false) // 2 bytes Planes
				.putShort(bitcount, false) // 2 bytes BitCount
				.skip(4) // 4 bytes Compression as zero
				.putInt(fileSize - 54, false) // 4 bytes SizeImage
				.skip(16); // XPelsPM, YPelsPM, ClrUsed, ClrImportant

		// Write the data.
		for (int i = datamap.length - 1; i >= 0; i--)
			writer.putBytes(datamap[i]).skip(unusedDataSizePerline);

		return writer.out();
	}

	/**
	 * Get this bitcount.
	 * 
	 * @return {@link #bitcount}
	 */
	public int bitcount() {

		return bitcount;
	}

	/**
	 * Get the byte count to hold a pixel.
	 * 
	 * @return A pixel size.
	 */
	public int pixelSize() {

		return bitcount >> 3;
	}

	/**
	 * Get the width of the image in pixel unit.
	 * 
	 * @return The bitmap image's width.
	 */
	public int width() {

		if (datamap.length == 0)
			return 0;

		return datamap[0].length / (bitcount >> 3);
	}

	/**
	 * Get the height of the image in pixel unit.
	 * 
	 * @return The bitmap image's height.
	 */
	public int height() {

		return datamap.length;
	}

	/**
	 * Get the file size of the bitmap image.
	 * 
	 * @return Byte count to hold the image.
	 */
	public int size() {

		if (datamap.length == 0)
			return 54;

		return datamap.length * ((datamap[0].length + 3) >> 2 << 2) + 54;
	}

	/**
	 * Fill this image by a known image.
	 */
	public BmpImage fillImage(int x, int y, BmpImage other) {

		if (bitcount != other.bitcount)
			throw new IllegalArgumentException("bit count unmatch");

		int startX = x > 0 ? x : 0;
		int startY = y > 0 ? y : 0;
		int endX = x + other.width();
		endX = endX > width() ? width() : endX;
		int endY = y + other.height();
		endY = endY > height() ? height() : endY;

		if (startX >= endX || startY >= endY)
			return this;

		int pixelSize = pixelSize();

		for (int row = startY; row < endY; row++) {
			System.arraycopy(other.datamap[row - y], (x >= 0 ? 0 : -x) * pixelSize, datamap[row], startX * pixelSize,
					(endX - startX) * pixelSize);
		}

		return this;
	}

	/**
	 * Returns the color at the specified pixel position
	 */
	public int getColor(int x, int y) {

		if (x < 0 || x >= width() || y < 0 || y >= height())
			throw new IllegalArgumentException("Out Range");

		int ps = pixelSize();
		int color = 0;
		for (int i = 0; i < ps; i++)
			color |= ((((int) datamap[y][x * ps + i]) & 0Xff) << (i << 3));
		return color;
	}

	/**
	 * Fill a rectangle in the {@link BmpImage} in a pure color.
	 */
	public BmpImage fillColor(int x, int y, int w, int h, int color) {

		if (x < 0) {
			w = w + x;
			x = 0;
		}

		if (y < 0) {
			h = h + y;
			y = 0;
		}

		if (w + x > width()) {
			w = width() - x;
		}

		if (h + y > height()) {
			h = height() - y;
		}

		if (w <= 0 || h <= 0) {
			return this;
		}

		int pixelSize = pixelSize();
		for (int row = y; row < y + h; row++) {
			for (int col = x; col < x + w; col++) {
				for (int i = 0; i < pixelSize; i++) {
					datamap[row][col * pixelSize + i] = (byte) ((color >> (i << 3)) & 0Xff);
				}
			}
		}

		return this;
	}

	/**
	 * Sub a new {@link BmpImage} from this {@link BmpImage}. The sub region must be
	 * a part of this {@link BmpImage}. So the start position should be in the
	 * region, otherwise a new <tt>Exception</tt> thrown. When the start position is
	 * in the region but the end position not then the end position moved to the
	 * margin.
	 * 
	 * @param x      The start position x
	 * @param y      The start position y
	 * @param width  The width to {@link BmpImage}
	 * @param height The height to {@link BmpImage}
	 * @return New sub {@link BmpImage} from this {@link BmpImage}
	 * @throws IllegalArgumentException When the parameters is illegal
	 */
	public BmpImage subImage(int x, int y, int width, int height) {

		int totalWidth = width();
		int totalHeight = height();

		if (width < 0 || height < 0)
			throw new IllegalArgumentException("Size");

		if (x < 0 || x > totalWidth || y < 0 || y > totalHeight)
			throw new IllegalArgumentException("Out Range");

		if (x + width < 0 || x + width > totalWidth)
			width = totalWidth - x;

		if (y + height < 0 || y + height > totalHeight)
			height = totalHeight - y;

		int pixelSize = pixelSize();

		byte[][] targetData = new byte[height][width * pixelSize];

		for (int i = 0; i < height; i++)
			System.arraycopy(datamap[i + y], x * pixelSize, targetData[i], 0, targetData[i].length);

		return new BmpImage(bitcount, targetData);
	}

	/**
	 * Reset the width and height for the {@link BmpImage}. The orignal image is on
	 * the left-top corner while the extra pixels fill with the default color.
	 */
	public BmpImage resize(int width, int height, int color) {

		if (width < 0 || height < 0)
			throw new IllegalArgumentException("Size");

		int totalWidth = width();
		int totalHeight = height();

		if (totalWidth == width && totalHeight == height)
			return this;

		int pixelSize = pixelSize();
		if (pixelSize < 3)
			color &= 0X00ffffff;

		BmpImage target = new BmpImage(bitcount, new byte[height][width * pixelSize]);

		for (int row = 0; row < height; row++) {
			int col = 0;
			if (row < totalHeight) {
				int len = Math.min(totalWidth, width) * pixelSize;
				System.arraycopy(datamap[row], 0, target.datamap[row], 0, len);
				col += (len / pixelSize);
			}
			if (color == 0) {
				continue;
			}
			for (; col < width; col++) {
				for (int i = 0; i < pixelSize; i++) {
					target.datamap[row][col * pixelSize + i] = (byte) ((color >> (i << 3)) & 0Xff);
				}
			}
		}

		return target;
	}

	/**
	 * Reset the width and height for the {@link BmpImage}. The orignal image is on
	 * the left-top corner while the extra pixels fill with the last row or col
	 * color.
	 */
	public BmpImage resize(int width, int height) {

		if (width < 0 || height < 0)
			throw new IllegalArgumentException("Size");

		int totalWidth = width();
		int totalHeight = height();

		if (totalWidth == width && totalHeight == height)
			return this;

		int ps = pixelSize();

		BmpImage target = new BmpImage(bitcount, new byte[height][width * ps]);

		for (int row = 0; row < height; row++) {
			int col = 0;
			if (row < totalHeight) {
				int len = Math.min(totalWidth, width) * ps;
				System.arraycopy(datamap[row], 0, target.datamap[row], 0, len);
				col += (len / ps);
			}
			for (; col < width; col++) {
				int color = 0Xff000000;
				if (totalWidth > 0 && totalHeight > 0)
					color = getColor(Math.min(col, totalWidth - 1), Math.min(row, totalHeight - 1));
				for (int i = 0; i < ps; i++)
					target.datamap[row][col * ps + i] = (byte) ((color >> (i << 3)) & 0Xff);
			}
		}

		return target;
	}

	/**
	 * Rotate a bitmap image on this {@link BmpImage} and then we'll get a new
	 * {@link BmpImage} but when nothing changed this {@link BmpImage} will be get.
	 * 
	 * @param rightAngleCount The 90<sup>o</sup><i>CW</i> rotated times. If is
	 *                        negative then it's 90<sup>o</sup><i>CCW</i> rotated
	 *                        method. For example if it's 1 then we'll rotate the
	 *                        image 90<sup>o</sup><i>CW</i>, if it's -1 then we'll
	 *                        rotate the image 90<sup>o</sup> <i>CCW</i>.
	 * @return A {@link BmpImage} to represent the result of this rotate operation.
	 */
	public BmpImage rotateClock(int rightAngleCount) {

		int times = ((rightAngleCount % 4) + 4) % 4;

		int width = width();
		int height = height();

		if (width == 0 || height == 0 || times == 0)
			return this;

		byte[][] targetDatamap = null;

		int pixelSize = pixelSize();

		if (times == 2) {

			targetDatamap = new byte[height][width * pixelSize];

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					for (int x = 0; x < pixelSize; x++) {
						targetDatamap[i][j * pixelSize + x] = datamap[height - 1 - i][(width - 1 - j) * pixelSize + x];
					}
				}
			}

		} else {

			targetDatamap = new byte[width][height * pixelSize];

			if (times == 1) {
				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						for (int x = 0; x < pixelSize; x++) {
							targetDatamap[i][j * pixelSize + x] = datamap[height - 1 - j][i * pixelSize + x];
						}
					}
				}
			} else {
				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						for (int x = 0; x < pixelSize; x++) {
							targetDatamap[i][j * pixelSize + x] = datamap[j][(width - 1 - i) * pixelSize + x];
						}
					}
				}
			}
		}

		return new BmpImage(bitcount, targetDatamap);
	}

	/**
	 * Flip the bitmap image in horizontal direction for this {@link BmpImage} and
	 * we'll get a new image but this when nothing changed.
	 * 
	 * @return The image to represent this flip operation result.
	 */
	public BmpImage flipHorizontal() {

		int height = datamap.length;

		if (height == 0)
			return this;

		int pixelSize = bitcount >> 3;

		int width = datamap[0].length / pixelSize;

		BmpImage target = new BmpImage(bitcount, new byte[height][datamap[0].length]);

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				for (int x = 0; x < pixelSize; x++) {
					target.datamap[i][j * pixelSize + x] = datamap[i][(width - 1 - j) * pixelSize + x];
				}
			}
		}

		return target;
	}

	/**
	 * Flip the bitmap image in vertical direction for this {@link BmpImage} and
	 * we'll get a new image but this when nothing changed.
	 * 
	 * @return The image to represent this flip operation result.
	 */
	public BmpImage flipVertical() {

		int height = datamap.length;

		if (height == 0)
			return this;

		byte[][] targetDatamap = new byte[height][];

		for (int i = 0; i < height; i++)
			targetDatamap[i] = datamap[height - 1 - i];

		return new BmpImage(bitcount, targetDatamap);
	}

}
