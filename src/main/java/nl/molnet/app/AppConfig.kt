package nl.molnet.app

// TODO do not use nullables here, do it the proper Kotlin way!
object AppConfig {

    val APP_PORT by lazy {
        getApplicationPort()
    }

    val ARANGO_HOST by lazy {
        getArangoDbHost()
    }

    val ARANGO_PORT by lazy {
        getArangoDbPort()
    }

    val ARANGO_USER by lazy {
        getArangoDbUser()
    }

    val ARANGO_PASSWORD by lazy {
        getArangoDbPassword()
    }

    private fun getApplicationPort(): String {
        var port: String? = getConfigFromEnvironment("APP_PORT")
        if (port == null) {
            port = "9090"
        }
        return port
    }

    private fun getArangoDbHost(): String {
        var host: String? = getConfigFromEnvironment("ARANGO_HOST")
        if (host == null) {
            host = "localhost"
        }
        return host
    }

    private fun getArangoDbPort(): String {
        var port: String? = getConfigFromEnvironment("ARANGO_PORT")
        if (port == null) {
            port = "8529"
        }
        return port
    }

    private fun getArangoDbUser(): String {
        var user: String? = getConfigFromEnvironment("ARANGO_USER")
        if (user == null) {
            user = "user"
        }
        return user
    }

    private fun getArangoDbPassword(): String {
        var password: String? = getConfigFromEnvironment("ARANGO_PASSWORD")
        if (password == null) {
            password = "password"
        }
        return password
    }

    // -------

    fun getConfigFromEnvironment(type: String): String? {
        var value: String? = System.getenv(type)
        return value
    }

}
