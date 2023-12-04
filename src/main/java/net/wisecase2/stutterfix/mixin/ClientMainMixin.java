package net.wisecase2.stutterfix.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.client.main.Main.class)
public class ClientMainMixin {

    @Inject(method = "main([Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "java/lang/Thread.setName (Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private static void setMaxPriorityClient(CallbackInfo ci) {
        Thread.currentThread().setPriority(10);
    }
}
