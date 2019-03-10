//package ru.rpuxa.ru.rpuxa.bomjonlineserver.db
//
//import ru.rpuxa.ru.rpuxa.bomjonlineserver.UserData
//import java.sql.Connection
//import java.sql.DriverManager
//import java.sql.Statement
//
//class SQLUserDataBase {
//    val connection: Connection = DriverManager.getConnection("jdbc:sqlite:test.db")
//    val statement: Statement = connection.createStatement()
//
//
//    init {
//        statement.execute()
//    }
//
//
//    fun updateUser(user: UserData) {
//        statement.
//    }
//
//    fun close() {
//        connection.close()
//        statement.close()
//    }
//
//    private companion object {
//        const val CREATE_USER_TABLE =
//            "CREATE TABLE IF NOT EXISTS users (" +
//                    "id INTEGER PRIMARY KEY," +
//                    "login TEXT," +
//                    "password TEXT," +
//                    "token TEXT"
//
//    }
//}