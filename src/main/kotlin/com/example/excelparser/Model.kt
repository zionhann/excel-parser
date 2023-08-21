package com.example.excelparser

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Model(
    @Id var id: Long,
    var name: String,
    var description: String?,
)