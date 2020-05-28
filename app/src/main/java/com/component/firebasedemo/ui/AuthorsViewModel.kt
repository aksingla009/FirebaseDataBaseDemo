package com.component.firebasedemo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.component.firebasedemo.data.Author
import com.component.firebasedemo.data.NODE_AUTHORS
import com.google.firebase.database.*
import java.lang.Exception

class AuthorsViewModel : ViewModel() {
    //Create Reference for Fire base Db
    //Pass node to create the reference otherwise it will create a reference of root node

    private val dbAuthors = FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)


    //To observe the added Author
    private val _result = MutableLiveData<Exception?>()

    val resultLiveData: LiveData<Exception?>
        get() = _result


    //To observe Fetched list of Authors
    private val _authorsList = MutableLiveData<List<Author>>()

    val authorsLiveListData: LiveData<List<Author>>
        get() = _authorsList

    //To observe Changed Author at real time
    private val _author = MutableLiveData<Author>()

    val authorLiveData: LiveData<Author>
        get() = _author


    fun addAuthor(author: Author) {
        //Now create unique key inside authors node use PUSH() to do that

        author.id = dbAuthors.push().key

        //dbAuthors.setValue(author) will save author in authors node
        //but we want to save author inside the unique key which we just created
        // so use child method to first create a child node of unique key and then inside that save author object

        dbAuthors.child(author.id!!).setValue(author)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _result.value = null
                } else {
                    _result.value = it.exception
                }
            }
    }

    fun updateAuthor(author: Author) {
        //dbAuthors.setValue(author) will save author in authors node
        //but we want to save author inside the unique key which we just created
        // so use child method to first create a child node of unique key and then inside that save author object

        dbAuthors.child(author.id!!).setValue(author)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _result.value = null
                } else {
                    _result.value = it.exception
                }
            }

    }


    fun deleteAuthor(author: Author) {
        dbAuthors.child(author.id!!).setValue(null)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _result.value = null
                } else {
                    _result.value = it.exception
                }
            }

    }

    private val childEventListener = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) {
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, p1: String?) {
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
            val author = dataSnapshot.getValue(Author::class.java)
            author?.id = dataSnapshot.key
            _author.value = author
        }

        override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
            val author = dataSnapshot.getValue(Author::class.java)
            author?.id = dataSnapshot.key
            _author.value = author

        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            val author = dataSnapshot.getValue(Author::class.java)
            author?.id = dataSnapshot.key
            author?.isDeleted = true
            _author.value = author
        }

    }


    fun getRealTimeUpdates() {
        dbAuthors.addChildEventListener(childEventListener)
    }


    fun fetchAllAuthors() {
        dbAuthors.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapShot: DataSnapshot) {

                if (snapShot.exists()) {

                    val authors = mutableListOf<Author>()

                    for (authorSnapShot in snapShot.children) {
                        val authorObj = authorSnapShot.getValue(Author::class.java)
                        authorObj?.id = authorSnapShot.key
                        authorObj?.let {
                            authors.add(authorObj)
                        }
                    }

                    _authorsList.value = authors

                }

            }

        })
    }

    fun fetchFilteredAuthors(index: Int) {
        val dbAuthors =
            when (index) {
                1 ->
                    //#1 SELECT * FROM Authors
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)

                2 ->
                    //#2 SELECT * FROM Authors WHERE id = ?
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
                        .child("-M-3fFw3GbovXWguSjp8")

                3 ->
                    //#3 SELECT * FROM Authors WHERE city = ?
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
                        .orderByChild("city")
                        .equalTo("Hyderabad")

                4 ->
                    //#4 SELECT * FROM Authors LIMIT 2
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
                        .limitToFirst(2)

                5 ->
                    //#5 SELECT * FROM Authors WHERE votes < 500
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
                        .orderByChild("votes")
                        .endAt(500.toDouble())

                6 ->
                    //#6 SELECT * FROM Artists WHERE name LIKE "A%"
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
                        .orderByChild("name")
                        .startAt("A")
                        .endAt("A\uf8ff")

                7 ->
                    //#7 SELECT * FROM Artists Where votes < 500 AND city = Bangalore
                    //can not be done directly in one query this is the limitation of firebase realtime db
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
                
                else -> FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
            }

        dbAuthors.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val authors = mutableListOf<Author>()
                    for (authorSnapshot in snapshot.children) {
                        val author = authorSnapshot.getValue(Author::class.java)
                        author?.id = authorSnapshot.key
                        author?.let { authors.add(it) }
                    }
                    _authorsList.value = authors
                }
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        dbAuthors.removeEventListener(childEventListener)
    }

}