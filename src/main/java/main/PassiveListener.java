package main;

import main.utility.BotUtils;
import main.utility.Visuals;
import main.utility.WarframeUtil;
import main.utility.warframe.wfstatus.WarframeCetusTimeObject;
import main.utility.warframe.wfstatus.alerts.WarframeAlert;
import main.utility.warframe.wfstatus.alerts.WarframeMission;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.IEmoji;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RateLimitException;

import java.time.Instant;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class PassiveListener {



    @EventSubscriber
    public void kaitlynsHangOut(MessageReceivedEvent event) {
        //please, no one ask. please please please please please
        if (event.getGuild().getStringID().equals("197158565004312576")) {
            String message = event.getMessage().getFormattedContent().toLowerCase();
            if (message.contains("penis")) {
                BotUtils.sendMessage(event.getChannel(), "penis.");
            }
            //not exclusive
            if (message.contains("turtle")) {
                BotUtils.sendMessage(event.getChannel(), new EmbedBuilder().withImage("https://assets3.thrillist.com/v1/image/2551479/size/tmg-article_tall.jpg"));
            }
        }
    }

    @EventSubscriber
    public void owo(MessageReceivedEvent event) {
        String msg = event.getMessage().getContent();
        if (msg.contains("owo")) {
            if (ThreadLocalRandom.current().nextInt(100) == 1)
                BotUtils.sendMessage(event.getChannel(), "degenerate");
        }
    }

    @EventSubscriber
    public void reactToEmojiMessage(MessageReceivedEvent event) {
        try {
            for (IEmoji e : event.getGuild().getEmojis()) {
                if (event.getMessage().getFormattedContent().contains(e.getName())) {
                    try {
                        event.getMessage().addReaction(e);
                        break;
                    } catch (RateLimitException exception) {
                        break;
                    }
                }
            }
        } catch (NullPointerException e) {
            //caught if in server with no custom emojis. (ie. pms)
        }
    }

    @EventSubscriber
    public void warframeCetusUpdater(ReadyEvent event) {
        BotUtils.setBottomText();
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);//not sure @todo

        //calculate initial delay
        Instant instant = Instant.parse(WarframeUtil.getCetus().getExpiry());
        long elapseMillis = instant.toEpochMilli() - System.currentTimeMillis(); //millis to next day/night change -- CORRECT

        System.out.println("elapseMillis: " + elapseMillis);

        final Runnable cetusTimeRunner = () -> {
            WarframeCetusTimeObject cetus = WarframeUtil.getCetus();

            //BotUtils.sendMessage(BotUtils.BOTTOM_TEXT, WarframeUtil.cetusCycleString());
            Main.client.changePresence(StatusType.ONLINE, ActivityType.WATCHING, (cetus.isDay()? " the Sun " : " Lua ") + " :: " + cetus.getShortString());

            //int minute = LocalDateTime.now().getMinute();
            //System.out.println("Updated Cetus Status " + LocalDateTime.now().getHour() + ":" + (minute < 10 ? "0" + minute : minute));
        };

        final ScheduledFuture<?> cetusStatusUpdater = scheduler.scheduleAtFixedRate(cetusTimeRunner, 0/*elapseMillis/1000*/, 60/*150*60*/, SECONDS);

        //this thing cancels execution loop
//        scheduler.schedule(() -> {
//            cetusStatusUpdater.cancel(false);
//        }, 168, HOURS);
    }

    @EventSubscriber
    public void alertFilter(ReadyEvent event) {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        final Runnable alertFilter = () -> {
            LinkedList<WarframeAlert> alerts = WarframeUtil.getCurrentAlerts();
            alerts.removeIf(warframeAlert -> {
                boolean containsKeyword = false;
                for (String s : WarframeUtil.alertFilters)
                    if (warframeAlert.getMission().getReward().getAsString().contains(s))
                        containsKeyword = true;

                return !containsKeyword;
            });

            EmbedBuilder eb = new EmbedBuilder()
                    .withTitle("Warframe | Filtered Alerts")
                    .withColor(Visuals.getVibrantColor());

            for (WarframeAlert alert : alerts) {
                WarframeMission mission = alert.getMission();
                eb.appendField(mission.getNode() + " | " + mission.getType() + " | " + alert.getEta() + " remaining", mission.getReward().getAsString(), false);
            }

            if (alerts.size() > 0) //only send if not 0
                BotUtils.sendMessage(BotUtils.BOTTOM_TEXT, eb);
        };

        final ScheduledFuture<?> alertFilterUpdater = scheduler.scheduleAtFixedRate(alertFilter, 0, 15, MINUTES);
    }

    @EventSubscriber
    public void userJoin(UserJoinEvent event) {
        BotUtils.sendMessage(event.getGuild().getDefaultChannel(), "Welcome " + event.getUser().getName() + " to " + event.getGuild().getName() + "!");
    }

    @Override
    public String toString() {
        return "This is janky asf";
    }
}