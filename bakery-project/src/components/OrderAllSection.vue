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
      productsData: [],
      showOrderSuccess: false
    };
  },
  methods: {
    updateCartStatus(items) {
      this.cartItems = items;
      this.hasItemsInCart = items.length > 0;
    },

    handleOrderCreated() {
      this.$refs.cartSection?.clearCartState();
      this.showOrderSuccess = true;
    },

    goToCatalog() {
      this.showOrderSuccess = false;
      this.$router.push('/catalog');
    }
  }
};
</script>

<template>
  <div class="order-page-container">
    <Breadcrumbs pageName="Заказ" />
    <div v-if="showOrderSuccess" class="success-state">
      <h1 class="success-heading text-3xl md:text-5xl font-semibold mb-6 md:mb-12 mt-6 md:mt-10">Заказ</h1>
      <div class="success-card">
        <p class="success-title">Заказ принят!</p>
        <p class="success-text">Мы свяжемся с вами для подтверждения.</p>
        <button type="button" class="success-button" @click="goToCatalog">
          Перейти в каталог
        </button>
      </div>
    </div>
    <template v-else>
      <CartSection
        ref="cartSection"
        class="cart-section"
        @cart-updated="updateCartStatus"
      />
      
      <PlacingSection 
        v-if="hasItemsInCart"
        :cart-items="cartItems"
        @order-created="handleOrderCreated"
      />
    </template>
  </div>
</template>

  <style scoped>
  .success-state {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .success-card {
    width: min(620px, 100%);
    margin-bottom: 24px;
    padding: 24px 28px;
    border: 1px solid #000;
    border-radius: 24px;
    background-color: #f1dfcc;
  }

  .success-title {
    font-size: 32px;
    font-weight: 500;
    margin-bottom: 8px;
  }

  .success-text {
    font-size: 20px;
    margin-bottom: 20px;
  }

  .success-button {
    min-width: 240px;
    padding: 12px 24px;
    border: 1px solid #000;
    border-radius: 16px;
    background-color: rgba(255, 255, 255, 0.45);
    font-size: 20px;
    transition: background-color 0.2s ease;
  }

  .success-button:hover {
    background-color: rgba(255, 255, 255, 0.7);
  }

  .cart-section{
    width: 100%;
  }

  .success-heading {
    align-self: flex-start;
    width: 100%;
  }

  .order-page-container {
    width: min(100%, var(--site-content-width));
    margin: 0 auto;
    padding: 0 var(--site-page-gutter);
    box-sizing: border-box;
  }
  
  @media (max-width: 768px) {
    .success-state {
      align-items: stretch;
    }

    .success-heading {
      margin-left: 0;
    }

    .success-card {
      padding: 20px;
      border-radius: 20px;
    }

    .success-title {
      font-size: 26px;
    }

    .success-text {
      font-size: 18px;
    }

    .success-button {
      width: 100%;
      min-width: 0;
      font-size: 18px;
    }

    .order-page-container {
      padding: 0 10px;
    }
  }
  </style>
