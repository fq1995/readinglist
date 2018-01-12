package com.fu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fu.po.Book;
import com.fu.po.Reader;

public interface ReadingListRepository extends JpaRepository<Book, Long> {
	List<Book> findByReader(String reader);
	
	List<Book> findByReader(Reader reader);
}