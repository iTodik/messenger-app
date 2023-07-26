package ge.itodadze.messengerapp.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.databinding.SearchUserBinding
import ge.itodadze.messengerapp.view.model.ViewUser

class SearchUsersAdapter(private val context: Context, private var userList: List<ViewUser>):
    RecyclerView.Adapter<SearchUserViewHolder>(){

    private lateinit var binding: SearchUserBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        binding = SearchUserBinding.inflate(LayoutInflater.from(parent.context))

        return SearchUserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        if (userList[position].imgUri == null) {
            holder.avatar.setImageResource(R.drawable.avatar_placeholder)
        } else {
            Glide.with(context).load(userList[position].imgUri)
                .apply(RequestOptions.circleCropTransform()).into(holder.avatar)
        }
        holder.nickname.text = userList[position].nickname
        holder.profession.text = userList[position].profession
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newUserList: List<ViewUser>) {
        userList = newUserList
        notifyDataSetChanged()
    }
}

class SearchUserViewHolder(binding: SearchUserBinding): RecyclerView.ViewHolder(binding.root) {
    val avatar = binding.avatar
    val nickname = binding.nickname
    val profession = binding.profession
}