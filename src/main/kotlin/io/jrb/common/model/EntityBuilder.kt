package io.jrb.common.model

import io.jrb.common.resource.Resource
import java.time.Instant
import java.util.UUID

abstract class EntityBuilder<ENTITY: Entity, RESOURCE: Resource>(
    protected var id: Long? = null,
    protected var guid: UUID? = null,
    protected var createdOn: Instant? = null,
    protected var createdBy: String? = null,
    protected var modifiedOn: Instant? = null,
    protected var modifiedBy: String? = null
) {
    constructor(resource: RESOURCE): this()

    fun id(id: Long?) : EntityBuilder<ENTITY, RESOURCE> = apply { this.id = id }
    fun guid(guid: UUID?) : EntityBuilder<ENTITY, RESOURCE> = apply { this.guid = guid }
    fun createdOn(createdOn: Instant?) : EntityBuilder<ENTITY, RESOURCE> = apply { this.createdOn = createdOn }
    fun createdBy(createdBy: String?) : EntityBuilder<ENTITY, RESOURCE> = apply { this.createdBy = createdBy }
    fun modifiedOn(modifiedOn: Instant?) : EntityBuilder<ENTITY, RESOURCE> = apply { this.modifiedOn = modifiedOn }
    fun modifiedBy(modifiedBy: String?) : EntityBuilder<ENTITY, RESOURCE> = apply { this.modifiedBy = modifiedBy }

    abstract fun build(): ENTITY
}
