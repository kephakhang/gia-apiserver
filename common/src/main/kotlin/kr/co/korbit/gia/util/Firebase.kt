package kr.co.korbit.gia.util

import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.*
import com.google.firebase.messaging.Message
import kr.co.korbit.common.extensions.stackTraceString
import kr.co.korbit.gia.jpa.common.UserApp
import mu.KotlinLogging
import org.springframework.context.ApplicationContext
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.URL
import java.util.*

class Firebase {
    private val accessToken: AccessToken? = null
    private var googleCredentials: GoogleCredentials? = null
    private var sendUrl: String? = null
    private var dbUrl: String? = null
    private var jsonKey: String? = null
    private var options: FirebaseOptions? = null
    private var firbaseApp: FirebaseApp? = null
    private var firebaseMessaging: FirebaseMessaging? = null

    fun getFirbaseApp(): FirebaseApp? {
        return firbaseApp
    }

    fun getFirebaseMessaging(): FirebaseMessaging? {
        return firebaseMessaging
    }

    fun toFile(url: URL?): File? {
        return if (url == null || url.protocol != "file") {
            null
        } else {
            var filename = url.file.replace('/', File.separatorChar)
            var pos = 0
            while (filename.indexOf('%', pos).also { pos = it } >= 0) {
                if (pos + 2 < filename.length) {
                    val hexStr = filename.substring(pos + 1, pos + 3)
                    val ch = hexStr.toInt(16).toChar()
                    filename = filename.substring(0, pos) + ch + filename.substring(pos + 3)
                }
            }
            File(filename)
        }
    }

    @Throws(Exception::class)
    fun getClassLoaderFile(filename: String?): InputStream? {
        // note that this method is used when initializing logging, so it must
        // not attempt to log anything.
        val loader = Firebase::class.java.classLoader
        val inputStream = loader.getResourceAsStream(filename)
        return if (inputStream != null) {
            inputStream
        } else {
            var url = loader.getResource(filename)
            val file = if (url == null) {
                url = ClassLoader.getSystemResource(filename)
                if (url == null) {
                    throw Exception("Unable to find $filename")
                }
                toFile(url)
            } else {
                toFile(url)
            }
            if (file == null || !file.exists()) {
                null
            } else FileInputStream(file)
        }
    }

    fun createApp(): FirebaseApp? {
        return try {
            logger.info("serviceAccount init")
            val serviceAccount = getClassLoaderFile(jsonKey)
                ?: throw Exception("Firebase serviceAccount is null : " + jsonKey)
            logger.info("serviceAccount init2")
            googleCredentials =
                GoogleCredentials.fromStream(serviceAccount).createScoped(Arrays.asList(*SCOPES))
            logger.info("googleCredentials init")
            serviceAccount.close()
            logger.info("serviceAccount close")
            options = FirebaseOptions.Builder()
                .setCredentials(googleCredentials)
                .setDatabaseUrl(dbUrl)
                .build()
            firbaseApp = FirebaseApp.initializeApp(options, dbUrl)
            if (firbaseApp == null) {
                logger.info("firbaseApp is null")
            }
            firebaseMessaging = FirebaseMessaging.getInstance(firbaseApp)
            firbaseApp
        } catch (e: Exception) {
            logger.error("createApp exception : " + e.stackTraceString)
            null
        }
    }

    @Throws(Exception::class)
    fun sendPush(data: Map<String?, String?>, pushKey: String?) {
        try {
            if (pushKey == null) {
                return
            }
            logger.debug("sendPush message : $data")
            val message =
                Message.builder()
                    .putAllData(data)
                    .setToken(pushKey)
                    .build()
            logger.info("sendPush Message : $message")
            logger.info("sendPush firebaseMessaging : " + firebaseMessaging.toString())
            val response = firebaseMessaging!!.send(message)
            logger.debug("sendPush response : $response")
        } catch (e: Exception) {
            logger.error("sendPush exception : " + e.stackTraceString)
        } finally {
            //try { this.firbaseApp.delete(); } catch(Exception e){}
        }
    }

    @Throws(Exception::class)
    fun sendMulticastPush(
        data: Map<String?, String?>,
        targetList: List<UserApp>,
        resultHandler: PushResultHandler?,
        handlerArg: Any?
    ) {

        try {
            logger.debug("sendPush message : $data")
            val pushKeyArr: MutableList<String> = ArrayList()
            for (target in targetList) {
                logger.info("targetKey : " + target.push_key)
                pushKeyArr.add(target.push_key)
            }
            val message = MulticastMessage.builder()
                .putAllData(data)
                .addAllTokens(pushKeyArr)
                .build()
            if (firebaseMessaging == null) {
                logger.info("firebaseMessaging is null")
                return
            }
            logger.info("sendPush Message : " + message.toString())
            logger.info("sendPush firebaseMessaging : " + firebaseMessaging.toString())
            val response = firebaseMessaging!!.sendMulticast(message)
            if (response != null) {
                logger.debug("sendPush response : $response")
                var i = 0
                for (res in response.getResponses()) {
                    try {
                        val id: String = res.getMessageId()
                        val ua: UserApp = targetList[i]
                        if (res.isSuccessful()) {
                            if (resultHandler != null) resultHandler.send(handlerArg, ua.user)
                        } else {
                            if (resultHandler != null) resultHandler.error(
                                handlerArg,
                                ua.user,
                                "Firbase Send Response Error"
                            )
                        }
                        i++
                    } catch (e: Exception) {
                        logger.error(e.stackTraceString)
                    }
                }
            }
        } catch (e: Exception) {
            logger.error("sendPush exception : " + e.stackTraceString)
        } finally {
            //try { this.firbaseApp.delete(); } catch(Exception e){}
        }
    }

    companion object {
        val logger = KotlinLogging.logger(Firebase::class.java.name)
        var firebase: Firebase? = null
        val SCOPES = arrayOf(
            "https://www.googleapis.com/auth/firebase.database",
            "https://www.googleapis.com/auth/userinfo.email",
            "https://www.googleapis.com/auth/firebase",
            "https://www.googleapis.com/auth/cloud-platform",
            "https://www.googleapis.com/auth/firebase.readonly"
        )
        const val FCM_KORBIT_SDK_KEY_JSON =
            "/google/korbit-ae2c9-firebase-adminsdk-ohajk-4efd1cf727.json"
        const val FCM_KORBIT_SEND_URL =
            "https://fcm.googleapis.com/v1/projects/korbit-ae2c9/messages:send"
        const val FCM_KORBIT_DB_URL = "https://korbit-ae2c9.firebaseio.com"

        fun geInstance(context: ApplicationContext): Firebase? {
            return Firebase.firebase
        }

        val instance: Firebase?
            get() {
                logger.info("getBizInstance")
                if( Firebase.firebase == null) {
                    logger.info("firebaseBiz null")
                    Firebase.firebase = Firebase()
                    Firebase.firebase!!.sendUrl = FCM_KORBIT_SEND_URL
                    Firebase.firebase!!.dbUrl = FCM_KORBIT_DB_URL
                    Firebase.firebase!!.jsonKey = FCM_KORBIT_SDK_KEY_JSON
                    Firebase.firebase!!.createApp()
                }
                return Firebase.firebase
            }

        @Throws(FirebaseMessagingException::class)
        fun sendToToken(registrationToken: String?) {
            // [START send_to_token]
            // This registration token comes from the client FCM SDKs.

            // ToDo push 알림 메세지 전송 실 연둥 필요
            // See documentation on defining a message payload.
            val message: Message = Message.builder()
                .putData("title", "Korbit Inc.")
                .putData("contents", "Hi")
                .putData("move_type2", "main")
                .setToken(registrationToken)
                .build()

            // Send a message to the device corresponding to the provided
            // registration token.
            val response: String = FirebaseMessaging.getInstance().send(message)
            // Response is a message ID string.
            println("Successfully sent message: $response")
            // [END send_to_token]
        }
    }
}