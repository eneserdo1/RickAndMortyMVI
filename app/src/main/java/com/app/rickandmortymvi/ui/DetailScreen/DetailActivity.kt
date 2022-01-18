package com.app.rickandmortymvi.ui.DetailScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.rickandmortymvi.R
import com.app.rickandmortymvi.databinding.ActivityDetailBinding
import com.app.rickandmortymvi.ui.DetailScreen.adapter.EpisodeRecyclerviewAdapter
import com.app.rickandmortymvi.ui.intent.DetailIntent
import com.app.rickandmortymvi.ui.viewState.DetailState
import com.app.rickandmortymvi.util.alertBox
import com.app.rickandmortymvi.util.clickListener
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private val viewModel: DetailViewModel by viewModels<DetailViewModel>()
    private lateinit var binding: ActivityDetailBinding
    private lateinit var episodeAdapter: EpisodeRecyclerviewAdapter
    var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getStringExtra("id")
        id?.let {
            lifecycleScope.launch {
                viewModel.detailIntent.send(DetailIntent.FetchCharacterDetail(it))
            }
        }

        setupRecyclerview()
        setupButtonsListener()
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is DetailState.Error -> {
                        alertBox(getString(R.string.hata),getString(R.string.bir_hata_olustu),this@DetailActivity)
                    }
                    is DetailState.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                    }
                    is DetailState.CharacterDetails -> {
                        binding.progressbar.visibility = View.GONE
                        it.characterDetails?.let { response ->
                            binding.apply {
                                nameTxt.text = response.name
                                descriptionTxt.text = "${response.status}, ${response.species}"
                                genderTxt.text = response.gender
                                Glide.with(this@DetailActivity).load(response.image)
                                    .into(profilePhoto)
                            }
                            episodeAdapter.setList(response.episode as ArrayList<String>)
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerview() {
        episodeAdapter = EpisodeRecyclerviewAdapter()
        binding.episodeRecyclerview.apply {
            adapter = episodeAdapter
            layoutManager = LinearLayoutManager(this@DetailActivity)
        }
    }

    private fun setupButtonsListener() {
        binding.openExpandableLayout.clickListener {
            if (binding.episodeRecyclerview.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(
                    binding.expandableCardview,
                    AutoTransition()
                )
                binding.episodeRecyclerview.visibility = View.VISIBLE
                binding.arrow.setBackgroundResource(R.drawable.arrow_up_24)
            } else {
                TransitionManager.beginDelayedTransition(
                    binding.expandableCardview,
                    AutoTransition()
                )
                binding.episodeRecyclerview.visibility = View.GONE
                binding.arrow.setBackgroundResource(R.drawable.arrow_down_24)
            }
        }

        binding.closeBtn.clickListener {
            onBackPressed()
        }
    }
}