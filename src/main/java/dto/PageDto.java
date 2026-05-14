package dto;

import java.util.List;

public record PageDto<T>(
        List<T> content,
        int currentPage,
        int totalPages,
        long totalElements
) {}