package main.commands.dontopendeadinside;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class Help implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getChannel(), "Details at: " + BotUtils.GITHUB_URL);
    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return false;
    }

    @Override
    public String getDescription() {
        return "Details at: " + BotUtils.GITHUB_URL;
    }
}