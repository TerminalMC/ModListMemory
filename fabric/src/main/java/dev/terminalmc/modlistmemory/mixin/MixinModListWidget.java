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

package dev.terminalmc.modlistmemory.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.terraformersmc.modmenu.gui.widget.ModListWidget;
import com.terraformersmc.modmenu.util.mod.Mod;
import dev.terminalmc.modlistmemory.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static dev.terminalmc.modlistmemory.config.Config.options;

@Mixin(value = ModListWidget.class, remap = false)
public class MixinModListWidget {
    @WrapOperation(
            method = "filter(Ljava/lang/String;ZZ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;sort(Ljava/util/Comparator;)V"
            )
    )
    private void wrapSort(List<Mod> modList, Comparator<? super Mod> comparator, Operation<Void> original) {
        original.call(modList, comparator);
        // Add recent mods to the top of the list
        if (options().mode.equals(Config.Mode.REMEMBER_RECENT)) {
            List<Mod> copy = new ArrayList<>(modList);
            modList.clear();
            Mod[] recentMods = new Mod[options().recentMods.size()];
            Mod[] pinnedMods = new Mod[options().pinnedMods.size()];
            for (Mod mod : copy) {
                int i = options().pinnedMods.indexOf(mod.getId());
                // If id is in pinned list, add to pinned mods array
                if (i != -1) {
                    pinnedMods[i] = mod;
                    continue;
                }
                // Else if id is in recent list, add to recent mods array
                i = options().recentMods.indexOf(mod.getId());
                if (i != -1) {
                    recentMods[i] = mod;
                    continue;
                }
                // Else, add to mod list
                modList.add(mod);
            }
            // Add recent mods (reverse order, on top of mod list)
            for (Mod mod : recentMods) {
                if (mod != null) {
                    modList.addFirst(mod);
                }
            }
            // Add pinned mods (reverse order, on top of recent mods)
            for (Mod mod : pinnedMods) {
                if (mod != null) {
                    modList.addFirst(mod);
                }
            }
        }
    }
}
