package main.commands.nasa;

import main.Command;
import main.utility.metautil.BotUtils;
import main.utility.nasa.EPICMetadata;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class BlueMarble implements Command {
    private final String nasaLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e5/NASA_logo.svg/928px-NASA_logo.svg.png";

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String metadataJsonRaw = BotUtils.getJson("https://epic.gsfc.nasa.gov/api/natural");
        EPICMetadata[] array = BotUtils.gson.fromJson(metadataJsonRaw, EPICMetadata[].class);

        String imageIdentifier = array[0].getIdentifier();
        String year = imageIdentifier.substring(0,4);
        String month = imageIdentifier.substring(4, 6);
        String day = imageIdentifier.substring(6, 8);

        String imageUrl = "";

        if (args.size() == 1)
            imageUrl = "https://epic.gsfc.nasa.gov/archive/natural/"+ year+"/"+month+"/"+ day+"/png/epic_1b_"+ imageIdentifier +".png";
        else
            imageUrl = "https://epic.gsfc.nasa.gov/archive/enhanced/"+ year+"/"+month+"/"+ day+"/png/epic_RGB_" + imageIdentifier +".png";

        String date = array[0].getDate();

        EmbedBuilder eb = new EmbedBuilder()
                .withAuthorName("Nasa :: Blue Marble")
                .withAuthorIcon(nasaLogoUrl)
                .withAuthorUrl("https://visibleearth.nasa.gov/view_cat.php?categoryID=1484")
                .withImage(imageUrl)
                .withFooterText(date);

        BotUtils.send(event.getChannel(), eb);
    }

    @Override
    public String getDesc() {
        return "NASA - Shows most recent \"Blue Marble\"";
    }
}
