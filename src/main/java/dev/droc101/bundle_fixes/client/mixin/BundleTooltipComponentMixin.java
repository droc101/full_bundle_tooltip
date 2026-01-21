package dev.droc101.bundle_fixes.client.mixin;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientBundleTooltip.class)
public class BundleTooltipComponentMixin {

    @Shadow
    @Final
    private BundleContents contents;
    @Unique
    private static final int SLOTS = 64;

    @Shadow
    @Final
    private static int SLOT_SIZE;
    @Shadow
    private static final int GRID_WIDTH = SLOT_SIZE * 4;

    /*
    For 0 - 16 stacks, show 4 columns
    For 17 - 36 stacks, show 6 columns
    For 37+ stacks, show 8 columns
     */
    @Unique
    private int getCols() {
        int size = this.contents.size();
        if (size > 36) {
            return 8;
        } else if (size > 16) {
            return 6;
        } else {
            return 4;
        }
    }

    @ModifyConstant(method = "getWidth", constant = @Constant(intValue = 96))
    private int getWidth(int original) {
        return getCols() * SLOT_SIZE;
    }

    @ModifyConstant(method = "slotCount", constant = @Constant(intValue = 12))
    private int modifyMaxSlots(int _original) {
        return SLOTS;
    }

    @ModifyConstant(method = "getContentXOffset", constant = @Constant(intValue = 96))
    private int modifyXMargin(int original) {
        return getCols() * SLOT_SIZE;
    }

    @ModifyConstant(method = "drawProgressbar", constant = @Constant(intValue = 96))
    private int modifyProgressBarWidth(int original) {
        return getCols() * SLOT_SIZE;
    }

    @ModifyConstant(method = "drawProgressbar", constant = @Constant(intValue = 48))
    private int modifyProgressBarTextPos(int original) {
        return (getCols() * SLOT_SIZE) / 2;
    }

    @ModifyConstant(method = "getProgressBarFill", constant = @Constant(intValue = 94))
    private int modifyProgressBarFill(int original) {
        return (getCols() * SLOT_SIZE) - 2;
    }

    @ModifyConstant(method = "drawEmptyBundleDescriptionText", constant = @Constant(intValue = 96))
    private static int modifyEmptyDescriptionWidth(int original) {
        return GRID_WIDTH;
    }

    @ModifyConstant(method = "getEmptyBundleDescriptionTextHeight", constant = @Constant(intValue = 96))
    private static int modifyDescriptionWidth(int original) {
        return GRID_WIDTH;
    }

    @ModifyConstant(method = "gridSizeY", constant = @Constant(intValue = 4))
    private int modifyMaxCols(int _original) {
        return getCols();
    }

    @ModifyConstant(method = "renderBundleWithItemsTooltip", constant = @Constant(intValue = 12))
    private int modifyMaxNonEmptyTooltipSlots(int _original) {
        return SLOTS;
    }

    @ModifyConstant(method = "renderBundleWithItemsTooltip", constant = @Constant(intValue = 4))
    private int modifyMaxNonEmptyTooltipCols(int _original) {
        return getCols();
    }

    @ModifyConstant(method = "renderBundleWithItemsTooltip", constant = @Constant(intValue = 96))
    private int modifyMaxNonEmptyTooltipWidth(int _original) {
        return (getCols() * SLOT_SIZE);
    }

    @Inject(method = "getProgressBarFillText", at = @At("HEAD"), cancellable = true)
    private void getProgressBarLabel(CallbackInfoReturnable<Component> cir) {
        cir.setReturnValue(Component.literal(String.format("%d/64", (int) (contents.weight().doubleValue() * 64.0))));
        cir.cancel();
    }

}
