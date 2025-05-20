package ru.liljarn.gandalf.domain.service.user.component

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.liljarn.gandalf.domain.model.dto.ChangedProfileData
import ru.liljarn.gandalf.domain.model.dto.UserData
import ru.liljarn.gandalf.domain.model.dto.UserPrivateData
import ru.liljarn.gandalf.domain.model.entity.UserDataEntity
import ru.liljarn.gandalf.domain.model.exception.UserNotFoundException
import ru.liljarn.gandalf.domain.repository.UserDataRepository
import ru.liljarn.gandalf.support.mapper.toUserData
import ru.liljarn.gandalf.support.mapper.toUserPrivateData
import java.util.*

/**
 * Компонент для работы с персистентными данными пользователей.
 *
 * Обеспечивает:
 * - CRUD операции с данными пользователей
 * - Конвертацию между DTO и Entity моделями
 * - Транзакционное управление операциями
 *
 * @property userDataRepository Репозиторий для доступа к хранилищу пользователей
 *
 * @see UserPrivateData Модель приватных данных пользователя
 * @see UserDataEntity JPA-сущность пользователя
 * @see ChangedProfileData DTO для обновления профиля
 */
@Component
class UserDataComponent(
    private val userDataRepository: UserDataRepository
) {

    /**
     * Создает нового пользователя в системе.
     *
     * @param userData Приватные данные пользователя [UserPrivateData]
     * @throws DataIntegrityViolationException при нарушении уникальности email/UUID
     *
     * Процесс сохранения:
     * 1. Конвертация DTO в JPA Entity
     * 2. Валидация данных
     * 3. Сохранение в репозиторий
     */
    @Transactional
    fun createUserData(userData: UserPrivateData) =
        userDataRepository.addUser(
            UserDataEntity(
                uuid = userData.uuid,
                email = userData.email,
                password = userData.password,
                salt = userData.salt,
                firstName = userData.firstName,
                lastName = userData.lastName,
                birthDate = userData.birthDate,
                photoUrl = userData.photoUrl
            )
        )

    /**
     * Проверяет существование пользователя по email.
     *
     * @param email Email для проверки
     * @return `true` если пользователь существует, иначе `false`
     */
    @Transactional(readOnly = true)
    fun findUserDataByEmail(email: String): Boolean =
        userDataRepository.existsByEmail(email)

    /**
     * Получает основные данные пользователя.
     *
     * @param userUuid UUID пользователя
     * @return [UserData] с публичными данными профиля
     * @throws UserNotFoundException если пользователь не найден
     */
    @Transactional
    fun getUserData(userUuid: UUID): UserData {
        val userDataEntity =
            userDataRepository.findByUuid(userUuid)
                ?: throw UserNotFoundException("User with userId=$userUuid not found")
        return userDataEntity.toUserData()
    }

    /**
     * Получает приватные данные пользователя для аутентификации.
     *
     * @param email Email пользователя
     * @return [UserPrivateData] с хешем пароля и солью
     * @throws UserNotFoundException если пользователь не найден
     */
    @Transactional(readOnly = true)
    fun getUserPrivateData(email: String): UserPrivateData =
        userDataRepository.findByEmail(email)?.toUserPrivateData()
            ?: throw UserNotFoundException("User with email=$email not found")

    /**
     * Обновляет данные профиля пользователя.
     *
     * @param userUuid UUID пользователя
     * @param data Измененные данные [ChangedProfileData]
     * @throws UserNotFoundException если пользователь не найден
     *
     * Особенности:
     * - Обновляются только не-null поля из `data`
     * - Пароль и соль обновляются только вместе
     * - Поддерживается частичное обновление данных
     */
    @Transactional
    fun editProfile(userUuid: UUID, data: ChangedProfileData) {
        val user = userDataRepository.findByUuid(userUuid)
            ?: throw UserNotFoundException("User with userId=$userUuid not found")

        userDataRepository.save(
            UserDataEntity(
                uuid = user.uuid,
                email = data.email ?: user.email,
                salt = data.salt ?: user.salt,
                password = data.password ?: user.password,
                firstName = data.firstName ?: user.firstName,
                lastName = data.lastName ?: user.lastName,
                birthDate = data.birthDate ?: user.birthDate,
                photoUrl = data.photoUrl ?: user.photoUrl
            )
        )
    }

    /**
     * Получает список email всех пользователей.
     *
     * @return Список email в формате String
     *
     * Использование:
     * - Для массовых уведомлений
     * - Административных задач
     */
    @Transactional(readOnly = true)
    fun getUserEmails() = userDataRepository.getUserEmails()
}
