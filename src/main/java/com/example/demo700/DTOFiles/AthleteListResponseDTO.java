package com.example.demo700.DTOFiles;

import java.util.List;

public class AthleteListResponseDTO {
    private List<AthleteRequestDTO> athletes;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
    
    // Navigation links - Users just follow these!
    private String selfLink;
    private String nextLink;
    private String prevLink;
    private String firstLink;
    private String lastLink;
    
    // Auto-detection metadata
    private String message;
    private String suggestedPageSize;
    private String navigationInstructions;
    private String currentUrl;
    private String searchParams;
    
    public AthleteListResponseDTO(List<AthleteRequestDTO> athletes, int currentPage, 
                                   int pageSize, long totalElements, int totalPages) {
        this.athletes = athletes;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasNext = currentPage < totalPages - 1;
        this.hasPrevious = currentPage > 0;
        this.navigationInstructions = "Use 'nextLink' for next page, 'prevLink' for previous page. No page numbers needed!";
    }
    
    // Getters and setters
    public List<AthleteRequestDTO> getAthletes() { return athletes; }
    public void setAthletes(List<AthleteRequestDTO> athletes) { this.athletes = athletes; }
    
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
    
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    
    public boolean isHasNext() { return hasNext; }
    public void setHasNext(boolean hasNext) { this.hasNext = hasNext; }
    
    public boolean isHasPrevious() { return hasPrevious; }
    public void setHasPrevious(boolean hasPrevious) { this.hasPrevious = hasPrevious; }
    
    public String getSelfLink() { return selfLink; }
    public void setSelfLink(String selfLink) { this.selfLink = selfLink; }
    
    public String getNextLink() { return nextLink; }
    public void setNextLink(String nextLink) { this.nextLink = nextLink; }
    
    public String getPrevLink() { return prevLink; }
    public void setPrevLink(String prevLink) { this.prevLink = prevLink; }
    
    public String getFirstLink() { return firstLink; }
    public void setFirstLink(String firstLink) { this.firstLink = firstLink; }
    
    public String getLastLink() { return lastLink; }
    public void setLastLink(String lastLink) { this.lastLink = lastLink; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getSuggestedPageSize() { return suggestedPageSize; }
    public void setSuggestedPageSize(String suggestedPageSize) { this.suggestedPageSize = suggestedPageSize; }
    
    public String getNavigationInstructions() { return navigationInstructions; }
    public void setNavigationInstructions(String navigationInstructions) { 
        this.navigationInstructions = navigationInstructions; 
    }
    
    public String getCurrentUrl() { return currentUrl; }
    public void setCurrentUrl(String currentUrl) { this.currentUrl = currentUrl; }
    
    public String getSearchParams() { return searchParams; }
    public void setSearchParams(String searchParams) { this.searchParams = searchParams; }
}