package main.commands.rotmg;

import main.Command;
import main.utility.ReadWrite;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SetKatInv implements Command {
    private final String KAT_INV_FILE_PATH = "C:\\Users\\Positron\\IdeaProjects\\Aspect\\txtfiles\\Mooshroom\\katinvurl.txt";
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if(!event.getAuthor().getStringID().equals("264213620026638336")) {
            BotUtils.send(event.getChannel(), "This command is for <@"+ "264213620026638336" + " only.");
        } else {
            ReadWrite.writeToFile(KAT_INV_FILE_PATH, args.get(0), false, false);
            BotUtils.send(event.getChannel(), "Inventory updated");
        }
    }

    @Override
    public String getDesc() {
        return "ROTMG";
    }
}
