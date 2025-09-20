package me.bombardeen.obsidra.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This code was made by jsexp, in case of any unauthorized
 * use, at least please leave credits.
 * Find more about me @ my <a href="https://github.com/hardcorefactions">GitHub</a> :D
 * © 2025 - jsexp
 */
@Getter
@RequiredArgsConstructor
public enum InstanceTypeEnum {
    BEDWARS("BedWars", "bw"),
    SKYWARS("SkyWars", "sw"),
    LOBBY("Lobby", "l");

    private final String display, minimal;
    // This is just an example of the usage.
}
