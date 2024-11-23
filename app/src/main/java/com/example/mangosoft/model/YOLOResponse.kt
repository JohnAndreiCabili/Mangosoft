package com.example.mangosoft.model

    data class YOLOResponse(
        val blemishes: Boolean,
        val confidence: Float,
        val mango_type: String,
        val texture: String
    )
