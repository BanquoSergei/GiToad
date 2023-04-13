package org.example.request_processing.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.data.dto.views.FileViewDTO;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;

public class GitoadHttpClient {

    private static final String GET_FILES = "https://api.github.com/repos/%s/%s/git/trees/%s?recursive=0";

    private static final RestTemplate client = new RestTemplate();

    public static List<FileViewDTO> getFiles(String login, String repo, String branch) {

        List<Map> rawResponse = client.getForObject(
                String.format(GET_FILES, login, repo, branch),
                TreeResponse.class
        ).tree();
        var files = rawResponse.stream()
                .filter(r -> r.get("type").equals("blob"))
                .map(r -> new FileViewDTO((String) r.get("path"), (String) r.get("sha"), true))
                .toList();

        return files;
    }

}

record TreeResponse(String sha, String url, List<Map> tree, boolean truncated) {

}
