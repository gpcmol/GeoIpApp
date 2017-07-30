package nl.molnet.app

import com.arangodb.ArangoDBAsync
import com.arangodb.ArangoDBException
import com.arangodb.ArangoDatabaseAsync
import com.arangodb.model.DocumentCreateOptions
import java.util.concurrent.CompletableFuture

object ArangoDbAccess {

    val host = AppConfig.ARANGO_HOST
    val port = AppConfig.ARANGO_PORT.toInt()
    val user = AppConfig.ARANGO_USER
    val password = AppConfig.ARANGO_PASSWORD

    val database by lazy {
        initArangoDb()
    }

    fun insertBatch(batch: MutableList<Any>) {
        val params = DocumentCreateOptions().waitForSync(true)
        val collection = database.collection(collectionName).insertDocuments(batch, params)
    }

    fun getDocumentByKey(key: String) : CompletableFuture<String> {
        val collection = database.collection(collectionName)
        var result = collection.getDocument(key, String::class.java)
        return result
    }

    fun initArangoDb(): ArangoDatabaseAsync {
        println("Init DB...")

        var arangoDb = ArangoDBAsync.Builder()
                    .host(host, port)
                    .user(user)
                    .password(password)
                .build()

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

}