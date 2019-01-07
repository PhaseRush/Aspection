package main.commands.utilitycommands;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteWaifus implements Command {
    private static final String MUDAMAID_ID = "264213620026638336"; //527432104204697600
    private static final List<String> filterCommands = Arrays.asList("$w", "$mu", "$h", "$i", "$im", "$wg");

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        // ask for confirmation
        EmbedBuilder confirmEb = new EmbedBuilder()
                .withTitle("Confirm Delete?")
                .withColor(Color.BLACK)
                .withDesc("Confirm channel cleanup");

        IMessage confirmMsg = BotUtils.sendGet(event.getChannel(), confirmEb);

        IListener reactionListener = (IListener<ReactionAddEvent>) reactionEvent -> {
            if (reactionEvent.getUser().equals(event.getAuthor()) && reactionEvent.getMessage().getStringID().equals(confirmMsg.getStringID())) {
                String emojiName = reactionEvent.getReaction().getEmoji().getName();
                switch (emojiName) {
                    case "\u2705": // check mark
                        List<IMessage> msgs = event.getChannel().getMessageHistoryTo(Instant.now().minus(1, ChronoUnit.HOURS));
                        runDelete(event, msgs);
                        break;
                    case "\u274c": // x
                        break;
                    default:
                        BotUtils.send(event.getChannel(), "Not a valid reaction, exiting cleanup");
                        break;
                }
                if (!confirmMsg.isDeleted()) //just in case
                    confirmMsg.delete();
            }
        };

        // react with emojis
        BotUtils.reactAllEmojis(confirmMsg, Arrays.asList(ReactionEmoji.of("\u2705"), ReactionEmoji.of("\u274c")));

        // register listener
        event.getClient().getDispatcher().registerListener(reactionListener);

        // unregister listener after 10000 ms
        BotUtils.unregisterListener(reactionListener, 10000, confirmMsg);
    }

    public void runDelete(MessageReceivedEvent event, List<IMessage> msgs) {
        event.getChannel().bulkDelete(
                msgs.stream()
                        .filter(msg -> msg.getAuthor().getStringID().equals(MUDAMAID_ID) ||
                                filterCommands.contains(msg.getContent()))
                        .collect(Collectors.toList()));
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return true; // will add other stuff later
    }

    @Override
    public String getDesc() {
        return "cleans up a text-chat after someone fucks it up again";
    }
}