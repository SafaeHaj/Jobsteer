<template>
    <div class="profile-view">
      <h2>{{ user.fullName }}</h2>
      <div v-if="user">
        <p>Email: {{ user.email }}</p>
        <p>Id: {{ user.id }}</p>
        <p v-if="user.role === 'jobseeker'">Location: {{ user.location }}</p>
        <p v-else>Company Name: {{ user.companyName }}</p>

      </div>
      <div v-else>
        <p>Loading user data...</p>
      </div>
    </div>
  </template>
  
  <script>
  import { useUserStore } from '../store/user';
  
  export default {
    name: 'ProfileView',
    computed: {
      user() {
        const userStore = useUserStore();
        console.log("apah", userStore.user);
        return userStore.user;
      }
    }
  };
  </script>
  
  
  
  <style scoped>
  .profile-view {
    padding: 20px;
    font-size: 18px;
  }
  </style>
  