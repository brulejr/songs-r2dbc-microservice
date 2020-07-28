package io.jrb.common.repository

import io.jrb.common.model.Entity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import java.util.UUID

interface EntityRepository<ENTITY : Entity> : ReactiveCrudRepository<ENTITY, Long> {

    fun findByGuid(guid: UUID): Mono<ENTITY>

}
