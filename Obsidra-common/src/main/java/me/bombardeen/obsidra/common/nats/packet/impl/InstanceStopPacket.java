package me.bombardeen.obsidra.common.nats.packet.impl;

import me.bombardeen.obsidra.common.instance.InstanceProperties;
import me.bombardeen.obsidra.common.nats.packet.Packet;

/**
 * This code was made by jsexp, in case of any unauthorized
 * use, at least please leave credits.
 * Find more about me @ my <a href="https://github.com/hardcorefactions">GitHub</a> :D
 * © 2025 - jsexp
 */
public record InstanceStopPacket(InstanceProperties instance) implements Packet {}
