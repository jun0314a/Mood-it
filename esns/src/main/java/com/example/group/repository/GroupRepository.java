package com.example.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.group.entity.Emotion;
import com.example.group.entity.Group;

import java.util.List;

/**
 * Group 테이블에 대한 CRUD 및 검색 기능 제공
 */
public interface GroupRepository extends JpaRepository<Group, Integer> {

    /**
     * 제목에 특정 문자열이 포함된 그룹 조회
     */
    List<Group> findByTitleContaining(String title);

    /**
     * tags 컬럼에 특정 태그가 포함된 그룹 조회
     */
    List<Group> findByTagsContaining(String tag);

    /**
     * emotion 값이 일치하는 그룹 조회
     */
    List<Group> findByEmotion(Emotion emotion);
}
