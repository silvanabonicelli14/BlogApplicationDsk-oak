package com.cgm.experiments.blogapplicationdsl.domain

interface Repository<T>  {
    fun getAll(): MutableList<T>
    fun getOne(id: Int): T?
    fun new(article: T): T
    fun update(article: T): T?
    fun delete(id: Int): T?
}