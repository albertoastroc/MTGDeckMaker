package com.albeartwo.mtgdeckmaker.di

import android.content.Context
import androidx.room.Room
import com.albeartwo.mtgdeckmaker.other.Constants.CARD_DATABASE_NAME
import com.albeartwo.mtgdeckmaker.database.CardDatabase
import com.albeartwo.mtgdeckmaker.database.CardDao
import com.albeartwo.mtgdeckmaker.database.Repository
import com.albeartwo.mtgdeckmaker.other.Constants.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
    fun provideRetrofit() : Retrofit {

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideDefaultRepository(
        dao : CardDao ,
        api : Retrofit
    ) = Repository(dao , api)
}