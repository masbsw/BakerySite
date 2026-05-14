<template>
  <section class="w-full sm:w-[94%] mx-auto px-4 sm:px-0">
    <Breadcrumbs pageName="Контакты"/>
    <div class="p-4 sm:p-6">
      <h2 class="text-[28px] sm:text-[36px] md:text-[48px] font-semibold mb-4">Контакты</h2>
      <hr class="h-[1px] sm:h-[1.5px] bg-black">
      <div class="grid grid-cols-1  md:grid-cols-2 gap-4 mx-[7%] my-[5%] sm:gap-4 sm:my-[5%]">
        <div>
          <p class="font-medium text-[18px] sm:text-[24px] md:text-3xl text-[#3A1F0D]">Номер телефона:</p>
          <p class="font-regular text-[16px] sm:text-[20px] md:text-2xl mt-2 sm:mt-[13px]">+7 000 000 00 00</p>
        </div>
        <div>
          <p class="font-medium text-[18px] sm:text-[24px] md:text-3xl text-[#3A1F0D]">Адрес:</p>
          <p class="font-regular text-[16px] sm:text-[20px] md:text-2xl mt-2 sm:mt-[13px]">Ростов-на-Дону, 344092, проспект Космонавтов, 1/26</p>
        </div>
        <div>
          <p class="font-medium text-[18px] sm:text-[24px] md:text-3xl text-[#3A1F0D]">Почта:</p>
          <p class="font-regular text-[16px] sm:text-[20px] md:text-2xl mt-2 sm:mt-[13px]">javoronok@mail.com</p>
        </div>
        <div>
          <p class="font-medium text-[18px] sm:text-[24px] md:text-3xl text-[#3A1F0D]">Режим работы:</p>
          <p class="font-regular text-[16px] sm:text-[20px] md:text-2xl mt-2 sm:mt-[13px]">пн-пт 8:00 - 21:00<br>сб-вс 9:00 - 21:00</p>
        </div>
      </div>
      <hr class="h-[1px] sm:h-[1.5px] bg-black">
    </div>
  </section>
</template>

<script>
import Breadcrumbs from '@/cross-components/Breadcrumbs.vue';


export default{
  components:{
    Breadcrumbs
  }
}

</script>