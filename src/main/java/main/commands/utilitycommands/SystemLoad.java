package main.commands.utilitycommands;

import main.Command;
import main.CommandManager;
import main.Main;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;

import java.lang.management.OperatingSystemMXBean;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class SystemLoad implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        // check special request
        if (!args.isEmpty() && args.get(0).equalsIgnoreCase("syncmap")){
            runSyncMap(event);
            return;
        }

        OperatingSystemMXBean osBean = Main.osBean;
        Runtime r = Runtime.getRuntime();
        double maxM = r.maxMemory() / 1E6;

        String msg = "```" +
                "System Load:  \t\t" + osBean.getSystemLoadAverage()*100 + " %" +
                "\nRam (MB): \t\t\t" + (int) (maxM - r.freeMemory() / 1E6) + " / " + (int) maxM +
                "\nArch: \t\t\t\t" + osBean.getArch() +
                "\nName: \t\t\t\t" + osBean.getName() +
                "\nVersion:  \t\t\t" + osBean.getVersion() +
                "\nAvail Cores:  \t\t" + osBean.getAvailableProcessors()
                + "```";

        BotUtils.send(event.getChannel(), msg);
    }

    private void runSyncMap(MessageReceivedEvent event) {
        if (CommandManager.syncExecuteMap.entrySet().isEmpty()) {
            BotUtils.send(event.getChannel(), "No servers have synchronous maps");
            return;
        }

        StringBuilder sb = new StringBuilder("The following servers have synchronous maps:```js\n");

        for (Map.Entry e : CommandManager.syncExecuteMap.entrySet()) {
            IGuild guild = Main.client.getGuildByID(Long.valueOf((String) e.getKey()));
            ExecutorService exe = (ExecutorService) e.getValue();
            sb.append(guild.getName()).append(" @ ")
                    .append(guild.getStringID()).append(" : ")
                    .append(exe.isTerminated() ? "TERMINATED" : "RUNNING")
                    .append("\n");
        }

        sb.append("```");

        BotUtils.send(event.getChannel(), sb.toString());
    }

    @Override
    public String getDesc() {
        return "How hard I'm working.";
    }
}