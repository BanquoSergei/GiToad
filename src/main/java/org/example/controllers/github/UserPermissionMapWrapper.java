package org.example.controllers.github;

import lombok.Data;
import lombok.Getter;
import org.kohsuke.github.GHOrganization.Permission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class UserPermissionMapWrapper {

    private Map<String, Permission> tags = new HashMap<>();

    public void setTags(Map<String, String> tags) {

//        Map<Permission, List<String>> map = tags.values().stream().map(Permission::valueOf).collect(Collectors.groupingBy(p -> p, p -> tags.));

        for(var login: tags.keySet())
            this.tags.put(login, Permission.valueOf(tags.get(login)));
    }
}
