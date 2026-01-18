package io.yukkuric.trinket_media_holder.mixin;

import at.petrak.hexcasting.api.HexAPI;
import com.llamalad7.mixinextras.sugar.Local;
import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.api.utils.MediaHelper;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(MediaHelper.class)
public class MixinTrinketHook {
    @Inject(method = "scanPlayerForMediaStuff", at = @At(value = "INVOKE", target = "Lkotlin/collections/CollectionsKt;sortWith(Ljava/util/List;Ljava/util/Comparator;)V"), remap = false)
    private static void injectAllTrinket(ServerPlayer player, CallbackInfoReturnable<List<ADMediaHolder>> cir, @Local List<ADMediaHolder> sources) {
        TrinketsApi.getTrinketComponent(player).ifPresent(comp -> {
            for (var pair : comp.getAllEquipped()) {
                var stack = pair.getB();
                var holder = HexAPI.instance().findMediaHolder(stack);
                if (holder != null && holder.canProvide()) sources.add(holder);
            }
        });
    }
}
