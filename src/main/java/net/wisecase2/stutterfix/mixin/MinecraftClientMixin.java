package net.wisecase2.stutterfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(net.minecraft.client.MinecraftClient.class)
public class MinecraftClientMixin {

    @ModifyArg(method = "run()V", at = @At(value = "INVOKE", target = "java/lang/Thread.setPriority (I)V"), index = 0)
    private int setMinecraftClientPriority(int p) {
        return 10;
    }

}
