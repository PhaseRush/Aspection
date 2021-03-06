package main.commands.warframe;

import main.Aspect;
import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class WfModInfo implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String modName = args.get(0);
        String json = BotUtils.getStringFromUrl("https://api.warframestats.us/mods/search/" + modName);

        Aspect.LOG.info(json);

    }

    @Override
    public String getDesc() {
        return null;
    }
}
