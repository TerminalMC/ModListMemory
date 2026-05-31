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

package dev.terminalmc.modlistmemory.mixin;

import com.terraformersmc.modmenu.gui.ModsScreen;
import com.terraformersmc.modmenu.gui.widget.ModListWidget;
import dev.terminalmc.modlistmemory.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.terminalmc.modlistmemory.config.Config.options;

@Mixin(ModsScreen.class)
public abstract class ModsScreenMixin {

    @Shadow
    private ModListWidget modList;

    @Inject(
            method = "init",
            at = @At("RETURN")
    )
    private void afterInit(CallbackInfo ci) {
        if (options().mode.equals(Config.Mode.REMEMBER_SCROLL)) {
            if (options().scrollAmount != Double.MIN_VALUE) {
                modList.setScrollAmount(options().scrollAmount);
            }
        }
    }
}
