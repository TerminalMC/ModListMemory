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

import com.terraformersmc.modmenu.util.mod.Mod;
import com.terraformersmc.modmenu.util.mod.ModBadgeRenderer;
import dev.terminalmc.modlistmemory.ModListMemory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.terminalmc.modlistmemory.config.Config.options;

@Mixin(ModBadgeRenderer.class)
public abstract class MixinModBadgeRenderer {
    @Shadow
    public abstract void drawBadge(GuiGraphics graphics, FormattedCharSequence text, int outlineColor, int fillColor, int textColor);

    @Shadow
    protected Mod mod;

    @Inject(
            method = "draw",
            at = @At("TAIL")
    )
    private void afterDraw(GuiGraphics graphics, CallbackInfo ci) {
        if (options().showBadges) {
            if (ModListMemory.pinnedBadgeText != null
                    && options().pinnedMods.contains(mod.getId())) {
                drawBadge(graphics, ModListMemory.pinnedBadgeText,
                        ModListMemory.PINNED_BADGE_OUTLINE, ModListMemory.PINNED_BADGE_FILL,
                        ModListMemory.BADGE_TEXT);
            }
            else if (ModListMemory.recentBadgeText != null
                    && options().recentMods.contains(mod.getId())) {
                drawBadge(graphics, ModListMemory.recentBadgeText,
                        ModListMemory.RECENT_BADGE_OUTLINE, ModListMemory.RECENT_BADGE_FILL,
                        ModListMemory.BADGE_TEXT);
            }
        }
    }
}
