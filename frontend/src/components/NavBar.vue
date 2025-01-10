<template>
  <nav class="bg-primary shadow-md px-6 py-3 flex justify-between items-center flex-wrap">
    <!-- Logo -->
    <div class="flex items-center gap-2">
      <router-link to="/">
        <img src="../assets/asset.svg" alt="Logo" class="h-10" />
      </router-link>
      <span class="text-lg text-white hidden sm:inline">JOBSTEER</span>
    </div>

    <!-- Main Navigation Links -->
    <div class="flex items-center gap-6">
      <router-link to="/" class="nav-link text-white">
        <font-awesome-icon :icon="['fas', 'house']" class="sm:hidden mr-2 text-white" />
        <span class="text-base hidden sm:inline text-white">Home</span>
      </router-link>
      <router-link
        v-if="isLoggedIn && user.role === 'jobseeker'"
        to="/upload-resume"
        class="nav-link bg-white text-white px-4 py-2 rounded-full hover:bg-secondary"
      >
        <font-awesome-icon
          :icon="['fas', 'upload']"
          class="sm:hidden mr-2 text-white content-center"
        />
        <span class="text-base hidden sm:inline text-primary">Upload Resume</span>
      </router-link>
      <router-link
        v-else-if="isLoggedIn && user.role === 'recruiter'"
        to="/recruiter-dashboard/post-job"
        class="nav-link text-white bg-white px-4 py-2 rounded-full hover:bg-secondary"
      >
        <font-awesome-icon
          :icon="['fas', 'upload']"
          class="sm:hidden mr-2 text-primary content-center"
        />
        <span class="text-base hidden sm:inline text-primary">Upload Job</span>
      </router-link>
    </div>

    <!-- User Actions -->
    <div class="flex items-center gap-4">
      <router-link v-if="!isLoggedIn" to="/login" class="btn btn-secondary text-white">
        <span class="hidden sm:inline text-white">Login</span>
        <font-awesome-icon :icon="['fas', 'sign-in-alt']" class="sm:hidden text-white" />
      </router-link>
      <router-link v-if="!isLoggedIn" to="/signup" class="btn btn-primary text-white">
        <span class="hidden sm:inline text-white">Register</span>
        <font-awesome-icon :icon="['fas', 'user-plus']" class="sm:hidden text-white" />
      </router-link>
      <button
        v-if="isLoggedIn"
        class="flex items-center gap-2 text-white"
        @click="goToProfile"
      >
        <font-awesome-icon :icon="['fas', 'user']" class="text-white sm:hidden" />
        <span class="text-base hidden sm:inline text-white">Profile</span>
      </button>
      <button
        v-if="isLoggedIn"
        class="flex items-center gap-2 text-white"
        @click="handleSignOut"
      >
        <font-awesome-icon :icon="['fas', 'sign-out-alt']" class="text-white" />
        <span class="text-base hidden sm:inline text-white">Logout</span>
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
