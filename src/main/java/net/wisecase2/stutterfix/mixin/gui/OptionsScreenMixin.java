package net.wisecase2.stutterfix.mixin.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import net.wisecase2.stutterfix.gui.StutterFixOptionsGUI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

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

    @ModifyArg(method = "init()V", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/widget/GridWidget$Adder.add (Lnet/minecraft/client/gui/widget/Widget;I)Lnet/minecraft/client/gui/widget/Widget;"), index = 0)
    private Widget modifyArg(Widget arg0) {
        return this.createButton(Text.translatable("stutterfix.name"), () -> {
            return new StutterFixOptionsGUI(this, this.settings);
        });
    }
}
