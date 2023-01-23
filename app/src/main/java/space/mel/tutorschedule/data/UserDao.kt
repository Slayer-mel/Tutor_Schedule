package space.mel.tutorschedule.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import space.mel.tutorschedule.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user:User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAllUsers()

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllData(): List<User>

    @Query("SELECT  * FROM user_table WHERE firstName LIKE :searchQuery OR grade LIKE :searchQuery")
    fun searchDatabase (searchQuery: String): Flow<List<User>>
}