package cc.unilock.narcolepsy.mixin;

import cc.unilock.narcolepsy.Narcolepsy;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
	@Inject(method = "spawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;isSpectator()Z"), cancellable = true)
	private void spawn(CallbackInfoReturnable<Integer> cir, @Local int i, @Local ServerPlayerEntity serverPlayerEntity) {
		if (serverPlayerEntity.getAttachedOrCreate(Narcolepsy.NARCOLEPTIC)) cir.setReturnValue(i);
	}
}
