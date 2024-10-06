package com.example.e_store.features.authentication.view_model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.e_store.utils.shared_models.UserSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class AuthenticationViewModel(private val auth: FirebaseAuth) : ViewModel() {

    private val _name = mutableStateOf("")
    private val _phone = mutableStateOf("")
    private val _email = mutableStateOf("")
    private val _password = mutableStateOf("")
    private val _confirmPassword = mutableStateOf("")
    val name = _name
    val phone = _phone
    val email = _email
    val password = _password
    val confirmPassword = _confirmPassword
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
        Log.d("passwordLog", "Password: ${_password.value}")
        Log.d("passwordLog", "Password Error: ${_passwordError.value}")
    }

    fun onConfirmPasswordChanged(newValue: String) {
        _confirmPassword.value = removeWhiteSpaces(newValue)
        _confirmPasswordError.value =
            _confirmPassword.value.isEmpty() || _confirmPassword.value != _password.value
    }



    private fun checkSignUpTextValues(context: Context): Boolean {
        if (_nameError.value) {
            Toast.makeText(context, "Name can't be empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (_phoneError.value) {
            if (_phone.value.isEmpty()) {
                Toast.makeText(context, "Phone can't be empty", Toast.LENGTH_SHORT).show()
            } else if (_phone.value.length != 11) {
                Toast.makeText(context, "Phone is not valid", Toast.LENGTH_SHORT).show()
            }
            return false
        }

        if (_emailError.value) {
            if (_email.value.isEmpty()) {
                Toast.makeText(context, "Email can't be empty", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(_email.value)) {
                Toast.makeText(context, "Email is not valid", Toast.LENGTH_SHORT).show()
            }
            return false
        }

        if (_passwordError.value) {
            if (_password.value.isEmpty()) {
                Toast.makeText(context, "Password can't be empty", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    context,
                    "Password is not valid, must contain at least 8 characters",
                    Toast.LENGTH_LONG
                ).show()
                Toast.makeText(
                    context,
                    "One uppercase, 1 lowercase, 1 number, 1 special character",
                    Toast.LENGTH_LONG
                ).show()
            }
            return false
        }

        if (_confirmPasswordError.value) {
            if (_confirmPassword.value.isEmpty()) {
                Toast.makeText(context, "Confirm Password can't be empty", Toast.LENGTH_SHORT)
                    .show()
            } else if (_confirmPassword.value != _password.value) {
                Toast.makeText(context, "Passwords don't match", Toast.LENGTH_SHORT).show()
            }
            return false
        }

        return true
    }

    private fun checkSignInTextValues(context: Context): Boolean {
        return when {

            _emailError.value -> {
                if (email.value.isEmpty()) {
                    Toast.makeText(context, "Email can't be empty", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Email is not valid", Toast.LENGTH_SHORT).show()
                }
                false

            }

            _passwordError.value -> {
                if (_password.value.isEmpty()) {
                    Toast.makeText(context, "Password can't be empty", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        context,
                        "Password is not valid, must contain at least 8 characters",
                        Toast.LENGTH_LONG
                    ).show()

                    Toast.makeText(
                        context,
                        "One uppercase, 1 lowercase, 1 number, 1 special character",
                        Toast.LENGTH_LONG
                    ).show()
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
        if (password.length < 8) {
            Log.d("passwordLog", "Password is too short, it must be at least 8 characters.")
            return false
        }
        if (!password.contains(Regex("[0-9]"))) {
            Log.d("passwordLog", "Password must contain at least one digit.")
            return false
        }
        if (!password.contains(Regex("[a-z]"))) {
            Log.d("passwordLog", "Password must contain at least one lowercase letter.")
            return false
        }
        if (!password.contains(Regex("[A-Z]"))) {
            Log.d("passwordLog", "Password must contain at least one uppercase letter.")
            return false
        }
        if (!password.contains(Regex("[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]"))) {
            Log.d(
                "passwordLog",
                "Password must contain at least one special character (e.g. !, @, #, etc.)."
            )
            return false
        }
        if (password.contains(Regex("\\s"))) {
            Log.d("passwordLog", "Password must not contain any spaces.")
            return false
        }

        Log.d("passwordLog", "Password is valid.")
        return true
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
                        onError(task.exception?.localizedMessage ?: "Registration failed")
                    }
                }
        } else {
            isProgressing.value = false
        }
    }


    private fun sendEmailVerification(context: Context) {
        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    context,
                    "An email has been sent to your email address, please verify it",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Failed to send email verification",
                    Toast.LENGTH_SHORT
                ).show()
                isProgressing.value = false
                val exception = it.exception
                if (exception is FirebaseAuthException) {
                    val errorCode = exception.errorCode
                    when (errorCode) {
                        "ERROR_USER_DISABLED" -> {
                            // The user account has been disabled by an administrator
                            Toast.makeText(context, "User account is disabled.", Toast.LENGTH_SHORT).show()
                        }
                        "ERROR_INVALID_USER_TOKEN" -> {
                            // The user's credential is no longer valid (user deleted or password changed)
                            Toast.makeText(context, "Invalid user token. Please sign in again.", Toast.LENGTH_SHORT).show()
                        }
                        "ERROR_TOO_MANY_REQUESTS" -> {
                            // The request was blocked due to too many email verification attempts
                            Toast.makeText(context, "Too many requests. Try again later.", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            // Other errors
                            Toast.makeText(context, "Failed to send verification email: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Handle unknown errors
                    Toast.makeText(context, "Failed to send verification email. Try again later.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun signInAndCheckEmailVerification(context: Context, onAuthSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!checkSignInTextValues(context)) {
            isProgressing.value = false
            return
        }

        isProgressing.value = true
        Log.d("signInAndCheckEmailVerification", "Signing in user")

        // Sign in the user first
        auth.signInWithEmailAndPassword(_email.value, _password.value)
            .addOnCompleteListener { task ->
                isProgressing.value = false  // Always reset the loading state
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("signInAndCheckEmailVerification", "Sign-in successful, checking email verification")

                    // Now check if the email is verified
                    user?.reload()?.addOnCompleteListener { reloadTask ->
                        if (reloadTask.isSuccessful) {
                            if (user.isEmailVerified) {
                                Log.d("signInAndCheckEmailVerification", "Email is verified")
                                onAuthSuccess()
                            } else {
                                Log.d("signInAndCheckEmailVerification", "Email not verified")
                                Toast.makeText(context, "Email not verified. Please check your inbox.", Toast.LENGTH_SHORT).show()
                                onError("Email not verified")
                            }
                        } else {
                            Log.e("signInAndCheckEmailVerification", "Failed to reload user: ${reloadTask.exception?.message}")
                            onError(reloadTask.exception?.localizedMessage ?: "Failed to reload user")
                        }
                    }?.addOnFailureListener { exception ->
                        isProgressing.value = false
                        onError(exception.localizedMessage ?: "Failed to reload user")
                    }
                } else {
                    Log.e("signInAndCheckEmailVerification", "Sign-in failed: ${task.exception?.message}")
                    onError(task.exception?.localizedMessage ?: "Login failed")
                }
            }
            .addOnFailureListener { exception ->
                isProgressing.value = false
                onError(exception.localizedMessage ?: "Login failed")
            }
    }


    fun handleGuestModeSignIn(context: Context) {
        isProgressing.value = true
        initializeUserSession(context, "", true)
    }


    fun initializeUserSession(context: Context, email: String, isGuest: Boolean) {

        if (isGuest) {
            UserSession.name = "Guest"
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
        Log.d("userSession", "Name: ${UserSession.name}")
        Log.d("userSession", "Email: ${UserSession.email}")
        Log.d("userSession", "Phone: ${UserSession.phone}")
        Log.d("userSession", "Uid: ${UserSession.Uid}")
        Log.d("userSession", "isGuest: ${UserSession.isGuest}")
    }


}
