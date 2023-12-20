package com.hn_2452.shoes_nike.utility.sign_in_util

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class FacebookSignInComponent @Inject constructor() {
    interface Callback {
        fun onCancel()
        fun onError(error: FacebookException)
        fun onSuccess(id: String?, name: String?, avatar: String?)
    }

    companion object {
        private const val TAG = "FacebookComponent"
    }

    private var mCallback: Callback? = null


    val mCallbackManager: CallbackManager = CallbackManager.Factory.create()

    init {
        LoginManager.getInstance().registerCallback(
            mCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleLoginResult(result)
                }

                override fun onCancel() {
                    mCallback?.onCancel()
                }

                override fun onError(error: FacebookException) {
                    mCallback?.onError(error)
                }

            })
    }


    private fun handleLoginResult(loginResult: LoginResult) {
        val accessToken = loginResult.accessToken.token
        Log.d(TAG, "Access token: $accessToken")
        val request = GraphRequest.newMeRequest(
            loginResult.accessToken
        ) { `object`: JSONObject, _: GraphResponse? ->
            Log.d(TAG, "object from request with access token: $`object`")
            var id: String? = null
            var name: String? = null
            var avatar: String? = null
            try {
                id = `object`.getString("id")
                name = `object`.getString("name")
                avatar = `object`.getJSONObject("picture").getJSONObject("data")["url"] as String
            } catch (e: JSONException) {
                Log.e(TAG, "Error get information: " + e.message)
            }
            mCallback?.onSuccess(id, name, avatar)
            LoginManager.getInstance().logOut()
        }
        val parameters = Bundle()
        parameters.putString("fields", "id, name, picture.getType(large)")
        request.parameters = parameters
        request.executeAsync()
    }

    fun login(activity: Activity?, callback: Callback) {
        mCallback = callback
        LoginManager.getInstance().logInWithReadPermissions(activity, listOf("public_profile"))
    }
}