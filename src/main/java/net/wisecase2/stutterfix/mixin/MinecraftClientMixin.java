package net.wisecase2.stutterfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(net.minecraft.client.MinecraftClient.class)
public class MinecraftClientMixin {

    @ModifyArg(method = "run()V", at = @At(value = "INVOKE", target = "java/lang/Thread.setPriority (I)V"), index = 0)
    private int setMinecraftClientPriority(int p) {
        int available_processors = Runtime.getRuntime().availableProcessors();
        return switch (available_processors) {
            case 1, 2, 3, 4 -> 5;
            case 5, 6 -> 6;
            case 7, 8 -> 7;
            default -> 8;
        };
    }

}
