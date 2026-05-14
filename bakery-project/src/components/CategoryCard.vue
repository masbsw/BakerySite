<template>
  <div class="container mx-auto px-4 mb-[60px] md:mb-[90px] lg:mb-[135px]">
    <div class="flex flex-wrap justify-center gap-[20px] sm:gap-[30px] lg:gap-[60px]">
      <div 
        v-for="category in categories" 
        :key="category.categoryId" 
        class="text-center w-[120px] sm:w-[150px] md:w-[180px] lg:w-[205px]"
      >
        <div class="category-card bg-[#FADFC5] rounded-[30px] md:rounded-[50px] lg:rounded-[65px] p-[1px] w-full">
          <img 
            :src="category.categoryImage" 
            :alt="category.categoryName" 
            class="w-[50%] sm:w-[55%] lg:w-[58%] h-auto mx-auto m-[15px] sm:my-8 lg:m-[43px]"
          >
        </div>
        <h3 class="mt-2 sm:mt-3 lg:mt-4 text-base sm:text-xl lg:text-2xl font-medium">
          {{ category.categoryName }}
        </h3>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      categories: []
    };
  },
  async created() {
    try {
      const response = await axios.get('/api/categories');
      this.categories = response.data.sort((a, b) => a.categoryId - b.categoryId);
    } catch (error) {
      console.error("Ошибка при получении категорий:", error);
    }
  }
};
</script>

<style>
.category-card {
  box-shadow: 0px 3px 3px 0px rgba(0, 0, 0, 0.3);
  transition: transform 0.3s ease;
}

.category-card:hover {
  transform: translateY(-5px);
}

@media (max-width: 425px) {
  .category-card {
    box-shadow: 0px 2px 2px 0px rgba(0, 0, 0, 0.2);
  }
}
</style>