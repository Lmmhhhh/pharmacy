package com.example.pharmacy15.repository.iface;

import com.example.pharmacy15.domain.Patient;

import java.util.Optional;

public interface PatientRepository {
    Optional<Patient> findByNameAndPhoneLast4(String name, String phoneLast4);
}
