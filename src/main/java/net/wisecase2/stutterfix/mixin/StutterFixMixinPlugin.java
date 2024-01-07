package net.wisecase2.stutterfix.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;


public class StutterFixMixinPlugin  implements IMixinConfigPlugin {

    private static final String MIXIN_PACKAGE_ROOT = "net.wisecase2.stutterfix.mixin.";

    private final Logger logger = LogManager.getLogger("StutterFix");

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if(!mixinClassName.startsWith(MIXIN_PACKAGE_ROOT)) {
            return false;
        } else {
            String mixin = mixinClassName.substring(MIXIN_PACKAGE_ROOT.length());

            if(mixin.equals("client.RemoveYieldMixin")) {
                return !FabricLoader.getInstance().isModLoaded("vulkanmod");
            }else{
                return true;
            }
        }
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}


