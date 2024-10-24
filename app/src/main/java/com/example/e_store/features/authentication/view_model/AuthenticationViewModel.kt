package com.example.e_store.features.authentication.view_model

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.R
import com.example.e_store.utils.constants.CurrencyKeys
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.Customer
import com.example.e_store.utils.shared_models.CustomerRequest
import com.example.e_store.utils.shared_models.UserSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AuthenticationViewModel(
    private val auth: FirebaseAuth,
    private val repository: EStoreRepository,
) : ViewModel() {

    private val _name = mutableStateOf("")
    private val _phone = mutableStateOf("")
    private val _email = mutableStateOf("")
    private val _password = mutableStateOf("")
    private val _confirmPassword = mutableStateOf("")
    val name: State<String> = _name
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
    val db = FirebaseFirestore.getInstance()

    private val _customer = mutableStateOf<Customer?>(null)


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
        _confirmPasswordError.value =
            _confirmPassword.value.isEmpty() || _confirmPassword.value != _password.value
    }
    // Regex for validating a basic phone number format
    val phoneNumberPattern = "^[+]?[0-9]{10,13}\$".toRegex()

    private fun checkSignUpTextValues(context: Context): Boolean {
        if (_nameError.value) {
            Toast.makeText(
                context, context.getString(R.string.name_empty_error), Toast.LENGTH_SHORT
            ).show()
            return false
        }

        if (_phoneError.value) {
            if (_phone.value.isEmpty()) {
                Toast.makeText(
                    context, context.getString(R.string.phone_empty_error), Toast.LENGTH_SHORT
                ).show()
            } else if (_phone.value.length != 11) {
                Toast.makeText(
                    context, context.getString(R.string.phone_invalid_error), Toast.LENGTH_SHORT
                ).show()
            }else if ( !_phone.value.matches(phoneNumberPattern)) {
                Toast.makeText(
                    context, context.getString(R.string.phone_invalid_error), Toast.LENGTH_SHORT
                ).show()
            }
            return false
        }

        if (_emailError.value) {
            if (_email.value.isEmpty()) {
                Toast.makeText(
                    context, context.getString(R.string.email_empty_error), Toast.LENGTH_SHORT
                ).show()
            } else if (!isValidEmail(_email.value)) {
                Toast.makeText(
                    context, context.getString(R.string.email_invalid_error), Toast.LENGTH_SHORT
                ).show()
            }
            return false
        }

        if (_passwordError.value) {
            if (_password.value.isEmpty()) {
                Toast.makeText(
                    context, context.getString(R.string.password_empty_error), Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context, context.getString(R.string.password_invalid_error), Toast.LENGTH_LONG
                ).show()
                Toast.makeText(
                    context,
                    context.getString(R.string.password_requirements_error),
                    Toast.LENGTH_LONG
                ).show()
            }
            return false
        }

        if (_confirmPasswordError.value) {
            if (_confirmPassword.value.isEmpty()) {
                Toast.makeText(
                    context,
                    context.getString(R.string.confirm_password_empty_error),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (_confirmPassword.value != _password.value) {
                Toast.makeText(
                    context, context.getString(R.string.password_mismatch_error), Toast.LENGTH_SHORT
                ).show()
            }
            return false
        }

        return true
    }

    private fun checkSignInTextValues(context: Context): Boolean {
        return when {
            _emailError.value -> {
                if (email.value.isEmpty()) {
                    Toast.makeText(
                        context, context.getString(R.string.email_empty_error), Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context, context.getString(R.string.email_invalid_error), Toast.LENGTH_SHORT
                    ).show()
                }
                false
            }

            _passwordError.value -> {
                if (_password.value.isEmpty()) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.password_empty_error),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.password_invalid_error),
                        Toast.LENGTH_LONG
                    ).show()
                    Toast.makeText(
                        context,
                        context.getString(R.string.password_requirements_error),
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
        return password.length >= 8 &&
                password.contains(Regex("[0-9]")) &&
                password.contains(Regex("[a-z]")) &&
                password.contains(Regex("[A-Z]")) &&
                password.contains(Regex("[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]")) &&
                !password.contains(Regex("\\s"))
    }


    fun signUpUser(context: Context, onAuthSuccess: () -> Unit, onError: (String) -> Unit) {
        Log.d("AuthenticationViewModel", "Signing up user with email: ${_email.value}")
        if (checkSignUpTextValues(context)) {
            auth.createUserWithEmailAndPassword(_email.value, _password.value)
                .addOnCompleteListener { task ->
                    Log.d(
                        "AuthenticationViewModel",
                        "createUserWithEmailAndPassword:onComplete:${task.isSuccessful}"
                    )
                    if (task.isSuccessful) {
                        Log.d("AuthenticationViewModel", "User created successfully")

                        auth.currentUser?.let {
                            saveUserCredentialsInFirestore(
                                context = context,
                                userId = it.uid,
                                name = _name.value,
                                phoneNumber = _phone.value,
                                email = _email.value,
                                onAuthSuccess = onAuthSuccess
                            )
                        }
                        viewModelScope.launch {
                        delay(1000)
                            }
                        sendEmailVerificationAndCreateShopifyCustomer(context)
                    } else {
                        onError(
                            task.exception?.localizedMessage
                                ?: context.getString(R.string.registration_failed_error)
                        )
                    }
                }
        } else {
            isProgressing.value = false
        }
    }

    private fun saveUserCredentialsInFirestore(
        context: Context,
        userId: String,
        name: String,
        phoneNumber: String,
        email: String,
        currencyChoice: String = "EGP",
        onAuthSuccess: () -> Unit
    ) {
        val userMap = hashMapOf(
            "uid" to userId,
            "name" to name,
            "phoneNumber" to phoneNumber,
            "email" to email.lowercase(),
            "currency" to currencyChoice
        )

        db.collection("users").document(userId).set(userMap).addOnSuccessListener {
            Log.d("AuthenticationViewModel", "User credentials and currency saved successfully")
            Toast.makeText(context, "User credentials saved successfully", Toast.LENGTH_SHORT)
                .show()
            onAuthSuccess()
        }.addOnFailureListener { e ->
            Log.e("AuthenticationViewModel", "Error saving user credentials and currency", e)
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }

    }

    private fun sendEmailVerificationAndCreateShopifyCustomer(context: Context) {
        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    context, context.getString(R.string.email_verification_sent), Toast.LENGTH_SHORT
                ).show()

                Log.d("AuthenticationViewModel", "Email verification sent successfully")

              
                createShopifyCustomer(
                    CustomerRequest(
                        Customer(
                            first_name = _name.value,
                            email = _email.value.lowercase(),
                            phone = _phone.value,
                        )
                    )
                )


            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.email_verification_failed),
                    Toast.LENGTH_SHORT
                ).show()
                isProgressing.value = false
            }
        }
    }

    fun signInAndCheckEmailVerification(
        context: Context, onAuthSuccess: () -> Unit, onError: (String) -> Unit
    ) {
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
                                getUserData(context, user.uid, onAuthSuccess)


                            } else {
                                onError(context.getString(R.string.email_not_verified_error))
                            }
                        } else {
                            onError(
                                reloadTask.exception?.localizedMessage
                                    ?: context.getString(R.string.failed_to_reload_user_error)
                            )
                        }
                    }
                } else {
                    onError(
                        task.exception?.localizedMessage
                            ?: context.getString(R.string.login_failed_error)
                    )
                }
            }
    }

    private fun getUserData(context: Context, userId: String, onAuthSuccess: () -> Unit) {
        db.collection("users").document(userId).get().addOnSuccessListener { document ->
            Log.d("AuthenticationViewModel", "User data retrieved successfully")
            if (document != null) {
                UserSession.name = document.getString("name")
                UserSession.phone = document.getString("phoneNumber")
                UserSession.email = document.getString("email")
                UserSession.Uid = userId
                UserSession.isGuest = false
                if (document.getString("currency") != null){
                    UserSession.currency = document.getString("currency").toString()

                }else{
                    UserSession.currency = CurrencyKeys.USD
                }
                onAuthSuccess()
            } else {
                Toast.makeText(context, "No such document", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }


    fun handleGuestModeSignIn(context: Context) {
        isProgressing.value = true
        UserSession.name = context.getString(R.string.guest_user)
        UserSession.email = ""
        UserSession.phone = ""
        UserSession.Uid = ""
        UserSession.isGuest = true
        UserSession.currency = "EGP"
    }


    fun checkEmailVerification(
        context: Context,
        onAuthSuccess: () -> Unit,
        onError: (String) -> Unit,
        onNoUserFound: () -> Unit
    ) {
        val user = auth.currentUser
        if (user != null) {
            if (user.isEmailVerified) {
                getUserData(context, user.uid, onAuthSuccess)
            } else {
                onError(context.getString(R.string.email_not_verified_error))
            }
        } else {
            Log.w("EmailVerification", "No user is signed in.")
            onNoUserFound()
        }
    }


    private fun createShopifyCustomer(customer: CustomerRequest) {
        viewModelScope.launch {
            try {
                Log.d("AuthenticationViewModel", "createShopifyCustomer: $customer")
                repository.createCustomer(customer)
            }catch (e: HttpException) {
                when (e.code()) {
                    429 -> {
                        Log.e("MapViewModel", "Error 429: Too many requests.")
                    }
                    422 -> {
                        Log.e("MapViewModel", "Error 422: Unprocessable entity.")
                    }
                    else -> {
                        Log.e("MapViewModel", "HttpException: ${e.message}")
                    }
                }
            } catch (e: IOException) {
                Log.e("MapViewModel", "IOException: ${e.message}")
            } catch (e: Exception) {
            Log.e("MapViewModel", "Exception: ${e.message}")
            }
        }
    }

}
