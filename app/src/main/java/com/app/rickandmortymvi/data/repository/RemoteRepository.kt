package com.app.rickandmortymvi.data.repository


import com.app.imkbapp.model.Resource
import com.app.rickandmortymvi.data.remote.RemoteDataSource
import com.app.rickandmortymvi.model.CharacterDetailResponse.CharacterDetail
import kotlinx.coroutines.flow.flow
import  kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject

class RemoteRepository @Inject constructor(val remoteDataSource: RemoteDataSource){

    suspend fun fetchCharacterDetail(id:String) :Flow<Resource<CharacterDetail>>{
        return flow {
            emit(Resource.loading())
            try {
                emit(remoteDataSource.getCharacterDetail(id))
            }catch (e:Exception){
                emit(Resource.error(e.message.toString(), null))
            }
        }
    }

}