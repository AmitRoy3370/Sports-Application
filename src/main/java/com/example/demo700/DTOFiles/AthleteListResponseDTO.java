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
    
    public AthleteListResponseDTO(List<AthleteRequestDTO> athletes, int currentPage, 
                                   int pageSize, long totalElements, int totalPages) {
        this.athletes = athletes;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasNext = currentPage < totalPages - 1;
        this.hasPrevious = currentPage > 0;
    }
    
    // Getters and setters
    public List<AthleteRequestDTO> getAthletes() { 
        return athletes; 
    }
    
    public void setAthletes(List<AthleteRequestDTO> athletes) { 
        this.athletes = athletes; 
    }
    
    public int getCurrentPage() { 
        return currentPage; 
    }
    
    public void setCurrentPage(int currentPage) { 
        this.currentPage = currentPage; 
    }
    
    public int getPageSize() { 
        return pageSize; 
    }
    
    public void setPageSize(int pageSize) { 
        this.pageSize = pageSize; 
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
    
    public boolean isHasNext() { 
        return hasNext; 
    }
    
    public void setHasNext(boolean hasNext) { 
        this.hasNext = hasNext; 
    }
    
    public boolean isHasPrevious() { 
        return hasPrevious; 
    }
    
    public void setHasPrevious(boolean hasPrevious) { 
        this.hasPrevious = hasPrevious; 
    }
}
