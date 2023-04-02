package org.example.data.github.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.data.github.dto.deserializers.BranchDTODeserializer;
import org.kohsuke.github.GHBranch;

@Data
@NoArgsConstructor
@JsonDeserialize(using = BranchDTODeserializer.class)
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
