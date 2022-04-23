package com.albeartwo.mtgdeckmaker.network

import com.albeartwo.mtgdeckmaker.generated.Data
import com.albeartwo.mtgdeckmaker.other.Constants.BASE_URL
import com.albeartwo.mtgdeckmaker.generated.GetCardList
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ScryfallApiService {


    @GET("cards/search")
    fun getCardListResults(@Query("q") cardName : String) : GetCardList

    @GET("cards/named")
    fun getSingleCardData(@Query("exact") cardName : String) :Data

}
