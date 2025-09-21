package me.bombardeen.obsidra.common.instance;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.bombardeen.obsidra.common.enums.InstanceTypeEnum;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * This code was made by jsexp, in case of any unauthorized
 * use, at least please leave credits.
 * Find more about me @ my <a href="https://github.com/hardcorefactions">GitHub</a> :D
 * © 2025 - jsexp
 */
@Data
@AllArgsConstructor
public class InstanceProperties {
    private UUID uniqueId;
    private InstanceTypeEnum instanceType;
    private String address;
    private int port;
}
