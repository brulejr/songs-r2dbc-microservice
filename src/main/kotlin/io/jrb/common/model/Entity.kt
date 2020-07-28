package io.jrb.common.model

import java.time.Instant
import java.util.UUID

interface Entity {
    val id: Long?
    val guid: UUID?
    val createdOn: Instant?
    val createdBy: String?
    val modifiedOn: Instant?
    val modifiedBy: String?
}
