package main.commands.utilitycommands.metautil;

import main.Command;
import main.CommandManager;
import main.Main;
import main.utility.BotUtils;
import main.utility.grapher.LineChart;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Stats implements Command {
    static double[] timeScale = {
            -15, -14.5, -14, -13.5, -13, -12.5, -12, -11.5, -11, -10.5,
            -10, -9.5, -9, -8.5, -8, -7.5, -7, -6.5, -6, -5.5, -5,
            -4.5, -4, -3.5, -3, -2.5, -2, -1.5, -1, -.5, 0};

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String imgDir = generateImage();
        File imageFile = new File(imgDir);
        BotUtils.send(event.getChannel(), generateStats(imageFile.getName().replace(".png", "")), imageFile);


        System.out.println(imageFile.getName().replace(".png", ""));
        //imageFile.delete();
    }

    private EmbedBuilder generateStats(String imgDir) {
        return new EmbedBuilder()
                .withTitle("Aspect v2.0")
                .withColor(Color.BLACK)
                .withThumbnail(Main.client.getApplicationIconURL())
                .withDesc(generateDesc())
                .withFooterIcon(Main.client.getUserByID(BotUtils.DEV_DISCORD_LONG_ID).getAvatarURL())
                .withFooterText("Built with love. " + BotUtils.GITHUB_URL_SHORT)

                //
                .withImage("attachment://" + imgDir)

                // fields
                .appendField("Guilds", String.valueOf(Main.client.getGuilds().size()), true)
                .appendField("Users","Unique: " + Main.client.getGuilds().stream().flatMap(g -> g.getUsers().stream()).collect(Collectors.toSet()).size() +
                        "\nTotal: " + Main.client.getGuilds().stream().mapToInt(u -> u.getUsers().size()).sum(), true)

                .appendField("Commands available", "Unique: "+ new HashSet<>(CommandManager.commandMap.values()).size() +
                        "\nAliases: " + CommandManager.commandMap.keySet().size(), true);
    }

    private String generateDesc() {
        return Uptime.genUptime() + "\n" +         // uptime
                CpuStats.genMessage() + '\n' +      // general stats
                CpuStats.genSyncMap().orElse("");     // syncmap
    }

    /*
    generates a line plot of memory and cpu vs time for last 15 minutes

    return filepath to image
     */
    private String generateImage() {
        //double[] cpuVals = ScheduledActions.cpuQueue.toArray();
        //double[] memVals = ScheduledActions.memQueue.toArray();
        double[] cpuVals = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        double[] memVals = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        return LineChart.generateTwoPlot("Cpu", "Ram", timeScale, timeScale, cpuVals, memVals);
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public String getSyntax() {
        return null;
    }
}
