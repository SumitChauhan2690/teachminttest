package com.example.github_search.data.model.generic

data class SelectModel(
    val id: Int,
    val name: String,
    var tick: Boolean = false
) {
    fun setSelection(select: Boolean) {
        this.tick = select
    }
}
