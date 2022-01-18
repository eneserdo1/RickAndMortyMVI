package com.app.rickandmortymvi.data.remote

import androidx.paging.PagingSource
import com.app.rickandmortymvi.data.network.ApiService
import com.app.rickandmortymvi.model.CharacterListResponse.Results
import retrofit2.HttpException
import javax.inject.Inject

class PagingDataSource @Inject constructor(val apiService: ApiService) :
    PagingSource<Int, Results>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Results> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            val response = apiService.getAllCharacters(currentLoadingPageKey)
            val responseData = mutableListOf<Results>()
            val data = response.body()?.results ?: emptyList<Results>()
            responseData.addAll(data)

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            return LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey =  if (currentLoadingPageKey < response.body()!!.info.pages)
                    currentLoadingPageKey.plus(1) else null
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}