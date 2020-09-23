package com.mbel.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.constants.Constants;
import com.mbel.dto.SchedulePatternDto;
import com.mbel.serviceImpl.SchedulePatternServiceImpl;
import com.mbel.serviceImpl.UtilityServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")

public class  SchedulePatternController{

	@Autowired
	private SchedulePatternServiceImpl schedulePatternServiceImpl;  
	
	@Autowired
	UtilityServiceImpl utilityServiceImpl;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/schedule/pattern")
	public ResponseEntity<Map<String, Object>> savePattern(@RequestBody SchedulePatternDto schedulePatternDto,
			HttpServletRequest request) {
		String token = request.getHeader(Constants.HEADER_STRING);
		int userId = this.utilityServiceImpl.getUserIdFromToken(token);
		Map<String, Object> response = new HashMap<>();
		try {
			schedulePatternServiceImpl.save(schedulePatternDto,userId);
		} catch (Exception e) {
			response.put(Constants.MESSAGE, "pattern cannot besaved");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		response.put(Constants.MESSAGE, "pattern saved");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	
	@GetMapping("/schedule/pattern")
	public List<SchedulePatternDto> gerAllpattern() {
		return schedulePatternServiceImpl.getAllPatterns();

	}
	
	
	@GetMapping("/schedule/pattern/{patternId}")
	public SchedulePatternDto patternById(@PathVariable (value="patternId") @Valid int patternId) {
		return schedulePatternServiceImpl.getPatternById(patternId);

	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/schedule/pattern/{patternId}")
	public ResponseEntity<Map<String, Object>> updatePatternById(@PathVariable (value="patternId")int patternId,
			@Valid @RequestBody SchedulePatternDto schedulePatternDto,HttpServletRequest request)   {
		String token = request.getHeader(Constants.HEADER_STRING);
		int userId = this.utilityServiceImpl.getUserIdFromToken(token);
		Map<String, Object> response = new HashMap<>();
		try {
			schedulePatternServiceImpl.getUpdatePatternById(patternId,schedulePatternDto,userId);
		} catch (Exception e) {
			response.put(Constants.MESSAGE, "pattern cannot be updated");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		response.put(Constants.MESSAGE, "pattern updated");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/schedule/pattern/{patternId}")
	public ResponseEntity<Map<String, Object>> deletePatternById(@PathVariable (value="patternId")@Valid int patternId) {
		Map<String, Object> response = new HashMap<>();
		try {
			schedulePatternServiceImpl.deletePatternById(patternId);
		} catch (Exception e) {
			response.put(Constants.MESSAGE, "pattern cannot be deleted");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		response.put(Constants.MESSAGE, "pattern deleted");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

}



