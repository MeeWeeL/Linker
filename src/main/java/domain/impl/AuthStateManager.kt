package domain.impl

import domain.model.auth.AuthState
import domain.model.auth.NoAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthStateManager {

    private val _authState = MutableStateFlow<AuthState>(NoAuth())
    val authState get() = _authState.asStateFlow()

    val actualState get() = _authState.value

    fun updateAuthState(newState: AuthState) {
        _authState.value = newState
    }
}