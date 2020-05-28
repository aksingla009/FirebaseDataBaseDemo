package com.component.firebasedemo.ui

import android.view.View
import com.component.firebasedemo.data.Author

interface RecyclerViewClickListener {

    fun onRecyclerViewItemClick(view : View, authorSelected : Author)
}