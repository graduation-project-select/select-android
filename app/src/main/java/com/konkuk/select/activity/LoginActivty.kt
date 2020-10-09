package com.konkuk.select.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.konkuk.select.R
import com.konkuk.select.network.Fbase
import com.konkuk.select.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_login_activty.*


class LoginActivty : AppCompatActivity() {
    val loginSharedPrefManager = SharedPrefManager.getInstance((this))

    lateinit var email: String
    lateinit var password: String

    val LOGIN_TAG = "login"
    val SIGNUP_TAG = "signup"
    val GOOGLE = "google"

    val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activty)
        checkLoginStatus()
        settingOnClickListener()
    }

    // 자동로그인 확인
    private fun checkLoginStatus(){
        if(loginSharedPrefManager.uid != "" && loginSharedPrefManager.uid != null){
            Toast.makeText(this, "uid: ${loginSharedPrefManager.uid}", Toast.LENGTH_SHORT).show()
            gotoMainPage()
        }
    }

    private fun clearField(){
        email_et.text.clear()
        password_et.text.clear()
    }

    private fun settingOnClickListener(){
        // 로그인 버튼
        loginBtn.setOnClickListener {
            initLoginField()
            if(email.isEmpty()){
                Log.d(LOGIN_TAG, "email_et is empty")
            }else if(password.isEmpty()){
                Log.d(LOGIN_TAG, "password_et is empty")
            }else {
                login(email, password)
            }
        }

        // 구글 회원가입, 로그인 버튼
        signInGoogleBtn.setOnClickListener {
            socialLogin(GOOGLE)
        }

        // 회원가입 버튼
        signupBtn.setOnClickListener {
            initLoginField()
            if(email.isNotEmpty() && password.isNotEmpty()) {
                signup(email, password)
            }
        }
    }

    private fun initLoginField(){
        email = email_et.text.toString()
        password = password_et.text.toString()
    }

    // 일반 로그인 (이메일, 비밀번호)
    private fun login(email: String, password: String){
        Fbase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.d(LOGIN_TAG, "signInWithEmail: success")
                    Fbase.auth.currentUser?.let { loginSuccess(it) }
                } else {
                    // Sign in fails
                    Log.w(LOGIN_TAG, "signInWithEmail:failure", task.exception)
                }
            }
    }

    // 소셜 로그인 (구글)
    private fun socialLogin(type: String){
        when(type){
            GOOGLE -> {
                // Configure Google Sign In
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(this, gso);
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
            else -> {}
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) { // 소셜 로그인
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(LOGIN_TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(LOGIN_TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Fbase.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.d(LOGIN_TAG, "signInWithCredential:success")
                    Fbase.auth.currentUser?.let { loginSuccess(it) }
                } else {
                    // Sign in fails
                    Log.w(LOGIN_TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    // 일반 회원가입 (이메일, 비밀번호)
    private fun signup(email: String, password: String){
        Fbase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.d(SIGNUP_TAG, "createUserWithEmail:success")
                    Log.d(SIGNUP_TAG, "new user id: ${Fbase.auth.currentUser?.uid}")
                    clearField()
                    Toast.makeText(this, "회원가입 완료, 로그인하세요", Toast.LENGTH_SHORT).show()
                    // TODO: 회원가입 후 추가 정보 DB에 저장
                    // addAditionalInfo(user.uid,"고서영", "man", 1998)
                } else {
                    // Sign in fails
                    Log.w(SIGNUP_TAG, "createUserWithEmail:failure", task.exception)
                }
            }
    }

    // 로그인 성공
    private fun loginSuccess(user: FirebaseUser){
        loginSharedPrefManager.saveUid(user.uid)
        gotoMainPage()
    }

    private fun gotoMainPage(){
        var mainPage = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(mainPage)
        finish()
    }

    // TODO: 회원가입 후 추가 정보 DB에 저장
    private fun addAditionalInfo(uid: String, name: String, gender: String, birthYear: Int){
        // Create a new user with a first and last name
        val user = hashMapOf(
            "name" to name,
            "gender" to gender,
            "born" to birthYear
        )

        // Add a new document with a generated ID
        Fbase.db.collection("users").document(uid).set(user)
            .addOnSuccessListener { documentReference ->
                Log.d(SIGNUP_TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w(SIGNUP_TAG, "Error adding document", e)
            }
    }

}