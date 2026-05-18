<template>
  <div class="product-container">
    <div v-if="products && products.length" class="product-grid">
      <div 
        v-for="product in products" 
        :key="product.productId" 
        class="product-item">
        <div class="product-image-frame">
          <img :src="product.productImg" :alt="product.productName" class="product-image">
        </div>
        <h5 class="product-name" @click="openInfo(product)">
          {{ product.productName }}
        </h5>
        <p class="product-price">{{ product.productPrice }} руб.</p>
        
        <div class="product-button">
          <ButtonProduct 
            :quantity="product.productQuantity"
            :in-cart="isInCart(product.productId)"
            :cart-quantity="getQuantityInCart(product.productId)"
            @add="addToCart(product.productId, 1)"
            @increase="increaseQuantity(product.productId)"
            @decrease="decreaseQuantity(product.productId)"
          />
        </div>
      </div>
    </div>
    <div v-else class="no-products">
      Товары не найдены
    </div>

    <ProductInfo
      v-if="selectedProduct"
      :product="selectedProduct"
      :is-visible="isInfoVisible"
      @close="closeInfo"
    />
  </div>
</template>

<script>
import ButtonProduct from '@/cross-components/ButtonProduct.vue';
import ProductInfo from './ProductInfo.vue';
import { v4 as uuidv4 } from 'uuid';
import axios from 'axios';

export default {
  components: {
    ButtonProduct,
    ProductInfo
  },
  props: {
    products: {
      type: Array,
      required: true,
      default: () => []
    },
  },
  data() {
    return {
      selectedProduct: null,
      isInfoVisible: false,
      sessionId: localStorage.getItem('sessionId') || null,
      cart: []
    }
  },
  async created() {
    if (!this.sessionId) {
      this.sessionId = uuidv4();
      localStorage.setItem('sessionId', this.sessionId);
    }
    
    await this.loadCart();
  },
  methods: {
    async loadCart() {
      if (!this.sessionId) return;
      
      try {
        const response = await axios.get(`/cart-api/cart/session/${this.sessionId}`);
        this.cart = response.data;
      } catch (error) {
        console.error("Ошибка загрузки корзины:", error);
      }
    },

    async addToCart(productId, quantity) {
      if (!this.sessionId) {
        this.sessionId = uuidv4();
        localStorage.setItem('sessionId', this.sessionId);
      }
      
      try {
        await axios.post('/cart-api/cart', {
          itemSessionId: this.sessionId,
          productId: productId,
          itemQuantity: quantity
        });
        await this.loadCart();
      } catch (error) {
        console.error("Ошибка добавления в корзину:", error);
      }
    },

    isInCart(productId) {
      return this.cart.some(item => item.productId === productId);
    },

    getQuantityInCart(productId) {
      const item = this.cart.find(item => item.productId === productId);
      return item ? item.itemQuantity : 0;
    },

    async increaseQuantity(productId) {
      await this.addToCart(productId, 1);
    },

    async decreaseQuantity(productId) {
      if (!this.sessionId) return;
      
      try {
        await axios.put(`/cart-api/cart/decrease/${this.sessionId}/${productId}`);
        await this.loadCart();
      } catch (error) {
        console.error("Ошибка уменьшения количества:", error);
      }
    },

    openInfo(product) {
      this.selectedProduct = product;
      this.isInfoVisible = true;
    },
    closeInfo() {
      this.selectedProduct = null;
      this.isInfoVisible = false;
    }
  }
};
</script>

<style scoped>
.product-container {
    width: 100%; 
    padding: 0;
    box-sizing: border-box;
}

.product-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(min(100%, 240px), 1fr));
    gap: clamp(24px, 3vw, 36px);
    width: 100%;
    max-width: 100%;
}

.product-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: min(100%, 300px);
    margin: 0 auto;
    min-width: 0;
}

.product-image-frame {
    width: 100%;
    max-width: 300px;
    aspect-ratio: 1 / 1;
    display: flex;
}

.product-image {
    width: 100%;
    height: 100%;
    object-fit: contain;
    object-position: center;
    background-color: #f4f4f4;
    border-radius: 10%;
    box-shadow: 0px 5px 5px 0px rgba(0, 0, 0, 0.35);
}

.product-name {
    font-size: 20px;
    font-weight: normal;
    text-align: center;
    margin-top: 17px;
    cursor: pointer;
}

.product-price {
    font-size: 16px;
    font-weight: 600;
    text-align: center;
    margin-top: 3px;
}

.product-button {
    margin-top: 10px;
    width: min(100%, 280px);
}

.no-products {
    text-align: center;
    padding: 40px 0;
    font-size: 18px;
}

@media (max-width: 768px) {
  .product-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .product-grid {
    grid-template-columns: repeat(1, 1fr);
  }

  .product-image-frame {
    width: min(100%, 260px);
  }

  .product-item {
    margin-bottom: 15px;
  }
}
</style>
