package cc.unilock.narcolepsy.mixin;

import cc.unilock.narcolepsy.Narcolepsy;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(targets = "net.minecraft.entity.mob.PhantomEntity$FindTargetGoal")
public class PhantomEntityFindTargetGoalMixin {
	@ModifyVariable(method = "canStart()Z", at = @At("STORE"), index = 1)
	private List<PlayerEntity> modifyTargetList(List<PlayerEntity> value){
		value.removeIf(player -> player.getAttachedOrCreate(Narcolepsy.NARCOLEPTIC));

		return value;
	}
}
