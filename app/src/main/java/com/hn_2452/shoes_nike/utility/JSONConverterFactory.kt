package com.hn_2452.shoes_nike.utility

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


class JSONConverterFactory private constructor() : Converter.Factory() {

    companion object {
        fun create() = JSONConverterFactory()
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<String, RequestBody>? {
        if (type == JSONObject::class.java) {
            return JSONRequestBodyConverter()
        }
        return null
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, JSONObject>? {
        if (type == JSONObject::class.java) {
            return JSONResponseBodyConverter()
        }
        return null
    }

}

class JSONRequestBodyConverter<T> : Converter<T, RequestBody> {
    override fun convert(value: T): RequestBody {
        return RequestBody.create("text/plain; charset=UTF-8".toMediaTypeOrNull(), value.toString())
    }
}

class JSONResponseBodyConverter : Converter<ResponseBody, JSONObject> {
    override fun convert(value: ResponseBody): JSONObject? {
        return try {
            JSONObject(value.string())
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}