package net.wisecase2.stutterfix;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.math.MathHelper;
import net.wisecase2.stutterfix.config.ThreadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.atomic.AtomicInteger;

public class StutterFix implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("stutterfix");

	public static boolean isClientInitialized = false;

	public static ThreadConfig threadconfig = new ThreadConfig();
	public static boolean isConfigInitialized = false;
	public static ExecutorService saveThread = Executors.newSingleThreadExecutor();


	public static ExecutorService mainWorkerExecutor;
	public static Thread renderThread;
	public static Thread serverThread;

	public static boolean isInitializedMainWorkerExecutor = false;
	public static boolean isInitializedRenderThread = false;
	public static boolean isInitializedServerThread = false;

	public static void loadMainWorkerExecutor() {

		int threadCount;
		int threadCut;

		if(isConfigInitialized){
			threadCount = threadconfig.mainWorkerExecutorCount;
			threadCut = threadconfig.mainWorkerExecutorPriorityCut;
		}else{
			threadCount = getDefaultMainWorkerExecutorCount();
			threadCut = threadCount / 2;
		}

		AtomicInteger NEXT_WORKER_ID = new AtomicInteger(0);

		mainWorkerExecutor = new ForkJoinPool(threadCount, forkJoinPool -> {
			ForkJoinWorkerThread forkJoinWorkerThread = new ForkJoinWorkerThread(forkJoinPool){

				@Override
				protected void onTermination(Throwable throwable) {
					if (throwable != null) {
						LOGGER.warn("{} died", (Object)this.getName(), (Object)throwable);
					} else {
						LOGGER.debug("{} shutdown", (Object)this.getName());
					}
					super.onTermination(throwable);
				}
			};
			forkJoinWorkerThread.setName("Worker-Main" + "-" + NEXT_WORKER_ID.getAndIncrement());

			if (NEXT_WORKER_ID.get() > threadCut) {
				forkJoinWorkerThread.setPriority(1);
			}

			return forkJoinWorkerThread;
		}, StutterFix::uncaughtExceptionHandler, true);
		isInitializedMainWorkerExecutor = true;
	}

	public static void loadRenderThread(Thread thread){
		renderThread = thread;
		isInitializedRenderThread = true;
	}
	public static void loadServerThread(Thread thread){
		serverThread = thread;
		isInitializedServerThread = true;
	}

	public static void configPriorityRenderThread(){
		if(isConfigInitialized && isInitializedRenderThread){
			renderThread.setPriority(threadconfig.renderThreadPriority);
		}
	}
	public static void configPriorityServerThread(){
		if(isConfigInitialized && isInitializedServerThread){
			serverThread.setPriority(threadconfig.serverThreadPriority);
		}
	}

	public static void reloadAllConfigs(){
		loadMainWorkerExecutor();
		configPriorityRenderThread();
		configPriorityServerThread();
	}


	@Override
	public void onInitialize() {
		threadconfig = ThreadConfig.readConfig("stutterfix-config.json");
		StutterFix.saveThread.execute(() -> { StutterFix.threadconfig.saveConfig(); });
		isConfigInitialized = true;
		reloadAllConfigs();
	}


	public static int getDefaultMainWorkerExecutorCount(){
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		int vanillaThreadsCount = MathHelper.clamp(availableProcessors - 1, 1,
				StutterFix.getMaxBackgroundThreads());

		int stutterFixThreadsCount = (availableProcessors >= 6)? availableProcessors - 5: 1;

		return Math.min(vanillaThreadsCount, stutterFixThreadsCount);
	}


	public static int getMaxBackgroundThreads() {
		String string = System.getProperty("max.bg.threads");
		if (string != null) {
			try {
				int i = Integer.parseInt(string);
				if (i >= 1 && i <= 255) {
					return i;
				}
			}
			catch (NumberFormatException ignored) {}
		}
		return 255;
	}

	public static void uncaughtExceptionHandler(Thread thread, Throwable throwable) {
	}

}