package ru.liljarn.gandalf.infrastructure.client

import org.springframework.stereotype.Component
import ru.liljarn.gandalf.domain.client.ImageClient
import ru.liljarn.gandalf.support.properties.S3Properties
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ObjectCannedACL
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.InputStream

@Component
class S3ImageClient(
    private val s3Client: S3Client,
    private val properties: S3Properties
) : ImageClient {

    override fun uploadImage(file: InputStream, name: String): String {
        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(properties.bucketName)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .key(name)
                .build(),
            RequestBody.fromBytes(file.readBytes())
        )

        return "https://storage.yandexcloud.net/${properties.bucketName}/$name"
    }

    override fun getImageUrl(name: String): String {
        return "https://storage.yandexcloud.net/${properties.bucketName}/$name"
    }
}