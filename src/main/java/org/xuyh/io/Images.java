/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.io;

/**
 * Toolkits relative with images.
 * 
 * @author XuYanhang
 * @since 2020-10-27
 *
 */
public final class Images {

	/**
	 * Cast an RGB color pixel to YUV color pixel.
	 * 
	 * @param rgb RGB color where only lower 24 bits used
	 * @return YUV color where only 24 bits used
	 */
	public static int rgb2yuv(int rgb) {
		// Split
		int r = (rgb >> 16) & 0Xff;
		int g = (rgb >> 8) & 0Xff;
		int b = rgb & 0Xff;
		// Calculate
		int y = (int) (0.299 * r + 0.587 * g + 0.114 * b);
		int u = (int) (-0.147 * r - 0.289 * g + 0.436 * b + 128);
		int v = (int) (0.615 * r - 0.515 * g - 0.100 * b + 128);
		// Check
		y = y < 0 ? 0 : (y > 255 ? 255 : y);
		u = u < 0 ? 0 : (u > 255 ? 255 : u);
		v = v < 0 ? 0 : (v > 255 ? 255 : v);
		// Pack
		return (y << 16) | (u << 8) | v;
	}

	/**
	 * Cast an YUV color pixel to RGB color pixel.
	 * 
	 * @param yuv YUV color where only lower 24 bits used
	 * @return RGB color where only 24 bits used
	 */
	public static int yuv2rgb(int yuv) {
		// Split
		int y = (yuv >> 16) & 0Xff;
		int u = (yuv >> 8) & 0Xff;
		int v = yuv & 0Xff;
		// Calculate
		int r = (int) (y + 1.4075 * (v - 128));
		int g = (int) (y - 0.3455 * (u - 128) - 0.7169 * (v - 128));
		int b = (int) (y + 1.7790 * (u - 128));
		// Check
		r = r < 0 ? 0 : (r > 255 ? 255 : r);
		g = g < 0 ? 0 : (g > 255 ? 255 : g);
		b = b < 0 ? 0 : (b > 255 ? 255 : b);
		// Pack
		return (r << 16) | (g << 8) | b;
	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private Images() {
		super();
	}

}
