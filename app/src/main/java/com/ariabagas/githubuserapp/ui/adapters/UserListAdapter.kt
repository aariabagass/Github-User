package com.ariabagas.githubuserapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ariabagas.githubuserapp.databinding.ItemUserBinding
import com.ariabagas.githubuserapp.models.UserModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class UserListAdapter :
    RecyclerView.Adapter<UserListAdapter.ListViewHolder>() {
    private val listUser = ArrayList<UserModel>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: ArrayList<UserModel>) {
        listUser.clear()
        listUser.addAll(list)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val bindingItem: ItemUserBinding) :
        RecyclerView.ViewHolder(bindingItem.root) {

        fun bind(userModelResponse: UserModel) {
            bindingItem.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(userModelResponse)
            }
            with(bindingItem) {
                Glide.with(itemView).load(userModelResponse.avatar_url).apply(RequestOptions())
                    .into(imgItemPhoto)
                tvItemName.text = userModelResponse.login
                tvItemId.text = userModelResponse.id.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserModel)
    }
}