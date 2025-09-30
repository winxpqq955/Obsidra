package me.bombardeen.obsidra.common.nats;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import io.nats.client.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.bombardeen.obsidra.common.nats.annotation.IncomingPacketHandler;
import me.bombardeen.obsidra.common.nats.packet.Packet;
import me.bombardeen.obsidra.common.nats.packet.PacketListener;

import java.io.Closeable;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class NatsHandler implements Closeable {
    private static final Logger LOGGER = Logger.getLogger(NatsHandler.class.getName());

    private final Connection connection;

    private final Gson gson = new GsonBuilder().create();

    // Thread-safe multimaps
    private final Multimap<Object, Method> listeners =
            MultimapBuilder.hashKeys().arrayListValues().build();

    private final Set<String> channels = ConcurrentHashMap.newKeySet();
    private final Multimap<String, Object> channelToListener =
            MultimapBuilder.hashKeys().arrayListValues().build();

    private final List<Subscription> subscriptions =
            Collections.synchronizedList(new ArrayList<>());

    private volatile Dispatcher dispatcher;

    public synchronized void subscribe() {
        if (channels.isEmpty()) {
            throw new IllegalStateException("No channels to subscribe to. Register listeners first.");
        }
        try {
            dispatcher = connection.createDispatcher(this::handleMessage);
            for (String channel : channels) {
                dispatcher.subscribe(channel); // no add to list
            }
            LOGGER.info("Subscribed to channels: " + channels);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Subscription failed", e);
        }
    }

    public void registerListener(PacketListener listener) {
        Set<Method> packetHandlerMethods =
                Arrays.stream(listener.getClass().getDeclaredMethods())
                        .filter(m -> m.isAnnotationPresent(IncomingPacketHandler.class))
                        .filter(m -> m.getParameterCount() == 1)
                        .collect(Collectors.toSet());

        if (packetHandlerMethods.isEmpty()) {
            LOGGER.warning("No valid @IncomingPacketHandler in " + listener.getClass().getName());
            return;
        }

        for (Method method : packetHandlerMethods) {
            Class<?> parameterType = method.getParameterTypes()[0];
            String channel = "obsidra@" + parameterType.getName();
            channels.add(channel);

            synchronized (this) {
                listeners.put(listener, method);
                channelToListener.put(channel, listener);
            }

            try {
                method.setAccessible(true);
            } catch (Exception ignored) {
                // not critical on Java 17 modules
            }
        }

        LOGGER.info("Registered listener: " + listener.getClass().getName());
    }

    public void sendPacket(Packet packet) {
        String channel = "obsidra@" + packet.getClass().getName();
        try {
            connection.publish(channel, gson.toJson(packet).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to send packet to channel: " + channel, e);
        }
    }

    private void handleMessage(Message message) {
        String channel = message.getSubject();
        String data = new String(message.getData(), StandardCharsets.UTF_8);

        Collection<Object> listenerObjs;
        synchronized (this) {
            listenerObjs = new ArrayList<>(channelToListener.get(channel));
        }

        for (Object listenerObj : listenerObjs) {
            Collection<Method> methods;
            synchronized (this) {
                methods = new ArrayList<>(listeners.get(listenerObj));
            }

            for (Method method : methods) {
                if (!method.isAnnotationPresent(IncomingPacketHandler.class)) continue;

                Class<?> paramType = method.getParameterTypes()[0];
                String expectedChannel = "obsidra@" + paramType.getName();
                if (!expectedChannel.equals(channel)) continue;

                try {
                    Object packet = gson.fromJson(data, paramType);
                    // Already on a non-IO thread; direct call is fine
                    method.invoke(listenerObj, packet);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE,
                            "Failed to invoke handler " + method.getName() + " on " +
                                    listenerObj.getClass().getName(), e);
                }
            }
        }
    }

    @Override
    @SneakyThrows
    public synchronized void close() {
        if (dispatcher != null) {
            for (String channel : channels) {
                dispatcher.unsubscribe(channel);
            }
        }
        connection.close();
        LOGGER.info("NATS connection closed.");
    }
}