package jp.co.yumemi.android.code_check.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.code_check.constant.DatabaseConstant.ROOM_GITHUB_REPO_TABLE_NAME
import jp.co.yumemi.android.code_check.database.AppDatabase
import jp.co.yumemi.android.code_check.database.GitHubRepositoryDao
import jp.co.yumemi.android.code_check.repository.LocalGitHubDatabaseRepository
import jp.co.yumemi.android.code_check.repository.LocalGitHubDatabaseRepositoryImpl
import javax.inject.Singleton

/**
 * This Dagger module provides all Room database related dependencies used throughout the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    /**
     * Creates a Room database instance for the application.
     *
     * @param application The application context used for database creation.
     * @return A Room database instance of type `AppDatabase`.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            ROOM_GITHUB_REPO_TABLE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    /**
     * Provides the DAO (Data Access Object) for interacting with GitHub repository data
     * within the Room database.
     *
     * @param database The instance of the Room database.
     * @return The DAO object of type `GitHubRepositoryDao`.
     */
    @Singleton
    @Provides
    fun provideGitHubRepositoryDao(database: AppDatabase): GitHubRepositoryDao {
        return database.gitHubRepositoryDao()
    }

    /**
     * Creates an instance of LocalGitHubDatabaseRepositoryImpl which interacts with the
     * Room database using the provided `gitHubRepositoryDao` for data access.
     *
     * @param gitHubRepositoryDao The DAO object for accessing GitHub repository data.
     * @return An instance of `LocalGitHubDatabaseRepository`.
     */
    @Singleton
    @Provides
    fun provideLocalGitHubDatabaseRepository(gitHubRepositoryDao: GitHubRepositoryDao): LocalGitHubDatabaseRepository {
        return LocalGitHubDatabaseRepositoryImpl(gitHubRepositoryDao)
    }
}