package nl.molnet.app

import com.arangodb.ArangoDatabaseAsync
import com.maxmind.db.Reader
import nl.molnet.app.ArangoDbAccess.initArangoDb
import nl.molnet.app.ArangoDbAccess.insertBatch

val dbName = "GeoIpDb"
val collectionName = "geoip"
val geoIpFilename = "/data/GeoLite2-City.mmdb"

fun main(args: Array<String>) {
    println("start app")
    WebApp.start()
    //Main.import()
    println("finished app")
}

object Main {

    fun import() {
        val db = initArangoDb()
        val reader = GeoSupport.getFileReader(geoIpFilename)

        readFileFromDisk(db, reader)

        reader.close()
    }

    fun readFileFromDisk(db: ArangoDatabaseAsync, reader: Reader) {
        var insertedIndex = 0
        var batchIndex = 0
        var count = 0

        val batch: MutableList<Any> = mutableListOf()

        // "0.0.0.0" -> 255.255.255.255

        for (k in 0..255) {
            for (l in 0..255) {
                for (m in 0..255) {
                    for (n in 0..255) {
                        var ip = "$k.$l.$m.$n"

                        val geoIpJson = GeoSupport.getGeoIpAddress(reader, ip)

                        if (!geoIpJson.isNullOrEmpty()) {
                            batch.add(geoIpJson)
                            insertedIndex++
                            batchIndex++
                        }

                        if (batchIndex > 0 && batchIndex % 500 == 0) {
                            insertBatch(db, batch)
                            batch.clear()
                        }

                        if (count % 10000 == 0) {
                            println("Count=$count")
                        }
                        count++
                    }
                }
            }
        }

        // don't forget last chunck
        if (batch.size > 0) {
            ArangoDbAccess.insertBatch(db, batch)
        }

        println("Total=$insertedIndex items inserted")
    }

}
