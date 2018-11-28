package main.commands.utilitycommands;

import main.Command;
import main.utility.BotUtils;
import main.utility.MessageDeleter;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.List;

public class DeleteMsg implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        try {
            new MessageDeleter(args.get(0), Integer.valueOf(args.get(1)), event.getAuthor(), event.getChannel()).runDelete(event.getGuild());
        } catch (MissingPermissionsException e) {
            BotUtils.send(event.getChannel(), "Aspect is missing the MANAGE_MESSAGES permission. Ask the server admin for more details!");
        }
    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return true;
    }

    @Override
    public String getDescription() {
        return "Deletes message by user. $deletemsg userId, [minutes to delete from]";
    }
}
