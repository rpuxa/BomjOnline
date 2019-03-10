package ru.rpuxa.bomjonlineserver.requests

import ru.rpuxa.bomjonlineserver.Request

object WelcomeRequest : Request("") {
    override fun request(query: Query): String = "Welcome to BomjOnline API"
}