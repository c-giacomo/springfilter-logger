package com.filter.logging.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class AnswerWrapperDTO<T> extends HeaderDTO {

	private T data;
	
	private PagedList pagedList = new PagedList();
	
	public AnswerWrapperDTO() {}
	
	public AnswerWrapperDTO(T data) {
		super();
		this.data = data;
	}
		
	public AnswerWrapperDTO<T> dato(T data) {
		this.data = data;
		return this;
	}
	
	public AnswerWrapperDTO(T data, Integer pageSize, Integer pageIndex, Integer totalCount, Integer totalPage, Integer currentPage) {
		this.data = data;
		this.pagedList.pageSize = pageSize;
		this.pagedList.pageIndex = pageIndex;
		this.pagedList.totalCount = totalCount;
		this.pagedList.totalPages = totalPage;
		this.pagedList.currentPage = currentPage;
	}
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	class PagedList {
		private long currentPage;
		private long pageIndex;
		private long pageSize;
		private long totalCount;
		private long totalPages;
		
		public PagedList() {

		}
		
		public long getCurrentPage() {
			return currentPage;
		}
		public void setCurrentPage(long currentPage) {
			this.currentPage = currentPage;
		}
		
		public long getPageIndex() {
			return pageIndex;
		}
		public void setPageIndex(long pageIndex) {
			this.pageIndex = pageIndex;
		}
		
		public long getPageSize() {
			return pageSize;
		}
		public void setPageSize(long pageSize) {
			this.pageSize = pageSize;
		}
		
		public long getTotalCount() {
			return totalCount;
		}
		public void setTotalCount(long totalCount) {
			this.totalCount = totalCount;
		}
		
		public long getTotalPages() {
			return totalPages;
		}
		public void setTotalPages(long totalPages) {
			this.totalPages = totalPages;
		}
		
		public boolean getHasPreviousPage() {
			return (this.pageIndex > 0);
		}

		public long getPreviousPage() {
			return this.currentPage - 1;
		}
		
		public boolean getHasNextPage() {
			 return (this.pageIndex + 1 < this.totalPages);
		}

		public long getNextPage() {
			return this.currentPage + 1;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(data, pagedList);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnswerWrapperDTO other = (AnswerWrapperDTO) obj;
		return Objects.equals(data, other.data)
				&& Objects.equals(pagedList.getTotalPages(), other.pagedList.getTotalPages())
				&& Objects.equals(pagedList.getTotalCount(), other.pagedList.getTotalCount());
	}
}
