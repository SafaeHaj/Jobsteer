<template>
  <div class="min-h-screen bg-white text-text flex flex-col py-8 px-6">
    <!-- Header Section -->
    <div class="mb-6 flex justify-between items-center">
      <h2 class="text-2xl font-bold text-primary">Upload Your Job Position</h2>
      <router-link
        to="../recruiter-dashboard"
        class="text-secondary hover:text-accent font-semibold"
      >
        Go Back
      </router-link>
    </div>

    <!-- Form Section -->
    <form
      class="flex-grow grid grid-cols-1 lg:grid-cols-3 gap-8"
      @submit.prevent="handleSubmit"
    >
      <!-- Job Information Section -->
      <div class="space-y-6 rounded-lg p-6 shadow-xl">
        <h3 class="text-xl font-bold text-primary">Job Information</h3>

        <!-- Job Title -->
        <div>
          <label for="title" class="block text-sm font-medium text-secondary"
            >Title</label
          >
          <input
            type="text"
            id="title"
            v-model="job.title"
            class="w-full p-3 rounded border border-gray-300 focus:ring focus:ring-accent"
            placeholder="Enter job position title"
            required
          />
        </div>

        <!-- Description -->
        <div>
          <label for="description" class="block text-sm font-medium text-secondary"
            >Description</label
          >
          <textarea
            id="description"
            v-model="job.description"
            class="w-full p-3 rounded border border-gray-300 focus:ring focus:ring-accent"
            placeholder="Enter job description"
          ></textarea>
        </div>

        <!-- Location -->
        <div class="relative">
          <label for="location" class="block text-sm font-medium text-secondary"
            >Location</label
          >
          <textarea
            id="location"
            v-model="job.location"
            class="w-full p-3 rounded border border-gray-300 focus:ring focus:ring-accent"
            placeholder="Enter City, Country"
            @input="handleLocationInput"
            @focus="isHovered = true"
            @blur="hideOnBlur"
          ></textarea>
          <ul
            v-if="suggestions.length && isHovered"
            class="absolute z-10 bg-white border border-gray-300 mt-1 w-full rounded shadow-lg max-h-40 overflow-auto"
          >
            <li
              v-for="(suggestion, index) in suggestions"
              :key="index"
              class="px-3 py-2 hover:bg-accent hover:text-white cursor-pointer"
              @click="selectSuggestion(suggestion)"
            >
              {{ suggestion }}
            </li>
          </ul>
        </div>

        <!-- Contact -->
        <div>
          <label for="contact" class="block text-sm font-medium text-secondary"
            >Contact</label
          >
          <input
            type="text"
            id="contact"
            v-model="job.toApply"
            class="w-full p-3 rounded border border-gray-300 focus:ring focus:ring-accent"
            placeholder="Enter contact information"
          />
        </div>
      </div>

      <!-- Candidate Requirements Section -->
      <div class="space-y-6 rounded-lg p-6 shadow-xl">
        <h3 class="text-xl font-bold text-primary">Candidate Requirements</h3>

        <!-- Education -->
        <div>
          <label for="education" class="block text-sm font-medium text-secondary"
            >Education Level</label
          >
          <select
            id="education"
            v-model="educationInput"
            class="w-full p-3 rounded border border-gray-300 focus:ring focus:ring-accent"
          >
            <option value="" disabled>Select Education Level</option>
            <option
              v-for="(option, index) in educationOptions"
              :key="index"
              :value="option"
              @change="addEducation()"
            >
              {{ option }}
            </option>
          </select>
          <ul class="flex flex-wrap gap-2 mt-2">
            <li
              v-for="(edu, index) in job.education"
              :key="index"
              class="bg-accent text-white px-3 py-1 rounded flex items-center gap-2"
            >
              {{ edu }}
              <button class="text-danger font-bold" @click="removeEducation(index)">
                x
              </button>
            </li>
          </ul>
        </div>

        <!-- Languages -->
        <div class="relative">
          <label for="languages" class="block text-sm font-medium text-secondary"
            >Languages</label
          >
          <input
            type="text"
            id="languages"
            v-model="languageInput"
            class="w-full p-3 rounded border border-gray-300 focus:ring focus:ring-accent"
            placeholder="Enter a language"
            @input="handleLanguageInput"
            @focus="isHovered = true"
            @blur="hideOnBlur"
          />
          <ul
            v-if="languageSuggestions.length && isHovered"
            class="absolute z-10 bg-white border border-gray-300 mt-1 w-full rounded shadow-lg max-h-40 overflow-auto"
          >
            <li
              v-for="(suggestion, index) in languageSuggestions"
              :key="index"
              class="px-3 py-2 hover:bg-accent hover:text-white cursor-pointer"
              @click="addLanguage(suggestion)"
            >
              {{ suggestion }}
            </li>
          </ul>
          <ul class="flex flex-wrap gap-2 mt-2">
            <li
              v-for="(language, index) in job.languages"
              :key="index"
              class="bg-primary text-white px-3 py-1 rounded flex items-center gap-2"
            >
              {{ language }}
              <button class="text-white font-bold" @click="removeLanguage(index)">
                <font-awesome-icon :icon="['fas', 'xmark']" />
              </button>
            </li>
          </ul>
        </div>

        <div>
          <label for="soft-skills" class="block text-sm font-medium text-secondary"
            >Soft Skills</label
          >
          <input
            type="text"
            id="skills"
            v-model="skillInput"
            class="w-full p-3 rounded border border-gray-300 focus:ring focus:ring-accent"
            placeholder="Add a skill"
            @keydown.enter.prevent="addSkill"
          />
          <ul class="flex flex-wrap gap-2 mt-2">
            <li
              v-for="(skill, index) in job.skills"
              :key="index"
              class="bg-primary text-white px-3 py-1 rounded flex items-center gap-2"
            >
              {{ skill }}
              <button class="text-white font-bold" @click="removeSkill(index)"><font-awesome-icon :icon="['fas', 'xmark']" /></button>
            </li>
          </ul>
        </div>
        <div>
          <label for="hard-skills" class="block text-sm font-medium text-secondary"
            >Hard Skills</label
          >
          <input
            type="text"
            id="skills"
            v-model="experienceInput"
            class="w-full p-3 rounded border border-gray-300 focus:ring focus:ring-accent"
            placeholder="Add an experience"
            @keydown.enter.prevent="addExperience"
          />
          <ul class="flex flex-wrap gap-2 mt-2">
            <li
              v-for="(experience, index) in job.experiences"
              :key="index"
              class="bg-primary text-white px-3 py-1 rounded flex items-center gap-2"
            >
              {{ experience }}
              <button class="text-white font-bold" @click="removeExperience(index)">
                <font-awesome-icon :icon="['fas', 'xmark']" />
              </button>
            </li>
          </ul>
        </div>
      </div>
      <!-- Action Buttons Section -->
      <div class="space-y-6 rounded-lg p-6 shadow-xl">
        <h3 class="text-xl font-bold text-primary">Actions</h3>
        <div class="flex flex-col gap-4">
          <button
            type="button"
            class="w-1/2 bg-danger text-white px-6 py-3 rounded hover:bg-opacity-80"
            @click="cancelForm"
          >
            Cancel
          </button>
          <button
            type="submit"
            class="w-1/2 bg-primary text-white px-6 py-3 rounded hover:bg-secondary"
          >
            Create Job
          </button>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import { useJobPostStore } from "@/store/jobpostStore.js";
import { useUserStore } from "@/store/user";
import { City, Country } from "country-state-city";
import ISO6391 from "iso-639-1";

export default {
  name: "UploadJobView",
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
      educationOptions: ["None", "High School", "Associate", "Bachelor", "Master", "PhD"],
      locations: [],
      languages: [],
      suggestions: [],
      locationError: false,
      languageInput: "",
      languageSuggestions: [],
      languageError: false,
      skillInput: "",
      experienceInput: "",
      isHovered: false,
    };
  },
  mounted() {
    const cities = City.getAllCities();
    const countries = Country.getAllCountries();
    this.languages = ISO6391.getAllNames();
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
    this.loading = false;
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
      if (this.educationInput.trim() && !this.job.education.includes(this.educationInput.trim())) {
        this.job.education.push(this.educationInput.trim());
        this.educationInput = "";
      }
    },
    removeEducation(index) {
      this.job.education.splice(index, 1);
    },
    handleLanguageInput() {
      const input = this.languageInput.trim().toLowerCase();
      if (!input) {
        this.languageSuggestions = [];
        this.languageError = false;
        return;
      }
      this.languageSuggestions = this.languages
        .filter((language) => language.toLowerCase().includes(input))
        .map((language) => language);

      this.languageError = this.languageSuggestions.length === 0;
    },
    addLanguage(suggestion) {
      if (suggestion) {
        this.job.languages.push(suggestion);
        this.languageSuggestions = [];
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
      this.$router.push("/recruiter-dashboard");
    },
    cancelForm() {
      this.resetForm();
      this.$router.push("/recruiter-dashboard");
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
