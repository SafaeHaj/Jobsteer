package com.Jobsteer.Jobsteer.models;

import com.Jobsteer.Jobsteer.entities.Language;
import com.Jobsteer.Jobsteer.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class LanguageModel {

    @Autowired
    private LanguageRepository languageRepository;

    // Get all languages
    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    // Get a language by its ID
    public Language getLanguageById(int id) {
        return languageRepository.findById(id).orElse(null);
    }

    // Save a new or existing language
    public Language saveLanguage(Language language) {
        return languageRepository.save(language);
    }

    // Delete a language by its ID
    public void deleteLanguage(int id) {
        languageRepository.deleteById(id);
    }
}
