package com.cgm.experiments.blogapplicationdsl.utils

interface ServerPort {
    fun value(): Int
}
object RandomServerPort : ServerPort {
    override fun value(): Int = (10000..10500).random()
}