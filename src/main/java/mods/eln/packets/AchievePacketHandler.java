package mods.eln.packets;

import mods.eln.Achievements;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AchievePacketHandler implements IMessageHandler<AchievePacket, IMessage> {

    @Override
    public IMessage onMessage(AchievePacket message, MessageContext ctx) {
        //System.out.println("Got message: " + message.text);
        if (message.text.equals("openWiki")) {
            ctx.getServerHandler().playerEntity.triggerAchievement(Achievements.openGuide);
        } else if (message.text.equals("craft50VMacerator")) {
            ctx.getServerHandler().playerEntity.triggerAchievement(Achievements.craft50VMacerator);
        } else {
            System.out.println("[ELN]: ELN Wiki Achievement Handler has received an invalid message/packet: " + message.text);
        }
        return null;
    }
}
