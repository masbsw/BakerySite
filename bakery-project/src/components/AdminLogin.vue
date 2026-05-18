<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
      <h2 class="text-2xl font-bold mb-6 text-center">Вход в админ-панель</h2>
      <form @submit.prevent="handleLogin">
        <div class="mb-4">
          <label class="block text-gray-700 mb-2" for="username">Логин</label>
          <input
            v-model="username"
            type="text"
            id="username"
            class="w-full px-3 py-2 border rounded-lg"
            required
          />
        </div>
        <div class="mb-6">
          <label class="block text-gray-700 mb-2" for="password">Пароль</label>
          <input
            v-model="password"
            type="password"
            id="password"
            class="w-full px-3 py-2 border rounded-lg"
            required
          />
        </div>
        <button
          type="submit"
          class="w-full bg-brown-600 text-black py-2 px-4 rounded-lg hover:bg-brown-700 transition"
        >
          Войти
        </button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { clearAdminSession, saveAdminSession } from '@/utils/adminAuth';

export default {
  data() {
    return {
      username: '',
      password: ''
    };
  },
  methods: {
    async handleLogin() {
      try {
        const response = await axios.post('/admin-api/auth/login', {
          username: this.username,
          password: this.password
        });

        if (response.data.role !== 'ADMIN') {
          clearAdminSession();
          alert('Доступ в админ-панель разрешён только пользователям с ролью ADMIN');
          return;
        }

        saveAdminSession(response.data);
        this.$router.push(this.$route.query.redirect || '/admin/dashboard');
      } catch (error) {
        console.error('Ошибка входа:', error);
        clearAdminSession();
        alert('Неверные учетные данные или проблемы с сервером');
      }
    }
  }
};
</script>
