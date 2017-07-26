package nl.molnet.app

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

object JsonStringUtils {

    fun toJsonString(jsonNode: JsonNode) : String {
        try {
            val mapper = ObjectMapper()
            val json = mapper.readValue(jsonNode.toString(), Any::class.java)
            return mapper.writeValueAsString(json)
        } catch (e: Exception) {
            return "{\"no result\"}"
        }
    }

}
