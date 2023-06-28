package com.example.mystoryapp.utils

import com.example.mystoryapp.data.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem>{
        val storyList : MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100){
            val story = ListStoryItem(
                "story-$i",
                "https://story-api.dicoding.dev/images/stories/photos-1683497415212_dnKjbXF-.jpg",
                "2023-05-07T22:10:15.213Z",
                "Andre",
                "Halo",
                37.422092,
                -122.08392
            )
            storyList.add(story)
        }
        return storyList
    }
}