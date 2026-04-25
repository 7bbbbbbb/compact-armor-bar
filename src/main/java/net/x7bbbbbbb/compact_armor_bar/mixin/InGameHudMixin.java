package net.x7bbbbbbb.compact_armor_bar.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.x7bbbbbbb.compact_armor_bar.CompactArmorBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Unique
    private static final Identifier ARMOR_FULL_TEXTURE = Identifier.ofVanilla("hud/armor_full");

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private static void redirectArmorRender(DrawContext context, PlayerEntity player, int i, int j, int k, int x, CallbackInfo ci) {
        int armorValue = player.getArmor();
        if (armorValue > 0) {
            MinecraftClient client = MinecraftClient.getInstance();
            TextRenderer textRenderer = client.textRenderer;

            int width = context.getScaledWindowWidth();
            int height = context.getScaledWindowHeight();

            int yOffset = CompactArmorBar.CONFIG.rowOffset() * 10;
            if (player.isSubmergedInWater() || player.getAir() < player.getMaxAir()) yOffset += 10;
            if (player.getVehicle() instanceof LivingEntity mount && mount.isAlive()) {
                if (mount.getMaxHealth() > 60) {
                    yOffset += 20;
                }
                else {
                    for (float mountOffset = mount.getMaxHealth(); mountOffset > 20; mountOffset -= 20) {
                        yOffset += 10;
                    }
                }
                if (CompactArmorBar.CONFIG.betterMountHUD()) yOffset += 10;
            } else if (player.getVehicle() != null) {
                Identifier vehicleId = Registries.ENTITY_TYPE.getId(player.getVehicle().getType());
                if ("immersive_aircraft".equals(vehicleId.getNamespace()) || "immersive_machinery".equals(vehicleId.getNamespace())) {
                    yOffset += 10;
                }
            }

            int rightEdge = (width / 2) + 90;
            int barWidth = 8;
            int spacing = 2;
            int iconWidth = 9;

            int barsX = rightEdge - barWidth;
            int iconX = barsX - spacing - iconWidth;
            int iconY = height - 49 - yOffset;

            EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

            for (int index = 0; index < slots.length; index++) {
                ItemStack stack = player.getEquippedStack(slots[index]);
                if (!stack.isEmpty() && stack.isDamageable()) {
                    int barY = iconY + (index * 2) + 1;

                    if (!stack.isDamaged()) {
                        context.fill(barsX, barY, barsX + barWidth, barY + 1, 0xFF00FF00);
                    } else {
                        int barColor = stack.getItemBarColor();
                        int step = stack.getItemBarStep();
                        if (step <= 1) {
                            if (client.world != null && (client.world.getTime() / 10) % 2 == 0) {
                                continue;
                            }
                        }
                        context.fill(barsX, barY, barsX + barWidth, barY + 1, 0xFF000000);

                        int scaledStep = (int) Math.round((step / 13.0) * barWidth);
                        context.fill(barsX, barY, barsX + scaledStep, barY + 1, 0xFF000000 | barColor);
                    }
                }
            }

            context.drawGuiTexture(ARMOR_FULL_TEXTURE, iconX, iconY, iconWidth, 9);

            String text = String.valueOf(armorValue);
            int textWidth = textRenderer.getWidth(text);
            int textX = iconX - textWidth - 2;
            int textY = iconY + 1;
            context.drawTextWithShadow(textRenderer, text, textX, textY, 0xFFFFFF);
        }
        ci.cancel();
    }
}