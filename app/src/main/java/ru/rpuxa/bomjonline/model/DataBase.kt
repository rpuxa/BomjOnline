package ru.rpuxa.bomjonline.model

import android.content.Context
import androidx.room.*
import ru.rpuxa.core.DirectAction

@Database(
    entities = [DataBase.User::class, DataBase.DirectActionItem::class],
    version = 1
)
abstract class DataBase : RoomDatabase() {

    fun saveToken(token: String) {
        userDao().save(User(token = token))
    }

    fun loadToken(): String? = userDao().load().firstOrNull()?.token

    fun updateDirectActions(list: List<DirectAction>) {
        val dao = directActionsDao()
        dao.clear()
        for (item in list) {
            dao.save(
                DirectActionItem(
                    item.id,
                    item.directId,
                    item.name,
                    item.energyRemove,
                    item.authorityAdd,
                    item.rublesAdd
                )
            )
        }
    }

    fun getAllDirectActions(): List<DirectAction> =
        directActionsDao()
            .getAll()
            .map { item ->
                DirectAction(
                    item.id,
                    item.directId,
                    item.name,
                    item.energyRemove,
                    item.authorityAdd,
                    item.rublesAdd
                )
            }

    companion object {
        fun create(applicationContext: Context) =
            Room.databaseBuilder(applicationContext, DataBase::class.java, "database.db")
                .build()

        private const val USER_TABLE = "user"
        private const val DIRECT_ACTIONS_TABLE = "direct_actions"
    }

    protected abstract fun userDao(): UserDao

    @Dao
    interface UserDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun save(user: User)

        @Query("SELECT * FROM $USER_TABLE WHERE id = 0")
        fun load(): List<User>
    }

    @Entity(tableName = USER_TABLE)
    class User(
        @PrimaryKey
        val id: Int = 0,
        val token: String
    )

    protected abstract fun directActionsDao(): DirectActionsDao

    @Dao
    interface DirectActionsDao {
        @Insert
        fun save(item: DirectActionItem)

        @Query("SELECT * FROM $DIRECT_ACTIONS_TABLE")
        fun getAll(): List<DirectActionItem>

        @Query("DELETE FROM $DIRECT_ACTIONS_TABLE")
        fun clear()
    }

    @Entity(tableName = DIRECT_ACTIONS_TABLE)
    class DirectActionItem(
        @PrimaryKey
        val id: Int,
        val directId: Int,
        val name: String,
        val energyRemove: Int,
        val authorityAdd: Long,
        val rublesAdd: Long
    )
}