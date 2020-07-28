package io.jrb.labs.songs.repository

import io.jrb.common.repository.EntityRepository
import io.jrb.labs.songs.model.SongEntity
import org.springframework.stereotype.Repository

@Repository
interface SongEntityRepository : EntityRepository<SongEntity>
