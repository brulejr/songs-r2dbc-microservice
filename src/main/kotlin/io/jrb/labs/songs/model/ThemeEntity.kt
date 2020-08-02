package io.jrb.labs.songs.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(value = "t_theme")
data class ThemeEntity(

        @Id
        @Column(value = "them_id")
        val id: Long? = null,

        @Column(value = "them_song_id")
        val songId: Long,

        @Column(value = "theme")
        val theme: String

)
