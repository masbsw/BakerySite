<script>
import Breadcrumbs from '@/cross-components/Breadcrumbs.vue';
import CartSection from './CartSection.vue';
import PlacingSection from './PlacingSection.vue';



export default {
  components: {
    Breadcrumbs,
    CartSection,
    PlacingSection
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

<template>
  <div class="order-page-container">
            <Breadcrumbs pageName="Заказ" class="ml-[3%]"/>
    <CartSection class="cart-section"
      @cart-updated="updateCartStatus"
    />
    
    <PlacingSection 
      v-if="hasItemsInCart"
      :cart-items="cartItems"
    />
  </div>
</template>

  <style scoped>
  .cart-section{
    margin-left: 3%;
  }
  .order-page-container {
    margin-left: 0%;
    width: 95%;
    max-width: 100%;
    padding: 0 15px;
    box-sizing: border-box;
  }
  
  @media (max-width: 768px) {
    .order-page-container {
      padding: 0 10px;
    }
  }
  </style>
