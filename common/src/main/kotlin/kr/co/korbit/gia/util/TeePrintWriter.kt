package kr.co.korbit.gia.util

import java.io.PrintWriter

class TeePrintWriter(main: PrintWriter?, var branch: PrintWriter) : PrintWriter(main, true) {
    override fun flush() {
        super.flush()
        branch.flush()
    }

    override fun write(c: Int) {
        super.write(c)
        super.flush()
        branch.write(c)
        branch.flush()
    }

    override fun write(buf: CharArray, off: Int, len: Int) {
        super.write(buf, off, len)
        super.flush()
        branch.write(buf, off, len)
        branch.flush()
    }

    override fun write(s: String, off: Int, len: Int) {
        super.write(s, off, len)
        super.write(s, off, len)
        branch.write(s, off, len)
        branch.flush()
    }

}