package org.example.github.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.github.dto.deserializers.GHContentDTODeserializer;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHContent;

import java.io.IOException;

@Data
@NoArgsConstructor
@JsonDeserialize(using = GHContentDTODeserializer.class)
public class GHContentDTO {

    private String name;

    private String downloadUrl;

    private String url;

    private String path;

    private byte[] content;

    private String encoding;

    private long size;

    public GHContentDTO(GHContent file) throws IOException {

        var uri = file.getName();
        name = uri.substring(uri.lastIndexOf("/") + 1) + "." + file.getType();
        downloadUrl = file.getDownloadUrl();
        if(uri.contains("/"))
            path = uri.substring(0, uri.lastIndexOf("/"));
        content = file.read().readAllBytes();
        encoding = file.getEncoding();
        url = file.getUrl();
        size = file.getSize();
    }


    public GHContentDTO(GHCommit.File file) throws IOException {

        var uri = file.getFileName();
        name = uri.substring(uri.lastIndexOf("/") + 1);
        downloadUrl = file.getBlobUrl().toExternalForm();
        if(uri.contains("/"))
            path = uri.substring(0, uri.lastIndexOf("/"));
        url = file.getRawUrl().getRef();
    }
}
