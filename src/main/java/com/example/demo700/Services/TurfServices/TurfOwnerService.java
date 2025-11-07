package com.example.demo700.Services.TurfServices;

import java.util.List;

import com.example.demo700.Models.Turf.Owner;

public interface TurfOwnerService {
	
	public Owner addOwner(Owner owner);
	public List<Owner> seeAllOwner();
	public Owner updateOwnerData(String id, Owner updatedOwner) throws Exception;
	public boolean removeOwner(String id, String ownId);
	public Owner findByUserId(String userId);
	List<Owner> searchByName(String name);
	List<Owner> searchByPhone(String phone);

}
