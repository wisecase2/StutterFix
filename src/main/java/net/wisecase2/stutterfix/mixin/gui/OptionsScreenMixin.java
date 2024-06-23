package net.wisecase2.stutterfix.mixin.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import net.wisecase2.stutterfix.gui.StutterFixOptionsGUI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Supplier;

@Mixin({OptionsScreen.class})
public abstract class OptionsScreenMixin extends Screen {

    @Shadow
    private GameOptions settings;

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    protected abstract ButtonWidget createButton(Text message, Supplier<Screen> screenSupplier);

    @Inject(method = "init()V", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/widget/GridWidget$Adder.add (Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;", ordinal = 0, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void inject_StutterFixOption(CallbackInfo ci, DirectionalLayoutWidget directionalLayoutWidget, DirectionalLayoutWidget directionalLayoutWidget2, GridWidget gridWidget, GridWidget.Adder adder) {
        adder.add(        this.createButton(Text.translatable("stutterfix.name"), () -> {
            return new StutterFixOptionsGUI((Screen)(Object)this, (GameOptions)(Object)this.settings);
        }), 2);
    }

}
