package fun.dashspace.carrentalsystem.dto.response.common;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PageMetadata implements Metadata {
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}
