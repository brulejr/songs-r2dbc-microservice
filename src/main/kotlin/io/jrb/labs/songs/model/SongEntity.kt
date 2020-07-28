package io.jrb.labs.songs.model

import io.jrb.common.model.Entity
import io.jrb.common.model.EntityBuilder
import io.jrb.labs.songs.resource.Song
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table(value = "t_song")
data class SongEntity(

        @Id
        @Column(value = "song_id")
        override val id: Long? = null,

        @Column(value = "guid")
        override val guid: UUID? = null,

        @Column(value = "title")
        val title: String,

        @Column(value = "created_on")
        override val createdOn: Instant?,

        @Column(value = "created_by")
        override val createdBy: String?,

        @Column(value = "modified_on")
        override val modifiedOn: Instant?,

        @Column(value = "modified_by")
        override val modifiedBy: String?

) : Entity {

    data class Builder(
            private var title: String? = null
    ) : EntityBuilder<SongEntity, Song>() {
        constructor(song: Song) : this() {
            this.guid = song.guid
            this.title = song.title
            this.createdOn = song.createdOn
            this.createdBy = song.createdBy
            this.modifiedOn = song.modifiedOn
            this.modifiedBy = song.modifiedBy
        }

        constructor(songEntity: SongEntity) : this() {
            this.id = songEntity.id
            this.guid = songEntity.guid
            this.title = songEntity.title
            this.createdOn = songEntity.createdOn
            this.createdBy = songEntity.createdBy
            this.modifiedOn = songEntity.modifiedOn
            this.modifiedBy = songEntity.modifiedBy
        }

        fun title(title: String?) = apply { this.title = title }

        override fun build() = SongEntity(
                id = this.id,
                guid = this.guid,
                title = this.title!!,
                createdOn = this.createdOn,
                createdBy = this.createdBy,
                modifiedOn = this.modifiedOn,
                modifiedBy = this.modifiedBy
        )
    }

}
