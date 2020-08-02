package io.jrb.labs.songs.resource

import com.fasterxml.jackson.annotation.JsonInclude
import io.jrb.common.resource.Resource
import io.jrb.common.resource.ResourceBuilder
import io.jrb.labs.songs.model.SongEntity
import java.time.Instant
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Song(
        override val guid: UUID? = null,
        val title: String,
        val authors: List<String> = listOf(),
        val additionalTitles: List<String> = listOf(),
        val themes: List<String> = listOf(),
        val sourceId: String?,
        val sourceSystem: String?,
        override val createdOn: Instant?,
        override val createdBy: String?,
        override val modifiedOn: Instant?,
        override val modifiedBy: String?
) : Resource {

    data class Builder(
            private var title: String? = null,
            private var authors: MutableList<String> = mutableListOf(),
            private var additionalTitles: MutableList<String> = mutableListOf(),
            private var themes: MutableList<String> = mutableListOf(),
            private var sourceId: String? = null,
            private var sourceSystem: String? = null
    ) : ResourceBuilder<Song, SongEntity>() {

        constructor(songEntity: SongEntity): this() {
            this.guid = songEntity.guid
            this.title = songEntity.title
            this.sourceId = songEntity.sourceId
            this.sourceSystem = songEntity.sourceSystem
            this.createdOn = songEntity.createdOn
            this.createdBy = songEntity.createdBy
            this.modifiedOn = songEntity.modifiedOn
            this.modifiedBy = songEntity.modifiedBy
        }

        fun title(title: String?) = apply { this.title = title }
        fun authors(authors: List<String>) = apply { this.authors = authors.toMutableList() }
        fun additionalTitles(additionalTitles: List<String>) = apply { this.additionalTitles = additionalTitles.toMutableList() }
        fun themes(themes: List<String>) = apply { this.themes = themes.toMutableList() }
        fun sourceId(sourceId: String?) = apply { this.sourceId = sourceId }
        fun sourceSystem(sourceSystem: String?) = apply { this.sourceSystem = sourceSystem }

        fun addAuthor(author: String) = apply { this.authors.add(author) }
        fun addAdditionalTitle(additionalTitle: String) = apply { this.additionalTitles.add(additionalTitle) }
        fun addTheme(theme: String) = apply { this.themes.add(theme) }

        override fun build() = Song(
                guid = this.guid,
                title = this.title!!,
                authors = this.authors,
                additionalTitles = this.additionalTitles,
                themes = this.themes,
                sourceId = this.sourceId,
                sourceSystem = this.sourceSystem,
                createdOn = this.createdOn,
                createdBy = this.createdBy,
                modifiedOn = this.modifiedOn,
                modifiedBy = this.modifiedBy
        )
    }

}
