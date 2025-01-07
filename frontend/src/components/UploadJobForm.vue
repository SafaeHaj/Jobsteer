<template>
  <div class="form-container">
    <form @submit.prevent="handleFormSubmission" class="form" method="post">
      <h2 class="form-title">Post a Job</h2>

      <div class="input-group">
        <label for="title">Job Title</label>
        <input
          type="text"
          id="title"
          v-model="job.title"
          placeholder="Enter job title"
          required
        />
      </div>

      <div class="input-group">
        <label for="description">Enter Description</label>
        <textarea
          id="description"
          v-model="job.description"
          placeholder="Enter job description"
        ></textarea>
      </div>

      <div class="input-group">
        <label for="location">Enter Job Location</label>
        <textarea
          id="location"
          v-model="job.location"
          placeholder="Enter City"
          @input="handleLocationInput"
          :class="{ error: locationError }"
        ></textarea>
        <ul v-if="suggestions.length" class="suggestions">
          <li
            v-for="(suggestion, index) in suggestions"
            :key="index"
            @click="selectSuggestion(suggestion)"
          >
            {{ suggestion }}
          </li>
        </ul>
        <p v-if="locationError" class="error-message">
          Invalid location. Please enter a valid City, Country.
        </p>
      </div>

      <div class="input-group">
        <label for="education">Education Requirements</label>
        <input
          type="text"
          v-model="educationInput"
          @keydown.enter.prevent="addEducation"
          placeholder="Add an education requirement and press Enter"
        />
        <ul class="tag-list">
          <li v-for="(edu, index) in job.education" :key="index" class="tag">
            {{ edu }}
            <button class="delete-button" @click="removeEducation(index)">x</button>
          </li>
        </ul>
      </div>

      <div class="input-group">
        <label for="languages">Languages</label>
        <input
          type="text"
          v-model="languageInput"
          @keydown.enter.prevent="addLanguage"
          placeholder="Add a language and press Enter"
        />
        <ul class="tag-list">
          <li v-for="(language, index) in job.languages" :key="index" class="tag">
            {{ language }}
            <button class="delete-button" @click="removeLanguage(index)">x</button>
          </li>
        </ul>
      </div>

      <div class="input-group">
        <label for="skills">Soft Skills</label>
        <input
          type="text"
          v-model="skillInput"
          @keydown.enter.prevent="addSkill"
          placeholder="Add a skill and press Enter"
        />
        <ul class="tag-list">
          <li v-for="(skill, index) in job.skills" :key="index" class="tag">
            {{ skill }}
            <button type="button" class="delete-button" @click="removeSkill(index)">
              x
            </button>
          </li>
        </ul>
      </div>

      <div class="input-group">
        <label for="experiences">Hard Skills</label>
        <input
          type="text"
          v-model="experienceInput"
          @keydown.enter.prevent="addExperience"
          placeholder="Add a skill and press Enter"
        />
        <ul class="tag-list">
          <li v-for="(experience, index) in job.experiences" :key="index" class="tag">
            {{ experience }}
            <button type="button" class="delete-button" @click="removeExperience(index)">
              x
            </button>
          </li>
        </ul>
      </div>

      <div class="input-group">
        <label for="toApply">Contact</label>
        <input
          type="text"
          id="toApply"
          v-model="job.toApply"
          placeholder="Contact"
          required
        />
      </div>
      <button type="submit" class="submit-button">Submit</button>
    </form>
  </div>
</template>

<script>
import { useJobPostStore } from "@/store/jobpostStore.js";
import { useUserStore } from "@/store/user";
import { City, Country } from "country-state-city";

export default {
  name: "UploadJobForm",
  data() {
    return {
      job: {
        title: "",
        description: "",
        location: "",
        toApply: "",
        education: [],
        languages: [],
        skills: [],
        experiences: [],
      },
      locations: [],
      suggestions: [],
      locationError: false,
      educationInput: "",
      languageInput: "",
      skillInput: "",
      experienceInput: "",
    };
  },
  mounted() {
    const cities = City.getAllCities();
    const countries = Country.getAllCountries();

    this.locationsMap = new Map();

    cities.forEach((city) => {
      const country =
        countries.find((c) => c.isoCode === city.countryCode)?.name || "Unknown";
      const cityKey = city.name.toLowerCase();
      const countryKey = country.toLowerCase();

      if (!this.locationsMap.has(cityKey)) {
        this.locationsMap.set(cityKey, new Set());
      }

      this.locationsMap.get(cityKey).add(countryKey);
    });

    this.locationsMap.forEach((countries, cityKey) => {
      this.locationsMap.set(cityKey, Array.from(countries));
    });

    this.flatLocations = Array.from(
      this.locationsMap.entries()
    ).flatMap(([city, countries]) =>
      countries.map(
        (country) =>
          `${city.charAt(0).toUpperCase() + city.slice(1)}, ${
            country.charAt(0).toUpperCase() + country.slice(1)
          }`
      )
    );
  },
  computed: {
    user() {
      const userStore = useUserStore();
      return userStore.user;
    },
  },
  methods: {
    debounce(fn, delay) {
      let timeout;
      return function (...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => fn.apply(this, args), delay);
      };
    },
    handleLocationInput() {
      this.debounce(() => {
        const input = this.job.location.trim().toLowerCase();
        if (!input) {
          this.suggestions = [];
          this.locationError = false;
          return;
        }

        const [cityInput = "", countryInput = ""] = input.split(",").map((s) => s.trim());
        const citySuggestions = this.locationsMap.get(cityInput) || [];

        if (countryInput) {
          this.suggestions = citySuggestions
            .filter((country) => country.includes(countryInput))
            .map((country) => `${cityInput}, ${country}`);
        } else {
          this.suggestions = citySuggestions.map((country) => `${cityInput}, ${country}`);
        }

        this.locationError = this.suggestions.length === 0;
      }, 300)();
    },
    selectSuggestion(suggestion) {
      this.job.location = suggestion;
      this.suggestions = [];
      this.locationError = false;
    },
    addEducation() {
      if (this.educationInput.trim()) {
        this.job.education.push(this.educationInput.trim());
        this.educationInput = "";
      }
    },
    removeEducation(index) {
      this.job.education.splice(index, 1);
    },
    addLanguage() {
      if (this.languageInput.trim()) {
        this.job.languages.push(this.languageInput.trim());
        this.languageInput = "";
      }
    },
    removeLanguage(index) {
      this.job.languages.splice(index, 1);
    },
    addSkill() {
      if (this.skillInput.trim()) {
        this.job.skills.push(this.skillInput.trim());
        this.skillInput = "";
      }
    },
    removeSkill(index) {
      this.job.skills.splice(index, 1);
    },
    addExperience() {
      if (this.experienceInput.trim()) {
        this.job.experiences.push(this.experienceInput.trim());
        this.experienceInput = "";
      }
    },
    removeExperience(index) {
      this.job.experiences.splice(index, 1);
    },
    async handleFormSubmission() {
      const jobPostStore = useJobPostStore();

      try {
        const jobPostData = {
          title: this.job.title,
          recruiterId: this.user.id,
          location: this.job.location,
          requirements: [],
          description: this.job.description,
          toApply: this.job.toApply,
        };

        this.job.education.forEach((edu) => {
          jobPostData.requirements.push({ type: "EDUCATION", description: edu });
        });
        this.job.languages.forEach((lang) => {
          jobPostData.requirements.push({ type: "LANGUAGE", description: lang });
        });
        this.job.skills.forEach((skill) => {
          jobPostData.requirements.push({ type: "SKILL", description: skill });
        });
        this.job.experiences.forEach((exp) => {
          jobPostData.requirements.push({ type: "EXPERIENCE", description: exp });
        });

        const response = await jobPostStore.addJobPost(jobPostData);

        if (response) {
          jobPostStore.jobPosts.push(response);
          this.resetForm();
          alert("Job post created successfully!");
        }
      } catch (error) {
        console.error("Error in job submission:", error);
        this.submissionError = `Failed to create job post: ${error.message}`;
      }
    },
    resetForm() {
      this.job = {
        title: "",
        description: "",
        location: "",
        toApply: "",
        education: [],
        languages: [],
        skills: [],
        experiences: [],
      };
      this.educationInput = "";
      this.languageInput = "";
      this.skillInput = "";
      this.experienceInput = "";
    },
  },
};
</script>

<style scoped>
.form-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background-color: #f8f8f8;
}

.form {
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  padding: 30px;
  width: 400px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.form-title {
  font-size: 24px;
  text-align: center;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

.input-group {
  margin-bottom: 20px;
}

.input-group label {
  display: block;
  color: #666;
  margin-bottom: 8px;
}

.input-group input,
.input-group select,
.input-group textarea {
  width: 90%;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 5px;
  outline: none;
}

.input-group select {
  cursor: pointer;
}

.input-group input:focus,
.input-group select:focus,
.input-group textarea:focus {
  border: 2px solid #845aa4;
}

.tag-list {
  list-style-type: none;
  padding: 0;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.tag {
  color: #fff;
  background-color: #845aa4;
  padding: 5px 10px;
  border-radius: 20px;
  font-size: 14px;
  display: flex;
  align-items: center;
}

.delete-button {
  background: transparent;
  border: none;
  font-size: 16px;
  margin-left: 10px;
  color: #fff;
  cursor: pointer;
}

.add-button,
.remove-button {
  background-color: #845aa4;
  color: white;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
  border-radius: 5px;
}

.add-button:hover,
.remove-button:hover {
  background-color: #62367d;
}

.submit-button {
  width: 100%;
  background-color: #845aa4;
  color: white;
  padding: 10px 15px;
  font-size: 16px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.submit-button:hover {
  background-color: #62367d;
}

.error-message {
  color: red;
  font-size: 14px;
  text-align: center;
}
</style>
