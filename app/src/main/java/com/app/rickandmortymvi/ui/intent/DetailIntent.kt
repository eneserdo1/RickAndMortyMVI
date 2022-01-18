package com.app.rickandmortymvi.ui.intent

sealed class DetailIntent{
    data class FetchCharacterDetail(val id:String) : DetailIntent()
}
