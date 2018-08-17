package com.solarnet.demo.data.story

import android.arch.persistence.room.Ignore
import java.util.Date

class Story(
    var people : People,
    var quote : String?,
    var mediaType : Int = MEDIA_TYPE_NONE,
    var media : String?,
    var thumbnail : String?,
    var submitted : Date
) {
    companion object {
        const val MEDIA_TYPE_NONE = 0
        const val MEDIA_TYPE_PICTURE = 1
        const val MEDIA_TYPE_VIDEO = 2
    }

//    @Ignore
    constructor(people: People, quote: String?, submitted : Date) : this(
        people, quote, MEDIA_TYPE_NONE, null, null, submitted
    )
}