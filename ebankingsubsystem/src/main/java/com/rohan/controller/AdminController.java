package com.rohan.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rohan.model.RegisterRequestAdmin;
import com.rohan.service.AdminService;
import com.rohan.vo.RegisterResponseVO;
import com.rohan.vo.UserVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
	
	private final AdminService service;
	
	@GetMapping("/getallusers")
	public ResponseEntity<List<UserVO>> getAllUsers(){
		log.info("AdminController: Inside getAllUsers");
		Optional<List<UserVO>> optional=service.getAllUsers();
		if(optional.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok().body(optional.get());
	}
	@PutMapping("/changeuserstatus/{username}")
	public ResponseEntity<Map<String, String>> changeUserStatus(@PathVariable String username){
		log.info("AdminController: Inside changeUserStatus");
		boolean check=service.changeUserStatus(username);
		Map<String, String> response = new HashMap<>();
		if(check) {
	        response.put("message", "User Status Change Successfully..");
	        return ResponseEntity.ok(response);
	    }
		response.put("message", "Unable to change User Status!!");
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/getallusersbystatus/{status}")
	public ResponseEntity<List<UserVO>> getAllUsersByActivityStatus(@PathVariable boolean status){
		log.info("AdminController: Inside getAllUsersByActivityStatus");
		Optional<List<UserVO>> optional=service.getAllUsersByActivityStatus(status);
		if(optional.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok().body(optional.get());
	}
	
	@DeleteMapping("/deleteuser/{username}")
	public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String username){
		log.info("AdminController: Inside deleteUser");
		boolean check=service.deleteUser(username);
		Map<String, String> response = new HashMap<>();
		if(check) {
	        response.put("message", "User Deleted Successfully..");
	        return ResponseEntity.ok(response);
	    }
		response.put("message", "Unable to delete User!!");
	    return ResponseEntity.ok(response);
	}
	
	@PostMapping("/createnewuser")
	public ResponseEntity<RegisterResponseVO> createUserByAdmin(@RequestBody RegisterRequestAdmin request) {
	    log.info("AuthController: Inside createUserByAdmin method");

	    Optional<RegisterResponseVO> optional = service.createUserByAdmin(request);

	    if(optional.isEmpty()){
	        return ResponseEntity.badRequest().build();
	    }

	    return ResponseEntity.ok(optional.get());
	}
}
