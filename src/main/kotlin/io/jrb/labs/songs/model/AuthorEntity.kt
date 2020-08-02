package io.jrb.labs.songs.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(value = "t_author")
data class AuthorEntity(

        @Id
        @Column(value = "auth_id")
        val id: Long? = null,

        @Column(value = "auth_song_id")
        val songId: Long,

        @Column(value = "author")
        val author: String

)
