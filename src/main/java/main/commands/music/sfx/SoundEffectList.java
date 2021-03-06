package main.commands.music.sfx;

import main.Command;
import main.utility.metautil.BotUtils;
import main.utility.music.SfxManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SoundEffectList implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        StringBuilder sb = new StringBuilder()
                .append("Available sound effects```\n");

        for (String s : SfxManager.sfxMap.keySet()) {
            sb.append(s + "\n");
            if (!s.startsWith("\n")) sb.append("\t");
        }

        BotUtils.send(event.getChannel(), sb.append("```").toString());
    }

    @Override
    public String getDesc() {
        return "Lists available sound effects";
    }
}
