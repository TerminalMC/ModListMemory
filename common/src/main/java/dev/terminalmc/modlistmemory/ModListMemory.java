/*
 * Copyright 2025 TerminalMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.terminalmc.modlistmemory;

import dev.terminalmc.modlistmemory.config.Config;
import dev.terminalmc.modlistmemory.util.ModLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import static dev.terminalmc.modlistmemory.config.Config.options;

public class ModListMemory {
    public static final String MOD_ID = "modlistmemory";
    public static final String MOD_NAME = "ModListMemory";
    public static final ModLogger LOG = new ModLogger(MOD_NAME);
    public static final int BADGE_TEXT = 0xFFCACACA;
    public static final int PINNED_BADGE_OUTLINE = 0xFFb36b19;
    public static final int PINNED_BADGE_FILL = 0xFF4d2e0b;
    public static final int RECENT_BADGE_OUTLINE = 0xFF1d9fb3;
    public static final int RECENT_BADGE_FILL = 0xFF0c444d;

    public static FormattedCharSequence pinnedBadgeText = Component.literal("I").getVisualOrderText();
    public static FormattedCharSequence recentBadgeText = Component.literal("R").getVisualOrderText();

    public static void init() {
        Config.getAndSave();

        if (!options().persistOnRestart) {
            options().recentMods.clear();
            options().scrollAmount = Config.Options.scrollAmountDefault;
        }
    }

    public static void onClientShutdown(Minecraft mc) {
        if (options().persistOnRestart) {
            Config.save();
        }
    }

    public static void onConfigSaved(Config config) {
        pinnedBadgeText = Component.literal(config.options.pinnedText).getVisualOrderText();
        recentBadgeText = Component.literal(config.options.recentText).getVisualOrderText();
    }

    public static void onModOpened(String modId) {
        if (options().pinnedMods.contains(modId)) return; // Ignore pinned mods
        // Move to the top of the recent list
        options().recentMods.remove(modId);
        options().recentMods.addLast(modId);
        while (options().recentMods.size() > options().recentModsSize) {
            options().recentMods.removeFirst();
        }
        if (options().persistOnRestart && options().saveOnUpdate) {
            Config.save();
        }
    }

    public static boolean onModClicked(MouseButtonEvent click, String modId) {
        if (ModListMemory.hasKeyDown(click, options().pinKey)) {
            // Pin, or move to the top of the pin list
            options().recentMods.remove(modId);
            options().pinnedMods.remove(modId);
            options().pinnedMods.addLast(modId);
            while (options().pinnedMods.size() > options().pinnedModsSize) {
                options().pinnedMods.removeFirst();
            }
            if (options().persistOnRestart && options().saveOnUpdate) {
                Config.save();
            }
            return true;
        }
        else if (ModListMemory.hasKeyDown(click, options().unpinKey)
                && options().pinnedMods.contains(modId)) {
            // Unpin
            options().pinnedMods.remove(modId);
            if (options().persistOnRestart && options().saveOnUpdate) {
                Config.save();
            }
            return true;
        }
        return false;
    }

    public static boolean hasKeyDown(MouseButtonEvent click, Config.Key key) {
        return switch(key) {
            case CONTROL -> click.hasControlDown();
            case ALT -> click.hasAltDown();
            case SHIFT -> click.hasShiftDown();
            case NONE -> false;
        };
    }
}