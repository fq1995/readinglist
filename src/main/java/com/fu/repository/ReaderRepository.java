package com.fu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fu.po.Reader;

public interface ReaderRepository extends JpaRepository<Reader, String> {
}
