package kr.co.korbit.gia.util

import org.springframework.util.Assert
import java.io.IOException
import java.io.InputStream
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream

/**
 * Delegating implementation of [ServletInputStream].
 *
 *
 * Used by [MockHttpServletRequest]; typically not directly
 * used for testing application controllers.
 *
 * @author Juergen Hoeller
 * @since 1.0.2
 * @see MockHttpServletRequest
 */
class DelegatingServletInputStream(sourceStream: InputStream) : ServletInputStream() {
    /**
     * Return the underlying source stream (never `null`).
     */
    val sourceStream: InputStream
    private var finished = false

    @Throws(IOException::class)
    override fun read(): Int {
        val data = sourceStream.read()
        if (data == -1) {
            finished = true
        }
        return data
    }

    @Throws(IOException::class)
    override fun available(): Int {
        return sourceStream.available()
    }

    @Throws(IOException::class)
    override fun close() {
        super.close()
        sourceStream.close()
    }

    override fun isFinished(): Boolean {
        return finished
    }

    override fun isReady(): Boolean {
        return true
    }

    override fun setReadListener(readListener: ReadListener) {
        throw UnsupportedOperationException()
    }

    /**
     * Create a DelegatingServletInputStream for the given source stream.
     * @param sourceStream the source stream (never `null`)
     */
    init {
        Assert.notNull(sourceStream, "Source InputStream must not be null")
        this.sourceStream = sourceStream
    }
}