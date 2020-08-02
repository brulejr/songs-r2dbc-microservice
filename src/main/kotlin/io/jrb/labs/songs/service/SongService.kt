package io.jrb.labs.songs.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import io.jrb.common.service.CrudService
import io.jrb.labs.songs.model.SongEntity
import io.jrb.labs.songs.model.SongValueEntity
import io.jrb.labs.songs.model.SongValueType
import io.jrb.labs.songs.repository.SongEntityRepository
import io.jrb.labs.songs.repository.SongValueEntityRepository
import io.jrb.labs.songs.resource.Song
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class SongService(
        val songEntityRepository: SongEntityRepository,
        val songValueEntityRepository: SongValueEntityRepository,
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
                .flatMap { songEntity ->
                    val songId = songEntity.id!!
                    Mono.zip(
                        Mono.just(songEntity),
                        createSongValues(SongValueType.ADDITIONAL_TITLE, song.additionalTitles, songId),
                        createSongValues(SongValueType.AUTHOR, song.authors, songId),
                        createSongValues(SongValueType.THEME, song.themes, songId)
                    )}
                .map { tuple ->
                    Song.Builder(tuple.t1)
                            .additionalTitles(tuple.t2)
                            .authors(tuple.t3)
                            .themes(tuple.t4)
                            .build()
                }
    }

    @Transactional
    fun deleteSong(guid: UUID): Mono<Void> {
        return super.findEntityByGuid(guid)
                .flatMap { songEntity -> songValueEntityRepository.deleteBySongId(songEntity.id!!) }
                .then(super.delete(guid))
    }

    @Transactional
    fun findSongByGuid(guid: UUID): Mono<Song> {
        return super.findEntityByGuid(guid)
                .zipWhen { songEntity -> findSongValueList(songEntity.id!!) }
                .map { tuple ->
                    val builder = Song.Builder(tuple.t1)
                    tuple.t2.forEach { songValue ->
                        val value = songValue.songValue
                        when (songValue.songValueType) {
                            SongValueType.ADDITIONAL_TITLE -> builder.addAdditionalTitle(value)
                            SongValueType.AUTHOR -> builder.addAuthor(value)
                            SongValueType.THEME -> builder.addTheme(value)
                            else -> { }
                        }
                    }
                    builder.build()
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

    private fun createSongValues(type: SongValueType, values: List<String>, songId: Long): Mono<List<String>> {
        return Flux.fromIterable(values)
                .map { value -> SongValueEntity(null, songId, value, type) }
                .flatMap { songValueEntityRepository.save(it) }
                .map(SongValueEntity::songValue)
                .collectList()
    }

    private fun findSongValueList(songId: Long): Mono<List<SongValueEntity>> {
        return songValueEntityRepository.findBySongId(songId)
                .collectList()
    }

}
