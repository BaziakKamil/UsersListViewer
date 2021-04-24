package pl.kamilbaziak.userslistviewer.ui.alluserfragment

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pl.kamilbaziak.userslistviewer.databinding.AllUserListRowBinding
import pl.kamilbaziak.userslistviewer.model.UserModel

class AllUserAdapter(private val listener: OnItemClickListener, private val context: Context) :
    ListAdapter<UserModel, AllUserAdapter.AllUserViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllUserViewHolder {
        val binding =
            AllUserListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllUserViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    //uzywamy viewBidingu do przekazywania kodu
    inner class AllUserViewHolder(private val binding: AllUserListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val shoppingList = getItem(position)
                        listener.onItemClicked(shoppingList)
                    }
                }
            }
        }

        fun bind(userModel: UserModel) {
            binding.apply {
                textViewUsername.text = userModel.userName
                loadImageWithUri(imageViewUserAvatar, userModel)
            }
        }
    }

    fun loadImageWithUri(imageView: ImageView, userModel: UserModel){
        Glide.with(imageView.context).load(Uri.parse(userModel.avatarUrl)).into(imageView)
    }

    interface OnItemClickListener {
        fun onItemClicked(userModel: UserModel)
    }

    class DiffCallback : DiffUtil.ItemCallback<UserModel>() {
        override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: UserModel,
            newItem: UserModel
        ): Boolean =
            oldItem == newItem
    }
}