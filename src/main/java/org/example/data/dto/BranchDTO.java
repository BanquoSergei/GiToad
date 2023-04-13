package org.example.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.data.dto.views.ViewCommitDTO;
import org.kohsuke.github.GHBranch;

@Data
@NoArgsConstructor
public class BranchDTO {

    private String url;
    private String name;
    private boolean isProtected;

    private ViewCommitDTO currentCommit;
    public BranchDTO(GHBranch branch) {

        name = branch.getName();
        isProtected = branch.isProtected();
    }

}
