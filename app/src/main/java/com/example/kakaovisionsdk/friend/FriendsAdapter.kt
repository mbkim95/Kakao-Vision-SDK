package com.example.kakaovisionsdk.friend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kakaovisionsdk.R
import com.example.kakaovisionsdk.databinding.ItemFriendBinding

class FriendsAdapter(var friends: List<FriendItem>) :
    RecyclerView.Adapter<FriendsAdapter.FriendHolder>() {
    private lateinit var binding: ItemFriendBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendHolder {
        binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendHolder, position: Int) {
        holder.bind(friends[position])
    }

    override fun getItemCount(): Int = friends.size

    class FriendHolder(private val binding: ItemFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FriendItem) {
            binding.apply {
                Glide.with(binding.root)
                    .load(item.friendInfo.profileThumbnailImage)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .centerCrop()
                    .into(profileIv)

                nameTv.text = item.friendInfo.profileNickname

                checkBox.setOnClickListener {
                    item.isChecked = !checkBox.isChecked
                }

                root.setOnClickListener {
                    checkBox.isChecked = !checkBox.isChecked
                    item.isChecked = checkBox.isChecked
                }
            }
        }
    }
}