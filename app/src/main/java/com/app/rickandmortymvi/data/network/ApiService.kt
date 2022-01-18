package com.app.rickandmortymvi.data.network

import com.app.rickandmortymvi.model.CharacterDetailResponse.CharacterDetail
import com.app.rickandmortymvi.model.CharacterListResponse.CharacterList
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("api/character/")
    suspend fun getAllCharacters(@Query("page") pageNumber : Int): Response<CharacterList>

    @GET("api/character/{id}")
    suspend fun getSingleCharacters(@Path("id") id:String): Response<CharacterDetail>

}