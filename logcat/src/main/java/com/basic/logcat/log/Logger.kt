package com.basic.logcat.log

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


object LogCat {
    private const val CHUNK_SIZE: Int = 4000//超过数值就换行
    private const val JSON_INDENT = 2//json字符串首行缩进2个字符
    private const val MIN_STACK_OFFSET = 5
    private const val TOP_LEFT_CORNER = '┌'
    private const val BOTTOM_LEFT_CORNER = '└'
    private const val MIDDLE_CORNER = '├'
    private const val HORIZONTAL_LINE = '│'
    private const val DOUBLE_DIVIDER =
        "────────────────────────────────────────────────────────"
    private const val SINGLE_DIVIDER =
        "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
    private const val TOP_BORDER = "$TOP_LEFT_CORNER$DOUBLE_DIVIDER$DOUBLE_DIVIDER"
    private const val BOTTOM_BORDER = "$BOTTOM_LEFT_CORNER$DOUBLE_DIVIDER$DOUBLE_DIVIDER"
    private const val MIDDLE_BORDER = "$MIDDLE_CORNER$SINGLE_DIVIDER$SINGLE_DIVIDER"



    /** 日志默认标签 */
    private var tag = "日志"

    /** 是否启用日志 */
    private var enabled = true

    /** 日志是否显示代码位置 */
    private var traceEnabled = false


    fun setTag(tab: String) {
        tag = tab
    }

    fun setEnable(enable: Boolean) {
        enabled = enable
    }

    fun setTraceEnable(traceEnable: Boolean) {
        traceEnabled = traceEnable
    }

    fun d(message: String?, vararg args: Any?) {
        log(Log.DEBUG, message ?: "")
    }

    fun e(message: String?, vararg args: Any?) {
        log(Log.ERROR, message ?: "")
    }

    fun json(json:String?,vararg  array: Any?){
        if (json == "") {
            e("Empty/Null json content")
            return
        }
        var localJson = json
        try {
            localJson = localJson!!.trim()
            if (localJson.startsWith("{")) {
                val jsonObject = JSONObject(localJson)
                val message = jsonObject.toString(JSON_INDENT)
                d(message)
                return
            }
            if (localJson.startsWith("[")) {
                val jsonArray = JSONArray(localJson)
                val message = jsonArray.toString(JSON_INDENT)
                d(message)
                return
            }
            e("Invalid Json")
        } catch (e: JSONException) {
            e("Invalid Json")
        }
    }


    @Synchronized
    private fun log(priority: Int, message: String) {
        if (!enabled) return

        logTopBorder(priority)
        logTrace(priority)

        val bytes: ByteArray = message.toByteArray()
        val length = bytes.size
        if (length <= CHUNK_SIZE) {
            logContent(priority, message)
            logBottomBorder(priority)
            return
        }
        var i = 0
        while (i < length) {
            val count = (length - i).coerceAtMost(CHUNK_SIZE)
            logContent(priority, String(bytes, i, count))
            i += CHUNK_SIZE
        }
        logBottomBorder(priority)
    }

    private fun logBottomBorder(logType: Int) {
        logChunk(logType, BOTTOM_BORDER)
    }

    private fun logTopBorder(logType: Int) {
        logChunk(logType, TOP_BORDER)
    }


    private fun logContent(logType: Int, chunk: String) {
        val lines = System.lineSeparator().let { it ->
            chunk.split(it.toRegex())
                .dropLastWhile { it.isEmpty() }.toTypedArray()
        }
        for (line in lines) {
            logChunk(logType, "$HORIZONTAL_LINE $line")
        }
    }

    private fun logTrace(logType: Int, occurred: Throwable = Exception()) {
        if (!traceEnabled) return
        occurred.stackTrace.getOrNull(3)?.run {
            logChunk(logType, "$HORIZONTAL_LINE ===>($fileName:$lineNumber)")
        }

    }

    private fun logChunk(priority: Int, message: String) {
        Log.println(priority, tag, message)
    }

}