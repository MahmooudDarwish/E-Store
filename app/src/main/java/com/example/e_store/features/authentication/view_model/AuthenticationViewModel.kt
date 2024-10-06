package com.example.e_store.features.authentication.view_model

import android.content.Context
import android.content.SharedPreferences
import android.util.Patterns
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.e_store.R
import com.example.e_store.utils.shared_models.UserSession
import com.google.firebase.auth.FirebaseAuth

class AuthenticationViewModel(private val auth: FirebaseAuth) : ViewModel() {

    private val _name = mutableStateOf("")
    private val _phone = mutableStateOf("")
    private val _email = mutableStateOf("")
    private val _password = mutableStateOf("")
    private val _confirmPassword = mutableStateOf("")
    val name :State<String> = _name
    val phone: State<String> = _phone
    val email: State<String> = _email
    val password: State<String> = _password
    val confirmPassword: State<String> = _confirmPassword
    val isProgressing = mutableStateOf(false)

    private val _nameError = mutableStateOf(true)
    private val _phoneError = mutableStateOf(true)
    private val _emailError = mutableStateOf(true)
    private val _passwordError = mutableStateOf(true)
    private val _confirmPasswordError = mutableStateOf(true)

    fun onNameChanged(newValue: String) {
        _name.value = newValue
        _nameError.value = newValue.isEmpty()
    }

    fun onPhoneChanged(newValue: String) {
        _phone.value = newValue
        _phoneError.value = newValue.isEmpty() || removeWhiteSpaces(newValue).length != 11
    }

    fun onEmailChanged(newValue: String) {
        _email.value = newValue
        _emailError.value = newValue.isEmpty() || !isValidEmail(newValue.replace(" ", ""))
    }

    fun onPasswordChanged(newValue: String) {
        _password.value = removeWhiteSpaces(newValue)
        _passwordError.value = _password.value.isEmpty() || !isValidPassword(_password.value)
    }

    fun onConfirmPasswordChanged(newValue: String) {
        _confirmPassword.value = removeWhiteSpaces(newValue)
        _confirmPasswordError.value = _confirmPassword.value.isEmpty() || _confirmPassword.value != _password.value
    }

    private fun checkSignUpTextValues(context: Context): Boolean {
        if (_nameError.value) {
            Toast.makeText(context, context.getString(R.string.name_empty_error), Toast.LENGTH_SHORT).show()
            return false
        }

        if (_phoneError.value) {
            if (_phone.value.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.phone_empty_error), Toast.LENGTH_SHORT).show()
            } else if (_phone.value.length != 11) {
                Toast.makeText(context, context.getString(R.string.phone_invalid_error), Toast.LENGTH_SHORT).show()
            }
            return false
        }

        if (_emailError.value) {
            if (_email.value.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.email_empty_error), Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(_email.value)) {
                Toast.makeText(context, context.getString(R.string.email_invalid_error), Toast.LENGTH_SHORT).show()
            }
            return false
        }

        if (_passwordError.value) {
            if (_password.value.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.password_empty_error), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, context.getString(R.string.password_invalid_error), Toast.LENGTH_LONG).show()
                Toast.makeText(context, context.getString(R.string.password_requirements_error), Toast.LENGTH_LONG).show()
            }
            return false
        }

        if (_confirmPasswordError.value) {
            if (_confirmPassword.value.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.confirm_password_empty_error), Toast.LENGTH_SHORT).show()
            } else if (_confirmPassword.value != _password.value) {
                Toast.makeText(context, context.getString(R.string.password_mismatch_error), Toast.LENGTH_SHORT).show()
            }
            return false
        }

        return true
    }

    private fun checkSignInTextValues(context: Context): Boolean {
        return when {
            _emailError.value -> {
                if (email.value.isEmpty()) {
                    Toast.makeText(context, context.getString(R.string.email_empty_error), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, context.getString(R.string.email_invalid_error), Toast.LENGTH_SHORT).show()
                }
                false
            }
            _passwordError.value -> {
                if (_password.value.isEmpty()) {
                    Toast.makeText(context, context.getString(R.string.password_empty_error), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, context.getString(R.string.password_invalid_error), Toast.LENGTH_LONG).show()
                    Toast.makeText(context, context.getString(R.string.password_requirements_error), Toast.LENGTH_LONG).show()
                }
                false
            }
            else -> true
        }
    }

    private fun removeWhiteSpaces(input: String): String {
        return input.replace(" ", "")
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8 &&
                password.contains(Regex("[0-9]")) &&
                password.contains(Regex("[a-z]")) &&
                password.contains(Regex("[A-Z]")) &&
                password.contains(Regex("[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]")) &&
                !password.contains(Regex("\\s"))
    }

    fun saveNameToSharedPref(context: Context, email: String, name: String, phone: String) {
        val sanitizedEmail = email.replace("[^a-zA-Z0-9_.-]".toRegex(), "_")

        val sharedPref: SharedPreferences =
            context.getSharedPreferences(sanitizedEmail, Context.MODE_PRIVATE)
        val sharedPrefEditor = sharedPref.edit()

        sharedPrefEditor.putString("name", name)
        sharedPrefEditor.putString("phone", phone)
        sharedPrefEditor.apply()
    }

    fun signUpUser(context: Context, onAuthSuccess: () -> Unit, onError: (String) -> Unit) {
        isProgressing.value = true
        if (checkSignUpTextValues(context)) {
            auth.createUserWithEmailAndPassword(_email.value, _password.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onAuthSuccess()
                        sendEmailVerification(context)
                    } else {
                        onError(task.exception?.localizedMessage ?: context.getString(R.string.registration_failed_error))
                    }
                }
        } else {
            isProgressing.value = false
        }
    }

    private fun sendEmailVerification(context: Context) {
        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, context.getString(R.string.email_verification_sent), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, context.getString(R.string.email_verification_failed), Toast.LENGTH_SHORT).show()
                isProgressing.value = false
            }
        }
    }

    fun signInAndCheckEmailVerification(context: Context, onAuthSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!checkSignInTextValues(context)) {
            isProgressing.value = false
            return
        }

        isProgressing.value = true

        auth.signInWithEmailAndPassword(_email.value, _password.value)
            .addOnCompleteListener { task ->
                isProgressing.value = false
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.reload()?.addOnCompleteListener { reloadTask ->
                        if (reloadTask.isSuccessful) {
                            if (user.isEmailVerified) {
                                onAuthSuccess()
                            } else {
                                Toast.makeText(context, context.getString(R.string.email_not_verified_error), Toast.LENGTH_SHORT).show()
                                onError(context.getString(R.string.email_not_verified_error))
                            }
                        } else {
                            onError(reloadTask.exception?.localizedMessage ?: context.getString(R.string.failed_to_reload_user_error))
                        }
                    }
                } else {
                    onError(task.exception?.localizedMessage ?: context.getString(R.string.login_failed_error))
                }
            }
    }

    fun handleGuestModeSignIn(context: Context) {
        isProgressing.value = true
        initializeUserSession(context, "", true)
    }

    fun initializeUserSession(context: Context, email: String, isGuest: Boolean) {
        if (isGuest) {
            UserSession.name = context.getString(R.string.guest_user)
            UserSession.email = email
            UserSession.phone = ""
            UserSession.Uid = ""
            UserSession.isGuest = true
        } else {
            val sanitizedEmail = email.replace("[^a-zA-Z0-9_.-]".toRegex(), "_")

            val sharedPref: SharedPreferences =
                context.getSharedPreferences(sanitizedEmail, Context.MODE_PRIVATE)

            UserSession.name = sharedPref.getString("name", "") ?: ""
            UserSession.email = email
            UserSession.phone = sharedPref.getString("phone", "") ?: ""
            UserSession.Uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            UserSession.isGuest = false
        }
    }
}
