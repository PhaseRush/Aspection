package main.utility;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import info.debatty.java.stringsimilarity.Levenshtein;
import main.utility.metautil.BotUtils;
import main.utility.warframe.market.item.WarframeItem;
import main.utility.warframe.market.item.WarframeItemPayloadContainer;
import main.utility.warframe.market.itemdetail.WarframeDetailedItem;
import main.utility.warframe.market.itemdetail.WarframeItemDetailPayloadContainer;
import main.utility.warframe.wfstatus.WarframeCetusTimeObject;
import main.utility.warframe.wfstatus.WarframeOrbVallisCycle;
import main.utility.warframe.wfstatus.alerts.WarframeAlert;
import main.utility.warframe.wfstatus.alerts.WarframeMission;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import sx.blah.discord.util.EmbedBuilder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class WarframeUtil {
    private static final String[] dayActivities = {
            "Go mining",
            "Go do bounties", //finally fixed this fucking typo
            "Go catch some Tralok.",
            "Go catch some Mawfish."};

    private static final String[] nightActivities = {
            "Go hunt some eidolons.",
            "Go catch some Cuthol",
            "Go catch some Glappid."
    };
    public static final String[] alertFilters = {
            "Nitain",
            "Riven",
            "Kavat"
            //"skin"
    };

    /**
     * will add more states to use
     */
    public enum PlayerState {
        ONLINE("online"),
        INGAME("ingame");

        private String state;

        PlayerState(String s) {
            this.state = s;
        }

        public String val() {
            return state;
        }
    }

    public static List<String> getIntendedStrings(String userString) {
        Map<String, Double> levenMap = new HashMap<>();

        Levenshtein leven = new Levenshtein();

        for (String s : WarframeUtil.getAllItemList()) {
            levenMap.put(s, leven.distance(s.toLowerCase(), userString.toLowerCase()));
        }

        Map<String, Double> sortedLeven = BotUtils.sortMap(levenMap, true, true); //linkedHashMap
        List<String> sortedNames = new LinkedList<>();

        if (new LinkedList<>(sortedLeven.values()).get(0).equals(Double.valueOf("0")))
            sortedNames.add("Index 1 is a perfect match");

        sortedNames.addAll(sortedLeven.keySet());

        return sortedNames;
    }

    //https://www.mkyong.com/java/how-to-sort-a-map-in-java/
//    public static Map<String, Double> sortByValue(Map<String, Double> unsortMap) {
//
//        // 1. Convert Map to List of Map
//        List<Map.Entry<String, Double>> list = new LinkedList<>(unsortMap.entrySet());
//
//        // 2. Sort list with Collections.sort(), provide a custom Comparator
//        //    Try switch the o1 o2 position for a different order
//        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
//            public int compare(Map.Entry<String, Double> o1,
//                               Map.Entry<String, Double> o2) {
//                return (o1.getValue()).compareTo(o2.getValue());
//            }
//        });
//
//        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
//        Map<String, Double> sortedMap = new LinkedHashMap<>();
//        for (Map.Entry<String, Double> entry : list) {
//            sortedMap.put(entry.getKey(), entry.getValue());
//        }
//
//        return sortedMap;
//    }

    public static EmbedBuilder generateAlertsEmbed() {
        List<WarframeAlert> alerts = getCurrentAlerts();

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Warframe | Alerts")
                .withColor(Visuals.getRandVibrantColour());

        for (WarframeAlert alert : alerts) {
            WarframeMission mission = alert.getMission();
            eb.appendField(mission.getNode() + " | " + mission.getType() + " | " + alert.getEta() + " remaining", mission.getReward().getAsString(), false);
        }
        return eb;
    }

    public static List<WarframeAlert> getCurrentAlerts() {
        String json = BotUtils.getStringFromUrl("https://api.warframestat.us/pc/alerts");
        Type alertListType = new TypeToken<List<WarframeAlert>>() {
        }.getType();
        return new Gson().fromJson(json, alertListType);
    }

    public static String getItemUrlName(String intended) {
        String s = "";
        String json = BotUtils.getStringFromUrl("https://api.warframe.market/v1/items");
        WarframeItemPayloadContainer obj = new Gson().fromJson(json, WarframeItemPayloadContainer.class);
        for (WarframeItem i : obj.getPayload().getItems()) {
            if (i.getItem_name().equals(intended))
                s = i.getUrl_name();
        }
        return s;
    }

    //@org.jetbrains.annotations.NotNull
    public static String getItemImageUrl(String intendedName) {
        //https://warframe.market/static/assets/icons/en/thumbs/Akbronco_Prime_Set.34b5a7f99e5f8c15cc2039a76c725069.128x128.png
        String jsonURL = "https://api.warframe.market/v1/items/" + getItemUrlName(intendedName);
        WarframeItemDetailPayloadContainer payloadContainer = new Gson().fromJson(BotUtils.getStringFromUrl(jsonURL), WarframeItemDetailPayloadContainer.class);
        WarframeDetailedItem item = payloadContainer.getPayload().getItem().getItems_in_set()[0];
        return "https://warframe.market/static/assets/" + item.getThumb();
    }

    //jank overload. but im not a google engineer what did you expect?
    public static String getItemImageUrl(String urlName, boolean isUrlName) {
        return getItemUrlName(getIntendedStrings(urlName).get(0)); //might actually work though
    }

    @Deprecated
    public static String getAllItemJson() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://warframe.market/api/items")
                .header("Platform", "pc")
                .header("Language", "en")
                .header("Accepts", "application/json")
                //.header("Content-Type", "application/json") //makes it sad
                .build();

        try (Response response = client.newCall(request).execute()) {
            //Aspect.LOG.info(response.body().string());
            String fullResponse = response.body().string();
            int payloadPosition = fullResponse.indexOf("payload");
            String startSubstring = "{\"" + fullResponse.substring(payloadPosition);
            String[] newLineSplit = startSubstring.split("\n");
            return newLineSplit[0].substring(0, newLineSplit[0].length() - 9);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static List<String> getAllItemList() {
        // return ReadWrite.readFromFileToStringList(path);
        List<String> allItemNames = new ArrayList<>();
        String json = BotUtils.getStringFromUrl("https://api.warframe.market/v1/items");
        WarframeItemPayloadContainer obj = new Gson().fromJson(json, WarframeItemPayloadContainer.class);
        for (WarframeItem i : obj.getPayload().getItems()) {
            allItemNames.add(i.getItem_name());
        }
        return allItemNames;
    }

    //Cetus cycle
    public static String cetusCycleString() {
        WarframeCetusTimeObject cetus = getCetus();
        return "It is currently " + (cetus.isDay() ?
                "day. It will be night in " + cetus.getTimeLeft() + "\n" + BotUtils.getRandStrArr(dayActivities) :
                "night. It will be day in " + cetus.getTimeLeft() + "\n" + BotUtils.getRandStrArr(nightActivities));
    }

    public static String orbVallisCycleString() {
        WarframeOrbVallisCycle orbVallis = getOrbVallis();
        return "It is currently " + (orbVallis.isWarm() ?
                "warm. It will be cold in " :
                "cold. It will be warm in ") + orbVallis.getTimeLeft();
    }

    public static WarframeCetusTimeObject getCetus() {
        return BotUtils.jsonUrlToPojo("https://api.warframestat.us/pc/cetusCycle", WarframeCetusTimeObject.class);
    }

    public static WarframeOrbVallisCycle getOrbVallis() {
        return BotUtils.jsonUrlToPojo("https://api.warframestat.us/pc/vallisCycle", WarframeOrbVallisCycle.class);
    }
}
