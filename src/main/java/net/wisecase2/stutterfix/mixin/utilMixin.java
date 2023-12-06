package net.wisecase2.stutterfix.mixin;

import net.minecraft.util.Util;

import org.spongepowered.asm.mixin.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Util.class)
public abstract class utilMixin {

	@ModifyVariable(method = "createWorker", at = @At("STORE"), ordinal = 0)
	private static int setNumThreadsOfMainWorker(int p) {
		if (p >= 7) {
			return p - 4;
		} else if (p >= 3) {
			return 1;
		} else {
			return 1;
		}
	}

	@Inject(method = "method_28123", at = @At(value = "INVOKE", target = "java/util/concurrent/ForkJoinWorkerThread.setName (Ljava/lang/String;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void setPriorityOfThreadMainWorker(String string, AtomicInteger val, ForkJoinPool forkjoinpool, CallbackInfoReturnable ci, ForkJoinWorkerThread forkJoinWorkerThread) {
		forkJoinWorkerThread.setPriority(1);
	}
}