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

import com.terraformersmc.mod_menu.gui.widget.entries.ModListEntry;
import com.terraformersmc.mod_menu.util.mod.Mod;
import dev.terminalmc.modlistmemory.ModListMemory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ModListEntry.class, remap = false)
public class MixinModListEntry {
    @Shadow
    @Final
    public Mod mod;

    @Inject(
            method = "openConfig",
            at = @At("HEAD")
    )
    private void onOpenConfig(CallbackInfo ci) {
        ModListMemory.onModOpened(mod.getId());
    }

    @Inject(
            method = "mouseClicked",
            at = @At("HEAD")
    )
    private void onMouseClicked(double mouseX, double mouseY, int delta, CallbackInfoReturnable<Boolean> cir) {
        ModListMemory.onModClicked(mod.getId());
    }
}
