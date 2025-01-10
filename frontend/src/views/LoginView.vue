<template>
  <div class="h-screen flex">
    <!-- Login Form Container -->
    <div class="flex-1 flex flex-col justify-center items-center shadow-md p-10">
      <h2 class="text-3xl font-bold mb-5 text-text-color">Login</h2>

      <form @submit.prevent="handleLogin" class="w-full max-w-sm">
        <div class="mb-4">
          <input
            v-model="email"
            type="email"
            id="email"
            placeholder="Enter email"
            required
            class="w-full px-4 py-2 rounded-lg border border-border-color bg-input-bg text-text-color focus:ring-2 focus:ring-primary-color focus:outline-none"
          />
        </div>

        <div class="mb-4">
          <input
            v-model="password"
            type="password"
            id="password"
            placeholder="Enter password"
            required
            class="w-full px-4 py-2 rounded-lg border border-border-color bg-input-bg text-text-color focus:ring-2 focus:ring-primary-color focus:outline-none"
          />
        </div>

        <button type="submit" class="w-full bg-primary text-white py-2 rounded-lg font-bold hover:bg-accent transition duration-300">
          Login
        </button>
      </form>

      <p class="mt-5 text-grey text-sm">
        Don't have an account? <router-link to="/signup" class="text-accent">Sign up</router-link>
      </p>
    </div>

    <!-- Welcome Section (Optional) -->
    <div class="flex-1 hidden md:flex justify-center items-center bg-primary z-[-1]">
    </div>
  </div>
</template>


<script>
import {useUserStore} from '../store/user';

export default {
  name: 'UserLogin',
  data() {
    return {
      email: '',
      password: '',
    };
  },
  methods: {
    async handleLogin() {
      const formData = {
        email: this.email,
        password: this.password
      };
      const userStore = useUserStore();
      try {
        const response = await userStore.loginUser(formData);
        const token = response.data.token;
        localStorage.setItem("authToken", token);
        userStore.fetchDataFromApi();
        this.$router.push("/");
      } catch (error) {
        console.log("Login error:", error);
      } 
    },
  },
};
</script>