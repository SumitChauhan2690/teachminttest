package com.example.github_search.data.model.generic

import androidx.room.Embedded
import androidx.room.Relation

data class ItemAndOwnerAndLicense(
    @Embedded val item: Item,
    @Relation(
        parentColumn = "itemId",
        entityColumn = "ownerId"
    )
    val owner: Owner,
    @Relation(
        parentColumn = "itemId",
        entityColumn = "licenseId"
    )
    val license: License
)
