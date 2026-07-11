package virement.masse.demo.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PacsFilePageResponse {

    private List<PacsFileResponse> content;

    private int page;

    private int size;

    private long totalElements;

    private int totalPages;

}