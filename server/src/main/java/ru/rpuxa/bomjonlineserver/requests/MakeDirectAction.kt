package ru.rpuxa.bomjonlineserver.requests

import ru.rpuxa.bomjonlineserver.actions.DirectActions
import ru.rpuxa.bomjonlineserver.dataBase
import ru.rpuxa.bomjonlineserver.db.updateUser
import ru.rpuxa.core.RequestCodes
import ru.rpuxa.core.UserData

object MakeDirectAction : TokenRequest("make_direct_action") {

    override fun request(user: UserData, query: Query): String {
        val directActionId = query["id"]?.toIntOrNull() ?: return RequestCodes.BAD_ARGUMENTS.string
        val action = DirectActions.getDirectActionById(directActionId) ?: return RequestCodes.WRONG_ACTION_ID.string
        dataBase.updateUser(user) {
            if (!user.remove(energy = action.energyRemove))
                return RequestCodes.OUT_OF_SYNC.string

            user.add(directAuthority = action.authorityAdd)
        }
        return RequestCodes.NO_ERROR.string
    }
}