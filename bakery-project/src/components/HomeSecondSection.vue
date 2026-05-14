<template>
    <div class="flex items-center justify-center mt-[80px] md:mt-[121px] mb-[60px] md:mb-[101px] px-4">
      <p class="w-full md:w-[89%] text-[24px] sm:text-[32px] md:text-[48px] font-normal text-center leading-tight md:leading-normal">
        Загляните к нам за свежей выпечкой или закажите праздничный торт для вашего особого события. Мы всегда готовы порадовать вас!
      </p>
    </div>
    <CategoryCard />
  </template>
  
  <script>
  import CategoryCard from './CategoryCard.vue';
  
  export default {
    components: {
      CategoryCard
    }
  }
  </script>
  
