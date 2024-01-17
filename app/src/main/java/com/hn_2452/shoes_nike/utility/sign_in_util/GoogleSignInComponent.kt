package com.hn_2452.shoes_nike.utility.sign_in_util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GoogleSignInComponent @Inject constructor(
    @ApplicationContext private val mContext: Context
) {

    companion object {
        const val REQUEST_CODE = 1
    }

    interface Callback {
        fun onError(error: Exception)
        fun onSuccess(id: String?, displayName: String?, avatar: String?, email: String?)
    }

    private val mGoogleSignInClient = GoogleSignIn.getClient(
        mContext,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("927557416953-53k4fakon2g977gli465h82vqljh0ctv.apps.googleusercontent.com")
            .requestEmail()
            .build()
    )

    var mCallbackManager: CallbackManager? = null
        private set

    fun login(activity: Activity, callback: Callback) {
        mCallbackManager = CallbackManager(callback)
        activity.startActivityForResult(
            mGoogleSignInClient.signInIntent,
            REQUEST_CODE
        )
    }


}

class CallbackManager(
    private val mCallback: GoogleSignInComponent.Callback
) {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GoogleSignInComponent.REQUEST_CODE) {
            if (data == null) {
                mCallback.onError(Exception("GG Data is null"))
            } else {
                handleSignInResult(data)
            }
        }
    }

    private fun handleSignInResult(intent: Intent) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            val account = task.getResult(ApiException::class.java)
            mCallback.onSuccess(
                account.id,
                account.displayName,
                account.photoUrl.toString(),
                account.email
            )
        } catch (e: Exception) {
            mCallback.onError(e)
        }
    }
}