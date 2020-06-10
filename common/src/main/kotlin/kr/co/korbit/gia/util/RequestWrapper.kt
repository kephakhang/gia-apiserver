package kr.co.korbit.gia.util

import org.apache.commons.io.input.TeeInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

class RequestWrapper(request: HttpServletRequest?) : HttpServletRequestWrapper(request) {
    private val bos = ByteArrayOutputStream()

    @Throws(IOException::class)
    override fun getInputStream(): ServletInputStream {
        return DelegatingServletInputStream(TeeInputStream(super.getInputStream(), bos))
    }

    fun toByteArray(): ByteArray {
        return bos.toByteArray()
    }

}