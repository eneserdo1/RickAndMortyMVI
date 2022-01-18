package com.app.rickandmortymvi.ui.MainScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.rickandmortymvi.databinding.RecyclerItemCharacterBinding
import com.app.rickandmortymvi.model.CharacterListResponse.Results
import com.app.rickandmortymvi.util.clickListener
import com.bumptech.glide.Glide

class CharacterRecyclerviewAdapter(val itemClickListener: ItemClickListener) : PagingDataAdapter<Results,CharacterRecyclerviewAdapter.MyHolder>(DataDifferntiator) {
    private lateinit var binding: RecyclerItemCharacterBinding

    inner class MyHolder(private val binding:RecyclerItemCharacterBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(data: Results) = with(itemView){
            binding.apply {
                Glide.with(itemView).load(data.image).into(characterImage)
                characterName.text = data.name
            }

            itemView.clickListener {
                itemClickListener.selectedCharacter(data)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        binding = RecyclerItemCharacterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }


    object DataDifferntiator : DiffUtil.ItemCallback<Results>() {

        override fun areItemsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem == newItem
        }
    }
}