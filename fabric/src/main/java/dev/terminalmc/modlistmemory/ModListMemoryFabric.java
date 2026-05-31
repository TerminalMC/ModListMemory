/*
 * Copyright 2026 TerminalMC
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

import dev.terminalmc.modlistmemory.command.Commands;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.event.Event;

@SuppressWarnings("unused")
public class ModListMemoryFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register client commands
        ClientCommandRegistrationCallback.EVENT.register(Commands::register);

        // Register shutdown hook
        ClientLifecycleEvents.CLIENT_STOPPING.register(
                Event.DEFAULT_PHASE,
                ModListMemory::onClientShutdown
        );

        // Initialize client
        ModListMemory.init();
    }
}
