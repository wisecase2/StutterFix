package net.wisecase2.stutterfix.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.wisecase2.stutterfix.StutterFix;


import java.util.concurrent.CompletableFuture;

import static net.minecraft.client.option.GameOptions.getGenericValueText;

@Environment(EnvType.CLIENT)
public class StutterFixOptionsGUI extends GameOptionsScreen {
    private OptionListWidget optionButtons;

    public StutterFixOptionsGUI(Screen parent, GameOptions options) {
        super(parent, options, Text.translatable("stutterfix.options.name"));
    }


    protected void init() {
        this.optionButtons = (OptionListWidget)this.addDrawableChild(new OptionListWidget(this.client, this.width, this.height - 64, 32, 25));

        int max_threads_count = Runtime.getRuntime().availableProcessors();

        int default_vanilla = StutterFix.getDefaultVanillaMainWorkerExecutorCount();
        int default_stutterfix = StutterFix.getDefaultStutterFixMainWorkerExecutorCount();

        int defaultRenderThreadPriority = (max_threads_count > 4)? 10:5;
        int defaultServerThreadPriority = (max_threads_count > 4)? 8:5;

        this.optionButtons.addSingleOptionEntry( new SimpleOption("stutterfix.options.worker_threads", SimpleOption.constantTooltip(Text.translatable("stutterfix.options.worker_threads.tooltip", default_vanilla, default_stutterfix)), (optionText, value) -> {
            return getGenericValueText(optionText, Text.translatable("stutterfix.options.threads", new Object[]{value}));
        }, new SimpleOption.ValidatingIntSliderCallbacks(1, max_threads_count), StutterFix.threadconfig.mainWorkerExecutorCount, (value) -> {
            StutterFix.threadconfig.mainWorkerExecutorCount = (int) value;
            StutterFix.loadMainWorkerExecutor();
        }));
        this.optionButtons.addSingleOptionEntry( new SimpleOption("stutterfix.options.Worker_threads_priority_cut", SimpleOption.constantTooltip(Text.translatable("stutterfix.options.Worker_threads_priority_cut.tooltip")), (optionText, value) -> {
            return getGenericValueText(optionText, Text.translatable("stutterfix.options.priority_cut", new Object[]{value}));
        }, new SimpleOption.ValidatingIntSliderCallbacks(0, max_threads_count), StutterFix.threadconfig.mainWorkerExecutorPriorityCut, (value) -> {
            StutterFix.threadconfig.mainWorkerExecutorPriorityCut = (int) value;
            StutterFix.loadMainWorkerExecutor();
        }));

        if(StutterFix.isInitializedRenderThread) {
            this.optionButtons.addSingleOptionEntry( new SimpleOption("stutterfix.options.render_thread_priority", SimpleOption.constantTooltip(Text.translatable("stutterfix.options.render_thread_priority.tooltip", defaultRenderThreadPriority)), (optionText, value) -> {
                return getGenericValueText(optionText, Text.translatable("stutterfix.options.thread_priority", new Object[]{value}));
            }, new SimpleOption.ValidatingIntSliderCallbacks(1, 10), StutterFix.threadconfig.renderThreadPriority, (value) -> {
                StutterFix.threadconfig.renderThreadPriority = (int) value;
                StutterFix.configPriorityRenderThread();
            }));
        }

        if(StutterFix.isInitializedServerThread) {
            this.optionButtons.addSingleOptionEntry( new SimpleOption("stutterfix.options.server_thread_priority", SimpleOption.constantTooltip(Text.translatable("stutterfix.options.server_thread_priority.tooltip", defaultServerThreadPriority)), (optionText, value) -> {
                return getGenericValueText(optionText, Text.translatable("stutterfix.options.thread_priority", new Object[]{value}));
            }, new SimpleOption.ValidatingIntSliderCallbacks(1, 10), StutterFix.threadconfig.serverThreadPriority, (value) -> {
                StutterFix.threadconfig.serverThreadPriority = (int) value;
                StutterFix.configPriorityServerThread();
            }));
        }

        if(StutterFix.removeYieldOption){
            this.optionButtons.addSingleOptionEntry( new SimpleOption("stutterfix.options.remove_yield", SimpleOption.constantTooltip(Text.translatable("stutterfix.options.remove_yield.tooltip")), (optionText, value) -> {
                return (boolean)value ? Text.translatable("stutterfix.options.remove") : Text.translatable("stutterfix.options.keep");
            }, SimpleOption.BOOLEAN, StutterFix.threadconfig.renderRemoveYield, (value) -> {
                StutterFix.threadconfig.renderRemoveYield = (boolean) value;
            }));
        }

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            StutterFix.saveThread.execute(() -> { StutterFix.threadconfig.saveConfig(); });
            this.client.setScreen(this.parent);
        }).dimensions(this.width / 2 - 100, this.height - 27, 200, 20).build());
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 16777215);
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
    }
}
