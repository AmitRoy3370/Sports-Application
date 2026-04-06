package com.example.demo700.Services.UserActiveServices;

import java.util.List;

import com.example.demo700.DTOFiles.UserActiveResponseDTO;
import com.example.demo700.Model.UserActiveModel.UserActive;

public interface UserActiveService {

	public UserActive addUserActive(UserActive userActive);

	public UserActive updateUserActive(UserActive userActive, String userId, String id);

	public UserActiveResponseDTO findById(String id);

	public List<UserActiveResponseDTO> findAll();

	public UserActiveResponseDTO findByUserId(String userId);

	public List<UserActiveResponseDTO> findByActive(boolean active);

	public boolean removeUserActive(String id, String userId);

}
