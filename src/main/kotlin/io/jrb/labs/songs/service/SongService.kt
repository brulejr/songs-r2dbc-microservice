package io.jrb.labs.songs.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import io.jrb.common.service.CrudService
import io.jrb.labs.songs.model.AuthorEntity
import io.jrb.labs.songs.model.SongEntity
import io.jrb.labs.songs.repository.AuthorEntityRepository
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
        val authorEntityRepository: AuthorEntityRepository,
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
        return super.createEntity(song)
                .zipWhen { songEntity -> createSongAuthors(song, songEntity.id!!) }
                .map { tuple ->
                    Song.Builder(tuple.t1)
                            .authors(tuple.t2)
                            .build()
                }
    }

    @Transactional
    fun deleteSong(guid: UUID): Mono<Void> {
        return super.findEntityByGuid(guid)
                .flatMap { songEntity -> authorEntityRepository.deleteBySongId(songEntity.id!!) }
                .then(super.delete(guid))
    }

    @Transactional
    fun findSongByGuid(guid: UUID): Mono<Song> {
        return super.findEntityByGuid(guid)
                .zipWhen { songEntity -> findSongAuthors(songEntity.id!!) }
                .map { tuple ->
                    Song.Builder(tuple.t1)
                            .authors(tuple.t2)
                            .build()
                }
    }

    @Transactional
    fun listAllSongs(): Flux<Song> {
        return super.listAll()
    }

    @Transactional
    fun updateSong(guid: UUID, patch: JsonPatch): Mono<Song> {
        return super.update(guid, patch)
    }

    private fun createSongAuthors(song: Song, songId: Long): Mono<List<String>> {
        return Flux.fromIterable(song.authors)
                .map { author -> AuthorEntity(null, songId, author) }
                .flatMap { authorEntityRepository.save(it) }
                .map(AuthorEntity::author)
                .collectList()
    }

    private fun findSongAuthors(songId: Long): Mono<List<String>> {
        return authorEntityRepository.findAllBySongId(songId)
                .map(AuthorEntity::author)
                .collectList()
    }

}
