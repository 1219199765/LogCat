package com.basic.logcat.utils

import android.util.Base64
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.math.BigInteger
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * 生成 32 位小写 MD5
 *  @param data string
 */
fun encryptMD32Lower(data: String) = encryptMD32Lower(data = data.toByteArray())

/**
 * 生成 32 位小写 MD5
 *  @param data ByteArray
 */
fun encryptMD32Lower(data: ByteArray) = encryptTemplate(data, "MD5").lowercase()

/**
 * 生成 32 位大写 MD5
 *  @param data string
 */
fun encryptMD32Upper(data: String) = encryptMD32Upper(data = data.toByteArray())

/**
 * 生成 32 位大写 MD5
 *  @param data ByteArray
 */
fun encryptMD32Upper(data: ByteArray) = encryptTemplate(data, "MD5").uppercase()

/**
 * 生成 16 位小写 MD5
 * @param data string
 */
fun encryptMD16Lower(data: String) = encryptMD16Lower(data = data.toByteArray())

/**
 * 生成 16 位小写 MD5
 * @param data ByteArray
 */
fun encryptMD16Lower(data: ByteArray) = encryptMD32Lower(data).substring(8, 24)

/**
 * 生成 16 位大写 MD5
 * @param data string
 */
fun encryptMD16Upper(data: String) = encryptMD16Upper(data = data.toByteArray())

/**
 * 生成 16 位大写 MD5
 * @param data ByteArray
 */
fun encryptMD16Upper(data: ByteArray) = encryptMD32Upper(data).substring(8, 24)


fun encryptMD5File(filePath: String) = encryptMD5File(File(filePath))

fun encryptMD5File(file: File): String? {
    val digest = MessageDigest.getInstance("MD5")
    try {
        file.inputStream().use { fis ->
            val buffer = ByteArray(8192)
            var read: Int
            while (fis.read(buffer).also { read = it } != -1) {
                digest.update(buffer, 0, read)
            }
        }
        return BigInteger(1, digest.digest()).toString(16).padStart(32, '0')

    } catch (e: Exception) {
        return "文件MD5加密出现异常-->${e.message}"
    }
}


/**
 * 生成 sha256
 * @param data string
 */
fun encryptSHA256(data: String) = encryptSHA256(data.toByteArray())

/**
 * 生成 sha256
 * @param data ByteArray
 */
fun encryptSHA256(data: ByteArray) = encryptTemplate(data, "SHA256")


/**
 * Base64编码
 * @param data string
 */
fun encryptBase64(data: String): String = encryptBase64(data.toByteArray())

/**
 * Base64编码
 *  @param data ByteArray
 */
fun encryptBase64(data: ByteArray): String = Base64.encodeToString(data, Base64.DEFAULT)

/**
 * Base64解码
 *
 * @param data 要解码的字符串
 * @return Base64解码后的字符串
 */
fun encryptBase64Decode(data: String) = encryptBase64Decode(data.toByteArray())

/**
 * Base64解码
 *
 * @param data 要解码的字符串
 * @return Base64解码后的字符串
 */
fun encryptBase64Decode(data: ByteArray) =
    String(Base64.decode(data, Base64.DEFAULT), Charsets.UTF_8)


// 核心加密方法
private fun encryptTemplate(data: ByteArray, type: String): String {
    return try {
        val md = MessageDigest.getInstance(type)
        val digest = md.digest(data)
        BigInteger(1, digest).toString(16).padStart(32, '0')
    } catch (e: Exception) {
        throw RuntimeException("MD5 encryption failed", e)
    }
}

