package net.kyrptonaught.velocityserverswitch;


import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public record VelocityPacket(String... args) implements CustomPayload {
    public static final CustomPayload.Id<VelocityPacket> PACKET_ID = new CustomPayload.Id<>(VelocityServerSwitchMod.BUNGEECORD_ID);
    public static final PacketCodec<RegistryByteBuf, VelocityPacket> codec = new PacketCodec<RegistryByteBuf, VelocityPacket>() {

        @Override
        public VelocityPacket decode(RegistryByteBuf buf) {
            //we do not receive this packet back.
            return null;
        }

        @Override
        public void encode(RegistryByteBuf buf, VelocityPacket value) {
            try (ByteBufDataOutput output = new ByteBufDataOutput(new PacketByteBuf(Unpooled.buffer()))) {
                for (String arg : value.args())
                    output.writeUTF(arg);

                buf.writeBytes(output.getBuf());
            } catch (Exception e) {
                System.out.println("Failed to send switch packet");
                e.printStackTrace();
            }
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    private static class ByteBufDataOutput extends OutputStream {
        private final PacketByteBuf packetByteBuf;
        private final DataOutputStream dataOutputStream;

        public ByteBufDataOutput(PacketByteBuf buf) {
            this.packetByteBuf = buf;
            this.dataOutputStream = new DataOutputStream(this);
        }

        public PacketByteBuf getBuf() {
            return packetByteBuf;
        }

        @Override
        public void write(int b) {
            packetByteBuf.writeByte(b);
        }

        public void writeUTF(String s) {
            try {
                this.dataOutputStream.writeUTF(s);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public void close() {

        }
    }
}