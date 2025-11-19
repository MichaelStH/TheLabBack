package com.riders.thelabback.di

import com.riders.thelabback.core.utils.Constants
import com.riders.thelabback.data.database.TheLabBackDatabase
import com.riders.thelabback.data.database.dao.UsersDao
import com.riders.thelabback.data.repositories.UserRepository
import com.riders.thelabback.data.repositories.UserRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Provides a singleton instance of [TheLabBackDatabase].
 * This function will be referenced in `application.conf`.
 */
private fun provideDatabase(): Module = module {
    single<TheLabBackDatabase> { TheLabBackDatabase.getInstance(connectionUrl = Constants.databaseOptions) }
}

/**
 * Provides an instance of [UsersDao], injecting its [TheLabBackDatabase] dependency.
 * This function will be referenced in `application.conf`.
 */
private fun provideUsersDao(): Module = module { single { UsersDao(db = get<TheLabBackDatabase>().get()) } }

/**
 * Provides an instance of [UserRepositoryImpl], injecting its [UsersDao] dependency.
 * This function will be referenced in `application.conf`.
 */
private fun provideUserRepository(): Module = module { singleOf(::UserRepositoryImpl) bind UserRepository::class }


fun provideAppModule() = provideDatabase() + provideUsersDao() + provideUserRepository()
