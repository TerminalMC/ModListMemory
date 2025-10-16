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

import dev.terminalmc.modlistmemory.gui.screen.ConfigScreenProvider;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.event.GameShuttingDownEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(ModListMemory.MOD_ID)
public class ModListMemoryForge {
    public ModListMemoryForge() {
        // Config screen
        //noinspection removal
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) -> ConfigScreenProvider.getConfigScreen(parent))
        );

        // Main initialization
        ModListMemory.init();
    }

    @Mod.EventBusSubscriber(modid = ModListMemory.MOD_ID, value = Dist.CLIENT)
    static class ClientEventHandler {
        // Shutdown event
        @SubscribeEvent
        public static void shutdownEvent(GameShuttingDownEvent event) {
            ModListMemory.onClientShutdown(Minecraft.getInstance());
        }
    }
}
