package kr.co.korbit.gia.util

import org.springframework.util.Assert
import java.io.IOException
import java.io.OutputStream
import javax.servlet.ServletOutputStream
import javax.servlet.WriteListener

class DelegatingServletOutputStream(targetStream: OutputStream) : ServletOutputStream() {
    val targetStream: OutputStream

    @Throws(IOException::class)
    override fun write(b: Int) {
        targetStream.write(b)
    }

    @Throws(IOException::class)
    override fun flush() {
        super.flush()
        targetStream.flush()
    }

    @Throws(IOException::class)
    override fun close() {
        super.close()
        targetStream.close()
    }

    override fun isReady(): Boolean {
        return true
    }

    override fun setWriteListener(listener: WriteListener) {}

    init {
        Assert.notNull(targetStream, "Target OutputStream must not be null")
        this.targetStream = targetStream
    }
}