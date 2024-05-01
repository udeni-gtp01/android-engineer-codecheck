package jp.co.yumemi.android.code_check.database

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository
import jp.co.yumemi.android.code_check.model.SavedGitHubRepository

/**
 * This abstract class represents the application's Room database. It defines the entities
 * that will be persisted and provides access to the corresponding DAO (Data Access Object).
 */
@Database(entities = [LocalGitHubRepository::class, SavedGitHubRepository::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Returns the DAO (Data Access Object) used to interact with `LocalGitHubRepository` entities
     * within the database.
     */
    abstract fun gitHubRepositoryDao(): GitHubRepositoryDao
}