package me.bombardeen.obsidra.paper;

import io.nats.client.Options;
import me.bombardeen.obsidra.common.ObsidraCommon;
import me.bombardeen.obsidra.common.enums.InstanceTypeEnum;
import me.bombardeen.obsidra.common.instance.InstanceProperties;
import me.bombardeen.obsidra.common.nats.packet.impl.InstanceStartPacket;
import me.bombardeen.obsidra.common.nats.packet.impl.InstanceStopPacket;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.UUID;

/**
 * This code was made by jsexp, in case of any unauthorized
 * use, at least please leave credits.
 * Find more about me @ my <a href="https://github.com/hardcorefactions">GitHub</a> :D
 * © 2025 - jsexp
 */
public class ObsidraPaperImpl extends JavaPlugin {

    private InstanceProperties properties;
    private ObsidraCommon common;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        try {

            this.common = new ObsidraCommon(Options.builder()
                    .server("nats://localhost:4222")
                    .build());

            getLogger().info("Obsidra common has loaded successfully.");
        } catch (IOException | InterruptedException e) {
            getLogger().severe("Exception ocurred whilst loading obsidra common.");
            throw new RuntimeException(e);
        }

        String ip = Bukkit.getServer().getIp().isEmpty() ? "0.0.0.0" : Bukkit.getServer().getIp();
        int port = Bukkit.getServer().getPort();

        // Check for INSTANCE-TYPE in runtime arguments
        String instanceTypeArg = System.getProperty("INSTANCE-TYPE");
        InstanceTypeEnum instanceType;
        if (instanceTypeArg != null && !instanceTypeArg.isEmpty()) {
            try {
                instanceType = InstanceTypeEnum.valueOf(instanceTypeArg);
                getLogger().info("Using INSTANCE-TYPE from runtime arguments: " + instanceType);
            } catch (IllegalArgumentException e) {
                getLogger().severe("Invalid INSTANCE-TYPE provided in runtime arguments. Falling back to config.");
                instanceType = InstanceTypeEnum.valueOf(getConfig().getString("CONFIG.INSTANCE-TYPE"));
            }
        } else {
            instanceType = InstanceTypeEnum.valueOf(getConfig().getString("CONFIG.INSTANCE-TYPE"));
        }

        this.properties = new InstanceProperties(
                UUID.randomUUID(),
                instanceType,
                ip,
                port
        );

        common.getNatsHandler().sendPacket(new InstanceStartPacket(properties));
        getLogger().info("Obsidra paper integration has been successfully enabled.");
    }

    @Override
    public void onDisable() {
        common.getNatsHandler().sendPacket(new InstanceStopPacket(properties));
        getLogger().info("Obsidra paper integration has been successfully disabled.");
    }
}
