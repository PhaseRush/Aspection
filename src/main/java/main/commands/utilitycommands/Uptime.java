package main.commands.utilitycommands;

import main.Command;
import main.Main;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Uptime implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        long millis = System.currentTimeMillis() - Main.startTime;
        int sec = (int) (millis/1000)%60;
        int min = (int) ((millis/(1000*60)) %60);
        int hr = (int) ((millis/(1000*60*60))%24);

        int days = (int) TimeUnit.MILLISECONDS.toDays(millis);
        String s = "Uptime: " + days + " d\t " + hr + ":" + (min<10? "0"+min:min) + ":" + (sec<10? "0"+sec:sec) + "\t= " + millis + " ms";
        BotUtils.sendMessage(event.getChannel(), s);
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}