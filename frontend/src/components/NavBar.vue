<template>
  <nav class="bg-white shadow-md rounded-md px-6 py-3 flex justify-between items-center flex-wrap">
    <!-- Logo -->
    <div class="flex items-center gap-2">
      <router-link to="/">
        <img src="../assets/asset.svg" alt="Logo" class="h-10" />
      </router-link>
      <span class="text-lg font-bold text-primary hidden sm:inline">JOBSTEER</span>
    </div>

    <!-- Main Navigation Links -->
    <div class="flex items-center gap-6">
      <router-link to="/" class="nav-link text-primary">
        <font-awesome-icon :icon="['fas', 'house']" class="sm:hidden mr-2 text-primary" />
        <span class="text-base hidden sm:inline text-primary">Home</span>
      </router-link>
      <router-link
        v-if="isLoggedIn && user.role === 'jobseeker'"
        to="/upload-resume"
        class="nav-link text-primary bg-primary text-white px-4 py-2 rounded-full hover:bg-secondary"
      >
        <font-awesome-icon :icon="['fas', 'upload']" class="sm:hidden mr-2 text-white" />
        <span class="text-base hidden sm:inline text-white">Upload Resume</span>
      </router-link>
      <router-link
        v-else-if="isLoggedIn && user.role === 'recruiter'"
        to="/recruiter-dashboard"
        class="nav-link text-primary bg-primary text-white px-4 py-2 rounded-full hover:bg-secondary"
      >
        <font-awesome-icon :icon="['fas', 'upload']" class="sm:hidden mr-2 text-white" />
        <span class="text-base hidden sm:inline text-white">Upload Job</span>
      </router-link>
    </div>

    <!-- User Actions -->
    <div class="flex items-center gap-4">
      <button
        v-if="!isLoggedIn"
        class="btn btn-secondary text-primary"
        @click="showLoginModal = true"
      >
        <span class="hidden sm:inline text-primary">Login</span>
        <font-awesome-icon :icon="['fas', 'sign-in-alt']" class="sm:hidden text-primary" />
      </button>
      <button
        v-if="!isLoggedIn"
        class="btn btn-primary text-primary"
        @click="showRegisterModal = true"
      >
        <span class="hidden sm:inline text-primary">Register</span>
        <font-awesome-icon :icon="['fas', 'user-plus']" class="sm:hidden text-primary" />
      </button>
      <button
        v-if="isLoggedIn"
        class="flex items-center gap-2 text-primary"
        @click="goToProfile"
      >
        <font-awesome-icon :icon="['fas', 'user']" class="text-primary sm:hidden" />
        <span class="text-base hidden sm:inline text-primary">Profile</span>
      </button>
      <button
        v-if="isLoggedIn"
        class="flex items-center gap-2 text-primary"
        @click="handleSignOut"
      >
        <font-awesome-icon :icon="['fas', 'sign-out-alt']" class="text-primary" />
        <span class="text-base hidden sm:inline text-primary">Logout</span>
      </button>
    </div>
  </nav>
</template>

<script>
import { useUserStore } from "../store/user";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

export default {
  components: {
    FontAwesomeIcon,
  },
  data() {
    return {
      showLoginModal: false,
      showRegisterModal: false,
    };
  },
  computed: {
    isLoggedIn() {
      const userStore = useUserStore();
      return userStore.user !== null;
    },
    user() {
      const userStore = useUserStore();
      return userStore.user;
    },
  },
  methods: {
    handleSignOut() {
      const userStore = useUserStore();
      userStore.logoutUser();
      this.$router.push("/");
    },
    goToProfile() {
      this.$router.push("/profile");
    },
  },
};
</script>

