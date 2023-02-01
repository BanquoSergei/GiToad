package org.example.github.dto;

import lombok.Data;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHContent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public class GHContentDTO {

    private String name;

    private String downloadUrl;

    private String url;

    private boolean isFile;

    private long size;

    private List<GHContentDTO> files = new ArrayList<>();

    public GHContentDTO(GHContent file) throws IOException {

        name = file.getName() + "." + file.getType();
        downloadUrl = file.getDownloadUrl();
        url = file.getUrl();
        isFile = file.isFile();
        size = file.getSize();
        if(file.isDirectory())
            for(var f: file.listDirectoryContent())
                files.add(new GHContentDTO(f));
    }

    public GHContentDTO(GHCommit.File file) {

        name = file.getFileName();
        downloadUrl = file.getBlobUrl().toString();
        url = file.getRawUrl().toString();
        isFile = true;
    }
}
