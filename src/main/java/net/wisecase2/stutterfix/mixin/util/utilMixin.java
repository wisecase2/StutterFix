package net.wisecase2.stutterfix.mixin.util;

import net.minecraft.util.Util;
import net.wisecase2.stutterfix.StutterFix;
import org.spongepowered.asm.mixin.*;

import net.minecraft.util.thread.NameableExecutor;
import java.util.concurrent.ExecutorService;

@Mixin(Util.class)
public abstract class utilMixin {
	@Overwrite
	public static NameableExecutor getMainWorkerExecutor() {

		if(!StutterFix.isInitializedMainWorkerExecutor) {
			StutterFix.loadMainWorkerExecutor();
		}

		return StutterFix.mainWorkerExecutor;
	}

}
