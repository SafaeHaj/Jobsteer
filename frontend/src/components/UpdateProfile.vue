<template>
    <div class="profile-view">
      <div v-if="!isEditing">
        <h2 class="profile-title">Profile</h2>
        <div class="profile-info">
          <p><strong>Name:</strong> {{ user.fullName }}</p>
          <p><strong>Email:</strong> {{ user.email }}</p>
          <p v-if="user.role === 'jobseeker'"><strong>Location:</strong> {{ user.location }}</p>
          <p v-else-if="user.role === 'recruiter'"><strong>Company:</strong> {{ user.companyName }}</p>
        </div>
        <button @click="startEditing" class="edit-button">Edit Profile</button>
      </div>
  
      <form v-else @submit.prevent="submitForm" method="post">
        <h2 class="profile-title">Edit Profile</h2>
  
        <div class="input-group">
          <label for="email">Email</label>
          <input type="email" id="email" v-model="formData.email" required>
        </div>
  
        <div class="input-group">
          <label for="firstName">First Name</label>
          <input type="text" id="firstName" v-model="formData.firstName">
        </div>
  
        <div class="input-group">
          <label for="lastName">Last Name</label>
          <input type="text" id="lastName" v-model="formData.lastName">
        </div>
  
        <template v-if="user.role === 'jobseeker'">
          <div class="input-group">
            <label for="city">City</label>
            <input type="text" id="city" v-model="formData.city">
          </div>
          <div class="input-group">
            <label for="country">Country</label>
            <input type="text" id="country" v-model="formData.country">
          </div>
        </template>
  
        <template v-else-if="user.role === 'recruiter'">
          <div class="input-group">
            <label for="company">Company Name</label>
            <input type="text" id="company" v-model="formData.companyName">
          </div>
        </template>
  
        <div class="button-group">
          <button type="submit" class="save-button">Save Changes</button>
          <button type="button" @click="cancelEdit" class="cancel-button">Cancel</button>
        </div>
      </form>
    </div>
  </template>
  
  <script>
  import { useUserStore } from '../store/user';
  import { ref, computed } from 'vue';
  
  export default {
    name: 'ProfileView',
    setup() {
      const userStore = useUserStore();
      const isEditing = ref(false);
      const user = computed(() => userStore.user);
  
      const formData = ref({
        email: '',
        firstName: '',
        lastName: '',
        city: '',
        country: '',
        companyName: ''
      });
  
      const startEditing = () => {
        const currentUser = user.value;
        formData.value = {
          email: currentUser.email,
          firstName: currentUser.firstName,
          lastName: currentUser.lastName,
          companyName: currentUser.companyName || '',
          ...(currentUser.location ? {
            city: currentUser.location.split(',')[0].trim(),
            country: currentUser.location.split(',')[1].trim()
          } : {})
        };
        isEditing.value = true;
      };
  
      const cancelEdit = () => {
        isEditing.value = false;
      };
  
      const submitForm = async () => {
        try {
          const updateData = {
            email: formData.value.email,
            firstName: formData.value.firstName,
            lastName: formData.value.lastName,
            ...(user.value.role === 'jobseeker' ? {
              location: `${formData.value.city}, ${formData.value.country}`
            } : {
              companyName: formData.value.companyName
            })
          };
  
          await userStore.updateUser(updateData);
          isEditing.value = false;
        } catch (error) {
          console.error('Update error:', error);
        }
      };
  
      return {
        user,
        isEditing,
        formData,
        startEditing,
        cancelEdit,
        submitForm
      };
    }
  };
  </script>
  
  <style scoped>
  .profile-view {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
  }
  
  .profile-title {
    font-size: 24px;
    text-align: center;
    font-weight: bold;
    color: #333;
    margin-bottom: 20px;
  }
  
  .profile-info {
    background-color: #f4f4f9;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 20px;
  }
  
  .profile-info p {
    margin: 10px 0;
    padding: 8px;
    border-left: 4px solid #845AA4;
    color: #4a148c;
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
  
  .button-group {
    display: flex;
    gap: 15px;
    justify-content: center;
    margin-top: 20px;
  }
  
  .edit-button, .save-button, .cancel-button {
    padding: 12px 24px;
    font-size: 16px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
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
  
  .edit-button:hover, .save-button:hover {
    background-color: #6c4891;
  }
  
  .cancel-button:hover {
    background-color: #c82333;
  }
  </style>