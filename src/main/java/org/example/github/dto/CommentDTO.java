package org.example.github.dto;

import lombok.Data;
import org.kohsuke.github.GHCommitComment;
import org.kohsuke.github.GHReaction;

import javax.xml.stream.events.Comment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CommentDTO {

    private String body;

    private GHUserDTO author;

    private List<CommentDTO> reactions = new ArrayList<>();

    private Date date;

    public CommentDTO(GHCommitComment comment) throws IOException {

        body = comment.getBody();
        author = new GHUserDTO(comment.getUser());
        date = comment.getCreatedAt();

        for(var reaction: comment.listReactions())
            reactions.add(new CommentDTO(reaction));
    }

    public CommentDTO(GHReaction reaction) throws IOException {

        author = new GHUserDTO(reaction.getUser());
        body = reaction.getContent().getContent();
    }
}
