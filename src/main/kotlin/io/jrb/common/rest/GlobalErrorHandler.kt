package io.jrb.common.rest

import com.google.common.base.VerifyException
import io.jrb.common.resource.ErrorResponseEntity
import io.jrb.common.service.DuplicateResourceException
import io.jrb.common.service.InvalidResourceException
import io.jrb.common.service.PatchInvalidException
import io.jrb.common.service.ResourceNotFoundException
import io.jrb.common.service.ServiceException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalErrorHandler {

    @ExceptionHandler(DuplicateResourceException::class)
    fun forumException(exception: DuplicateResourceException) = ErrorResponseEntity.conflict(exception.message)

    @ExceptionHandler(InvalidResourceException::class)
    fun forumException(exception: InvalidResourceException) = ErrorResponseEntity.badRequest(exception.message)

    @ExceptionHandler(PatchInvalidException::class)
    fun forumException(exception: PatchInvalidException) = ErrorResponseEntity.badRequest(exception.message)

    @ExceptionHandler(ResourceNotFoundException::class)
    fun forumException(exception: ResourceNotFoundException) = ErrorResponseEntity.notFound(exception.message)

    @ExceptionHandler(ServiceException::class)
    fun forumException(exception: ServiceException) = ErrorResponseEntity.serverError(exception.message)

    @ExceptionHandler(VerifyException::class)
    fun forumException(exception: VerifyException) = ErrorResponseEntity.badRequest(exception.message)

}
