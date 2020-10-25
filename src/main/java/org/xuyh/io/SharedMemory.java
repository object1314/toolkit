/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * Shared memory between processes. The memory can be used between processes
 * those one can read and another one can write. It's concurrent safe in
 * processes while not in threads in one process. In this memory, the start
 * address of each write method or read method is count from <code>zero</code>
 * to {@link #size} who maps the file physical memory from {@link #start} to
 * <code>{@link #start}+{@link #size}</code>
 * 
 * @author XuYanhang
 * @since 2020-10-26
 *
 */
public final class SharedMemory implements Closeable {

	/** The bin file */
	private File binFile;
	/** The start byte position to manage in the file */
	private long start;
	/** The memory size to manage in the file */
	private int size;
	/** The IO for the memory */
	private RandomAccessFile randomAccessFile;
	/** The operating channel for the memory */
	private FileChannel channel;
	/** The mapped byte buffer for the memory */
	private MappedByteBuffer buffer;

	/**
	 * Create an instance of this class. Ensure the file has been created before or
	 * a {@link java.io.FileNotFoundException} thrown.
	 * 
	 * @param binFile the binary file to manage mapped in the shared memory
	 * @param start   the start byte position to manage in the file
	 * @param size    the manage memory size in bytes
	 * @throws IOException if any I/O exception happens on map memory
	 */
	public SharedMemory(File binFile, long start, int size) throws IOException {
		super();
		if (!binFile.exists())
			throw new FileNotFoundException(binFile.getPath());
		if (start < 0)
			throw new IllegalArgumentException("Negative file position " + start);
		if (size < 0)
			throw new IllegalArgumentException("Negative memory size " + size);
		this.binFile = binFile.getCanonicalFile();
		this.start = start;
		this.size = size;
		this.randomAccessFile = new RandomAccessFile(this.binFile, "rw");
		try {
			this.channel = this.randomAccessFile.getChannel();
			this.buffer = this.channel.map(FileChannel.MapMode.READ_WRITE, start, size);
		} catch (Throwable t) {
			this.randomAccessFile.close();
			throw t;
		}
	}

	/**
	 * @return the {@link #binFile}
	 */
	public File getBinFile() {
		return binFile;
	}

	/**
	 * @return the {@link #start}
	 */
	public long getStart() {
		return start;
	}

	/**
	 * @return the {@link #size}
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Write data
	 */
	public void putData(byte[] data, int address, long timeout) throws IOException, InterruptedException {
		putData(data, 0, data.length, address, timeout);
	}

	/**
	 * Write data
	 */
	public void putData(byte[] data, int offset, int length, int address, long timeout)
			throws IOException, InterruptedException {
		if (offset < 0 || offset + length < offset || offset + length > data.length)
			throw new IllegalArgumentException("array range");
		_checkMemoryRange(address, length);
		FileLock lock = _tryLockFile(address, length, timeout);
		try {
			for (int index = 0; index < length; index++) {
				buffer.put(index + address, data[offset + index]);
			}
		} finally {
			lock.release();
		}
	}

	/**
	 * Read data
	 */
	public byte[] getData(int address, int length, long timeout) throws IOException, InterruptedException {
		_checkMemoryRange(address, length);
		byte[] array = new byte[length];
		FileLock lock = _tryLockFile(address, length, timeout);
		try {
			for (int index = 0; index < length; index++) {
				array[index] = buffer.get(index + address);
			}
		} finally {
			lock.release();
		}
		return array;
	}

	/**
	 * Write a byte
	 */
	public void putByte(byte value, int address, long timeout) throws IOException, InterruptedException {
		_checkMemoryRange(address, Byte.BYTES);
		FileLock lock = _tryLockFile(address, Byte.BYTES, timeout);
		try {
			buffer.put(address, value);
		} finally {
			lock.release();
		}
	}

	/**
	 * Read a byte
	 */
	public byte getByte(int address, long timeout) throws IOException, InterruptedException {
		_checkMemoryRange(address, Byte.BYTES);
		FileLock lock = _tryLockFile(address, Byte.BYTES, timeout);
		try {
			return buffer.get(address);
		} finally {
			lock.release();
		}
	}

	/**
	 * Write a character
	 */
	public void putChar(char value, int address, long timeout) throws IOException, InterruptedException {
		_checkMemoryRange(address, Character.BYTES);
		FileLock lock = _tryLockFile(address, Character.BYTES, timeout);
		try {
			buffer.putChar(address, value);
		} finally {
			lock.release();
		}
	}

	/**
	 * Read a character
	 */
	public char getChar(int address, long timeout) throws IOException, InterruptedException {
		_checkMemoryRange(address, Character.BYTES);
		FileLock lock = _tryLockFile(address, Character.BYTES, timeout);
		try {
			return buffer.getChar(address);
		} finally {
			lock.release();
		}
	}

	/**
	 * Write a short
	 */
	public void putShort(short value, int address, long timeout) throws IOException, InterruptedException {
		_checkMemoryRange(address, Short.BYTES);
		FileLock lock = _tryLockFile(address, Short.BYTES, timeout);
		try {
			buffer.putShort(address, value);
		} finally {
			lock.release();
		}
	}

	/**
	 * Read a short
	 */
	public short getShort(int address, long timeout) throws IOException, InterruptedException {
		_checkMemoryRange(address, Short.BYTES);
		FileLock lock = _tryLockFile(address, Short.BYTES, timeout);
		try {
			return buffer.getShort(address);
		} finally {
			lock.release();
		}
	}

	/**
	 * Write a integer
	 */
	public void putInt(int value, int address, long timeout) throws IOException, InterruptedException {
		_checkMemoryRange(address, Integer.BYTES);
		FileLock lock = _tryLockFile(address, Integer.BYTES, timeout);
		try {
			buffer.putInt(address, value);
		} finally {
			lock.release();
		}
	}

	/**
	 * Read a integer
	 */
	public int getInt(int address, long timeout) throws IOException, InterruptedException {
		_checkMemoryRange(address, Integer.BYTES);
		FileLock lock = _tryLockFile(address, Integer.BYTES, timeout);
		try {
			return buffer.getInt(address);
		} finally {
			lock.release();
		}
	}

	/**
	 * Write a long
	 */
	public void putLong(long value, int address, long timeout) throws IOException, InterruptedException {
		_checkMemoryRange(address, Long.BYTES);
		FileLock lock = _tryLockFile(address, Long.BYTES, timeout);
		try {
			buffer.putLong(address, value);
		} finally {
			lock.release();
		}
	}

	/**
	 * Read a long
	 */
	public long getLong(int address, long timeout) throws IOException, InterruptedException {
		_checkMemoryRange(address, Long.BYTES);
		FileLock lock = _tryLockFile(address, Long.BYTES, timeout);
		try {
			return buffer.getLong(address);
		} finally {
			lock.release();
		}
	}

	/**
	 * Write a float
	 */
	public void putFloat(float value, int address, long timeout) throws IOException, InterruptedException {
		_checkMemoryRange(address, Float.BYTES);
		FileLock lock = _tryLockFile(address, Float.BYTES, timeout);
		try {
			buffer.putFloat(address, value);
		} finally {
			lock.release();
		}
	}

	/**
	 * Read a float
	 */
	public float getFloat(int address, long timeout) throws IOException, InterruptedException {
		_checkMemoryRange(address, Float.BYTES);
		FileLock lock = _tryLockFile(address, Float.BYTES, timeout);
		try {
			return buffer.getFloat(address);
		} finally {
			lock.release();
		}
	}

	/**
	 * Write a double
	 */
	public void putDouble(double value, int address, long timeout) throws IOException, InterruptedException {
		_checkMemoryRange(address, Double.BYTES);
		FileLock lock = _tryLockFile(address, Double.BYTES, timeout);
		try {
			buffer.putDouble(address, value);
		} finally {
			lock.release();
		}
	}

	/**
	 * Read a double
	 */
	public double getDouble(int address, long timeout) throws IOException, InterruptedException {
		_checkMemoryRange(address, Double.BYTES);
		FileLock lock = _tryLockFile(address, Double.BYTES, timeout);
		try {
			return buffer.getDouble(address);
		} finally {
			lock.release();
		}
	}

	/**
	 * Load the content into physical memory
	 */
	public void flush(int timeout) throws IOException, InterruptedException {
		FileLock lock = _tryLockFile(0, size, timeout);
		try {
			buffer.load();
		} finally {
			lock.release();
		}
	}

	/**
	 * Check if the range is available
	 */
	private void _checkMemoryRange(int address, int length) throws RuntimeException {
		if (address < 0 || address + length < address || address + length > start + size)
			throw new IndexOutOfBoundsException("OutOfMemory");
	}

	/**
	 * Get file lock in timeout milliseconds
	 */
	private FileLock _tryLockFile(int address, int length, long timeout) throws IOException, InterruptedException {
		if (timeout < 0) {
			throw new IllegalArgumentException("Negative timeout " + timeout);
		}
		FileLock lock = null;
		long timecursor = System.currentTimeMillis();
		while ((lock = channel.tryLock(address + start, length, false)) == null) {
			if (timeout != 0) {
				if (Math.abs(System.currentTimeMillis() - timecursor) > timeout) {
					throw new InterruptedIOException("File lock timeout");
				}
			}
			Thread.sleep(1);
		}
		return lock;
	}

	/**
	 * Close this memory
	 */
	public void close() throws IOException {
		this.randomAccessFile.close();
	}

}
