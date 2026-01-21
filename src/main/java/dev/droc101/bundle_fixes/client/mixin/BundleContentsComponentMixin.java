package dev.droc101.bundle_fixes.client.mixin;

import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleContents.class)
public class BundleContentsComponentMixin {

    @Inject(method = "getNumberOfItemsToShow", at = @At("HEAD"), cancellable = true)
    public void getNumberOfStacksShown(CallbackInfoReturnable<Integer> cir) {
        BundleContents component = (BundleContents) (Object) this;
        cir.setReturnValue(component.size());
        cir.cancel();
    }

}
