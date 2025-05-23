package com.basic.logcat

import com.drake.net.NetConfig
import com.drake.net.convert.NetConverter
import com.drake.net.exception.ConvertException
import com.drake.net.exception.RequestParamsException
import com.drake.net.exception.ResponseException
import com.drake.net.exception.ServerResponseException
import com.squareup.moshi.Moshi
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MoshiConverter(
    private val success: String = "0",
    private val code: String = "errorCode",
    private val message: String = "errorMsg"
) : NetConverter {
    companion object {
        private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }


    override fun <R> onConvert(succeed: Type, response: Response): R? {
        try {
            return NetConverter.onConvert<R>(succeed, response)
        } catch (e: ConvertException) {
            val code = response.code
            when {
                code in 200..299 -> { // 请求成功
                    val bodyString = response.body?.string() ?: return null
                    return try {
                        val json = JSONObject(bodyString) // 获取JSON中后端定义的错误码和错误信息
                        val srvCode = json.getString(this.code)
                        if (srvCode == success) { // 对比后端自定义错误码
                            bodyString.parseBody<R>(succeed)
                        } else { // 错误码匹配失败, 开始写入错误异常
                            val errorMessage = json.optString(
                                message,
                                NetConfig.app.getString(com.drake.net.R.string.no_error_message)
                            )
                            throw ResponseException(
                                response,
                                errorMessage,
                                tag = srvCode
                            ) // 将业务错误码作为tag传递
                        }
                    } catch (e: JSONException) { // 固定格式JSON分析失败直接解析JSON
                        bodyString.parseBody<R>(succeed)
                    }
                }

                code in 400..499 -> throw RequestParamsException(//请求参数错误
                    response,
                    code.toString()
                )
                code >= 500 -> throw ServerResponseException(response, code.toString()) // 服务器异常错误
                else -> throw ConvertException(
                    response,
                    message = "Http status code not within range"
                )
            }
        }
    }

    private fun <R> String.parseBody(succeed: Type): R? {
        val string = try {
            JSONObject(this).getString("data")
        } catch (e: Exception) {
            this
        }
        return moshi.adapter<R>(succeed).fromJson(string)
    }
}