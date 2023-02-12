package org.example.github.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.github.dto.deserializers.ViewCommitDTODeserializer;
import org.kohsuke.github.GHCommit;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@JsonDeserialize(using = ViewCommitDTODeserializer.class)
public class ViewCommitDTO {

    private Date date;

    private String message;

    private String sha;

    private String url;

    private String commiter;

    private List<GHContentDTO> files;


    public ViewCommitDTO(GHCommit.ShortInfo info) {

        date = info.getAuthoredDate();
        message = info.getMessage();
        sha = info.getSha();
        url = info.getUrl();
        commiter = info.getCommitter().getUsername();
    }
}
