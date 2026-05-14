<template>
  <header class="bg-cover bg-[url('/header.svg')] bg-center bg-no-repeat lg:h-[152px] h-[100px] fixed top-0 w-full z-50">
    <div class="flex justify-between items-center px-8 pt-[5px]">
      <router-link to="/" class="flex items-center lg:space-x-4 space-x-1 ">
        <img src="/logo.svg" alt="Логотип Жаворонок" class="lg:w-24 w-14" />
        <h2 class="lg:text-[40px] text-2xl font-normal ">ЖАВОРОНОК</h2>
      </router-link>

      <nav class="hidden lg:block">
        <ul class="flex items-center space-x-[60px]">
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

      <div class="contacts hidden lg:flex flex-col items-center">
        <p class="xl:text-[24px] font-normal lg:text-[20px]">+7 000 000 00 00</p>
        <p class="xl:text-[14px] font-normal lg:text-[10px]">00:00 - 10:00, без выходных</p>
      </div>

      <button 
        @click="toggleMenu" 
        class="lg:hidden focus:outline-none"
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
        class="lg:hidden bg-white w-full absolute left-0 top-[152px] shadow-lg z-40"
      >
        <nav>
          <ul class="flex flex-col items-center space-y-6 p-4">
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
            
            <div>
              <li class="text-[16px] font-normal py-2">+7 000 000 00 00</li>
              <li class="text-[10px] font-normal py-2">00:00 - 00:00, без выходных</li>
            </div>
          </ul>
        </nav>
      </div>
    </Transition>
  </header>
</template>

<script setup>
import { ref } from 'vue';

const isMenuOpen = ref(false);

const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value;
};

const closeMenu = () => {
  isMenuOpen.value = false;
};
</script>

<style scoped>
h2 {
  font-family: "Oranienbaum", serif;
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
</style>