package com.example.myunlimitedpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myunlimitedpage.databinding.ItemUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var list = ArrayList<DataItem>()

    inner class ViewHolder (private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: DataItem){
            binding.tvEmail.text = user.email
            binding.tvName.text = user.firstName
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addList(items: ArrayList<DataItem>){
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun clear(){
        list.clear()
        notifyDataSetChanged()
    }
}