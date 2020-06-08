package kr.co.korbit.gia.util

import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.*
import com.google.firebase.messaging.Message
import kr.co.korbit.exception.stackTraceString
import kr.co.korbit.gia.jpa.common.UserApp
import mu.KotlinLogging
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.URL
import java.util.*

class Firebase {
//    @Autowired
//    var msgSvc: MsgService? = null
    val accessToken: AccessToken? = null
    var googleCredentials: GoogleCredentials? = null
    var sendUrl: String? = null
    var dbUrl: String? = null
    var jsonKey: String? = null
    var options: FirebaseOptions? = null
    var firbaseApp: FirebaseApp? = null
    var firebaseMessaging: FirebaseMessaging? = null
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
        var file: File? = null
        val loader = Firebase::class.java.classLoader
        val inputStream = loader.getResourceAsStream(filename)
        return if (inputStream != null) {
            inputStream
        } else {
            var url = loader.getResource(filename)
            if (url == null) {
                url = ClassLoader.getSystemResource(filename)
                if (url == null) {
                    throw Exception("Unable to find $filename")
                }
                file = toFile(url)
            } else {
                file = toFile(url)
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
        var message: Message? = null
        var response: String? = null
        try {
            if (pushKey == null) {
                return
            }
            logger.debug("sendPush message : $data")
            message =
                Message.builder()
                    .putAllData(data)
                    .setToken(pushKey)
                    .build()
            logger.info("sendPush Message : $message")
            logger.info("sendPush firebaseMessaging : " + firebaseMessaging.toString())
            response = firebaseMessaging.send(message)
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
        resultHandler: MsgConsumer.PushResultHandler?,
        handlerArg: Any?
    ) {
        var message: MulticastMessage? = null
        var response: BatchResponse? = null
        try {
            logger.debug("sendPush message : $data")
            val pushKeyArr: MutableList<String> = ArrayList()
            for (target in targetList) {
                logger.info("targetKey : " + target.push_key)
                pushKeyArr.add(target.push_key)
            }
            message = MulticastMessage.builder()
                .putAllData(data)
                .addAllTokens(pushKeyArr)
                .build()
            if (firebaseMessaging == null) {
                logger.info("firebaseMessaging is null")
                return
            }
            logger.info("sendPush Message : " + message.toString())
            logger.info("sendPush firebaseMessaging : " + firebaseMessaging.toString())
            response = firebaseMessaging!!.sendMulticast(message)
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
        var firebaseUser: Firebase? = null
        var firebaseBiz: Firebase? = null
        val SCOPES = arrayOf(
            "https://www.googleapis.com/auth/firebase.database",
            "https://www.googleapis.com/auth/userinfo.email",
            "https://www.googleapis.com/auth/firebase",
            "https://www.googleapis.com/auth/cloud-platform",
            "https://www.googleapis.com/auth/firebase.readonly"
        )
        const val FCM_USER_SDK_KEY_JSON =
            "/google/nearme-ae2c9-firebase-adminsdk-ohajk-4efd1cf727.json"
        const val FCM_BIZ_SDK_KEY_JSON = "/google/nearme-ae2c9-firebase-adminsdk-ohajk-4efd1cf727.json"
        const val FCM_USER_SEND_URL =
            "https://fcm.googleapis.com/v1/projects/nearme-ae2c9/messages:send"
        const val FCM_BIZ_SEND_URL =
            "https://fcm.googleapis.com/v1/projects/nearme-ae2c9/messages:send"
        const val FCM_USER_DB_URL = "https://nearme-ae2c9.firebaseio.com"
        const val FCM_BIZ_DB_URL = "https://nearme-ae2c9.firebaseio.com"
        fun getUserInstance(context: ApplicationContext): Firebase? {
            val firebase = userInstance
            //firebase!!.msgSvc = context.getBean("msgService") as MsgService
            return firebase
        }

        fun getBizInstance(context: ApplicationContext): Firebase? {
            val firebase = bizInstance
            //firebase!!.msgSvc = context.getBean("msgService") as MsgService
            return firebase
        }

        val userInstance: Firebase?
            get() {
                if (firebaseUser == null) {
                    firebaseUser = Firebase()
                    firebaseUser!!.sendUrl = FCM_USER_SEND_URL
                    firebaseUser!!.dbUrl = FCM_USER_DB_URL
                    firebaseUser!!.jsonKey = FCM_USER_SDK_KEY_JSON
                    firebaseUser!!.createApp()
                }
                return firebaseUser
            }

        val bizInstance: Firebase?
            get() {
                logger.info("getBizInstance")
                if (firebaseBiz == null) {
                    logger.info("firebaseBiz null")
                    firebaseBiz = Firebase()
                    firebaseBiz!!.sendUrl = FCM_BIZ_SEND_URL
                    firebaseBiz!!.dbUrl = FCM_BIZ_DB_URL
                    firebaseBiz!!.jsonKey = FCM_BIZ_SDK_KEY_JSON
                    firebaseBiz!!.createApp()
                }
                return firebaseBiz
            }

        @Throws(FirebaseMessagingException::class)
        fun sendToToken(registrationToken: String?) {
            // [START send_to_token]
            // This registration token comes from the client FCM SDKs.

            // See documentation on defining a message payload.
            val message: Message = Message.builder()
                .putData("title", "럭키볼 푸쉬")
                .putData("contents", "Hi")
                .putData("move_type2", "order")
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