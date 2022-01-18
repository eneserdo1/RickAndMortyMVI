package com.app.rickandmortymvi.data.remote


import com.app.imkbapp.model.Resource
import com.app.rickandmortymvi.data.network.ApiService
import com.app.rickandmortymvi.model.CharacterDetailResponse.CharacterDetail
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(val apiService: ApiService){

    suspend fun getCharacterDetail(id:String): Resource<CharacterDetail> {
        return getResponse(request = {apiService.getSingleCharacters(id)},defaultErrorMessage = "Error Fetching Detail")
    }

    suspend fun  <T> getResponse(request: suspend () -> Response<T>, defaultErrorMessage: String): Resource<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                return Resource.success(result.body())
            } else {
                Resource.error(defaultErrorMessage, null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.error("Error ${e.message}", null)
        }
    }

}