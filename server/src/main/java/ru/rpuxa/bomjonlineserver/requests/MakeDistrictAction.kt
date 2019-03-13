package ru.rpuxa.bomjonlineserver.requests

import ru.rpuxa.bomjonlineserver.actions.DistrictActions
import ru.rpuxa.bomjonlineserver.dataBase
import ru.rpuxa.bomjonlineserver.db.updateUser
import ru.rpuxa.core.RequestCodes
import ru.rpuxa.core.UserData
import ru.rpuxa.core.UserData.Companion.NO_ERROR

object MakeDistrictAction : TokenRequest("makeDistrictAction") {

    override fun request(user: UserData, query: Query): String {
        val directActionId = query["id"]?.toIntOrNull() ?: return RequestCodes.BAD_ARGUMENTS.string
        val action = DistrictActions.getDirectActionById(directActionId) ?: return RequestCodes.WRONG_ACTION_ID.string
        dataBase.updateUser(user) {
            if (it.makeAction(action) != NO_ERROR)
                return RequestCodes.OUT_OF_SYNC.string
        }
        return RequestCodes.NO_ERROR.string
    }
}