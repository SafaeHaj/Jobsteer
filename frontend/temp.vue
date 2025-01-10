<template>
    <div>
      <FormKit type="form" :actions="false">
        <FormKit type="multi-step" tab-style="progress" :allow-incomplete="false">
          <FormKit type="step" name="jobInfo">
            <h2>Tell us more about your <span>job</span></h2>
            <FormKit
              type="text"
              name="title"
              label="Title"
              placeholder="Enter title"
              v-model="job.title"
              :validation="'required'"
            />
            <FormKit type="group" name="location">
              <FormKit
                type="text"
                name="country"
                label="Country"
                placeholder="Enter Country"
                v-model="country"
                @input="handleCountryInput"
                @focus="isHovered = true"
                @blur="hideOnBlur"
              />
              <ul v-if="countrySuggestions.length && isHovered">
                <li
                  v-for="(suggestion, index) in countrySuggestions"
                  :key="index"
                  @click="selectCountrySuggestion(suggestion)"
                >
                  {{ suggestion }}
                </li>
              </ul>
              <FormKit
                type="text"
                name="city"
                label="City"
                placeholder="Enter City"
                v-model="city"
                @input="handleCityInput"
                @focus="isHovered = true"
                @blur="hideOnBlur"
              />
              <ul v-if="citySuggestions.length && isHovered">
                <li
                  v-for="(suggestion, index) in citySuggestions"
                  :key="index"
                  @click="selectCitySuggestion(suggestion)"
                >
                  {{ suggestion }}
                </li>
              </ul>
            </FormKit>
            <FormKit
              type="textarea"
              name="description"
              label="Description"
              placeholder="Enter a short description of the position"
              maxlength="300"
              v-model="job.description"
            />
            <FormKit
              type="text"
              name="contact"
              label="Contact"
              placeholder="e.g: contact@me.com"
              v-model="job.toApply"
            />
            <template #stepNext>
              <FormKit type="button" label="Next" icon="arrow-right" />
            </template>
          </FormKit>
  
          <!-- Step 2: Candidate Requirements -->
          <FormKit type="step" name="candidateRequirements">
            <h2>What are you looking for in a <span>candidate</span>?</h2>
            <FormKit
              type="select"
              name="education"
              label="Education"
              v-model="educationLevel"
              :options="educationOptions.map(option => ({ label: option, value: option }))"
              placeholder="Select Level"
            />
            <FormKit
              type="text"
              name="educationField"
              label="Field of Study"
              placeholder="Enter field"
              v-model="educationField"
            />
            <ul>
              <li v-for="(edu, index) in job.education" :key="index">
                {{ edu }}
                <button @click="removeEducation(index)">
                  <font-awesome-icon :icon="['fas', 'xmark']" />
                </button>
              </li>
            </ul>
            <FormKit
              type="text"
              name="languages"
              label="Languages"
              placeholder="Enter a language"
              v-model="languageInput"
              @input="handleLanguageInput"
              @focus="isHovered = true"
              @blur="hideOnBlur"
            />
            <ul>
              <li v-for="(language, index) in job.languages" :key="index">
                {{ language }}
                <button @click="removeLanguage(index)">
                  <font-awesome-icon :icon="['fas', 'xmark']" />
                </button>
              </li>
            </ul>
            <FormKit
              type="text"
              name="skills"
              label="Skills"
              placeholder="e.g Communication"
              v-model="skillInput"
              @keydown.enter.prevent="addSkill"
            />
            <ul>
              <li v-for="(skill, index) in job.skills" :key="index">
                {{ skill }}
                <button @click="removeSkill(index)">
                  <font-awesome-icon :icon="['fas', 'xmark']" />
                </button>
              </li>
            </ul>
            <FormKit
              type="text"
              name="experience"
              label="Experience"
              placeholder="e.g Java, Frontend"
              v-model="experienceInput"
              @keydown.enter.prevent="addExperience"
            />
            <ul>
              <li v-for="(experience, index) in job.experiences" :key="index">
                {{ experience }}
                <button @click="removeExperience(index)">
                  <font-awesome-icon :icon="['fas', 'xmark']" />
                </button>
              </li>
            </ul>
            <template #stepPrev>
              <FormKit type="button" label="Go Back" icon="arrow-left" />
            </template>
            <template #stepNext>
              <FormKit type="submit" label="Create Job" />
            </template>
          </FormKit>
        </FormKit>
      </FormKit>
    </div>
  </template>

<script>
import { useJobPostStore } from "@/store/jobpostStore.js";
import { useUserStore } from "@/store/user";
import { City, State, Country } from "country-state-city";
import ISO6391 from "iso-639-1";

export default {
    name: "UploadJobView",
    data() {
        return {
            job: {
                title: "",
                description: "",
                city: "",
                country: "",
                toApply: "",
                education: [],
                languages: [],
                skills: [],
                experiences: [],
            },
            educationOptions: ["None", "High School", "Associate", "Bachelor", "Master", "PhD"],
            countries: [],
            countrySuggestions: [],
            citySuggestions: [],
            countryCityMap: {},
            languages: [],
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
    this.countries = Country.getAllCountries();
    this.languages = ISO6391.getAllNames();

    this.countryCityMap = new Map();
    const countryStateCities = {};

    this.countries.forEach(country => {
        const countryCode = country.isoCode;

        countryStateCities[country.name] = State.getStatesOfCountry(countryCode)
            .map(state => ({
                stateCode: state.isoCode,
                cities: City.getCitiesOfState(countryCode, state.isoCode).map(city => city.name),
            }));
    });

    for (const [countryName, states] of Object.entries(countryStateCities)) {
        const allCities = states.flatMap(state => state.cities);
        this.countryCityMap.set(countryName, allCities);
    }
}
,
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
        handleCountryInput() {
            this.debounce(() => {
                const input = this.country.trim().toLowerCase();
                if (!input) {
                    this.countrySuggestions = [];
                    this.locationError = false;
                    return;
                }
                this.countrySuggestions = this.countries
                    .filter((country) => country.name.toLowerCase().includes(input))
                    .map((country) => country.name);

                this.locationError = this.countrySuggestions.length === 0;
            }, 300)();
        },
        handleCityInput() {
            this.debounce(() => {
                const input = this.city.trim().toLowerCase();
                if (!input) {
                    this.citySuggestions = [];
                    this.locationError = false;
                    return;
                }
                if (!this.job.country) {
                    this.locationError = true;
                    return;
                }

                this.citySuggestions = this.countryCityMap.get(this.job.country)
                    .filter((city) => city.toLowerCase().includes(input))
                    .map((city) => city);

                this.locationError = this.citySuggestions.length === 0;
            }, 300)();
        },
        selectCountrySuggestion(suggestion) {
            this.job.country = suggestion;
            this.countrySuggestions = [];
            this.locationError = false;
        },
        selectCitySuggestion(suggestion) {
            this.job.city = suggestion;
            this.citySuggestions = [];
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
                    location: `${this.job.city}, ${this.job.country}`,
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