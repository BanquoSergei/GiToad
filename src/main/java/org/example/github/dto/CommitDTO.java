package org.example.github.dto;

import lombok.Data;
import org.kohsuke.github.GHCommit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CommitDTO {

    private GHUserDTO author;

    private List<GHContentDTO> files;

    private List<CommentDTO> comments = new ArrayList<>();

    private Date date;

    private String url;

    private int changed;

    private int deleted;

    private int added;

    public CommitDTO(GHCommit commit) throws IOException {

        author = new GHUserDTO(commit.getAuthor());
        files = commit.getFiles().stream().map(GHContentDTO::new).toList();

        for(var comment: commit.listComments())
            comments.add(new CommentDTO(comment));

        date = commit.getCommitDate();
        url = commit.getUrl().toString();
        changed = commit.getLinesChanged();
        added = commit.getLinesAdded();
        deleted = commit.getLinesDeleted();
    }
}
