package net.wisecase2.stutterfix.mixin.server;

import net.minecraft.server.MinecraftServer;
import net.wisecase2.stutterfix.StutterFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "runServer()V", at = @At(value = "INVOKE", target = "net/minecraft/util/Util.getMeasuringTimeMs ()J"))
    private void loadThread(CallbackInfo ci) {
        StutterFix.loadServerThread(Thread.currentThread());
        StutterFix.configPriorityServerThread();
    }

    @Inject(method = "runServer()V", at = @At(value = "INVOKE", target = "net/minecraft/server/MinecraftServer.shutdown ()V"))
    private void shutdownThread(CallbackInfo ci) {
        StutterFix.isInitializedServerThread = false;
        StutterFix.serverThread = null;
    }
}
