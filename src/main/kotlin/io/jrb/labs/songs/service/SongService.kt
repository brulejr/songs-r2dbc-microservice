package io.jrb.labs.songs.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import io.jrb.common.service.CrudService
import io.jrb.labs.songs.model.AuthorEntity
import io.jrb.labs.songs.model.SongEntity
import io.jrb.labs.songs.model.ThemeEntity
import io.jrb.labs.songs.repository.AuthorEntityRepository
import io.jrb.labs.songs.repository.SongEntityRepository
import io.jrb.labs.songs.repository.ThemeEntityRepository
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
        val themeEntityRepository: ThemeEntityRepository,
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
                .flatMap { songEntity -> Mono.zip(
                        Mono.just(songEntity),
                        createAuthors(song, songEntity.id!!),
                        createThemes(song, songEntity.id)
                )}
                .map { tuple ->
                    Song.Builder(tuple.t1)
                            .authors(tuple.t2)
                            .themes(tuple.t3)
                            .build()
                }
    }

    @Transactional
    fun deleteSong(guid: UUID): Mono<Void> {
        return super.findEntityByGuid(guid)
                .flatMap { songEntity -> Mono.zip(
                        authorEntityRepository.deleteBySongId(songEntity.id!!),
                        themeEntityRepository.deleteBySongId(songEntity.id)
                )}
                .then(super.delete(guid))
    }

    @Transactional
    fun findSongByGuid(guid: UUID): Mono<Song> {
        return super.findEntityByGuid(guid)
                .flatMap { songEntity -> Mono.zip(
                        Mono.just(songEntity),
                        findAuthorList(songEntity.id!!),
                        findThemeList(songEntity.id)
                )}
                .map { tuple ->
                    Song.Builder(tuple.t1)
                            .authors(tuple.t2)
                            .themes(tuple.t3)
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

    private fun createAuthors(song: Song, songId: Long): Mono<List<String>> {
        return Flux.fromIterable(song.authors)
                .map { author -> AuthorEntity(null, songId, author) }
                .flatMap { authorEntityRepository.save(it) }
                .map(AuthorEntity::author)
                .collectList()
    }

    private fun createThemes(song: Song, songId: Long): Mono<List<String>> {
        return Flux.fromIterable(song.themes)
                .map { theme -> ThemeEntity(null, songId, theme) }
                .flatMap { themeEntityRepository.save(it) }
                .map(ThemeEntity::theme)
                .collectList()
    }

    private fun findAuthorList(songId: Long): Mono<List<String>> {
        return authorEntityRepository.findBySongId(songId)
                .map(AuthorEntity::author)
                .collectList()
    }

    private fun findThemeList(songId: Long): Mono<List<String>> {
        return themeEntityRepository.findBySongId(songId)
                .map(ThemeEntity::theme)
                .collectList()
    }

}
