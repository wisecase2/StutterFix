package net.wisecase2.stutterfix.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;


import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;

import net.minecraft.text.Text;
import net.wisecase2.stutterfix.StutterFix;


import static net.minecraft.client.option.GameOptions.getGenericValueText;

@Environment(EnvType.CLIENT)
public class StutterFixOptionsGUI extends GameOptionsScreen {


    public StutterFixOptionsGUI(Screen parent, GameOptions options) {
        super(parent, options, Text.translatable("stutterfix.options.name"));
    }

    @Override
    protected void addOptions() {
        int max_threads_count = Runtime.getRuntime().availableProcessors();

        int default_vanilla = StutterFix.getDefaultVanillaMainWorkerExecutorCount();
        int default_stutterfix = StutterFix.getDefaultStutterFixMainWorkerExecutorCount();

        int defaultRenderThreadPriority = (max_threads_count > 4)? 10:5;
        int defaultServerThreadPriority = (max_threads_count > 4)? 8:5;

        this.body.addSingleOptionEntry( new SimpleOption("stutterfix.options.worker_threads", SimpleOption.constantTooltip(Text.translatable("stutterfix.options.worker_threads.tooltip", default_vanilla, default_stutterfix)), (optionText, value) -> {
            return getGenericValueText(optionText, Text.translatable("stutterfix.options.threads", new Object[]{value}));
        }, new SimpleOption.ValidatingIntSliderCallbacks(1, max_threads_count), StutterFix.threadconfig.mainWorkerExecutorCount, (value) -> {
            StutterFix.threadconfig.mainWorkerExecutorCount = (int) value;
            StutterFix.loadMainWorkerExecutor();
        }));
        this.body.addSingleOptionEntry( new SimpleOption("stutterfix.options.Worker_threads_priority_cut", SimpleOption.constantTooltip(Text.translatable("stutterfix.options.Worker_threads_priority_cut.tooltip")), (optionText, value) -> {
            return getGenericValueText(optionText, Text.translatable("stutterfix.options.priority_cut", new Object[]{value}));
        }, new SimpleOption.ValidatingIntSliderCallbacks(0, max_threads_count), StutterFix.threadconfig.mainWorkerExecutorPriorityCut, (value) -> {
            StutterFix.threadconfig.mainWorkerExecutorPriorityCut = (int) value;
            StutterFix.loadMainWorkerExecutor();
        }));

        if(StutterFix.isInitializedRenderThread) {
            this.body.addSingleOptionEntry( new SimpleOption("stutterfix.options.render_thread_priority", SimpleOption.constantTooltip(Text.translatable("stutterfix.options.render_thread_priority.tooltip", defaultRenderThreadPriority)), (optionText, value) -> {
                return getGenericValueText(optionText, Text.translatable("stutterfix.options.thread_priority", new Object[]{value}));
            }, new SimpleOption.ValidatingIntSliderCallbacks(1, 10), StutterFix.threadconfig.renderThreadPriority, (value) -> {
                StutterFix.threadconfig.renderThreadPriority = (int) value;
                StutterFix.configPriorityRenderThread();
            }));
        }

        if(StutterFix.isInitializedServerThread) {
            this.body.addSingleOptionEntry( new SimpleOption("stutterfix.options.server_thread_priority", SimpleOption.constantTooltip(Text.translatable("stutterfix.options.server_thread_priority.tooltip", defaultServerThreadPriority)), (optionText, value) -> {
                return getGenericValueText(optionText, Text.translatable("stutterfix.options.thread_priority", new Object[]{value}));
            }, new SimpleOption.ValidatingIntSliderCallbacks(1, 10), StutterFix.threadconfig.serverThreadPriority, (value) -> {
                StutterFix.threadconfig.serverThreadPriority = (int) value;
                StutterFix.configPriorityServerThread();
            }));
        }

        if(StutterFix.removeYieldOption) {
            this.body.addSingleOptionEntry(new SimpleOption("stutterfix.options.remove_yield", SimpleOption.constantTooltip(Text.translatable("stutterfix.options.remove_yield.tooltip")), (optionText, value) -> {
                return (boolean) value ? Text.translatable("stutterfix.options.remove") : Text.translatable("stutterfix.options.keep");
            }, SimpleOption.BOOLEAN, StutterFix.threadconfig.renderRemoveYield, (value) -> {
                StutterFix.threadconfig.renderRemoveYield = (boolean) value;
            }));
        }
    }

}
