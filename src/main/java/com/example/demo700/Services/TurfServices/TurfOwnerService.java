package com.example.demo700.Services.TurfServices;

import java.util.List;

import com.example.demo700.DTOFiles.TurfOwnerResponse;
import com.example.demo700.Models.Turf.Owner;

public interface TurfOwnerService {
	
	public Owner addOwner(Owner owner);
	public List<TurfOwnerResponse> seeAllOwner();
	public Owner updateOwnerData(String id, Owner updatedOwner) throws Exception;
	public boolean removeOwner(String id, String ownId);
	public TurfOwnerResponse findByUserId(String userId);
	List<TurfOwnerResponse> searchByName(String name);
	List<TurfOwnerResponse> searchByPhone(String phone);

}
