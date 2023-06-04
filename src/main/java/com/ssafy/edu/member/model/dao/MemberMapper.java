package com.ssafy.edu.member.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.edu.member.model.dto.MemberDto;


@Mapper
public interface MemberMapper {
	void join(MemberDto mdto);
	MemberDto login(MemberDto mdto);
	List<MemberDto> memberlist();
	MemberDto memberDetail(String memberId);
	void memberDelete(String memberId);
	void memberUpdate(MemberDto mdto);
	String findById(String memberId);
	MemberDto loginMember(String memberId, String memberPassword);

	public void saveRefreshToken(Map<String, String> map);
	public Object getRefreshToken(String memberId);
	public void deleteRefreshToken(Map<String, String> map);
}