package jp.co.yumemi.android.code_check.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository

@Database(entities = [LocalGitHubRepository::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gitHubRepositoryDao(): GitHubRepositoryDao
}