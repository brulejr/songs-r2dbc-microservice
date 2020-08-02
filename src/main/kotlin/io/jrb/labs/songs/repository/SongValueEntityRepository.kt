package io.jrb.labs.songs.repository

import io.jrb.labs.songs.model.SongValueEntity
import io.jrb.labs.songs.model.SongValueType
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface SongValueEntityRepository : ReactiveCrudRepository<SongValueEntity, Long> {

    fun deleteBySongId(songId: Long): Mono<Void>

    fun findBySongId(songId: Long): Flux<SongValueEntity>

}
