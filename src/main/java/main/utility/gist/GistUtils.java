package main.utility.gist;


import main.utility.BotUtils;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.GistService;

import java.io.IOException;
import java.util.Collections;

public class GistUtils {
    public static GitHubClient client = new GitHubClient().setCredentials(BotUtils.DEV_GITHUB_NAME, BotUtils.DEV_GITHUB_PASSWORD);


    public static String makeGistGetUrl(String fileName, String fileDescription, String fileContent) {
        Gist gist = new Gist().setDescription(fileDescription);
        GistFile file = new GistFile().setContent(fileContent);
        gist.setFiles(Collections.singletonMap(fileName, file))
                .setPublic(true);

        try {
            gist = new GistService(client).createGist(gist);
            System.out.println(gist.getUrl());
            return gist.getUrl();
        } catch (IOException e) {
            System.out.println("make gist error");
            e.printStackTrace();
        }
        return null;
    }
}