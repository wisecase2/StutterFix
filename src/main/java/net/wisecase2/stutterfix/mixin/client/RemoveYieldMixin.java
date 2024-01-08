package net.wisecase2.stutterfix.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.wisecase2.stutterfix.StutterFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public class RemoveYieldMixin {
    @Redirect(method = "render(Z)V", at = @At(value = "INVOKE", target = "java/lang/Thread.yield ()V"))
    private void removeYield(){
        if(!StutterFix.threadconfig.renderRemoveYield){
            Thread.yield();
        }
    }
}

