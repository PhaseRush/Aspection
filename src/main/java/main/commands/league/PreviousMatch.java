package main.commands.league;


import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.core.match.Frame;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.match.Participant;
import com.merakianalytics.orianna.types.core.match.Timeline;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import main.Aspect;
import main.Command;
import main.utility.league.LeagueUtil;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PreviousMatch implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        IMessage loadingMsg = event.getChannel().sendMessage("```Loading. This may take a while...```");

        final boolean isNA = args.size() != 2;
        final Summoner summoner = Summoner.named(args.get(0)).withRegion((isNA ? Region.NORTH_AMERICA : LeagueUtil.parseRegion(args.get(1)))).get();
        //final MatchHistory matchHistory = MatchHistory.forSummoner(summoner).withQueues(Queue.RANKED_SOLO_5x5).withSeasons(Season.SEASON_8).get();
        final MatchHistory matchHistory = MatchHistory.forSummoner(summoner).get();

        Timeline timeline = matchHistory.get(0).getTimeline();
        List<Frame> matchFrames = new ArrayList<>(timeline);

        for (Frame f : matchFrames) {
            Set<Participant> participants = f.getParticipantFrames().keySet();
            for (Participant p : participants) {
                Aspect.LOG.info(p.getItems().get(0).getName());
            }
        }

        BotUtils.send(event.getChannel(), summoner.getName() + " has played " + matchHistory.size() + " games");
        loadingMsg.delete();
    }

    /**
     * WIP
     * @param event
     * @param args
     * @return
     */
    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return false;
    }

    @Override
    public String getDesc() {
        return "WIP, displays detail about previous match for given player";
    }

    @Override
    public String toString() {
        return "Hello friends";
    }
}
