<template>
    <nav class="content mt-[13%] mb-8">
      <ol class="list-reset flex">
        <li>
          <router-link to="/" class="text-grey-600 font-extralight hover:font-light transition-transform transform hover:scale-105">Главная</router-link>
        </li>
        <li><span class="mx-3 font-extralight">></span></li>
        <li class="text-black-500 font-normal">{{ pageName }}</li>
      </ol>
    </nav>
</template>

<style scoped>

@media (max-width: 768px) {
  .content{
    margin-left: 20px ;
  }
}

@media (max-width: 426px) {
  .content{
    margin-top: 26%;
    margin-left: 5px ;
  }
}

</style>



<script>
export default {
  name: 'Breadcrumbs',
  props:{
    pageName:{
        type: String,
        required: true,
    }
  }
};

</script>