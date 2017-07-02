package com.jollypanda.gitforce.data.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Yamushev Igor
 * @since  12.06.17
 */
class MyDateTypeAdapter : JsonDeserializer<Date> {

    companion object {
        private val serverDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date {
        try {
            return serverDateFormat.parse(json.asString)
        } catch (e: ParseException) {
            e.printStackTrace()
            return Date()
        }

    }

}