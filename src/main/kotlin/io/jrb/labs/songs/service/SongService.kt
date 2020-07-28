package io.jrb.labs.songs.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import io.jrb.common.service.CrudService
import io.jrb.labs.songs.model.SongEntity
import io.jrb.labs.songs.repository.SongEntityRepository
import io.jrb.labs.songs.resource.Song
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class SongService(
        val songEntityRepository: SongEntityRepository,
        val objectMapper: ObjectMapper
) : CrudService<SongEntity, Song>(
        songEntityRepository,
        "Song",
        SongEntity::class.java,
        SongEntity.Builder::class.java,
        Song::class.java,
        Song.Builder::class.java,
        objectMapper
) {

    @Transactional
    fun createSong(song: Song): Mono<Song> {
        return super.create(song)
    }

    @Transactional
    fun deleteSong(guid: UUID): Mono<Void> {
        return super.delete(guid)
    }

    @Transactional
    fun findSongByGuid(guid: UUID): Mono<Song> {
        return super.findByGuid(guid)
    }

    @Transactional
    fun listAllSongs(): Flux<Song> {
        return super.listAll()
    }

    @Transactional
    fun updateSong(guid: UUID, patch: JsonPatch): Mono<Song> {
        return super.update(guid, patch)
    }

}
