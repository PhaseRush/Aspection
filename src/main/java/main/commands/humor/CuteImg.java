package main.commands.humor;

import main.Command;
import main.utility.BotUtils;
import main.utility.humorUtil.CuteUtil;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class CuteImg implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        try {
            String url = CuteUtil.cuteUrls.get(args.get(0));
            BotUtils.sendMessage(event.getChannel(),
                    new EmbedBuilder().withImage(url));
        } catch (Exception e) {
            BotUtils.sendMessage(event.getChannel(), "Cuteness doesn't exist!");
        }
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }


    public String helpMsg() {
        return null;
    }
}
