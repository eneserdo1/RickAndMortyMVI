package com.app.rickandmortymvi.ui.MainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.app.imkbapp.model.Status
import com.app.rickandmortymvi.data.remote.PagingDataSource
import com.app.rickandmortymvi.data.repository.RemoteRepository
import com.app.rickandmortymvi.ui.intent.DetailIntent
import com.app.rickandmortymvi.ui.intent.MainIntent
import com.app.rickandmortymvi.ui.viewState.DetailState
import com.app.rickandmortymvi.ui.viewState.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val dataSource: PagingDataSource) : ViewModel() {


    val mainIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<MainState>(MainState.Idle)
    val state : StateFlow<MainState>
        get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            mainIntent.consumeAsFlow().collect {
                when(it){
                    is MainIntent.FetchList -> getCharacterList()
                }
            }
        }
    }

    private fun getCharacterList() {
        _state.value = MainState.CharacterList(Pager(PagingConfig(pageSize = 33)) {
            dataSource
        }.flow.cachedIn(viewModelScope))
    }


}