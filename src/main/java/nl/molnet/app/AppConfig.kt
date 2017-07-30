package nl.molnet.app

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

    private fun getApplicationPort() : String {
        var port = getConfigFromEnvironment("APP_PORT")
        if (port == null) {
            port = "9090"
        }
        return port
    }

    private fun getArangoDbHost() : String {
        var host = getConfigFromEnvironment("ARANGO_HOST")
        if (host == null) {
            host = "localhost"
        }
        return host
    }

    private fun getArangoDbPort() : String {
        var port = getConfigFromEnvironment("ARANGO_PORT")
        if (port == null) {
            port = "8529"
        }
        return port
    }

    private fun getArangoDbUser() : String {
        var user = getConfigFromEnvironment("ARANGO_USER")
        if (user == null) {
            user = "user"
        }
        return user
    }

    private fun getArangoDbPassword() : String {
        var password = getConfigFromEnvironment("ARANGO_PASSWORD")
        if (password == null) {
            password = "password"
        }
        return password
    }

    // -------

    fun getConfigFromEnvironment(type: String) : String {
        var value = System.getenv(type)
        return value
    }

}
