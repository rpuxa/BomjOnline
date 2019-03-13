package ru.rpuxa.core

import kotlin.math.sqrt


class UserData(
    login: String,
    var password: String,
    var email: String,
    id: Int,
    @Transient var token: String,
    var rubles: Long,
    var energy: Int,
    var maxEnergy: Int,
    var health: Int,
    var maxHealth: Int,
    districtAuthority: Long,
    val districtActionsProgress: HashMap<Int, Int>
) : PublicUserData(login, id, districtAuthority) {

    val allAuthority: Long get() = directAuthority

    val level: Int get() = ((1 + sqrt(1.0 + 4 * allAuthority / 100)) / 2).toInt() + 1

    fun add(
        energy: Int = 0,
        directAuthority: Long = 0,
        rubles: Long = 0
    ): Boolean {
        if (this.energy + energy < 0)
            return false
        this.energy += energy

        if (this.directAuthority + directAuthority < 0)
            return false
        this.directAuthority += directAuthority

        if (this.rubles + rubles < 0)
            return false
        this.rubles += rubles

        return true
    }

    fun makeAction(action: DistrictAction): Int {
        if (!add(energy = -action.energyRemove))
            return NOT_ENOUGH_ENERGY

        val c = districtActionsProgress[action.id] ?: 0
        if (c == action.count)
            return DIRECT_ACTION_ALREADY_FINISHED

        districtActionsProgress[action.id] = c + 1
        add(directAuthority = action.authorityAdd, rubles = action.rublesAdd)

        return NO_ERROR
    }

    fun toPublicUser() = PublicUserData(login, id, directAuthority)

    companion object {
        const val NO_ERROR = 0
        const val DIRECT_ACTION_ALREADY_FINISHED = 1
        const val NOT_ENOUGH_ENERGY = 2
    }
}