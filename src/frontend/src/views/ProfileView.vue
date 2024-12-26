<template>
  <div v-if="user" class="profile-view">
    <h2>{{ user.fullName }}</h2>
      <p>Email: {{ user.email }}</p>
      <p>Id: {{ user.id }}</p>
      <p v-if="user.role === 'jobseeker'">Location: {{ user.location }}</p>
      <p v-else-if="user.role === 'recruiter'">Company Name: {{ user.companyName }}</p>
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
  background-color: #f4f4f9; 
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  max-width: 600px;
  margin: 0 auto; 
}

h2 {
  font-size: 24px;
  margin-bottom: 15px;
  text-align: center; 
}

p {
  color: #4a148c; 
  margin: 5px 0;
}

p:first-of-type {
  font-weight: bold;
}

.profile-view p {
  font-size: 16px;
}

.profile-view div {
  margin-top: 15px;
}

.profile-view p {
  padding: 8px;
  border-left: 4px solid #8e24aa; 
}

.profile-view p:first-of-type {
  font-size: 18px; 
}
</style>