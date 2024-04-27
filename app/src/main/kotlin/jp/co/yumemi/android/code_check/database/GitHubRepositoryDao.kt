package jp.co.yumemi.android.code_check.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository

@Dao
interface GitHubRepositoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGitHubRepository(localGitHubRepository: LocalGitHubRepository)

    @Query("SELECT * FROM github_repo_table WHERE id = :id")
    suspend fun getGitHubRepositoryById(id: Long): LocalGitHubRepository?

    @Delete
    suspend fun deleteGitHubRepository(localGitHubRepository: LocalGitHubRepository)
}