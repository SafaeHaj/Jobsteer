// src/views/ProfileView.vue

<template>
  <div class="profile-container">
    <!-- Profile Information Display -->
    <div v-if="!isEditing" class="profile-card">
      <h2>Profile Information</h2>
      <div class="profile-info">
        <p><strong>Name:</strong> {{ user?.fullName }}</p>
        <p><strong>Email:</strong> {{ user?.email }}</p>
        <p v-if="user?.role === 'jobseeker'"><strong>Location:</strong> {{ user?.location }}</p>
        <p v-else-if="user?.role === 'recruiter'"><strong>Company:</strong> {{ user?.companyName }}</p>
      </div>
      <button @click="startEditing" class="edit-button">Edit Profile</button>
    </div>

    <!-- Edit Profile Form -->
    <form v-else @submit.prevent="submitForm" class="edit-form">
      <h2>Edit Profile</h2>

      <div class="input-group">
        <label for="email">Email</label>
        <input type="email" id="email" v-model="formData.email" required>
      </div>

      <div class="input-group">
        <label for="firstName">First Name</label>
        <input type="text" id="firstName" v-model="formData.firstName" required>
      </div>

      <div class="input-group">
        <label for="lastName">Last Name</label>
        <input type="text" id="lastName" v-model="formData.lastName" required>
      </div>

      <template v-if="user?.role === 'jobseeker'">
        <div class="input-group">
          <label for="location">Location</label>
          <input type="text" id="location" v-model="formData.location" 
                 placeholder="City, Country">
        </div>
      </template>

      <template v-else-if="user?.role === 'recruiter'">
        <div class="input-group">
          <label for="companyName">Company Name</label>
          <input type="text" id="companyName" v-model="formData.companyName" required>
        </div>
      </template>

      <div class="button-group">
        <button type="submit" class="save-button">Save Changes</button>
        <button type="button" @click="cancelEdit" class="cancel-button">Cancel</button>
      </div>
    </form>

    <!-- Success/Error Messages -->
    <div v-if="message" :class="['message', messageType]">
      {{ message }}
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import { useUserStore } from '../store/user';
import { useRouter } from 'vue-router';

export default {
  name: 'ProfileView',
  setup() {
    const userStore = useUserStore();
    const router = useRouter();
    const isEditing = ref(false);
    const message = ref('');
    const messageType = ref('');
    
    const user = computed(() => userStore.user);

    const formData = ref({
      email: '',
      firstName: '',
      lastName: '',
      location: '',
      companyName: ''
    });

    onMounted(() => {
      if (!userStore.user) {
        router.push('/login');
      }
    });

    const startEditing = () => {
      formData.value = {
        email: user.value.email,
        firstName: user.value.firstName,
        lastName: user.value.lastName,
        location: user.value.location || '',
        companyName: user.value.companyName || ''
      };
      isEditing.value = true;
    };

    const cancelEdit = () => {
      isEditing.value = false;
      message.value = '';
    };

    const showMessage = (text, type = 'success') => {
      message.value = text;
      messageType.value = type;
      setTimeout(() => {
        message.value = '';
      }, 3000);
    };

    const submitForm = async () => {
      try {
        await userStore.updateUser(formData.value);
        isEditing.value = false;
        showMessage('Profile updated successfully!');
      } catch (error) {
        showMessage(error.response?.data?.message || 'Error updating profile', 'error');
      }
    };

    return {
      user,
      isEditing,
      formData,
      message,
      messageType,
      startEditing,
      cancelEdit,
      submitForm
    };
  }
};
</script>

<style scoped>
.profile-container {
  max-width: 600px;
  margin: 40px auto;
  padding: 20px;
}

.profile-card, .edit-form {
  background-color: #f4f4f9;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h2 {
  color: #333;
  text-align: center;
  margin-bottom: 20px;
}

.profile-info p {
  padding: 10px;
  margin: 5px 0;
  border-left: 4px solid #845AA4;
  background-color: white;
}

.input-group {
  margin-bottom: 20px;
}

.input-group label {
  display: block;
  margin-bottom: 5px;
  color: #666;
}

.input-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.input-group input:focus {
  border-color: #845AA4;
  outline: none;
}

.button-group {
  display: flex;
  gap: 10px;
  justify-content: center;
  margin-top: 20px;
}

.edit-button, .save-button, .cancel-button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.edit-button, .save-button {
  background-color: #845AA4;
  color: white;
}

.cancel-button {
  background-color: #dc3545;
  color: white;
}

.message {
  margin-top: 20px;
  padding: 10px;
  border-radius: 4px;
  text-align: center;
}

.success {
  background-color: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}

.error {
  background-color: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}
</style>