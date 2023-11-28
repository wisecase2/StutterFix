package net.wisecase2.stutterfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(net.minecraft.server.MinecraftServer.class)
public class MinecraftServerMixin {

    @ModifyArg(method = "startServer(Ljava/util/function/Function;)Lnet/minecraft/server/MinecraftServer;", at = @At(value = "INVOKE", target = "java/lang/Thread.setPriority (I)V"), index = 0)
    private static int setMinecraftServerPriority(int p) {
        int available_processors = Runtime.getRuntime().availableProcessors();
        return switch (available_processors) {
            case 1, 2, 3, 4 -> 5;
            case 5, 6 -> 6;
            case 7, 8 -> 7;
            default -> 8;
        };
    }

}
