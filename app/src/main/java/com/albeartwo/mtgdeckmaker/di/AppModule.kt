package com.albeartwo.mtgdeckmaker.di

import android.content.Context
import androidx.room.Room
import com.albeartwo.mtgdeckmaker.other.Constants.CARD_DATABASE_NAME
import com.albeartwo.mtgdeckmaker.database.CardDatabase
import com.albeartwo.mtgdeckmaker.database.CardDatabaseDao
import com.albeartwo.mtgdeckmaker.database.Repository
import com.albeartwo.mtgdeckmaker.network.ScryfallApiService
import com.albeartwo.mtgdeckmaker.other.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app : Context
    ) = Room.databaseBuilder(
        app ,
        CardDatabase::class.java ,
        CARD_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(db : CardDatabase) = db.getCardDatabaseDao()

    @Singleton
    @Provides
    fun provideScryFallApi (): ScryfallApiService {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ScryfallApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDefaultRepository(
        dao: CardDatabaseDao,
        api: ScryfallApiService
    ) = Repository(dao, api)



}