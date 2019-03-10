package ru.rpuxa.core


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
    directAuthority: Long
) : PublicUserData(login, id, directAuthority) {

    val allAuthority: Long get() = directAuthority

    fun remove(
        energy: Int = 0,
        directAuthority: Long = 0
    ): Boolean {

        if (this.energy - energy < 0)
            return false
        this.energy -= energy

        if (this.directAuthority - directAuthority < 0)
            return false
        this.directAuthority -= directAuthority

        return true
    }

    fun add(
        directAuthority: Long = 0
    ) {
        this.directAuthority += directAuthority
    }

    fun toPublicUser() = PublicUserData(login, id, directAuthority)
}