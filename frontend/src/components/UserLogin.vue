<template>
      <h2 class="login-title">Log in</h2>
      <form @submit.prevent="handleLogin">
        <div class="input-group">
          <label for="email">Email</label>
          <input type="text" id="login" v-model="email" placeholder="Enter your email" />
        </div>
        <div class="input-group">
          <label for="password">Password</label>
          <input type="password" id="password" v-model="password" placeholder="Enter your password" />
        </div>
        <button type="submit" class="login-button">Login</button>
      </form>
</template>

<script>
import {useUserStore} from '../store/user';

export default {
  name: 'LoginForm',
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
        this.$emit('login-success');
      } catch (error) {
        console.log("Login error:", error);
      } 
    },
  },
};
</script>

<style scoped>

.login-title {
  font-size: 24px;
  text-align: center;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

.input-group {
  margin-bottom: 20px;
  text-align: left;
}

.input-group label {
  display: block;
  color: #666;
  margin-bottom: 5px;
}

.input-group input {
  width: 90%;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 5px;
  outline: none;
}

.input-group input:focus{
    border: 2px solid #845AA4;
}

.login-button {
  width: 50%;
  display: block;
  margin: 0 auto;
  padding: 12px 0;
  font-size: 16px;
  background-color: #845AA4;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

</style>
