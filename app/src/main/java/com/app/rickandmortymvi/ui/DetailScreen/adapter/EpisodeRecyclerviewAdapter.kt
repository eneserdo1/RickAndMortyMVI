package com.app.rickandmortymvi.ui.DetailScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.rickandmortymvi.databinding.RecyclerItemEpisodeBinding

class EpisodeRecyclerviewAdapter : RecyclerView.Adapter<EpisodeRecyclerviewAdapter.MyHolder>() {
    private lateinit var binding: RecyclerItemEpisodeBinding
    var originalList : ArrayList<String> = arrayListOf()


    fun setList(newList:ArrayList<String>){
        originalList = newList
        notifyDataSetChanged()
    }

    class MyHolder(private val binding:RecyclerItemEpisodeBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(data:String) = with(itemView){
            binding.apply {
                episodeName.text = data
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        binding = RecyclerItemEpisodeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(originalList[position])
    }

    override fun getItemCount(): Int {
        return originalList.size
    }
}