package ru.liljarn.gandalf.domain.service.image

import org.springframework.stereotype.Service
import ru.liljarn.gandalf.domain.client.ImageClient
import java.io.InputStream

@Service
class ImageService(
    private val imageClient: ImageClient,
) {

    fun uploadImage(file: InputStream, name: String): String = imageClient.uploadImage(file, name)
}
