package kr.co.korbit.gia.util

import org.apache.commons.io.output.TeeOutputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.PrintWriter
import javax.servlet.ServletOutputStream
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponseWrapper

class ResponseWrapper(response: HttpServletResponse?) : HttpServletResponseWrapper(response) {
    private val bos = ByteArrayOutputStream()
    private val writer = PrintWriter(bos)

    @Throws(IOException::class)
    override fun getOutputStream(): ServletOutputStream {
        return DelegatingServletOutputStream(TeeOutputStream(super.getOutputStream(), bos))
    }

    override fun getResponse(): ServletResponse {
        return this
    }

    @Throws(IOException::class)
    override fun getWriter(): PrintWriter {
        return TeePrintWriter(super.getWriter(), writer)
    }

    fun toByteArray(): ByteArray {
        return bos.toByteArray()
    }

}