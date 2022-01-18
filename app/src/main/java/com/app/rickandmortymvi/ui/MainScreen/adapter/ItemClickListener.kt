package com.app.rickandmortymvi.ui.MainScreen.adapter

import com.app.rickandmortymvi.model.CharacterListResponse.Results


interface ItemClickListener {

    fun selectedCharacter(character: Results)

}