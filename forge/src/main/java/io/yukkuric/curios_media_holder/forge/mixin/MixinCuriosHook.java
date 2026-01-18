package io.yukkuric.curios_media_holder.forge.mixin;

import at.petrak.hexcasting.api.HexAPI;
import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.api.utils.MediaHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

@Mixin(MediaHelper.class)
public class MixinCuriosHook {
    @Inject(method = "scanPlayerForMediaStuff", at = @At(value = "INVOKE", target = "Lkotlin/collections/CollectionsKt;sortWith(Ljava/util/List;Ljava/util/Comparator;)V"), remap = false)
    private static void injectAllCurios(ServerPlayer player, CallbackInfoReturnable<List<ADMediaHolder>> cir, @Local List<ADMediaHolder> sources) {
        CuriosApi.getCuriosInventory(player).ifPresent(inv -> {
            var allCurios = inv.getEquippedCurios();
            var slots = allCurios.getSlots();
            for (var i = 0; i < slots; i++) {
                var stack = allCurios.getStackInSlot(i);
                var holder = HexAPI.instance().findMediaHolder(stack);
                if (holder != null && holder.canProvide()) sources.add(holder);
            }
        });
    }
}
