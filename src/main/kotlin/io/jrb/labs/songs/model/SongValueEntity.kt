package io.jrb.labs.songs.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(value = "t_song_value")
data class SongValueEntity(

        @Id
        @Column(value = "sval_id")
        val id: Long? = null,

        @Column(value = "sval_song_id")
        val songId: Long,

        @Column(value = "song_value")
        val songValue: String,

        @Column(value = "song_value_type")
        val songValueType: SongValueType

)
