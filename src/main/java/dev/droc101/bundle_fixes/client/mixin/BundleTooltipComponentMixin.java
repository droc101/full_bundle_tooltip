package dev.droc101.bundle_fixes.client.mixin;

import net.minecraft.client.gui.tooltip.BundleTooltipComponent;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleTooltipComponent.class)
public class BundleTooltipComponentMixin {

    @Shadow @Final public BundleContentsComponent bundleContents;
    @Unique
    private static final int SLOTS = 64;

    @Shadow @Final private static int SLOT_DIMENSION;
    @Shadow
    private static final int ROW_WIDTH = SLOT_DIMENSION * 4;

    /*
    For 0 - 16 stacks, show 4 columns
    For 17 - 36 stacks, show 6 columns
    For 37+ stacks, show 8 columns
     */
    @Unique
    private int getCols() {
        int size = this.bundleContents.size();
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
        return getCols() * SLOT_DIMENSION;
    }

    @ModifyConstant(method = "getNumVisibleSlots", constant = @Constant(intValue = 12))
    private int modifyMaxSlots(int _original) {
        return SLOTS;
    }

    @ModifyConstant(method = "getXMargin", constant = @Constant(intValue = 96))
    private int modifyXMargin(int original) {
        return getCols() * SLOT_DIMENSION;
    }

    @ModifyConstant(method = "drawProgressBar", constant = @Constant(intValue = 96))
    private int modifyProgressBarWidth(int original) {
        return getCols() * SLOT_DIMENSION;
    }

    @ModifyConstant(method = "drawProgressBar", constant = @Constant(intValue = 48))
    private int modifyProgressBarTextPos(int original) {
        return (getCols() * SLOT_DIMENSION) / 2;
    }

    @ModifyConstant(method = "getProgressBarFill", constant = @Constant(intValue = 94))
    private int modifyProgressBarFill(int original) {
        return (getCols() * SLOT_DIMENSION) - 2;
    }

    @ModifyConstant(method = "drawEmptyDescription", constant = @Constant(intValue = 96))
    private static int modifyEmptyDescriptionWidth(int original) {
        return ROW_WIDTH;
    }

    @ModifyConstant(method = "getDescriptionHeight", constant = @Constant(intValue = 96))
    private static int modifyDescriptionWidth(int original) {
        return ROW_WIDTH;
    }

    @ModifyConstant(method = "getRows", constant = @Constant(intValue = 4))
    private int modifyMaxCols(int _original) {
        return getCols();
    }

    @ModifyConstant(method="drawNonEmptyTooltip", constant = @Constant(intValue = 12))
    private int modifyMaxNonEmptyTooltipSlots(int _original) {
        return SLOTS;
    }

    @ModifyConstant(method="drawNonEmptyTooltip", constant = @Constant(intValue = 4))
    private int modifyMaxNonEmptyTooltipCols(int _original) {
        return getCols();
    }

    @ModifyConstant(method="drawNonEmptyTooltip", constant = @Constant(intValue = 96))
    private int modifyMaxNonEmptyTooltipWidth(int _original) {
        return (getCols() * SLOT_DIMENSION);
    }

    @Inject(method = "getProgressBarLabel", at = @At("HEAD"), cancellable = true)
    private void getProgressBarLabel(CallbackInfoReturnable<Text> cir) {
        cir.setReturnValue(Text.of(String.format("%d/64", (int)(bundleContents.getOccupancy().doubleValue() * 64.0))));
        cir.cancel();
    }

}
