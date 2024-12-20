package com.Jobsteer.Jobsteer.entities;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Language implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String language; 

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "resume_id",  referencedColumnName = "id",nullable = false)
    private Resume resume;

 
    public Language() {}


    public Language(String language, Resume resume) {
        this.language = validateLanguage(language); 
        this.resume = resume;
    }

   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = validateLanguage(language); 
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    // Utility Method for Validation
    private static String validateLanguage(String language) {
        if (isValidLanguage(language)) {
            return language;
        } else {
            throw new IllegalArgumentException("Invalid language: " + language);
        }
    }

    private static boolean isValidLanguage(String language) {
        for (String isoLanguage : java.util.Locale.getISOLanguages()) {
          
            Locale locale = Locale.forLanguageTag(isoLanguage);
            
            if (locale.getDisplayLanguage().equalsIgnoreCase(language)) {
                return true;
            }
    
            Locale specificLocale = Locale.forLanguageTag(isoLanguage + "-US");
            if (specificLocale.getDisplayLanguage().equalsIgnoreCase(language)) {
                return true;
            }
            specificLocale = Locale.forLanguageTag(isoLanguage + "-GB");
            if (specificLocale.getDisplayLanguage().equalsIgnoreCase(language)) {
                return true;
            }
            specificLocale = Locale.forLanguageTag(isoLanguage + "-FR");
            if (specificLocale.getDisplayLanguage().equalsIgnoreCase(language)) {
                return true;
            }
        }
        return false;
    }
    
}
