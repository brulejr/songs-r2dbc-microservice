package io.jrb.labs.songs.config

import io.jrb.labs.songs.model.SongEntity
import io.jrb.labs.songs.resource.Song
import io.jrb.labs.songs.service.SongService
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.Arrays

class DemoInitializer(val songService: SongService) : ApplicationListener<ApplicationReadyEvent> {

    private val log = KotlinLogging.logger {}

    override fun onApplicationEvent(event: ApplicationReadyEvent) {

        log.info("Creating Songs")
        log.info("--------------")
        Flux.fromIterable(Arrays.asList(
                Song.Builder().title("ABC").build(),
                Song.Builder().title("DEF").build(),
                Song.Builder().title("GHI").build()
        ))
                .flatMap { songService.createSong(it) }
                .blockLast(Duration.ofSeconds(10))

        // fetch all customers
        log.info("Songs found with findAll()")
        log.info("--------------------------")
        songService.listAllSongs()
                .doOnNext { song -> log.info(song.toString()) }
                .blockLast(Duration.ofSeconds(10))

    }

}
