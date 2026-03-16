package com.rohan.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {

	    private Integer userId;
	    private String username;
	    private String email;
	    private String mobile;
	    private String role;
	    private boolean isActive;
}
