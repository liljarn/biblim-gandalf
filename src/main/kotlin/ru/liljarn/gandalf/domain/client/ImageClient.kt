package ru.liljarn.gandalf.domain.client

import java.io.InputStream

interface ImageClient {
    fun uploadImage(file: InputStream, name: String): String

    fun getImageUrl(name: String): String
}
