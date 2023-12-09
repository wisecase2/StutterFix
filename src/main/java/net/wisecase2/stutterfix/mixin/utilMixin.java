package net.wisecase2.stutterfix.mixin;

import net.minecraft.util.Util;

import org.spongepowered.asm.mixin.*;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Util.class)
public abstract class utilMixin {

	@ModifyVariable(method = "createWorker", at = @At("STORE"), ordinal = 0)
	private static int setNumThreadsOfMainWorker(int p) {
		if (p >= 7) {
			return p - 4;
		} else {
			return 1;
		}
	}
}