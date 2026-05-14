package com.example.demo700.Services.TurfServices;

import java.util.List;
import java.util.Optional;

import com.example.demo700.DTOFiles.VenueListResponseDTO;
import com.example.demo700.DTOFiles.VenueResponse;
import com.example.demo700.Models.Turf.Venue;

public interface VenueService {
	Venue createVenue(Venue v);

	VenueResponse getVenueById(String id);

	List<VenueResponse> searchByAddress(String q);
	List<VenueResponse> getAllVenue();
	List<VenueResponse> findByVenueOwner(String ownerId);
	List<VenueResponse> findByVenueOwners(List<String> ownersId);
	Venue updateVeue(String id, Venue updateVenue);
	boolean removeVenue(String id, String userId);
	VenueResponse findByName(String name);
	List<VenueResponse> findByNameContainingIgnoreCase(String name);
	VenueResponse findBySpecificUser(String venueId, String userId);
	
	  // ========== NEW PAGINATED METHODS ==========
    VenueListResponseDTO getAllVenuesPaginated(int page, int size, String sortBy, String sortDir);
    VenueListResponseDTO searchVenuesByAddressPaginated(String address, int page, int size, String sortBy, String sortDir);
    VenueListResponseDTO searchVenuesByNamePaginated(String name, int page, int size, String sortBy, String sortDir);
    VenueListResponseDTO getVenuesByOwnerPaginated(String ownerId, int page, int size, String sortBy, String sortDir);
    VenueListResponseDTO getVenuesByOwnersPaginated(List<String> ownersId, int page, int size, String sortBy, String sortDir);
	
}
