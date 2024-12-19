<template>
  <div class="navbar">
    <div class="logo">
      <router-link to="/"><img src="../assets/asset.svg" alt="Logo"></router-link>
      <p>JOBSTEER</p>
    </div>
    <div class="navlinks-main">
      <router-link to="/" class="navlink">Home</router-link>
      <router-link v-if="isLoggedIn && user.role=='jobseeker'" to="/upload-resume" class="navlink">Upload Resume</router-link>
      <router-link v-else-if="isLoggedIn && user.role=='recruiter'" to="/upload-job" class="navlink">Upload Job</router-link>
    </div>
    <div class="navlinks-side">
      <button v-if="!isLoggedIn" class="nav-button login" @click="showLoginModal = true">Login</button>
      <button v-if="!isLoggedIn" class="nav-button register" @click="showRegisterModal = true">Register</button>
      <button v-if="isLoggedIn" class="nav-button profile" @click="goToProfile">Profile</button>
      <button v-if="isLoggedIn" class="nav-button logout" @click="handleSignOut">Sign Out</button>
      <!-- Login Modal -->
      <VModal :isVisible="showLoginModal" @close="showLoginModal = false">
        <UserLogin @login-success="handleLoginSuccess" />
      </VModal>
      <!-- Register Modal -->
      <VModal :isVisible="showRegisterModal" @close="showRegisterModal = false">
        <UserSignup @register-success="handleRegisterSuccess" />
      </VModal>
    </div>
  </div>
</template>

<script>
import VModal from './VModal.vue';
import UserLogin from './UserLogin.vue';
import UserSignup from './UserSignup.vue';
import { useUserStore } from '../store/user';

export default {
  components: {
    VModal,
    UserLogin,
    UserSignup,
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
      console.log("user: ",userStore.user);
      return userStore.user;
    }
  }
  ,
  methods: {
    handleLoginSuccess() {
      this.showLoginModal = false;
      this.$router.push('/upload-resume');
    },
    handleRegisterSuccess() {
      this.showRegisterModal = false;
      this.showLoginModal = true;
    },
    goToProfile() {
      this.$router.push('/profile');
    },
    handleSignOut() {
      const userStore = useUserStore();
      userStore.logoutUser();
      this.$router.push("/"); 
    }
  }
};
</script>


<style scoped>
/* General Navbar Styling */
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #ffffff;
  padding: 15px 30px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

/* Logo */
.logo img {
  height: 40px;
  object-fit: contain;
}

.logo {
  display: flex;
  gap: 4px;
  color: #845AA4;
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
}
/* Main navigation links */
.navlinks-main {
  display: flex;
  gap: 25px;
}

.navlink {
  text-decoration: none;
  color: #845AA4;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.3s ease;
  position: relative;
  padding: 8px 12px;
}

.navlink:hover {
  color: #5d3e7e;
}

.navlinks-side {
  display: flex;
  gap: 15px;
  align-items: center;
}

.nav-button {
  font-size: 16px;
  font-weight: 500;
  padding: 8px 20px;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
}

.nav-button.login {
  background-color: #e0e0e0;
  color: #333;
}

.nav-button.login:hover {
  background-color: #d6d6d6;
}

.nav-button.register {
  background-color: #845AA4;
  color: white;
}

.nav-button.logout {
  background-color: #5d3e7e;
  color: #fff;
}

.nav-button.register:hover {
  background-color: #5d3e7e;
}

/* Responsive Design */
@media (max-width: 768px) {
  .navlinks-main {
    display: none;
  }

  .navbar {
    flex-direction: column;
    align-items: flex-start;
  }

  .logo img {
    height: 35px;
  }
}

@media (max-width: 480px) {
  .nav-button {
    font-size: 12px;
    padding: 6px 12px;
  }

  .navlink {
    font-size: 14px;
  }
}
</style>
