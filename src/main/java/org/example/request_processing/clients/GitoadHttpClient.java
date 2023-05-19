package org.example.request_processing.clients;

import org.example.data.dto.views.FileViewDTO;
import org.example.utils.crypt.Cryptographer;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTreeEntry;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GitoadHttpClient {

    private static final String FILES = "https://api.github.com/repos/%s/%s/git/trees/%s?recursive=0";

    private final Cryptographer cryptographer;

    private byte[] token;
    private final RestTemplate client = new RestTemplate();

    public GitoadHttpClient(Cryptographer cryptographer, byte[] token) {
        this.cryptographer = cryptographer;
        this.token = token;
    }

    public List<FileViewDTO> getFiles(String login, String repo, String branch) {

        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("Authorization", "Bearer " + new String(cryptographer.decrypt(token)));
        var request = new RequestEntity<>(
                headers,
                HttpMethod.GET,
                URI.create(String.format(FILES, login, repo, branch))
        );


        try {
            var response = client.exchange(request, TreeResponse.class);

            if(response.getStatusCode().isError())
                throw new IllegalArgumentException();  // TO DO
            var files = response.getBody().tree().stream()
                    .filter(r -> r.get("type").equals("blob"))
                    .map(r -> new FileViewDTO((String) r.get("path"), (String) r.get("sha"), true))
                    .toList();

            return files;
        } catch (HttpClientErrorException e) {
            return Collections.emptyList();
        }
    }

    public List<FileViewDTO> getFiles(GHRepository repo, String sha) {
        try {

            var files = repo.getTree(sha).getTree().stream()
                    .filter(r -> r.getType().equals("blob"))
                    .map(r -> new FileViewDTO(r.getPath(), r.getSha(), true))
                    .toList();

            return files;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}

record TreeResponse(String sha, String url, List<Map> tree, boolean truncated) {

}
