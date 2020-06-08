package kr.co.korbit.gia.util

import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

class S3 {
    private var amazonS3Client: AmazonS3? = null
    private var bucket: String? = null
    private var region: String? = null
    private var key: String? = null
    private var secretKey: String? = null
    fun shutdown() {
        amazonS3Client.shutdown()
    }

    fun init() {
        try {
            val `is`: InputStream = StoreUtil.getClassLoaderFile("kr/co/pplus/store/config/rootkey.csv")
            val prop = Properties()
            prop.load(`is`)
            key = prop.getProperty("AWSAccessKeyId")
            secretKey = prop.getProperty("AWSSecretKey")
            bucket = prop.getProperty("AWSBucket")
            region = prop.getProperty("AWSRegion")
            `is`.close()
            val credentials = BasicAWSCredentials(key, secretKey)
            val credentialsProvider = AWSStaticCredentialsProvider(credentials)
            amazonS3Client =
                AmazonS3ClientBuilder.standard().withCredentials(credentialsProvider).withRegion(region).build()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun upload(multipartFile: MultipartFile, dirName: String): String {
        val uploadFile = convert(multipartFile)
            .orElseThrow {
                IllegalArgumentException(
                    "MultipartFile -> File로 전환이 실패했습니다."
                )
            }
        return upload(uploadFile, dirName)
    }

    fun upload(uploadFile: File, dirName: String): String {
        val fileName = dirName + "/" + uploadFile.name
        val uploadImageUrl = putS3(uploadFile, fileName)
        removeNewFile(uploadFile)
        return uploadImageUrl
    }

    fun putS3(uploadFile: File?, fileKey: String?): String {
        amazonS3Client.putObject(
            PutObjectRequest(
                bucket,
                fileKey,
                uploadFile
            ).withCannedAcl(CannedAccessControlList.PublicRead)
        )
        return amazonS3Client.getUrl(bucket, fileKey).toString()
    }

    fun removeS3(fileKey: String?) {
        amazonS3Client.deleteObject(DeleteObjectRequest(bucket, fileKey))
    }

    fun removeNewFile(targetFile: File) {
        if (targetFile.delete()) {
            logger.info("파일이 삭제되었습니다.")
        } else {
            logger.error("파일이 삭제되지 못했습니다.")
        }
    }

    @Throws(IOException::class)
    fun convert(file: MultipartFile): Optional<File> {
        val convertFile = File(file.originalFilename)
        if (convertFile.createNewFile()) {
            FileOutputStream(convertFile).use { fos -> fos.write(file.bytes) }
            return Optional.of(convertFile)
        }
        return Optional.empty()
    }

    companion object {
        private var s3Instace: S3? = null
        private val logger: Logger = LoggerFactory.getLogger(BuyController::class.java)
        val instance: S3?
            get() = if (s3Instace == null) {
                s3Instace = S3()
                s3Instace!!.init()
                s3Instace
            } else {
                s3Instace
            }

        @JvmStatic
        fun main(argv: Array<String>) {
            try {
                val path = "/Users/peter/Documents/web/eventBanner/2019/05/30/1559188716172_904969.jpg"
                val file = File(path)
                System.out.println(kr.co.pplus.store.util.S3.getInstance().putS3(file, "web/tmp/test2.jpg"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}