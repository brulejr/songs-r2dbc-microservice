package io.jrb.common.resource

import java.time.Instant
import java.util.UUID

interface Resource {
    val guid: UUID?
    val createdOn: Instant?
    val createdBy: String?
    val modifiedOn: Instant?
    val modifiedBy: String?
}
