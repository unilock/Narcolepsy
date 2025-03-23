package cc.unilock.narcolepsy;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.literal;

@SuppressWarnings("UnstableApiUsage")
public class Narcolepsy implements ModInitializer {
	private static final String MOD_ID = "narcolepsy";
	public static final AttachmentType<Boolean> NARCOLEPTIC = AttachmentRegistry.<Boolean>builder().copyOnDeath().initializer(() -> Boolean.FALSE).persistent(Codec.BOOL).buildAndRegister(id("narcoleptic"));

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(
				literal("insomnia").executes(this::displayStatus)
					.then(literal("toggle").executes(this::toggleInsomnia))
			);
		});
	}

	private int displayStatus(CommandContext<ServerCommandSource> context) {
		if (!(context.getSource().getEntity() instanceof ServerPlayerEntity player)) {
			context.getSource().sendError(Text.translatable("permissions.requires.player"));
			return -1;
		}

		context.getSource().sendFeedback(() -> Text.literal("Insomnia is currently ").append(player.getAttachedOrCreate(NARCOLEPTIC) ? Text.literal("disabled").formatted(Formatting.RED) : Text.literal("enabled").formatted(Formatting.GREEN)).append("."), false);

		return Command.SINGLE_SUCCESS;
	}

	private int toggleInsomnia(CommandContext<ServerCommandSource> context) {
		if (!(context.getSource().getEntity() instanceof ServerPlayerEntity player)) {
			context.getSource().sendError(Text.translatable("permissions.requires.player"));
			return -1;
		}

		player.setAttached(NARCOLEPTIC, !player.getAttachedOrCreate(NARCOLEPTIC));
		context.getSource().sendFeedback(() -> Text.literal("Insomnia is now ").append(player.getAttachedOrThrow(NARCOLEPTIC) ? Text.literal("disabled").formatted(Formatting.RED) : Text.literal("enabled").formatted(Formatting.GREEN)).append(Text.literal(".")), false);

		return Command.SINGLE_SUCCESS;
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
