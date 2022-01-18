package com.app.rickandmortymvi.ui.viewState

import androidx.paging.PagingData
import com.app.rickandmortymvi.model.CharacterListResponse.Results
import kotlinx.coroutines.flow.Flow

sealed class MainState{

    object Idle : MainState()
    object Loading : MainState()
    data class Error(val message:String) : MainState()
    data class CharacterList(val characterList:Flow<PagingData<Results>>) : MainState()

}
