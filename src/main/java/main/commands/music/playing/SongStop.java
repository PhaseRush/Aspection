package main.commands.music.playing;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import main.Command;
import main.commands.music.MusicEqualizer;
import main.utility.metautil.BotUtils;
import main.utility.music.MasterManager;
import main.utility.music.TrackScheduler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.List;

public class SongStop implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        IVoiceChannel voiceChannel = event.getClient().getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel();

        if (voiceChannel == null) {
            BotUtils.send(event.getChannel(), "Aspect is currently not in any voice channels." +
                    " If you believe this is a mistake, please pm the mods/admins, or bot owner @ Requiem#8148");
            return;
        }

        voiceChannel.leave();

        TrackScheduler scheduler = MasterManager.getGuildAudioPlayer(event.getGuild()).getScheduler();
        AudioTrack currentTrack = scheduler.getCurrentTrack();
        try {currentTrack.setPosition(currentTrack.getDuration());} //sets it to the end of this song
        catch (Exception ignored) {}

        // clear EQ filter
        MusicEqualizer.replaceFilter(
                event.getGuild(),
                MusicEqualizer.Filters.ZERO.getFilter(),
                0
        );

        scheduler.getQueue().clear(); //purge entire queue
        scheduler.getPastQueue().clear();

        scheduler.deleteCurrentEmbed(); //clean up the chat
        BotUtils.reactWithCheckMark(event.getMessage());
        long startTime = MasterManager.getGuildAudioPlayer(event.getGuild()).getThisStartTime();
        if (startTime != 0) BotUtils.send(event.getChannel(), "This music session lasted `" + BotUtils.millisToHMS(System.currentTimeMillis() - startTime) + "`");
    }

    @Override
    public String getDesc() {
        return "Stops current playing songs. Clears queue.";
    }

    @Override
    public boolean requireSynchronous() {
        return true;
    }
}
