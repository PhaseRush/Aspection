package main.commands.music;

import main.Command;
import main.utility.BotUtils;
import main.utility.music.MasterManager;
import main.utility.music.SfxManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;
import java.util.Map;

public class SoundEffect implements Command {

    private Map<String, String> sfxMap = SfxManager.sfxMap;

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (event.getClient().getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel() == null) {
            BotUtils.joinVC(event);
        }

        if (sfxMap.keySet().contains(args.get(0)))
            MasterManager.loadAndPlay(event.getChannel(), sfxMap.get(args.get(0)), event);
        else
            BotUtils.sendMessage(event.getChannel(), "This sound effect does not exist. Check your spelling or go bother kat to add it");
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }
}
