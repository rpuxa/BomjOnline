package ru.rpuxa.bomjonlineserver

import com.sun.net.httpserver.HttpServer
import ru.rpuxa.bomjonlineserver.requests.*
import java.net.InetSocketAddress


class HTTPServer : Runnable, AutoCloseable {

    private var server: HttpServer? = null

    override fun run() {
        val server = HttpServer.create(InetSocketAddress(ADDRESS, PORT), 0)
        this.server = server
        for (request in ALL_REQUESTS) {
            server.createContext("/api/${request.name}") {
                val answer = request.request(it)
                val bytes = answer.toByteArray()//charset = Charsets.UTF_16)
                it.sendResponseHeaders(200, bytes.size.toLong())
                val os = it.responseBody
                os.write(bytes)
                os.close()
            }
        }
        server.executor = null
        server.start()
    }

    override fun close() {
        server?.stop(10)
    }

    companion object {
        const val PORT = 80
        const val ADDRESS = "192.168.137.1"
        @JvmStatic
        val ALL_REQUESTS = arrayOf(
            CheckRegRequest,
            CheckUpdateRequest,
            LoginRequest,
            MakeDistrictAction,
            UserRequest,
            RegParameters,
            RegRequest,
            UpdateRequest,
            PublicUserRequest,
            WelcomeRequest
        )
    }
}