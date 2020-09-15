package com.mbel.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.constants.Constants;
import com.mbel.dto.SchedulePatternDto;
import com.mbel.serviceImpl.SchedulePatternServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")

public class  SchedulePatternController{

	@Autowired
	private SchedulePatternServiceImpl schedulePatternServiceImpl;  


	@PostMapping("/schedule/pattern/")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Map<String, Object>> savePattern(@Valid @RequestBody SchedulePatternDto schedulePatternDto) {
		Map<String, Object> response = new HashMap<>();
		try {
			schedulePatternServiceImpl.save(schedulePatternDto);
		} catch (Exception e) {
			response.put(Constants.MESSAGE, "pattern cannot besaved");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		response.put(Constants.MESSAGE, "pattern saved");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@GetMapping("/schedule/pattern/{patternId}")
	public SchedulePatternDto patternById(@PathVariable (value="patternId") @Valid int patternId) {
		return schedulePatternServiceImpl.getPatternById(patternId);

	}

	@PutMapping("/schedule/pattern/{patternId}")
	public ResponseEntity<Map<String, Object>> updatePatternById(@PathVariable (value="patternId")int patternId,
			@Valid @RequestBody SchedulePatternDto schedulePatternDto)   {
		Map<String, Object> response = new HashMap<>();
		try {
			schedulePatternServiceImpl.getUpdatePatternById(patternId,schedulePatternDto);
		} catch (Exception e) {
			response.put(Constants.MESSAGE, "pattern cannot be updated");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		response.put(Constants.MESSAGE, "pattern updated");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

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
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

}



