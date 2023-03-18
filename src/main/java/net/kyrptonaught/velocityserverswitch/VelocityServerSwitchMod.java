package net.kyrptonaught.velocityserverswitch;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.netty.buffer.Unpooled;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;



public class VelocityServerSwitchMod implements ModInitializer {
    private static final Identifier BUNGEECORD_ID = new Identifier("bungeecord", "main");
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(VelocityServerSwitchMod::register);
    }
    private static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment ) {
        dispatcher.register(CommandManager.literal("velocityserverswitch")
                .requires(Permissions.require("velocityserverswitch.command"))
                .then(CommandManager.argument("servername", StringArgumentType.word())
                        .executes((commandContext) -> {
                            String servername = StringArgumentType.getString(commandContext, "servername");
                            String permissionNode = "velocityserverswitch." + servername;
                            if (Permissions.check(commandContext.getSource().getPlayer(), permissionNode)) {
                                try (ByteBufDataOutput output = new ByteBufDataOutput(new PacketByteBuf(Unpooled.buffer()))) {
                                    output.writeUTF("Connect");
                                    output.writeUTF(servername);
                                    ServerPlayNetworking.send(commandContext.getSource().getPlayer(), BUNGEECORD_ID, output.getBuf());
                                } catch (Exception e) {
                                    System.out.println("Failed to send switch packet");
                                    e.printStackTrace();
                                    return 1;
                                }
                            } else {
                                // Player does not have permission to switch to this server
                                commandContext.getSource().sendFeedback(Text.translatable("commands.velocityserverswitch.no_permission", servername), false);
                            }
                            return 1;
                        })));
    }
}
