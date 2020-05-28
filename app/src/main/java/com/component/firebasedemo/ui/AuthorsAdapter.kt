package com.component.firebasedemo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.component.firebasedemo.R
import com.component.firebasedemo.data.Author
import kotlinx.android.synthetic.main.author_list_item.view.*

class AuthorsAdapter : RecyclerView.Adapter<AuthorsAdapter.AuthorsViewHolder>() {

    private var authorsList = mutableListOf<Author>()

    var listener: RecyclerViewClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AuthorsViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.author_list_item, parent, false)
    )

    override fun getItemCount() = authorsList.size

    override fun onBindViewHolder(holder: AuthorsViewHolder, position: Int) {

        holder.view.text_view_name.text = authorsList[position].name

        holder.view.button_edit.setOnClickListener {
            listener?.onRecyclerViewItemClick(it, authorsList[position])
        }

        holder.view.button_delete.setOnClickListener {
            listener?.onRecyclerViewItemClick(it, authorsList[position])
        }

    }

    fun setAuthorsList(authors: List<Author>) {
        this.authorsList = authors as MutableList<Author>
        notifyDataSetChanged()
    }

    fun authorOperations(author: Author) {

        //To prevent duplicacy
        if (!authorsList.contains(author)) {
            authorsList.add(author)

        } else {
            //To update or delete the Exisiting author
            val index = authorsList.indexOf(author)

            if (author.isDeleted) {
                //To Delete
                authorsList.removeAt(index)
            } else {
                //To edit
                authorsList[index] = author
            }

        }

        notifyDataSetChanged()

    }

    class AuthorsViewHolder(val view: View) : RecyclerView.ViewHolder(view)


}


