package ru.rpuxa.bomjonline.model

import android.content.Context
import androidx.room.*
import ru.rpuxa.core.District
import ru.rpuxa.core.DistrictAction
import ru.rpuxa.core.GameData

@Database(
    entities = [
        DataBase.User::class,
        DataBase.DirectActionItem::class,
        DataBase.DistrictItem::class
    ],
    version = 1
)
abstract class DataBase : RoomDatabase() {

    fun saveToken(token: String) {
        userDao().save(User(token = token))
    }

    fun loadToken(): String? = userDao().load().firstOrNull()?.token


    fun updateGameData(gameData: GameData) {
        val districtActionsDao = directActionsDao()
        districtActionsDao.clear()
        gameData.districtsActions.forEach {
            it.run {
                districtActionsDao.save(
                    DirectActionItem(
                        id,
                        directId,
                        name,
                        energyRemove,
                        authorityAdd,
                        rublesAdd,
                        count
                    )
                )
            }
        }

        val districtsDao = districtsDao()
        districtsDao.clear()
        gameData.districts.forEach {
            it.run {
                districtsDao.save(DistrictItem(id, name, lvlNeeded, completeRubles, completeAuthority))
            }
        }
    }


    fun loadGameData() = GameData(
        directActionsDao()
            .getAll()
            .map { it.run { DistrictAction(id, directId, name, energyRemove, authorityAdd, rublesAdd, count) } },
        districtsDao()
            .getAll()
            .map { it.run { District(id, name, lvlNeeded, completeRubles, completeAuthority) } }
    )

    companion object {
        fun create(applicationContext: Context) =
            Room.databaseBuilder(applicationContext, DataBase::class.java, "database.db")
                .build()

        private const val USER_TABLE = "user"
        private const val DIRECT_ACTIONS_TABLE = "direct_actions"
        private const val DIRECTS_TABLE = "districts"
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

    protected abstract fun directActionsDao(): DistrictActionsDao

    @Dao
    interface DistrictActionsDao {
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
        val rublesAdd: Long,
        val count: Int
    )

    protected abstract fun districtsDao(): DistrictsDao

    @Dao
    interface DistrictsDao {
        @Insert
        fun save(item: DistrictItem)

        @Query("SELECT * FROM $DIRECTS_TABLE")
        fun getAll(): List<DistrictItem>

        @Query("DELETE FROM $DIRECTS_TABLE")
        fun clear()
    }

    @Entity(tableName = DIRECTS_TABLE)
    class DistrictItem(
        @PrimaryKey
        val id: Int,
        val name: String,
        val lvlNeeded: Int,
        val completeRubles: Int,
        val completeAuthority: Int
    )
}














































































