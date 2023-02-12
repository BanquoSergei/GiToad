package org.example.github.dto;

import lombok.Data;
import org.kohsuke.github.GHUser;

import java.io.IOException;

@Data
public class GHUserDTO {

    private String bio;

    private String name;

    private int countFollowers;

    private String avatarUrl;

    private String url;

    public GHUserDTO(GHUser user) throws IOException {

        bio = user.getBio();
        avatarUrl = user.getAvatarUrl();
        name = user.getName();
        countFollowers = user.getFollowersCount();
        url = user.getUrl().toString();
    }
}
