package com.EducCore.EduCore.repositories;

import com.EducCore.EduCore.domain.Faq.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Long> {
}
