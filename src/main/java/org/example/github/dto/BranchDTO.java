package org.example.github.dto;

import lombok.Data;
import org.kohsuke.github.GHBranch;

@Data
public class BranchDTO {

    private String name;
    private boolean isProtected;
    public BranchDTO(GHBranch branch) {

        name = branch.getName();
        isProtected = branch.isProtected();
    }
}
