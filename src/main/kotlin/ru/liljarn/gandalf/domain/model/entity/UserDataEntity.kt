package ru.liljarn.gandalf.domain.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.util.*

/**
 * Сущность для хранения данных пользователя в базе данных.
 *
 * Соответствует таблице `user_data` и содержит:
 * - Основные персональные данные
 * - Учетные данные для аутентификации
 * - Ссылка на фото профиля
 *
 * @property uuid Уникальный идентификатор пользователя (первичный ключ)
 * @property email Email пользователя (уникальное поле)
 * @property password Захешированный пароль с использованием [salt]
 * @property salt Криптографическая соль для хеширования пароля
 * @property firstName Имя пользователя
 * @property lastName Фамилия пользователя
 * @property birthDate Дата рождения
 * @property photoUrl Ссылка на аватар пользователя (опционально)
 *
 * @see UserDataComponent Основной компонент для работы с сущностью
 * @see UserPrivateData DTO для передачи приватных данных
 * @see UserData DTO для передачи публичных данных
 */
@Table("user_data")
data class UserDataEntity(
    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    val uuid: UUID,

    /**
     * Email пользователя (уникальный идентификатор).
     */
    val email: String,

    /**
     * Захешированный пароль пользователя.
     */
    val password: String,

    /**
     * Криптографическая соль для хеширования пароля.
     *
     * Генерируется автоматически при:
     * - Создании пользователя
     * - Изменении пароля
     */
    val salt: String,

    /**
     * Имя пользователя.
     */
    val firstName: String,

    /**
     * Фамилия пользователя.
     */
    val lastName: String,

    /**
     * Дата рождения пользователя.
     */
    val birthDate: LocalDate,

    /**
     * Ссылка на аватар пользователя.
     *
     * Может быть `null` если аватар не установлен.
     */
    val photoUrl: String?
)
