package net.wisecase2.stutterfix.mixin;

import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.SaveLoader;
import net.minecraft.util.ApiServices;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.WorldGenerationProgressListenerFactory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.server.integrated.IntegratedServer.class)
public class IntegratedServerMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void newIntegratedServerInstance(Thread serverThread, MinecraftClient client, LevelStorage.Session session, ResourcePackManager dataPackManager, SaveLoader saveLoader, ApiServices apiServices, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci) {
        int serverpriority;
        switch (Runtime.getRuntime().availableProcessors()) {
            case 1, 2, 3, 4:
                serverpriority = 5;
                break;
            case 5, 6:
                serverpriority = 7;
                break;
            case 7, 8:
                serverpriority = 8;
                break;
            default:
                serverpriority = 9;
        };

        serverThread.setPriority(serverpriority);
    }
}
