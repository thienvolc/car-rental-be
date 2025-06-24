package fun.dashspace.carrentalsystem.dto.common.response;

import fun.dashspace.carrentalsystem.dto.common.metadata.Metadata;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PageMetadata implements Metadata {
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}
