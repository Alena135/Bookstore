package com.inv.withAuth.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents a paginated response containing a list of items of type {@code T}.
 * Includes information about the current page, total number of pages, and whether this is the last page.
 *
 * @param <T> the type of content in the paginated response
 */
@Getter
@Setter
public class PagedResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public PagedResponse(List<T> content, int pageNumber, int pageSize,
                         long totalElements, int totalPages, boolean last) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    public PagedResponse() {

    }
}