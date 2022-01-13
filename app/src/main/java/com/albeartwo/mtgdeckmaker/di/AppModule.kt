package com.albeartwo.mtgdeckmaker.di

import android.content.Context
import androidx.room.Room
import com.albeartwo.mtgdeckmaker.other.Constants.CARD_DATABASE_NAME
import com.albeartwo.mtgdeckmaker.database.CardDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app : Context
    ) = Room.databaseBuilder(
        app,
        CardDatabase::class.java,
        CARD_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(db : CardDatabase) = db.getCardDatabaseDao()

}