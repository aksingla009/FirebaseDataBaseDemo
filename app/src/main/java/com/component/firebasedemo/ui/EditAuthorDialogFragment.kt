package com.component.firebasedemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.component.firebasedemo.R
import com.component.firebasedemo.data.Author
import kotlinx.android.synthetic.main.author_list_item.*
import kotlinx.android.synthetic.main.dialog_fragment_add_author.*
import kotlinx.android.synthetic.main.dialog_fragment_add_author.edit_text_name
import kotlinx.android.synthetic.main.dialog_fragment_add_author.input_layout_name
import kotlinx.android.synthetic.main.dialog_fragment_edit_author.*


class EditAuthorDialogFragment(
    private val selectedAuthor: Author
) : DialogFragment() {

    private lateinit var authorsViewModel: AuthorsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        authorsViewModel = ViewModelProvider(this).get(AuthorsViewModel::class.java)
        return inflater.inflate(R.layout.dialog_fragment_edit_author, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        edit_text_name.setText(selectedAuthor.name)

        authorsViewModel.resultLiveData.observe(viewLifecycleOwner, Observer {
            val message = if (it == null) {
                getString(R.string.author_updated_successfully)
            } else {
                getString(R.string.error_occurred_while_adding, it.message)
            }

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            dismiss()//Dismiss the Dialog of editing an author

        })

        button_update.setOnClickListener {
            val nameStr = edit_text_name.text.toString().trim()

            if (nameStr.isEmpty()) {
                input_layout_name.error = getString(R.string.error_field_required)
                return@setOnClickListener
            }

            selectedAuthor.name = nameStr

            //call addAuthor method of ViewModel
            authorsViewModel.updateAuthor(selectedAuthor)


        }
    }
}