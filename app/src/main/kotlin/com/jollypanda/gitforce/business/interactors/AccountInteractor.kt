package com.jollypanda.gitforce.business.interactors

import com.jollypanda.gitforce.GitforceApp
import com.jollypanda.gitforce.data.network.models.AccessTokenRequest
import com.jollypanda.gitforce.data.network.models.UserResponse
import com.jollypanda.gitforce.data.repositories.AccountRepository
import com.jollypanda.gitforce.utils.Optional
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author Yamushev Igor
 * @since  14.06.17
 */
class AccountInteractor : IAccountInteractor {

    @Inject
    lateinit var accountRepository: AccountRepository

    init {
        GitforceApp.appComponent.injectTo(this)
    }

    override fun loadToken(accessTokenRequest: AccessTokenRequest) =
        accountRepository.loadAccessToken(accessTokenRequest)

    override fun getSavedToken() = accountRepository.getSavedToken()

    override fun loadUser() = accountRepository.loadUser()

    override fun getSavedUser() = accountRepository.getSavedUser()

    override fun saveCode(code: String) {
        accountRepository.saveCode(code)
    }

    override fun getSavedCode() =
            accountRepository.getSavedCode()

    override fun clearAccountData() {
       accountRepository.clearAccountData()
    }
}

interface IAccountInteractor {
    fun loadToken(accessTokenRequest: AccessTokenRequest): Single<Optional<String?>>
    fun loadUser(): Single<UserResponse>
    fun getSavedToken(): String?
    fun getSavedUser(): UserResponse?
    fun saveCode(code: String)
    fun getSavedCode(): String?
    fun clearAccountData()
}