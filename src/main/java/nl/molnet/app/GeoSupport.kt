package nl.molnet.app

import com.maxmind.db.Reader
import java.io.File
import java.net.InetAddress

object GeoSupport {

    fun getFileReader(filename: String): Reader {
        val fileDatabase = File(filename)
        val reader = Reader(fileDatabase)
        return reader
    }

    fun getGeoIpAddress(reader: Reader, ip: String) : String {
        if (ip.isNullOrEmpty()){
            return ""
        }

        val address = InetAddress.getByName(ip)

        val responseJsonNode = reader.get(address)

        if (responseJsonNode == null){
            return ""
        }

        // dirty hack to add the key to the json string
        var json = JsonStringUtils.toJsonString(responseJsonNode)
        var lastPart = json.substring(1, json.length)

        var result = "{\"_key\" : \"$ip\", $lastPart"

        return result
    }

}
