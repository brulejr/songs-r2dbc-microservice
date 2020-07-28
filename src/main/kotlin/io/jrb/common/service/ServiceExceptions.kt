package io.jrb.common.service

import java.util.UUID

open class ServiceException(message: String, cause: Throwable?): Exception(message, cause)

class DuplicateResourceException(guid: UUID?, message: String?): ServiceException(
        message = "Duplicate resource - guid = [$guid], detail = $message",
        cause = null
)

class InvalidResourceException(guid: UUID?, message: String?): ServiceException(
        message = "Invalid resource - guid = [$guid], detail = $message",
        cause = null
)

class PatchInvalidException(guid: UUID?, message: String?): ServiceException(
        message = "Invalid patch for resource - guid = [$guid], detail = $message",
        cause = null
)

class ResourceNotFoundException(type: String, guid: UUID): ServiceException(
        message = "No $type resource can be found - guid = [$guid]",
        cause = null
)
