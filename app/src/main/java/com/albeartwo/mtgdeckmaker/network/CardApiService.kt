package com.albeartwo.mtgdeckmaker.network

import com.albeartwo.mtgdeckmaker.generated.Data
import com.albeartwo.mtgdeckmaker.other.Constants.BASE_URL
import com.albeartwo.mtgdeckmaker.generated.GetCardList
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface CardApiService {


        @GET("cards/search")
        fun getCardListResults(@Query("q") cardName : String) : Call<GetCardList>

        @GET("cards/named")
        fun getCardImage(@Query("exact") cardName : String) : Call<Data>

}

object CardApi {
    val retrofitService : CardApiService by lazy { retrofit.create(CardApiService::class.java) }
}
