package main.commands.utilitycommands;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

public class BanUser implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        IUser metaAdmin = event.getClient().getUserByID(264213620026638336L);
        if (!event.getAuthor().equals(metaAdmin))
            return;
        else {
            IUser targetBan = event.getClient().getUserByID(Long.valueOf(args.get(0)));
            BotUtils.bannedUsers.add(targetBan);
            metaAdmin.getOrCreatePMChannel().sendMessage("Banned: " + targetBan.getName() + " from " + event.getGuild().getName());
        }
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return "doesn't do too much right now";
    }
}