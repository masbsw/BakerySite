<template>
  <div class="container">
    <Header />
    <div class="content flex items-center justify-center px-4">
      <div class="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
        <h2 class="text-2xl font-bold mb-6 text-center">Регистрация</h2>
        <form @submit.prevent="handleRegister">
          <div class="mb-4">
            <label class="block text-gray-700 mb-2" for="username">Логин</label>
            <input v-model="username" id="username" type="text" class="w-full px-3 py-2 border rounded-lg" required />
          </div>
          <div class="mb-6">
            <label class="block text-gray-700 mb-2" for="password">Пароль</label>
            <input v-model="password" id="password" type="password" class="w-full px-3 py-2 border rounded-lg" required />
          </div>
          <button type="submit" class="w-full bg-[#F1DFCC] text-black py-2 px-4 rounded-lg border border-black">
            Зарегистрироваться
          </button>
        </form>
        <p class="mt-4 text-center">
          Уже есть аккаунт?
          <router-link :to="loginLink" class="underline">Войти</router-link>
        </p>
      </div>
    </div>
    <Footer class="footer" />
  </div>
</template>

<script>
import axios from 'axios';
import Footer from '@/cross-components/Footer.vue';
import Header from '@/cross-components/Header.vue';
import { getUserToken, saveUserSession } from '@/utils/userAuth';

export default {
  components: { Header, Footer },
  data() {
    return {
      username: '',
      password: ''
    };
  },
  computed: {
    loginLink() {
      return this.$route.query.redirect
        ? `/login?redirect=${encodeURIComponent(this.$route.query.redirect)}`
        : '/login';
    }
  },
  created() {
    if (getUserToken()) {
      this.$router.replace(this.$route.query.redirect || '/order');
    }
  },
  methods: {
    async handleRegister() {
      try {
        const response = await axios.post('/admin-api/auth/register', {
          username: this.username,
          password: this.password
        });
        saveUserSession(response.data);
        this.$router.push(this.$route.query.redirect || '/order');
      } catch (error) {
        console.error('Ошибка регистрации:', error);
        alert(error.response?.data || 'Не удалось зарегистрироваться');
      }
    }
  }
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.content {
  flex: 1;
  padding-top: 140px;
}

.footer {
  width: 100%;
}
</style>
