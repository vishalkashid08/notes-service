package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface NotesRepository extends JpaRepository<Notes,Long>{
	
	List<Notes> findBySubject(String subject);

	List<Notes> findByUserId(Long userId);
	

	    List<Notes> findBySubjectIgnoreCase(String subject);
}
