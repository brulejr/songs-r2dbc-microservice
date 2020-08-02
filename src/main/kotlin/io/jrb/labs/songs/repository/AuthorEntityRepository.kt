package io.jrb.labs.songs.repository

import io.jrb.labs.songs.model.AuthorEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
interface AuthorEntityRepository : ReactiveCrudRepository<AuthorEntity, Long> {

    fun deleteBySongId(songId: Long): Mono<Void>

    fun findAllBySongId(songId: Long): Flux<AuthorEntity>

}
