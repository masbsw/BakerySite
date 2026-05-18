<template>
  <header class="site-header">
    <div class="header-shell">
      <router-link to="/" class="brand">
        <img src="/logo.svg" alt="Логотип Жаворонок" class="brand-logo" />
        <h2 class="brand-title">ЖАВОРОНОК</h2>
      </router-link>

      <nav class="desktop-nav">
        <ul class="desktop-nav-list">
          <router-link to="/" v-slot="{ isActive }">
            <li :class="['text-normal', { 'font-semibold': isActive }, 'hover:font-semibold transition-transform hover:scale-105']">Главная</li>
          </router-link>
          <router-link to="/catalog" v-slot="{ isActive }">
            <li :class="['text-normal', { 'font-semibold': isActive }, 'hover:font-semibold transition-transform hover:scale-105']">Каталог</li>
          </router-link>
          <router-link to="/order" v-slot="{ isActive }">
            <li :class="['text-normal', { 'font-semibold': isActive }, 'hover:font-semibold transition-transform hover:scale-105']">Заказ</li>
          </router-link>
          <router-link to="/contacts" v-slot="{ isActive }">
            <li :class="['text-normal', { 'font-semibold': isActive }, 'hover:font-semibold transition-transform hover:scale-105']">Контакты</li>
          </router-link>
        </ul>
      </nav>

      <div class="desktop-auth auth-panel">
        <template v-if="isAuthenticated">
          <span class="auth-username">{{ username }}</span>
          <button @click="logout" class="auth-button auth-button-outline">Выйти</button>
        </template>
        <template v-else>
          <router-link to="/login" class="auth-button auth-button-filled">
            Войти / зарегистрироваться
          </router-link>
        </template>
      </div>

      <button 
        @click="toggleMenu" 
        class="menu-toggle focus:outline-none"
        aria-label="Меню"
      >
        <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path 
            stroke-linecap="round" 
            stroke-linejoin="round" 
            stroke-width="2" 
            d="M4 6h16M4 12h16M4 18h16"
          />
        </svg>
      </button>
    </div>


    <Transition name="slide">
      <div 
        v-if="isMenuOpen" 
        class="mobile-menu"
      >
        <nav>
          <ul class="mobile-nav-list">
            <router-link 
              to="/" 
              @click="closeMenu"
              v-slot="{ isActive }"
            >
              <li :class="['py-2', { 'font-semibold': isActive }, 'hover:font-semibold']">Главная</li>
            </router-link>
            
            <router-link 
              to="/catalog" 
              @click="closeMenu"
              v-slot="{ isActive }"
            >
              <li :class="['py-2', { 'font-semibold': isActive }, 'hover:font-semibold']">Каталог</li>
            </router-link>
            
            <router-link 
              to="/order" 
              @click="closeMenu"
              v-slot="{ isActive }"
            >
              <li :class="['py-2', { 'font-semibold': isActive }, 'hover:font-semibold']">Заказ</li>
            </router-link>
            
            <router-link 
              to="/contacts" 
              @click="closeMenu"
              v-slot="{ isActive }"
            >
              <li :class="['py-2', { 'font-semibold': isActive }, 'hover:font-semibold']">Контакты</li>
            </router-link>
            
            <template v-if="isAuthenticated">
              <li class="font-semibold py-2">{{ username }}</li>
              <li>
                <button @click="logout" class="mobile-auth-button">Выйти</button>
              </li>
            </template>
            <template v-else>
              <li>
                <router-link to="/login" @click="closeMenu" class="mobile-auth-button mobile-auth-button-filled">
                  Войти / зарегистрироваться
                </router-link>
              </li>
            </template>
          </ul>
        </nav>
      </div>
    </Transition>
  </header>
</template>

<script setup>
import { onBeforeUnmount, onMounted, ref } from 'vue';
import { clearUserSession, getUserAuthEventName, getUserUsername, isUserAuthenticated } from '@/utils/userAuth';

const isMenuOpen = ref(false);
const isAuthenticated = ref(false);
const username = ref('');

const syncUserState = () => {
  isAuthenticated.value = isUserAuthenticated();
  username.value = getUserUsername() || '';
};

const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value;
};

const closeMenu = () => {
  isMenuOpen.value = false;
};

const logout = () => {
  clearUserSession();
  closeMenu();
};

onMounted(() => {
  syncUserState();
  window.addEventListener('storage', syncUserState);
  window.addEventListener(getUserAuthEventName(), syncUserState);
});

onBeforeUnmount(() => {
  window.removeEventListener('storage', syncUserState);
  window.removeEventListener(getUserAuthEventName(), syncUserState);
});
</script>

<style scoped>
:global(:root) {
  --site-header-height: 152px;
  --site-header-drip-height: 52px;
  --site-page-gutter: clamp(16px, 3vw, 40px);
  --site-content-width: 1440px;
}

.site-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  width: 100%;
  height: var(--site-header-height);
  z-index: 50;
  overflow: visible;
  isolation: isolate;
}

.site-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: calc(var(--site-header-height) - var(--site-header-drip-height));
  background-color: #f6dabf;
  z-index: -1;
}

.site-header::after {
  content: '';
  position: absolute;
  left: 0;
  right: 0;
  top: calc(var(--site-header-height) - var(--site-header-drip-height));
  height: var(--site-header-drip-height);
  background-image: url('/header-drips.png');
  background-repeat: repeat-x;
  background-position: center top;
  background-size: auto 100%;
  z-index: -1;
  pointer-events: none;
}

.header-shell {
  position: relative;
  width: min(100%, var(--site-content-width));
  height: calc(var(--site-header-height) - var(--site-header-drip-height));
  margin: 0 auto;
  padding: 6px var(--site-page-gutter) 0;
  display: grid;
  grid-template-columns: auto 1fr auto;
  align-items: start;
  gap: 20px;
}

.brand {
  display: inline-flex;
  align-items: center;
  gap: clamp(8px, 1.2vw, 16px);
  min-width: 0;
}

.brand-logo {
  width: clamp(58px, 7vw, 96px);
  flex-shrink: 0;
}

h2 {
  font-family: "Oranienbaum", serif;
}

.brand-title {
  font-size: clamp(28px, 3vw, 40px);
  font-weight: 400;
  line-height: 1;
  white-space: nowrap;
}

.desktop-nav {
  display: block;
  min-width: 0;
}

.desktop-nav-list {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: clamp(24px, 4vw, 60px);
  padding-top: 18px;
}

.desktop-auth {
  display: flex;
  align-items: center;
  gap: 12px;
  padding-top: 8px;
}

.auth-panel {
  min-width: 260px;
  justify-content: flex-end;
}

.auth-username {
  font-size: 18px;
  font-weight: 600;
}

.auth-button,
.mobile-auth-button {
  border: 1px solid #000;
  border-radius: 999px;
  padding: 8px 18px;
  font-size: 15px;
  transition: all 0.2s ease;
}

.auth-button:hover,
.mobile-auth-button:hover {
  transform: translateY(-1px);
}

.auth-button-outline,
.mobile-auth-button {
  background: rgba(255, 255, 255, 0.7);
}

.auth-button-filled,
.mobile-auth-button-filled {
  background: #f1dfcc;
}

.mobile-auth-button {
  display: inline-block;
  min-width: 170px;
  text-align: center;
}

.menu-toggle {
  display: none;
  justify-self: end;
  align-self: start;
  margin-top: 14px;
}

.mobile-menu {
  display: none;
}

.mobile-nav-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding: 18px 20px 22px;
}

.slide-enter-active,
.slide-leave-active {
  transition: all 0.3s ease;
}

.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}

@media (max-width: 1279px) {
:global(:root) {
    --site-header-height: 142px;
    --site-header-drip-height: 48px;
  }

  .header-shell {
    gap: 16px;
  }

  .desktop-nav-list {
    gap: 28px;
    padding-top: 16px;
  }

  .auth-panel {
    min-width: 220px;
  }
}

@media (max-width: 1110px) {
:global(:root) {
    --site-header-height: 134px;
    --site-header-drip-height: 44px;
  }

  .header-shell {
    grid-template-columns: minmax(0, 1fr) auto;
    gap: 12px;
  }

  .desktop-nav,
  .desktop-auth {
    display: none;
  }

  .menu-toggle {
    display: inline-flex;
  }

  .mobile-menu {
    display: block;
    position: absolute;
    left: 12px;
    right: 12px;
    top: calc(var(--site-header-height) - 12px);
    background: rgba(255, 255, 255, 0.96);
    border: 1px solid rgba(0, 0, 0, 0.12);
    border-radius: 20px;
    box-shadow: 0 12px 32px rgba(0, 0, 0, 0.12);
    z-index: 40;
  }
}

@media (max-width: 767px) {
:global(:root) {
    --site-header-height: 118px;
    --site-header-drip-height: 36px;
  }

  .header-shell {
    padding-top: 6px;
  }

  .brand-logo {
    width: 56px;
  }

  .brand-title {
    font-size: 26px;
  }

  .menu-toggle {
    margin-top: 10px;
  }

  .mobile-nav-list {
    gap: 18px;
    padding: 16px 18px 20px;
  }
}

@media (max-width: 479px) {
:global(:root) {
    --site-header-height: 108px;
    --site-header-drip-height: 32px;
  }

  .header-shell {
    gap: 12px;
    padding-inline: 12px;
  }

  .brand {
    gap: 6px;
  }

  .brand-logo {
    width: 50px;
  }

  .brand-title {
    font-size: 22px;
  }
}
</style>
