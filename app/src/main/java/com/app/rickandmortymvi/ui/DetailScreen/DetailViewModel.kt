package com.app.rickandmortymvi.ui.DetailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imkbapp.model.Status
import com.app.rickandmortymvi.data.repository.RemoteRepository
import com.app.rickandmortymvi.ui.intent.DetailIntent
import com.app.rickandmortymvi.ui.viewState.DetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: RemoteRepository):ViewModel() {

    val detailIntent = Channel<DetailIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<DetailState>(DetailState.Idle)
    val state : StateFlow<DetailState>
            get() = _state


    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            detailIntent.consumeAsFlow().collect {
                when(it){
                    is DetailIntent.FetchCharacterDetail-> fetchDetail(it.id)
                }
            }
        }
    }

    private fun fetchDetail(id:String) {
        viewModelScope.launch {
            repository.fetchCharacterDetail(id).collect {
                when(it.status){
                    Status.SUCCESS->{
                        _state.value = DetailState.CharacterDetails(it.data!!)
                    }
                    Status.LOADING->{
                        _state.value = DetailState.Loading
                    }
                    Status.ERROR->{
                        _state.value = DetailState.Error(it.message.toString())
                    }
                }
            }
        }

    }

}