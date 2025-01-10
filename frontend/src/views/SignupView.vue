<template>
  <div class="h-screen flex">
    <!-- Login Form Container -->
    <div class="flex-1 flex flex-col justify-center items-center shadow-md p-10">
      <h2 class="text-3xl font-bold mb-5 text-text-color">Sign Up</h2>

      <form @submit.prevent="submitForm" class="w-full max-w-sm">
        <div class="input-group mb-4">
          <label for="email">Email</label>
          <input
            type="email"
            id="email"
            v-model="email"
            placeholder="youremail@domain.com"
            required
            class="w-full px-4 py-2 border border-border-color rounded-md focus:outline-none focus:ring-2 focus:ring-primary-color"
          />
        </div>

        <div class="input-group mb-4">
          <label for="password" class="block text-sm font-medium text-gray-700"
            >Password</label
          >
          <input
            type="password"
            id="password"
            v-model="password"
            placeholder="6 to 10 chars"
            required
            class="w-full px-4 py-2 border border-border-color rounded-md focus:outline-none focus:ring-2 focus:ring-primary-color"
          />
        </div>

        <div class="input-group mb-4 flex gap-5">
          <div>
            <label for="firstName" class="block text-sm font-medium text-gray-700"
              >First Name</label
            >
            <input
              type="text"
              id="firstName"
              v-model="firstName"
              class="w-full px-4 py-2 border border-border-color rounded-md focus:outline-none focus:ring-2 focus:ring-primary-color"
            />
          </div>
          <div>
            <label for="lastName">Last Name</label>
            <input
              type="text"
              id="lastName"
              v-model="lastName"
              class="w-full px-4 py-2 border border-border-color rounded-md focus:outline-none focus:ring-2 focus:ring-primary-color"
            />
          </div>
        </div>
        <!-- Radio Button Group -->
        <div class="input-group mb-4 flex items-center">
          <label>Are you a:</label>

          <div class="flex items-center w-max">
            <label for="jobseeker" class="w-max flex items-center">
              <input
                type="radio"
                id="jobseeker"
                name="userType"
                value="jobseeker"
                v-model="userType"
              />
              Candidate
            </label>
            <label for="recruiter" class="w-max flex items-center">
              <input
                type="radio"
                id="recruiter"
                name="userType"
                value="recruiter"
                v-model="userType"
              />
              Recruiter
            </label>
          </div>
        </div>

        <!-- Conditional Inputs for Jobseeker or Recruiter -->
        <div v-if="userType === 'jobseeker'" class="input-group mb-4">
          <label for="city">City</label>
          <input
            type="text"
            id="city"
            v-model="city"
            class="w-full px-4 py-2 border border-border-color rounded-md focus:outline-none focus:ring-2 focus:ring-primary-color"
          />
        </div>

        <div v-if="userType === 'jobseeker'" class="input-group mb-4">
          <label for="country">Country</label>
          <input
            type="text"
            id="country"
            v-model="country"
            class="w-full px-4 py-2 border border-border-color rounded-md focus:outline-none focus:ring-2 focus:ring-primary-color"
          />
        </div>

        <div v-if="userType === 'recruiter'" class="input-group mb-4">
          <label for="company">Company Name</label>
          <input
            type="text"
            id="company"
            v-model="company"
            class="w-full px-4 py-2 border border-border-color rounded-md focus:outline-none focus:ring-2 focus:ring-primary-color"
          />
        </div>

        <p class="mt-5 text-grey text-sm">
          Already have an account?
          <router-link to="/login" class="text-accent">Log in</router-link>
        </p>

        <button
          type="submit"
          class="w-full py-2 bg-primary text-white font-bold rounded-md hover:bg-accent focus:outline-none focus:ring-2 focus:ring-primary"
        >
          Sign Up
        </button>
      </form>
    </div>

    <div
      class="flex-1 hidden md:flex justify-center items-center bg-primary z-[-1]"
    ></div>
  </div>
</template>

<script>
import { useUserStore } from "../store/user";

export default {
  name: "UserSignup",
  data() {
    return {
      email: "",
      password: "",
      firstName: "",
      lastName: "",
      userType: "",
      city: "",
      country: "",
      company: "",
    };
  },
  methods: {
    async submitForm() {
      const formData = {
        email: this.email,
        password: this.password,
        firstName: this.firstName,
        lastName: this.lastName,
        userType: this.userType,
        location: this.userType === "jobseeker" ? `${this.city}, ${this.country}` : null,
        company: this.userType === "recruiter" ? this.company : null,
      };
      const userStore = useUserStore();
      try {
        const response = await userStore.registerUser(formData);
        this.$router.push("/login");
        console.log("User registered:", response);
      } catch (error) {
        console.error("Registration error:", error);
      }
    },
  },
};
</script>
