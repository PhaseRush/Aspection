package main.commands.warframe;

import main.Command;
import main.utility.BotUtils;
import main.utility.WarframeUtil;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class WarframeCetusCycle implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.sendMessage(event.getChannel(), WarframeUtil.cetusCycleString());
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Warframe - Current Cetus cycle. (PC only)";
    }
}