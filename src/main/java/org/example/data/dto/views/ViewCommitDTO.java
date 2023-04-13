package org.example.data.dto.views;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.data.dto.FileDTO;
import org.kohsuke.github.GHCommit;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ViewCommitDTO {

    private Date date;

    private String message;

    private String sha;

    private String url;

    private String commiter;

    private List<FileDTO> files;


    public ViewCommitDTO(GHCommit.ShortInfo info) {

        date = info.getAuthoredDate();
        message = info.getMessage();
        sha = info.getSha();
        url = info.getUrl();
        commiter = info.getCommitter().getUsername();
    }
}
