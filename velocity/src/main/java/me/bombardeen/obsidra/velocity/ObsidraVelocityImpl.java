package me.bombardeen.obsidra.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import me.bombardeen.obsidra.common.ObsidraCommon;
import me.bombardeen.obsidra.shaded.nats.client.Options;

import javax.inject.Inject;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * This code was made by jsexp, in case of any unauthorized
 * use, at least please leave credits.
 * Find more about me @ my <a href="https://github.com/hardcorefactions">GitHub</a> :D
 * © 2025 - jsexp
 */
@Plugin(
        id = "obsidra-velocity",
        name = "Obsidra",
        version = "1.0.0"
)
public class ObsidraVelocityImpl {

    private final ProxyServer server;
    private final Logger logger;
    private final ObsidraCommon common;

    @Inject
    public ObsidraVelocityImpl(ProxyServer proxyServer, Logger logger) {
        this.server = proxyServer;
        this.logger = logger;

        try {
            this.common = new ObsidraCommon(Options.builder()
                    .server("nats://localhost:4222")
                    .build());

            logger.info("Obsidra common has loaded successfully.");
        } catch (IOException | InterruptedException e) {
            logger.severe("Exception ocurred whilst loading obsidra common.");
            throw new RuntimeException(e);
        }
    }

    @Subscribe
    public void onProxyInit(final ProxyInitializeEvent event) {
        this.common.getNatsHandler().registerListener(new VelocityPacketListener(server, logger));
        this.common.getNatsHandler().subscribe();
    }

}