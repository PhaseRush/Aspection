package main.commands.music.playing;

import main.Command;
import main.utility.metautil.BotUtils;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SongSkip implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (args.size() == 0) {
            MasterManager.skipTrack(event);
            BotUtils.reactWithCheckMark(event.getMessage());
        } else {
            try {
                MasterManager.skipNumTracks(event, Integer.valueOf(args.get(0)));
                MasterManager.getGuildAudioPlayer(event.getGuild()).getScheduler().deleteCurrentEmbed(); //runDelete the floating player
                BotUtils.reactWithCheckMark(event.getMessage());
            } catch (NumberFormatException e) {
                BotUtils.send(event.getChannel(), "Use: ```\n" + BotUtils.DEFAULT_BOT_PREFIX + "help skip``` for proper formatting");
                BotUtils.reactWithX(event.getMessage());
            }
        }
    }

    @Override
    public String getDesc() {
        return "skips to next song, or next x songs if specified";
    }

    @Override
    public boolean requireSynchronous() {
        return true;
    }
}
