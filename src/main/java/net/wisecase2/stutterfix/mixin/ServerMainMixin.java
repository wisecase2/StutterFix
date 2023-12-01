package net.wisecase2.stutterfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.server.Main.class)
public class ServerMainMixin {
    @Inject(method = "main", at = @At("HEAD"))
    private static void ServerMain(CallbackInfo ci) {
        Thread.currentThread().setPriority(10);
    }
}
