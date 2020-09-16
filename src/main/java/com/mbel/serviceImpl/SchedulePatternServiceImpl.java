package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbel.dao.SchedulePatternDao;
import com.mbel.dao.UserDao;
import com.mbel.dto.SchedulePatternDto;
import com.mbel.model.SchedulePattern;
import com.mbel.model.UserEntity;

@Service("SchedulePatternServiceImpl")
public class SchedulePatternServiceImpl {

	@Autowired
	SchedulePatternDao schedulePatternDao;
	
	@Autowired
	UserDao userDao;




	public void save(@Valid SchedulePatternDto schedulePatternDto, int userId) throws Exception {
		SchedulePattern schedulePattern =new SchedulePattern();
		schedulePattern.setSchedulePatternName(schedulePatternDto.getSchedulePatternName()); 
		schedulePattern.setCreatedAt(LocalDateTime.now());
		schedulePattern.setUpdatedAt(LocalDateTime.now());
		schedulePattern.setCreatedUserId(userId);
		schedulePattern.setUpdatedUserId(userId);
		schedulePattern.setPattern(schedulePatternDto.getPattern());
		schedulePattern.setPrivate(schedulePatternDto.isPrivate());
		try {
			schedulePatternDao.save(schedulePattern);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());

		}
	}
	
	public List<SchedulePatternDto> getAllPatterns() {
		List<SchedulePattern> schedulePatternList =schedulePatternDao.findAll();
		List<UserEntity>usersList=userDao.findAll();
		List<SchedulePatternDto> schedulePatternDtoList = new ArrayList<SchedulePatternDto>();
		for(SchedulePattern schedulePattern:schedulePatternList) {
			SchedulePatternDto schedulePatternDto = new SchedulePatternDto();
			schedulePatternDto.setSchedulePatternId(schedulePattern.getSchedulePatternId());
			schedulePatternDto.setSchedulePatternName(schedulePattern.getSchedulePatternName());
			schedulePatternDto.setCreatedAt(schedulePattern.getCreatedAt());
			schedulePatternDto.setUpdatedAt(schedulePattern.getUpdatedAt());
			schedulePatternDto.setCreatedUser(getUser(usersList,schedulePattern.getCreatedUserId()));
			schedulePatternDto.setUpdatedUser(getUser(usersList,schedulePattern.getUpdatedUserId()));
			schedulePatternDto.setIsPrivate(schedulePattern.isPrivate());
			schedulePatternDto.setPattern(schedulePattern.getPattern());
			schedulePatternDtoList.add(schedulePatternDto);
		}

		return schedulePatternDtoList;
	}


	private UserEntity getUser(List<UserEntity> usersList, int userId) {
		return usersList.stream()
				.filter(predicate->predicate.getUserId()==userId)
				.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
		            if (list.size() != 1) {
		                return null;
		            }
		            return list.get(0);
		        }));
	}

	public SchedulePatternDto getPatternById(@Valid int patternId) {
		SchedulePattern schedulePattern =schedulePatternDao.findById(patternId).orElse(null);
		SchedulePatternDto schedulePatternDto = new SchedulePatternDto();
		if(Objects.nonNull(schedulePattern)) {
		List<Integer>ids=new ArrayList<Integer>();
		ids.add(schedulePattern.getCreatedUserId());
		ids.add(schedulePattern.getUpdatedUserId());
		List<UserEntity>usersList=userDao.getByUserIds(ids);
			schedulePatternDto.setSchedulePatternId(schedulePattern.getSchedulePatternId());
			schedulePatternDto.setSchedulePatternName(schedulePattern.getSchedulePatternName());
			schedulePatternDto.setCreatedAt(schedulePattern.getCreatedAt());
			schedulePatternDto.setUpdatedAt(schedulePattern.getUpdatedAt());
			schedulePatternDto.setCreatedUser(usersList.get(0));
			schedulePatternDto.setUpdatedUser(schedulePattern.getCreatedUserId()==schedulePattern.getUpdatedUserId()?usersList.get(0):usersList.get(1));
			schedulePatternDto.setIsPrivate(schedulePattern.isPrivate());
			schedulePatternDto.setPattern(schedulePattern.getPattern());
		}

		return schedulePatternDto;
	}

	public void getUpdatePatternById(int patternId, @Valid SchedulePatternDto schedulePatternDto, int userId) throws Exception {
		SchedulePattern schedulePattern =schedulePatternDao.findById(patternId).orElse(null);
		if(Objects.nonNull(schedulePattern)) {
			schedulePattern.setSchedulePatternName(schedulePatternDto.getSchedulePatternName()); 
			schedulePattern.setUpdatedAt(LocalDateTime.now());
			schedulePattern.setUpdatedUserId(userId);
			schedulePattern.setPattern(schedulePatternDto.getPattern());
			schedulePattern.setPrivate(schedulePatternDto.isPrivate());
			try {
				schedulePatternDao.save(schedulePattern);
			} catch (Exception ex) {
				throw new Exception(ex.getMessage());

			}
		}
	}

	public void deletePatternById(@Valid int patternId) throws Exception {
		try {
			schedulePatternDao.deleteById(patternId);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());

		}
	}

}
