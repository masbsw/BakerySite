<template>
    <div class="container">
        <Header />
        <div class="content">

            <OrderAllSection />
        </div>
        <Footer class="footer" />
    </div>
  </template>
  
  <script>
import OrderAllSection from '@/components/OrderAllSection.vue';
import Footer from '@/cross-components/Footer.vue';
import Header from '@/cross-components/Header.vue';


  
  export default {
    components: {
      Header,
      
      OrderAllSection,
      Footer

    },
    data() {
      return {
        hasItemsInCart: false,
        cartItems: [],
        productsData: []
      };
    },
    methods: {
      updateCartStatus(items) {
        this.cartItems = items;
        this.hasItemsInCart = items.length > 0;
      }
    }
  };
  </script>
  

  <style scoped>
.container {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
    width: 100%;
    max-width: 100%;
    margin: 0;
    padding: 0;
}

.content {
    flex: 1;
    width: 100%;
    padding: 0;
    box-sizing: border-box;
}

.footer {
    width: 100%;
    padding: 20px 0;
}

@media (max-width: 768px) {
    .content {
        padding: 0;
    }
}
</style>
