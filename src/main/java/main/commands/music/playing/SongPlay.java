package main.commands.music.playing;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import main.Command;
import main.utility.BotUtils;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.List;

public class SongPlay implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        IVoiceChannel aspectChannel = event.getClient().getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel();
        IVoiceChannel userChannel = event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel();
        AudioPlayer player = MasterManager.getGuildAudioPlayer(event.getGuild()).getPlayer();

        if (aspectChannel != userChannel) { //cant use .equals b/c could be null
            BotUtils.joinVC(event);
            MasterManager.getGuildAudioPlayer(event.getGuild()).setThisStartTime(System.currentTimeMillis());
        }

        if (player.isPaused()) {
            player.setPaused(false);
            BotUtils.send(event.getChannel(), "Player is now unpaused.");
        }

        String searchStr = String.join(" ", args);
        MasterManager.loadAndPlay(event.getChannel(), searchStr, event, false, "");
    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return false;
    }

    @Override
    public String getDescription() {
        return "plays song url, or searches youtube if url is not valid.";
    }
}
