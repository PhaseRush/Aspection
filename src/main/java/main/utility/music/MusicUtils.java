package main.utility.music;

import sx.blah.discord.handle.impl.obj.ReactionEmoji;

import java.util.LinkedList;
import java.util.List;

public class MusicUtils {

    public static List<ReactionEmoji> floatingReactions;
    public static char[] nextTrackUnicodeArray = {'\u23ED', '\uFE0F'}; //no bueno

    static {

        floatingReactions = new LinkedList<>();
        floatingReactions.add(ReactionEmoji.of("\u25b6")); //play -- might also need to attack \ufe0f
        floatingReactions.add(ReactionEmoji.of("\u23f8")); //pause -- might also need to attack \ufe0f
        //floatingReactions.add(ReactionEmoji.of(String.valueOf(nextTrackUnicodeArray)));
        floatingReactions.add(ReactionEmoji.of(String.valueOf("\u23E9"))); //also next track emoji
        floatingReactions.add(ReactionEmoji.of("\uD83C\uDDF6")); //regional Q

    }
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////