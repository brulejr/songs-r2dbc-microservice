package io.jrb.common.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import com.github.fge.jsonpatch.JsonPatchException
import io.jrb.common.model.Entity
import io.jrb.common.model.EntityBuilder
import io.jrb.common.repository.EntityRepository
import io.jrb.common.resource.Resource
import io.jrb.common.resource.ResourceBuilder
import mu.KotlinLogging
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.UUID

abstract class CrudService<ENTITY: Entity, RESOURCE: Resource>(
        private val entityRepository: EntityRepository<ENTITY>,
        private val entityName: String,
        private val entityClass: Class<ENTITY>,
        private val entityBuilderClass: Class<out EntityBuilder<ENTITY, RESOURCE>>,
        private val resourceClass: Class<RESOURCE>,
        private val resourceBuilderClass: Class<out ResourceBuilder<RESOURCE, ENTITY>>,
        private val jacksonObjectMapper: ObjectMapper
) {

    private val log = KotlinLogging.logger {}

    protected fun create(resource: RESOURCE): Mono<RESOURCE> {
        return createEntity(resource)
                .map { toResource(it) }
    }

    protected fun createEntity(resource: RESOURCE): Mono<ENTITY> {
        return Mono.just(resource)
            .map {
                val timestamp: Instant = Instant.now()
                createEntityBuilder(it)
                    .guid(UUID.randomUUID())
                    .createdOn(timestamp)
                    .modifiedOn(timestamp)
                    .build()
            }
            .flatMap { entityRepository.save(it) }
            .onErrorResume(serviceErrorHandler("Unexpected error when creating $entityName"))
    }

    protected fun delete(guid: UUID): Mono<Void> {
        return entityRepository.findByGuid(guid)
            .switchIfEmpty(Mono.error(ResourceNotFoundException(entityName, guid)))
            .flatMap { entityRepository.delete(it) }
            .onErrorResume(serviceErrorHandler("Unexpected error when deleting $entityName"))
    }

    protected fun findByGuid(guid: UUID): Mono<RESOURCE> {
        return findEntityByGuid(guid)
            .map { createResourceBuilder(it).build() }
    }

    protected fun findEntityByGuid(guid: UUID): Mono<ENTITY> {
        return entityRepository.findByGuid(guid)
                .switchIfEmpty(Mono.error(ResourceNotFoundException(entityName, guid)))
                .onErrorResume(serviceErrorHandler("Unexpected error when finding $entityName"))
    }

    protected fun listAll(): Flux<RESOURCE> {
        return listAllEntities()
            .map { createResourceBuilder(it).build() }
    }

    protected fun listAllEntities(): Flux<ENTITY> {
        return entityRepository.findAll()
                .onErrorResume(serviceErrorHandler("Unexpected error when retrieving $entityName"))
    }

    protected fun update(guid: UUID, patch: JsonPatch): Mono<RESOURCE> {
        return entityRepository.findByGuid(guid)
            .map { entity : ENTITY ->
                val resource : RESOURCE = createResourceBuilder(entity).build()
                val updatedResource : RESOURCE = applyPatch(guid, resource, patch)
                createEntityBuilder(updatedResource)
                        .id(entity.id)
                        .guid(guid)
                        .createdOn(entity.createdOn)
                        .modifiedOn(Instant.now())
                        .build()
            }
            .flatMap { entityRepository.save(it) }
            .map { createResourceBuilder(it).build() }
            .onErrorResume(serviceErrorHandler("Unexpected error when updating $entityName"))
    }

    protected fun createEntityBuilder(resource: RESOURCE): EntityBuilder<ENTITY, RESOURCE> {
        return entityBuilderClass.getConstructor(resourceClass).newInstance(resource)
    }

    protected fun createResourceBuilder(entity: ENTITY): ResourceBuilder<RESOURCE, ENTITY> {
        return resourceBuilderClass.getConstructor(entityClass).newInstance(entity)
    }

    protected fun toResource(entity: ENTITY): RESOURCE {
        return createResourceBuilder(entity).build()
    }

    private fun applyPatch(guid: UUID, resource: RESOURCE, patch: JsonPatch): RESOURCE {
        try {
            val patched: JsonNode = patch.apply(jacksonObjectMapper.convertValue(resource, JsonNode::class.java))
            return jacksonObjectMapper.treeToValue(patched, resourceClass)
        } catch (e: JsonPatchException) {
            throw PatchInvalidException(guid, e.message)
        }
    }

    private fun <R> serviceErrorHandler(message: String): (Throwable) -> Mono<R> {
        return { e ->
            if (e !is ServiceException) {
                log.error(e.message, e)
            }
            Mono.error(if (e is ServiceException) e else ServiceException(message, e))
        }
    }

}
