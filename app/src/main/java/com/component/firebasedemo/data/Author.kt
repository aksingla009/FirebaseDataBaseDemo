package com.component.firebasedemo.data

import com.google.firebase.database.Exclude

data class Author(
    @get : Exclude
    var id: String? = null,
    var name: String? = null,
    @get : Exclude
    var isDeleted: Boolean = false
) {
    //Determine whether the authors are same or not
    override fun equals(other: Any?): Boolean {
        return if (other is Author) {
            other.id == id
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        return result
    }

}