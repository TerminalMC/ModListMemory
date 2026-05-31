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

package dev.terminalmc.modlistmemory.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.terminalmc.modlistmemory.ModListMemory;
import dev.terminalmc.modlistmemory.gui.screen.ConfigScreenProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;

import static net.minecraft.commands.Commands.literal;

public class Commands {

    private Commands() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    public static <S> void register(CommandDispatcher<S> dispatcher, CommandBuildContext buildCtx) {
        Minecraft mc = Minecraft.getInstance();
        //noinspection unchecked
        dispatcher.register((LiteralArgumentBuilder<S>) literal(ModListMemory.MOD_ID)
                .executes((ctx) -> {
                    mc.schedule(() -> mc.setScreen(ConfigScreenProvider.getConfigScreen(null)));
                    return Command.SINGLE_SUCCESS;
                })
        );
    }
}
