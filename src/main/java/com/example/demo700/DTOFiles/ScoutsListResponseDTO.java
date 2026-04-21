package com.example.demo700.DTOFiles;

import java.util.List;

public class ScoutsListResponseDTO {
	private List<ScoutResponse> content;
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;
	private boolean last;
	private boolean first;
	private int numberOfElements;
	private String nextLink;
	private String prevLink;
	private String firstLink;
	private String lastLink;

	public ScoutsListResponseDTO() {
	}

	public ScoutsListResponseDTO(List<ScoutResponse> content, int page, int size, long totalElements, int totalPages,
			boolean last, boolean first, int numberOfElements) {
		this.content = content;
		this.page = page;
		this.size = size;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.last = last;
		this.first = first;
		this.numberOfElements = numberOfElements;
	}

	// Getters and Setters
	public List<ScoutResponse> getContent() {
		return content;
	}

	public void setContent(List<ScoutResponse> content) {
		this.content = content;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public String getNextLink() {
		return nextLink;
	}

	public void setNextLink(String nextLink) {
		this.nextLink = nextLink;
	}

	public String getPrevLink() {
		return prevLink;
	}

	public void setPrevLink(String prevLink) {
		this.prevLink = prevLink;
	}

	public String getFirstLink() {
		return firstLink;
	}

	public void setFirstLink(String firstLink) {
		this.firstLink = firstLink;
	}

	public String getLastLink() {
		return lastLink;
	}

	public void setLastLink(String lastLink) {
		this.lastLink = lastLink;
	}

	@Override
	public String toString() {
		return "ScoutsListResponseDTO [content=" + content + ", page=" + page + ", size=" + size + ", totalElements="
				+ totalElements + ", totalPages=" + totalPages + ", last=" + last + ", first=" + first
				+ ", numberOfElements=" + numberOfElements + ", nextLink=" + nextLink + ", prevLink=" + prevLink
				+ ", firstLink=" + firstLink + ", lastLink=" + lastLink + "]";
	}

}