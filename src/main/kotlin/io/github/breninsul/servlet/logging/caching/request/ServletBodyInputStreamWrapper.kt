/*
 * MIT License
 *
 * Copyright (c) 2024 BreninSul
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.breninsul.servlet.logging.caching.request

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import java.io.InputStream
import java.util.concurrent.Callable
import java.util.concurrent.Semaphore

/**
 * This class extends the ServletInputStream class and provides an
 * implementation for reading the request body.
 *
 * @property delegate The underlying ByteArrayInputStream to read from.
 * @property internalSemaphore semaphore to sync calls between threads
 */
open class ServletBodyInputStreamWrapper(
    val delegate: InputStream,
) : ServletInputStream() {
    protected val internalSemaphore = Semaphore(1)

    /**
     * Determines if the reading of the request body is finished.
     *
     * @return true if
     */
    override fun isFinished(): Boolean = false

    /**
     * Determines whether the reading of the request body is ready.
     *
     * @return true if the reading of the
     */
    override fun isReady(): Boolean = true

    /**
     * Sets the ReadListener for this BodyInputStream.
     *
     * The ReadListener is responsible for being notified when data from the
     * request body is available for reading and when the request body has been
     * fully read.
     *
     * @param readListener the ReadListener to set
     */
    override fun setReadListener(readListener: ReadListener): Unit = throw UnsupportedOperationException()

    /**
     * Reads a single byte of data from the input stream.
     *
     * @return the byte read as an integer value
     * @throws IOException if an I/O error occurs
     */
    override fun read(): Int = delegate.read()

    /**
     * Reads a sequence of bytes from the input stream into the specified byte
     * array.
     *
     * @param b the buffer into which the data is read.
     * @param off the start offset in the buffer at which the data is written.
     * @param len the maximum number of bytes to read.
     * @return the total number of bytes read into the buffer, or -1 if there
     *     is no more data because the end of the stream has been reached.
     * @throws IOException if an I/O error occurs.
     */
    override fun read(
        b: ByteArray,
        off: Int,
        len: Int,
    ): Int = delegate.read(b, off, len)

    /**
     * Reads a sequence of bytes from the input stream into the specified byte
     * array.
     *
     * @param b the buffer into which the data is read.
     * @return the total number of bytes read into the buffer, or -1 if there
     *     is no more data because the end of the stream has been reached.
     * @throws IOException if an I/O error occurs.
     */
    override fun read(b: ByteArray): Int = delegate.read(b)

    /**
     * Skips over and discards a specified number of bytes from the input
     * stream.
     *
     * @param n the number of bytes to be skipped.
     * @return the actual number of bytes skipped.
     * @throws IOException if an I/O error occurs.
     */
    override fun skip(n: Long): Long = delegate.skip(n)

    /**
     * Returns the number of bytes that can be read from this input stream
     * without blocking.
     *
     * @return the number of bytes available for reading
     */
    override fun available(): Int = delegate.available()

    /**
     * Closes the input stream.
     *
     * This method delegates the close operation to the underlying stream. Once
     * closed, the input stream can no longer be used for reading data.
     *
     * @throws IOException if an I/O error occurs while closing the stream
     */
    override fun close() {
        delegate.close()
    }

    /**
     * Marks the current position in this input stream. A subsequent call*/
    override fun mark(readlimit: Int) {
        internalSemaphore.sync {
            delegate.mark(readlimit)
        }
    }

    /**
     * Resets the input stream by calling the `reset()` method on the delegate after acquiring the semaphore lock.
     * This method is synchronized using the `internalSemaphore` to ensure thread safety.
     */
    override fun reset() {
        internalSemaphore.sync {
            delegate.reset()
        }
    }

    /**
     * Determines whether this input stream supports the mark and reset methods.
     *
     * @return true if this input stream supports the mark and reset methods, false otherwise.
     */
    override fun markSupported(): Boolean = delegate.markSupported()

    protected open fun <T> Semaphore.sync(runnable: Callable<T>): T {
        try {
            this.acquire()
            return runnable.call()
        } finally {
            this.release()
        }
    }
}
