package main.commands.music.queue;

import main.Command;
import main.utility.metautil.BotUtils;
import main.utility.music.MasterManager;
import main.utility.music.TrackScheduler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

import java.util.List;

public class SongQueue implements Command {
    private IMessage previousQMsg;

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        TrackScheduler scheduler = MasterManager.getGuildAudioPlayer(event.getGuild()).getScheduler();

        boolean mobile = false;
        // check if mobile formatting
        if (!args.isEmpty() && args.get(0).startsWith("m")) mobile = true;

        StringBuilder sb = scheduler.getQueueStrB(event, mobile);

        // runDelete previous message if not null
        if (previousQMsg != null) previousQMsg.delete();
        previousQMsg = BotUtils.sendGet(event.getChannel(), sb.toString());

//        try {
//            Aspect.LOG.info("sleeping in queue");
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public String getDesc() {
        return "Displays the music queue. Will only show first 15 tracks if the queue is longer than 15.";
    }

    @Override
    public boolean requireSynchronous() {
        return true;
    }
}
