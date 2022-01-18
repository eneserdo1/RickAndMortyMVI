package com.app.rickandmortymvi.ui.viewState

import com.app.rickandmortymvi.model.CharacterDetailResponse.CharacterDetail

sealed class DetailState{

    object Idle : DetailState()
    object Loading : DetailState()
    data class Error(val message:String) : DetailState()
    data class CharacterDetails(val characterDetails: CharacterDetail) : DetailState()

}
