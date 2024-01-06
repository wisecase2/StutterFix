package net.wisecase2.stutterfix.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.wisecase2.stutterfix.StutterFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "run()V", at = @At(value = "INVOKE", target = "net/minecraft/util/TickDurationMonitor.create (Ljava/lang/String;)Lnet/minecraft/util/TickDurationMonitor;"))
    private void loadThread(CallbackInfo ci) {
        if(!StutterFix.isClientInitialized) {
            StutterFix.loadRenderThread(Thread.currentThread());
            StutterFix.configPriorityRenderThread();
            StutterFix.isClientInitialized = true;
        }
    }

}
