import java.io.Closeable
import javax.servlet.ServletContext
import javax.servlet.ServletContextEvent

class BackgroundJob(runnable: Runnable, name: String) : Closeable, Thread(runnable, name) {
    /** 작업을 수행할 thread  */
    private var isShutdown = false

    /** context  */
    private var sc: ServletContext? = null

    override fun close() {
        super<Thread>.interrupt()
        try {
            super<Thread>.join()
            isShutdown = true
        } catch(e: Exception) {
            if( e is InterruptedException ) {
                //Do nothing : normal close
            } else {
                e.printStackTrace()
            }
        }
    }
}