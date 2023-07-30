package ge.itodadze.messengerapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.databinding.LastChatsListItemBinding
import ge.itodadze.messengerapp.view.model.ViewChat


class FrontPageChatsAdapter(private val context: Context,
                            private var chatList: List<ViewChat>): RecyclerView.Adapter<FrontPageChatViewHolder>() {

    private lateinit var binding: LastChatsListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrontPageChatViewHolder {
        binding = LastChatsListItemBinding.inflate(LayoutInflater.from(parent.context))
        return FrontPageChatViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: FrontPageChatViewHolder, position: Int) {
        if (chatList[position].viewUser?.imgUri == null) {
            holder.image.setImageResource(R.drawable.avatar_placeholder)
        } else {
            Glide.with(context).load(chatList[position].viewUser?.imgUri)
                .apply(RequestOptions.circleCropTransform()).into(holder.image)
        }


        holder.message.text = handleText(chatList[position].message?.text)
        holder.username.text = chatList[position].viewUser?.nickname
        // handle date (date type might need changing)
        holder.date.text = chatList[position].message?.timestamp.toString()


    }

    private fun handleText(text: String?): String {
        if(text!=null && text.length > 60){
            return text.subSequence(0, 60).toString() + "..."
        }
        return "text"
    }

    private fun handleDate(text: String?): String?{
        return ""
    }
}

class FrontPageChatViewHolder(binding: LastChatsListItemBinding): RecyclerView.ViewHolder(binding.root){
    val image = binding.userProfileImage
    val username = binding.frontPageUserName
    val date = binding.frontPageDate
    val message = binding.frontPageText
}