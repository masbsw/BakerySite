<template>
  <section class="py-8 sm:py-12">
    <div class="container mx-auto px-4">
      <h2 class="text-[28px] sm:text-[36px] md:text-[42px] lg:text-5xl font-medium text-center mb-[40px] sm:mb-[60px] md:mb-[95px]">
        Мы обо всём позаботились
      </h2>
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-[30px] sm:gap-[40px] lg:gap-[60px]">
        <CareCard
          imageSrc="/info1.png"
          title="Лучшие ингредиенты"
          description="Мы используем только натуральные и свежие продукты"
        />

        <CareCard
          imageSrc="/info2.png"
          title="Упаковка"
          description="Каждый товар упакован с любовью и вниманием к деталям"
        />

        <CareCard
          imageSrc="/info3.png"
          title="Получение в день заказа"
          description="В день заказа доставка курьером или самовывоз"
        />

        <CareCard
          imageSrc="/info4.png"
          title="Анонимная доставка"
          description="Можем преподнести Ваш заказ как анонимный подарок"
        />
      </div>
    </div>
  </section>
</template>

<script>
import CareCard from './CareCard.vue';

export default {
  name: 'CareSection',
  components: {
      CareCard,
  },
};
</script>