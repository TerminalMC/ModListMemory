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

import java.util.ArrayList;
import java.util.List;

import static dev.terminalmc.modlistmemory.config.Config.options;

public class ModListMemory {
    public static final String MOD_ID = "modlistmemory";
    public static final String MOD_NAME = "ModListMemory";
    public static final ModLogger LOG = new ModLogger(MOD_NAME);

    public static List<String> recentMods = new ArrayList<>();
    public static double scrollAmount = Double.MIN_VALUE;

    public static void init() {
        Config.getAndSave();
        
        if (options().persistOnRestart) {
            recentMods.addAll(options().recentMods);
            scrollAmount = options().scrollAmount;
        }
    }

    public static void onClientShutdown(Minecraft mc) {
        if (options().persistOnRestart) {
            options().recentMods = recentMods;
            options().scrollAmount = scrollAmount;
            Config.save();
        }
    }
    
    public static void onConfigSaved(Config config) {
        // If you are maintaining caches based on config values, update them here.
    }

    public static void onModOpened(String modId) {
        recentMods.addLast(modId);
        while (recentMods.size() > options().memorySize) {
            recentMods.removeFirst();
        }
        if (options().persistOnRestart && options().saveOnUpdate) {
            Config.save();
        }
    }
}
