<template>
  <div class="container">
    <Header />
    <div class="content flex items-center justify-center px-4">
      <div class="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
        <h2 class="text-2xl font-bold mb-6 text-center">Вход</h2>
        <form @submit.prevent="handleLogin">
          <div class="mb-4">
            <label class="block text-gray-700 mb-2" for="username">Логин</label>
            <input v-model="username" id="username" type="text" class="w-full px-3 py-2 border rounded-lg" required />
          </div>
          <div class="mb-6">
            <label class="block text-gray-700 mb-2" for="password">Пароль</label>
            <input v-model="password" id="password" type="password" class="w-full px-3 py-2 border rounded-lg" required />
          </div>
          <button type="submit" class="w-full bg-[#F1DFCC] text-black py-2 px-4 rounded-lg border border-black">
            Войти
          </button>
        </form>
        <p class="mt-4 text-center">
          Нет аккаунта?
          <router-link :to="registerLink" class="underline">Зарегистрироваться</router-link>
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
import { clearAdminSession, saveAdminSession } from '@/utils/adminAuth';
import { getUserRole, getUserToken, saveUserSession } from '@/utils/userAuth';

export default {
  components: { Header, Footer },
  data() {
    return {
      username: '',
      password: ''
    };
  },
  computed: {
    registerLink() {
      return this.$route.query.redirect
        ? `/register?redirect=${encodeURIComponent(this.$route.query.redirect)}`
        : '/register';
    }
  },
  created() {
    if (getUserToken()) {
      if (getUserRole() === 'ADMIN') {
        this.$router.replace('/admin/dashboard');
        return;
      }

      this.$router.replace(this.$route.query.redirect || '/order');
    }
  },
  methods: {
    async handleLogin() {
      try {
        const response = await axios.post('/admin-api/auth/login', {
          username: this.username,
          password: this.password
        });
        saveUserSession(response.data);

        if (response.data.role === 'ADMIN') {
          saveAdminSession(response.data);
          this.$router.push('/admin/dashboard');
          return;
        }

        clearAdminSession();
        this.$router.push(this.$route.query.redirect || '/order');
      } catch (error) {
        console.error('Ошибка входа:', error);
        alert('Неверный логин или пароль');
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
