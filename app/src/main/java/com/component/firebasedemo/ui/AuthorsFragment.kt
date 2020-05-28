package com.component.firebasedemo.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.component.firebasedemo.R
import com.component.firebasedemo.data.Author
import kotlinx.android.synthetic.main.fragment_authors.*

class AuthorsFragment : Fragment(), RecyclerViewClickListener {

    private lateinit var viewModel: AuthorsViewModel
    private val adapter = AuthorsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(AuthorsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_authors, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recycler_view_authors.adapter = adapter

        adapter.listener = this

        viewModel.fetchAllAuthors()
        viewModel.authorsLiveListData.observe(viewLifecycleOwner, Observer {
            adapter.setAuthorsList(it)

        })

        viewModel.getRealTimeUpdates()
        viewModel.authorLiveData.observe(viewLifecycleOwner, Observer {
            adapter.authorOperations(it)
        })


        button_add.setOnClickListener {
            AddAuthorDialogFragment()
                .show(childFragmentManager, "")
        }
    }

    override fun onRecyclerViewItemClick(view: View, authorSelected: Author) {
        when (view.id) {
            R.id.button_edit -> {
                EditAuthorDialogFragment(authorSelected).show(childFragmentManager, "")
            }
            R.id.button_delete -> {

                AlertDialog.Builder(requireContext()).also {
                    it.setTitle(getString(R.string.delete_confirmation))
                    it.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                        viewModel.deleteAuthor(authorSelected)
                    }
                }.create().show()
            }
        }
    }

}
