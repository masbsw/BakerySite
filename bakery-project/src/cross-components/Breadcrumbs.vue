<template>
    <nav class="content">
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
.content {
  margin: 0 0 32px;
  padding-top: calc(var(--site-header-height) + 20px);
}

@media (max-width: 768px) {
  .content {
    margin-bottom: 24px;
    padding-top: calc(var(--site-header-height) + 16px);
  }
}

@media (max-width: 426px) {
  .content {
    padding-top: calc(var(--site-header-height) + 12px);
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
