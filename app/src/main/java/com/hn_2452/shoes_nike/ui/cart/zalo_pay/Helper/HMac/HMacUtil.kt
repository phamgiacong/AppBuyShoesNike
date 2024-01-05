package com.example.zalopaykotlin.Helper.HMac

import android.os.Build
import androidx.annotation.RequiresApi
import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class HMacUtil {
    companion object{
        val HMACMD5 = "HmacMD5"
        val HMACSHA1 = "HmacSHA1"
        val HMACSHA256 = "HmacSHA256"
        val HMACSHA512 = "HmacSHA512"
        val UTF8CHARSET = StandardCharsets.UTF_8
        val HMACS = listOf("UnSupport", "HmacSHA256", "HmacMD5", "HmacSHA384", "HMacSHA1", "HmacSHA512")

        private fun HMacEncode(algorithm: String, key: String, data: String): ByteArray? {
            return try {
                val macGenerator = Mac.getInstance(algorithm)
                val signingKey = SecretKeySpec(key.toByteArray(UTF8CHARSET), algorithm)
                macGenerator.init(signingKey)
                macGenerator.doFinal(data.toByteArray(UTF8CHARSET))
            } catch (ex: Exception) {
                null
            }
        }

        /**
         * Calculates a base64-encoded HMAC string.
         *
         * @param algorithm The HMAC algorithm.
         * @param key The secret key.
         * @param data The data to authenticate.
         * @return The base64-encoded HMAC string, or null if an error occurred.
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun HMacBase64Encode(algorithm: String, key: String, data: String): String? {
            val hmacEncodeBytes = HMacEncode(algorithm, key, data) ?: return null
            return Base64.getEncoder().encodeToString(hmacEncodeBytes)
        }

        /**
         * Calculates a hexadecimal-encoded HMAC string.
         *
         * @param algorithm The HMAC algorithm.
         * @param key The secret key.
         * @param data The data to authenticate.
         * @return The hexadecimal-encoded HMAC string, or null if an error occurred.
         */
        fun HMacHexStringEncode(algorithm: String, key: String, data: String): String? {
            val hmacEncodeBytes = HMacEncode(algorithm, key, data) ?: return null
            return HexStringUtil.byteArrayToHexString(hmacEncodeBytes)
        }
    }

}