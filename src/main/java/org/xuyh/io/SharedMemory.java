/*
 * Copyright (c) 2020-2023 XuYanhang
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
 */
public final class SharedMemory implements Closeable {
    /**
     * The bin file
     */
    private final File binFile;
    /**
     * The start byte position to manage in the file
     */
    private final long start;
    /**
     * The memory size to manage in the file
     */
    private final int size;
    /**
     * The IO for the memory
     */
    private final RandomAccessFile randomAccessFile;
    /**
     * The operating channel for the memory
     */
    private final FileChannel channel;
    /**
     * The mapped byte buffer for the memory
     */
    private final MappedByteBuffer buffer;

    /**
     * Create an instance of this class. Ensure the file has been created before or
     * a {@link FileNotFoundException} thrown.
     *
     * @param binFile the binary file to manage mapped in the shared memory
     * @param start   the start byte position to manage in the file
     * @param size    the manage memory size in bytes
     * @throws IOException if any I/O exception happens on map memory
     */
    public SharedMemory(File binFile, long start, int size) throws IOException {
        super();
        if (!binFile.exists()) throw new FileNotFoundException(binFile.getPath());
        if (start < 0) throw new IllegalArgumentException("Negative file position " + start);
        if (size < 0) throw new IllegalArgumentException("Negative memory size " + size);
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
        try (FileLock ignored = _tryLockFile(address, length, timeout)) {
            for (int index = 0; index < length; index++) {
                buffer.put(index + address, data[offset + index]);
            }
        }
    }

    /**
     * Read data
     */
    public byte[] getData(int address, int length, long timeout) throws IOException, InterruptedException {
        _checkMemoryRange(address, length);
        byte[] array = new byte[length];
        try (FileLock ignored = _tryLockFile(address, length, timeout)) {
            for (int index = 0; index < length; index++) {
                array[index] = buffer.get(index + address);
            }
        }
        return array;
    }

    /**
     * Write a byte
     */
    public void putByte(byte value, int address, long timeout) throws IOException, InterruptedException {
        _checkMemoryRange(address, Byte.BYTES);
        try (FileLock ignored = _tryLockFile(address, Byte.BYTES, timeout)) {
            buffer.put(address, value);
        }
    }

    /**
     * Read a byte
     */
    public byte getByte(int address, long timeout) throws IOException, InterruptedException {
        _checkMemoryRange(address, Byte.BYTES);
        try (FileLock ignored = _tryLockFile(address, Byte.BYTES, timeout)) {
            return buffer.get(address);
        }
    }

    /**
     * Write a character
     */
    public void putChar(char value, int address, long timeout) throws IOException, InterruptedException {
        _checkMemoryRange(address, Character.BYTES);
        try (FileLock ignored = _tryLockFile(address, Character.BYTES, timeout)) {
            buffer.putChar(address, value);
        }
    }

    /**
     * Read a character
     */
    public char getChar(int address, long timeout) throws IOException, InterruptedException {
        _checkMemoryRange(address, Character.BYTES);
        try (FileLock ignored = _tryLockFile(address, Character.BYTES, timeout)) {
            return buffer.getChar(address);
        }
    }

    /**
     * Write a short
     */
    public void putShort(short value, int address, long timeout) throws IOException, InterruptedException {
        _checkMemoryRange(address, Short.BYTES);
        try (FileLock ignored = _tryLockFile(address, Short.BYTES, timeout)) {
            buffer.putShort(address, value);
        }
    }

    /**
     * Read a short
     */
    public short getShort(int address, long timeout) throws IOException, InterruptedException {
        _checkMemoryRange(address, Short.BYTES);
        try (FileLock ignored = _tryLockFile(address, Short.BYTES, timeout)) {
            return buffer.getShort(address);
        }
    }

    /**
     * Write a integer
     */
    public void putInt(int value, int address, long timeout) throws IOException, InterruptedException {
        _checkMemoryRange(address, Integer.BYTES);
        try (FileLock ignored = _tryLockFile(address, Integer.BYTES, timeout)) {
            buffer.putInt(address, value);
        }
    }

    /**
     * Read a integer
     */
    public int getInt(int address, long timeout) throws IOException, InterruptedException {
        _checkMemoryRange(address, Integer.BYTES);
        try (FileLock ignored = _tryLockFile(address, Integer.BYTES, timeout)) {
            return buffer.getInt(address);
        }
    }

    /**
     * Write a long
     */
    public void putLong(long value, int address, long timeout) throws IOException, InterruptedException {
        _checkMemoryRange(address, Long.BYTES);
        try (FileLock ignored = _tryLockFile(address, Long.BYTES, timeout)) {
            buffer.putLong(address, value);
        }
    }

    /**
     * Read a long
     */
    public long getLong(int address, long timeout) throws IOException, InterruptedException {
        _checkMemoryRange(address, Long.BYTES);
        try (FileLock ignored = _tryLockFile(address, Long.BYTES, timeout)) {
            return buffer.getLong(address);
        }
    }

    /**
     * Write a float
     */
    public void putFloat(float value, int address, long timeout) throws IOException, InterruptedException {
        _checkMemoryRange(address, Float.BYTES);
        try (FileLock ignored = _tryLockFile(address, Float.BYTES, timeout)) {
            buffer.putFloat(address, value);
        }
    }

    /**
     * Read a float
     */
    public float getFloat(int address, long timeout) throws IOException, InterruptedException {
        _checkMemoryRange(address, Float.BYTES);
        try (FileLock ignored = _tryLockFile(address, Float.BYTES, timeout)) {
            return buffer.getFloat(address);
        }
    }

    /**
     * Write a double
     */
    public void putDouble(double value, int address, long timeout) throws IOException, InterruptedException {
        _checkMemoryRange(address, Double.BYTES);
        try (FileLock ignored = _tryLockFile(address, Double.BYTES, timeout)) {
            buffer.putDouble(address, value);
        }
    }

    /**
     * Read a double
     */
    public double getDouble(int address, long timeout) throws IOException, InterruptedException {
        _checkMemoryRange(address, Double.BYTES);
        try (FileLock ignored = _tryLockFile(address, Double.BYTES, timeout)) {
            return buffer.getDouble(address);
        }
    }

    /**
     * Load the content into physical memory
     */
    public void flush(int timeout) throws IOException, InterruptedException {
        try (FileLock ignored = _tryLockFile(0, size, timeout)) {
            buffer.load();
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
        if (timeout < 0L) {
            throw new IllegalArgumentException("Negative timeout " + timeout);
        }
        FileLock lock = null;
        long timeCursor = System.currentTimeMillis();
        while ((lock = channel.tryLock(address + start, length, false)) == null) {
            if (timeout != 0L) {
                if (Math.abs(System.currentTimeMillis() - timeCursor) > timeout) {
                    throw new InterruptedIOException("File lock timeout");
                }
            }
            Thread.sleep(1L);
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
