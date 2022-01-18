package com.app.rickandmortymvi.ui.MainScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.app.rickandmortymvi.R
import com.app.rickandmortymvi.databinding.ActivityMainBinding
import com.app.rickandmortymvi.model.CharacterListResponse.Results
import com.app.rickandmortymvi.ui.DetailScreen.DetailActivity
import com.app.rickandmortymvi.ui.MainScreen.adapter.CharacterRecyclerviewAdapter
import com.app.rickandmortymvi.ui.MainScreen.adapter.ItemClickListener
import com.app.rickandmortymvi.ui.intent.MainIntent
import com.app.rickandmortymvi.ui.viewState.MainState
import com.app.rickandmortymvi.util.alertBox
import com.app.rickandmortymvi.util.hideProgressDialog
import com.app.rickandmortymvi.util.showProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()
    private lateinit var characterAdapter: CharacterRecyclerviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerview()
        setupObservers()
        setupAdapterLoader()
        lifecycleScope.launch {
            viewModel.mainIntent.send(MainIntent.FetchList)
        }
    }

    private fun setupAdapterLoader() {
        characterAdapter.addLoadStateListener { loadState->
            if (loadState.refresh is LoadState.Loading){
                this.showProgressDialog()
            }else{
                hideProgressDialog()
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> {
                        loadState.refresh as LoadState.Error
                    }
                    else -> null
                }
                errorState?.let {
                    alertBox(getString(R.string.hata),getString(R.string.bir_hata_olustu),this)
                }
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when(it){
                    is MainState.CharacterList->{
                        it.characterList.collect {
                            characterAdapter.submitData(it)
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerview() {
        characterAdapter = CharacterRecyclerviewAdapter(object : ItemClickListener {
            override fun selectedCharacter(character: Results) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("id",character.id.toString())
                startActivity(intent)
            }
        })
        binding.recyclerview.apply {
            layoutManager = GridLayoutManager(this@MainActivity,2)
            adapter = characterAdapter
        }
    }

    override fun onStop() {
        super.onStop()
        hideProgressDialog()
    }
}