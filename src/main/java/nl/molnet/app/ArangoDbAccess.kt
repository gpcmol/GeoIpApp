package nl.molnet.app

import com.arangodb.ArangoDBAsync
import com.arangodb.ArangoDBException
import com.arangodb.ArangoDatabaseAsync
import com.arangodb.model.DocumentCreateOptions
import java.util.concurrent.CompletableFuture

object ArangoDbAccess {

    val user = ""
    val password = ""

    fun insertBatch(db: ArangoDatabaseAsync, batch: MutableList<Any>) {
        val params = DocumentCreateOptions().waitForSync(true)
        val collection = db.collection(collectionName).insertDocuments(batch, params)
    }

    fun getDocumentByKey(key: String) : CompletableFuture<String> {
        val db = initArangoDb()
        val collection = db.collection(collectionName)
        var result = collection.getDocument(key, String::class.java)
        return result
    }

    fun initArangoDb(): ArangoDatabaseAsync {
        println("Init DB...")

        val arangoDb = ArangoDBAsync.Builder()
                    .host("127.0.0.1", 8529)
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