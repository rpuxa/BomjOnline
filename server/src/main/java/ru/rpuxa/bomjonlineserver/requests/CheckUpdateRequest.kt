package ru.rpuxa.bomjonlineserver.requests

import ru.rpuxa.bomjonlineserver.Request
import ru.rpuxa.bomjonlineserver.requests.UpdateRequest.UPDATE_HASH
import ru.rpuxa.bomjonlineserver.toJson
import ru.rpuxa.core.RequestCodes

object CheckUpdateRequest : Request("check_update") {
    override fun request(query: Query): String {
        val hash = query["hash"]?.toIntOrNull() ?: return RequestCodes.BAD_ARGUMENTS.string

        return ("required" to if (hash == UPDATE_HASH) 0 else 1).toJson()
    }
}