<template>
  <form @submit.prevent="submitForm" method="post">
    <h2 class="signup-title">Sign Up</h2>

    <div class="input-group">
      <label for="email">Email</label>
      <input type="email" id="email" v-model="email" placeholder="youremail@domain.com" required>
    </div>

    <div class="input-group">
      <label for="password">Password</label>
      <input type="password" id="password" v-model="password" placeholder="6 to 10 chars" required>
    </div>

    <div class="input-group">
      <label for="firstName">First Name</label>
      <input type="text" id="firstName" v-model="firstName">
    </div>

    <div class="input-group">
      <label for="lastName">Last Name</label>
      <input type="text" id="lastName" v-model="lastName">
    </div>

    <div class="input-group radio-group">
      <label>Are you a:</label>
      <div class="radio-options">
        <label for="jobseeker">
          <input type="radio" id="jobseeker" name="userType" value="jobseeker" v-model="userType"> Candidate
        </label>
        <label for="recruiter">
          <input type="radio" id="recruiter" name="userType" value="recruiter" v-model="userType"> Recruiter
        </label>
      </div>
    </div>

    <div v-if="userType === 'jobseeker'" class="input-group">
      <label for="city">City</label>
      <input type="text" id="city" v-model="city">

      <label for="country">Country</label>
      <input type="text" id="country" v-model="country">
    </div>

    <div v-if="userType === 'recruiter'" class="input-group">
      <label for="company">Company Name</label>
      <input type="text" id="company" v-model="company">
    </div>

    <button type="submit" class="signup-button">Sign Up</button>
  </form>
</template>


<script>
import  {useUserStore}  from '../store/user';

export default {
  name: 'SignupForm',
  data() {
    return {
      email: '',
      password: '',
      firstName: '',
      lastName: '',
      userType: '',
      city: '',
      country: '',
      company: '',
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
        location: this.userType === 'jobseeker' ? `${this.city}, ${this.country}` : null,
        company: this.userType === 'recruiter' ? this.company : null
      };
      const userStore = useUserStore();
      try {
        const response = await userStore.registerUser(formData);
        this.$emit('register-success');
        console.log('User registered:', response); 
      } catch (error) {
        console.error('Registration error:', error);
      }
    }
  },
};
</script>


<style scoped>
.signup-title {
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
  width: max-content;
  color: #666;
  margin-bottom: 8px;
}

.input-group input {
  width: 90%;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 5px;
  outline: none;
}

.input-group input:focus {
  border: 2px solid #845AA4;
}

.radio-group {
  margin-bottom: 20px;
  width: 100%;
}

.radio-group label {
  margin-right: 15px;
  width: max-content;
}

.radio-options {
  display: flex;
  align-items: center;
  gap: 20px;
}

.radio-options label {
  display: flex;
  align-items: center;
  gap: 5px;
}

.signup-button {
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

.signup-button:hover {
  background-color: #6c4891;
}
</style>
