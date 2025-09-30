package me.bombardeen.obsidra.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import me.bombardeen.obsidra.common.instance.InstanceProperties;
import me.bombardeen.obsidra.common.nats.annotation.IncomingPacketHandler;
import me.bombardeen.obsidra.common.nats.packet.PacketListener;
import me.bombardeen.obsidra.common.nats.packet.impl.InstanceStartPacket;
import me.bombardeen.obsidra.common.nats.packet.impl.InstanceStopPacket;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * This code was made by jsexp, in case of any unauthorized
 * use, at least please leave credits.
 * Find more about me @ my <a href="https://github.com/hardcorefactions">GitHub</a> :D
 * © 2025 - jsexp
 */
public class VelocityPacketListener implements PacketListener {
    private final Map<InstanceProperties, ServerInfo> instanceToServer = new ConcurrentHashMap<>();

    private final ProxyServer proxyServer;
    private final Logger logger;

    public VelocityPacketListener(ProxyServer proxyServer, Logger logger) {
        this.proxyServer = proxyServer;
        this.logger = logger;
    }

    @IncomingPacketHandler
    public void onInstanceStart(final InstanceStartPacket packet) {
        ServerInfo serverInfo = new ServerInfo(
                packet.instance().getInstanceType().getMinimal() + "-" + packet.instance().getUniqueId().toString().split("-")[0],
                new InetSocketAddress(packet.instance().getAddress(), packet.instance().getPort())
        );

        RegisteredServer server = proxyServer.registerServer(serverInfo);
        instanceToServer.put(packet.instance(), server.getServerInfo());
        logger.info("Registered a new instance with name " + server.getServerInfo().getName() + " on address " + formatCensoredAddress(server.getServerInfo().getAddress()) + ".");
    }

    @IncomingPacketHandler
    public void onInstanceStop(final InstanceStopPacket packet) {
        ServerInfo serverInfo = instanceToServer.remove(packet.instance());
        if (serverInfo != null) {
            proxyServer.unregisterServer(serverInfo);
            logger.info("Unregistered a instance with name " + serverInfo.getName() + " on address " + formatCensoredAddress(serverInfo.getAddress()) + ".");
            return;
        }

        logger.severe("Failed to unregister the following instance: " + packet.instance());
    }

    public static String formatCensoredAddress(InetSocketAddress address) {
        String ip = address.getHostString();
        int port = address.getPort();

        String[] parts = ip.split("\\.");

        if (parts.length == 4) {
            return parts[0] + "." + parts[1] + ".*.*:" + port;
        }

        return ip + ":" + port;
    }

}
