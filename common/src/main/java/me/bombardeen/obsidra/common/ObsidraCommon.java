package me.bombardeen.obsidra.common;

import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;
import lombok.Getter;
import me.bombardeen.obsidra.common.nats.NatsHandler;

import java.io.IOException;

/**
 * This code was made by jsexp, in case of any unauthorized
 * use, at least please leave credits.
 * Find more about me @ my <a href="https://github.com/hardcorefactions">GitHub</a> :D
 * © 2025 - jsexp
 */
@Getter
public class ObsidraCommon {
    private final NatsHandler natsHandler;

    public ObsidraCommon(Options natsOptions) throws IOException, InterruptedException {
        Connection connection = Nats.connect(natsOptions);

        this.natsHandler = new NatsHandler(connection);
    }

    public void destroy() {
        if (natsHandler != null) natsHandler.close();
    }

}