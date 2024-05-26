package com.balex.fbnewsclient.extensions

import com.balex.fbnewsclient.domain.entity.FeedPost


fun MutableList<FeedPost>.getItemById(id: String):  FeedPost{
    var item = FeedPost(id = "", publicationDate = "", contentText = "")
        for (element in this) {
            if (element.id == id) item = element
        }
    return item

}