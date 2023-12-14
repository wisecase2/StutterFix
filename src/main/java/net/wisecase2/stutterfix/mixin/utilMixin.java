package net.wisecase2.stutterfix.mixin;

import com.google.common.util.concurrent.MoreExecutors;
import net.minecraft.util.Util;

import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.*;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;

@Mixin(Util.class)
public abstract class utilMixin {

	@Shadow @Final
	static Logger LOGGER;

	@Shadow @Final
	private static AtomicInteger NEXT_WORKER_ID;

	@ModifyVariable(method = "createWorker", at = @At("STORE"), ordinal = 0)
	private static int setNumThreadsOfMainWorker(int p, String name) {

		if(Objects.equals(name, "Main")){
			if (p >= 7) {	// MAIN_WORKER_EXECUTOR
				return p - 4;
			} else {
				return 1;
			}
		}

		return p;
	}

	@Inject(method = "method_28123", at = @At(value = "INVOKE", target = "java/util/concurrent/ForkJoinWorkerThread.setName (Ljava/lang/String;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void setPriorityOfThreadMainWorker(String string, ForkJoinPool forkjoinpool, CallbackInfoReturnable ci, ForkJoinWorkerThread forkJoinWorkerThread) {
		if(Objects.equals(string, "Main")) { //MAIN_WORKER_EXECUTOR
			int i = MathHelper.clamp(Runtime.getRuntime().availableProcessors() - 1, 1, getMaxBackgroundThreads());
			int next_worker_id = NEXT_WORKER_ID.get() - 1;

			if (i >= 7) {
				i -= 4;
			} else {
				i = 1;
			}

			int halfNumWorkerMainThreads = i / 2;

			if (next_worker_id > halfNumWorkerMainThreads) {
				forkJoinWorkerThread.setPriority(1);
			}
		}
	}

	private static int getMaxBackgroundThreads() {
		String string = System.getProperty("max.bg.threads");
		if (string != null) {
			try {
				int i = Integer.parseInt(string);
				if (i >= 1 && i <= 255) {
					return i;
				}

				LOGGER.error("Wrong {} property value '{}'. Should be an integer value between 1 and {}.", new Object[]{"max.bg.threads", string, 255});
			} catch (NumberFormatException var2) {
				LOGGER.error("Could not parse {} property value '{}'. Should be an integer value between 1 and {}.", new Object[]{"max.bg.threads", string, 255});
			}
		}
		return 255;
	}
}