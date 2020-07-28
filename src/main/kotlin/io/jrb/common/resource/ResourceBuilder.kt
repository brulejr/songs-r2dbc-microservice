package io.jrb.common.resource

import io.jrb.common.model.Entity
import java.time.Instant
import java.util.UUID

abstract class ResourceBuilder<RESOURCE: Resource, ENTITY: Entity>(
        protected var guid: UUID? = UUID.randomUUID(),
        protected var createdOn: Instant? = null,
        protected var createdBy: String? = null,
        protected var modifiedOn: Instant? = null,
        protected var modifiedBy: String? = null
) {

    constructor(entity: ENTITY): this()

    fun guid(guid: UUID?) : ResourceBuilder<RESOURCE, ENTITY> = apply { this.guid = guid }
    fun createdOn(createdOn: Instant?) : ResourceBuilder<RESOURCE, ENTITY> = apply { this.createdOn = createdOn }
    fun createdBy(createdBy: String?) : ResourceBuilder<RESOURCE, ENTITY> = apply { this.createdBy = createdBy }
    fun modifiedOn(modifiedOn: Instant?) : ResourceBuilder<RESOURCE, ENTITY> = apply { this.modifiedOn = modifiedOn }
    fun modifiedBy(modifiedBy: String?) : ResourceBuilder<RESOURCE, ENTITY> = apply { this.modifiedBy = modifiedBy }

    abstract fun build(): RESOURCE
}
