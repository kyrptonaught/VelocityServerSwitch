package net.kyrptonaught.velocityserverswitch;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public class VelocityServerSwitchMod implements ModInitializer {
    public static final Identifier BUNGEECORD_ID = Identifier.of("bungeecord", "main");

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(VelocityServerSwitchMod::register);
        PayloadTypeRegistry.playS2C().register(VelocityPacket.PACKET_ID, VelocityPacket.codec);
    }

    private static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("velocityserverswitch")
                .requires((source) -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("servername", StringArgumentType.word())
                        .executes((commandContext) -> {
                            String servername = StringArgumentType.getString(commandContext, "servername");
                            ServerPlayNetworking.send(commandContext.getSource().getPlayer(), new VelocityPacket("Connect", servername));
                            return 1;
                        })));
    }
}
