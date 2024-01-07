package net.wisecase2.stutterfix.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.math.MathHelper;
import net.wisecase2.stutterfix.StutterFix;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ThreadConfig {
    public int mainWorkerExecutorCount;
    public int mainWorkerExecutorPriorityCut;
    public int renderThreadPriority;
    public int serverThreadPriority;
    public boolean renderRemoveYield;
    private Path configPath;

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .create();

    public ThreadConfig() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        this.mainWorkerExecutorCount = StutterFix.getDefaultStutterFixMainWorkerExecutorCount();
        this.mainWorkerExecutorPriorityCut = mainWorkerExecutorCount / 2;
        this.renderThreadPriority = (availableProcessors > 4)? 10: 5;
        this.serverThreadPriority = (availableProcessors > 4)? 8: 5;
        this.renderRemoveYield = false;
    }

    public void validateConfig(){
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        this.mainWorkerExecutorCount = MathHelper.clamp(this.mainWorkerExecutorCount, 1, availableProcessors);
        this.mainWorkerExecutorPriorityCut = MathHelper.clamp(this.mainWorkerExecutorPriorityCut, 0, availableProcessors);
        this.renderThreadPriority = MathHelper.clamp(this.renderThreadPriority, 1, 10);
        this.serverThreadPriority = MathHelper.clamp(this.serverThreadPriority, 1, 10);
    }

    public static ThreadConfig readConfig(String name){
        Path path = FabricLoader.getInstance()
                .getConfigDir()
                .resolve(name);

        ThreadConfig config;
        if (Files.exists(path)) {
            try (FileReader reader = new FileReader(path.toFile())) {
                config = GSON.fromJson(reader, ThreadConfig.class);
            } catch (IOException e) {
                throw new RuntimeException("Could not parse config", e);
            }
        } else {
            config = new ThreadConfig();
        }
        config.configPath = path;
        config.validateConfig();
        return config;
    }

    public void saveConfig() {
        try {
            Path dir = this.configPath.getParent();
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            } else if (!Files.isDirectory(dir)) {
                throw new IOException("Not a directory: " + dir);
            }
            Path tempPath = this.configPath.resolveSibling(this.configPath.getFileName() + ".tmp");
            Files.writeString(tempPath, GSON.toJson(this));
            Files.move(tempPath, this.configPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e) {
            throw new RuntimeException("Couldn't save config file", e);
        }
    }
}
