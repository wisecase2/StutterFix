package net.wisecase2.stutterfix.mixin;

import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Util;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "run()V", at = @At(value = "INVOKE", target = "java/lang/Runtime.availableProcessors ()I", shift = At.Shift.AFTER))
    private void setMinecraftClientPriority(CallbackInfo ci) {
        Thread.currentThread().setPriority(10);
    }

    @Redirect(method = "render(Z)V", at = @At(value = "INVOKE", target = "java/lang/Thread.yield ()V"))
    private void removeThreadYield() {
    }

}
