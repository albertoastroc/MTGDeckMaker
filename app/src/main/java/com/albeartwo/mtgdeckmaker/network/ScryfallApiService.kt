package com.albeartwo.mtgdeckmaker.network

import com.albeartwo.mtgdeckmaker.generated.Data
import com.albeartwo.mtgdeckmaker.generated.GetCardList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ScryfallApiService {

    @GET("cards/search")
    suspend fun getCardListResults(@Query("q") cardName : String) : Response<GetCardList>

    @GET("cards/named")
    suspend fun getSingleCardData(@Query("exact") cardName : String) : Response<Data>
}