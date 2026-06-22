package com.placement.service;

import com.placement.exception.ApiException;
import com.placement.model.Company;
import com.placement.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository repo;

    public CompanyService(CompanyRepository repo) { this.repo = repo; }

    public Company create(Company c) {
        if (c.getName() == null || c.getName().isBlank())
            throw ApiException.badRequest("Company name is required");
        Long id = repo.save(c);
        c.setId(id);
        return c;
    }

    public List<Company> getAll() { return repo.findAll(); }

    public Company getById(Long id) {
        return repo.findById(id).orElseThrow(() -> ApiException.notFound("Company not found"));
    }

    public void delete(Long id) { repo.delete(id); }
}
