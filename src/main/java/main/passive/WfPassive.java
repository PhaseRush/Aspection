package main.passive;

import main.Main;
import main.utility.BotUtils;
import main.utility.Visuals;
import main.utility.WarframeUtil;
import main.utility.warframe.wfstatus.WarframeCetusTimeObject;
import main.utility.warframe.wfstatus.alerts.WarframeAlert;
import main.utility.warframe.wfstatus.alerts.WarframeMission;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.util.EmbedBuilder;

import java.time.Instant;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class WfPassive {
    public static ScheduledFuture<?> cetusStatusUpdater = null, alertFilterUpdater = null;

    @EventSubscriber
    public void warframeCetusUpdater(ReadyEvent event) {
        // cetusTimePresense();
    }

    public static void cetusTimePresense() {
        BotUtils.setBottomText();
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // calculate initial delay
        Instant instant = Instant.parse(WarframeUtil.getCetus().getExpiry());

        try {
            final Runnable cetusTimeRunner = () -> {
                WarframeCetusTimeObject cetus = WarframeUtil.getCetus();

                //BotUtils.send(BotUtils.BOTTOM_TEXT, WarframeUtil.cetusCycleString());
                Main.client.changePresence(StatusType.ONLINE, ActivityType.WATCHING, (cetus.isDay() ? " the Sun " : " Lua ") + " :: " + cetus.getShortString());

                //int minute = LocalDateTime.now().getMinute();
                //System.out.println("Updated Cetus Status " + LocalDateTime.now().getHour() + ":" + (minute < 10 ? "0" + minute : minute));
            };

           cetusStatusUpdater = scheduler.scheduleAtFixedRate(cetusTimeRunner, 0/*elapseMillis/1000*/, 60/*150*60*/, SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("warframe cetus passive time error");
        }
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
                    .withTitle("Warframe :: Filtered Alerts")
                    .withColor(Visuals.getVibrantColor());

            if (alerts.size() == 0) return;

            for (WarframeAlert alert : alerts) {
                WarframeMission mission = alert.getMission();
                eb.appendField(mission.getNode() + " | " + mission.getType() + " | " + alert.getEta() + " remaining", mission.getReward().getAsString(), false);
            }

            if (BotUtils.BOTTOM_TEXT != null) //bottom text is null on startup, throwing NPE.
                BotUtils.send(BotUtils.BOTTOM_TEXT, eb);
        };

        alertFilterUpdater = scheduler.scheduleAtFixedRate(alertFilter, 0, 15, MINUTES);
    }
}
