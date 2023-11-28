package net.wisecase2.stutterfix.mixin;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.client.main.Main.class)
public class ClientMainMixin {
    @Inject(method = "main([Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "java/lang/Thread.setName (Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private static void setMaxPriorityRender(CallbackInfoReturnable ci) {
        Thread.currentThread().setPriority(10);
    }

    @Inject(method = "main([Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "net/minecraft/client/MinecraftClient.isRunning ()Z", shift = At.Shift.AFTER))
    private static void setThreadYield(CallbackInfoReturnable ci) {
        Thread.yield();
    }
}
