package org.example.request_processing.wrappers;

import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHOrganization.RepositoryRole;
import org.kohsuke.github.GHUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserPermissionMapWrapper {

    private Map<GHOrganization.RepositoryRole, List<GHUser>> tags = new HashMap<>();

    public void setTags(Map<String, String> tags) {

        for(var login: tags.keySet()) {

            var role = RepositoryRole.custom(tags.get(login));
            this.tags.putIfAbsent(role, new ArrayList<>());
            this.tags.get(role).add(new User(login));
        }
    }

    public Map<RepositoryRole, List<GHUser>> getTags() {
        return tags;
    }
}