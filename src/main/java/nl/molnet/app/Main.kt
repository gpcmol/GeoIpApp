package nl.molnet.app

import com.arangodb.ArangoDBAsync
import com.arangodb.ArangoDBException
import com.arangodb.ArangoDatabaseAsync
import com.arangodb.model.DocumentCreateOptions
import com.fasterxml.jackson.databind.ObjectMapper
import com.maxmind.db.Reader
import java.io.File
import java.net.InetAddress
import com.fasterxml.jackson.databind.JsonNode

val collectionName = "geoip"
val dbName = "GeoIpDb"

fun main(args: Array<String>) {
    println("start app")
    val db = initArangoDb()
    readFileFromDisk(db)
}

fun readFileFromDisk(db: ArangoDatabaseAsync) {
    val database = File("~/Downloads/GeoLite2-City_20170704/GeoLite2-City.mmdb")
    val reader = Reader(database)

    var i = 0
    var batchIndex = 0

    val batch: MutableList<Any> = mutableListOf()

    // "0.0.0.0" -> 255.255.255.255

    for (k in 0..255) {
        for (l in 0..255) {
            for (m in 0..255) {
                for (n in 0..255) {
                    var ip = "$k.$l.$m.$n"

                    val address = InetAddress.getByName(ip)
                    val response = reader.get(address)

                    if (response != null) {
                        val str = printJsonString(response)
                        batch.add(str)
                        i++
                        batchIndex++
                    }

                    if (batchIndex > 0 && batchIndex % 500 == 0) {
                        insertBatch(db, batch)
                        batch.clear()
                    }

                    //println("$ip=" + response)
                }
            }
        }
    }

    // don't forget last chunck
    if (batch.size > 0){
        insertBatch(db, batch)
    }

    println("Total=$i items inserted")

    reader.close()
}

fun printJsonString(jsonNode: JsonNode): String {
    try {
        val mapper = ObjectMapper()
        val json = mapper.readValue(jsonNode.toString(), Any::class.java)
        return mapper.writeValueAsString(json)
    } catch (e: Exception) {
        return "error json print"
    }
}

fun insertBatch(db: ArangoDatabaseAsync, batch: MutableList<Any>) {
    val params = DocumentCreateOptions().waitForSync(true)
    val collection = db.collection(collectionName).insertDocuments(batch, params)
}

fun initArangoDb(): ArangoDatabaseAsync {
    println("Init DB...")

    val arangoDb = ArangoDBAsync.Builder().host("127.0.0.1", 8529).build()

    try {
        try {
            arangoDb.createDatabase(dbName)
        } catch(e: Exception) {
            println("db already exists...")
        }

        val db = arangoDb.db(dbName)

        try {
            db.createCollection(collectionName, null)
        } catch(e: Exception) {
            println("collection already exists...")
        }

        return db
    } catch (e: ArangoDBException) {
        // todo do some logging
        //logger.error("Error initializing ArangoDb", e)
    }

    throw RuntimeException("Error creating db " + dbName)
}
