<template>
  <div class="w-full px-4 mx-auto">
    <h1 class="text-3xl md:text-5xl font-semibold mb-6 md:mb-12 mt-6 md:mt-10">Заказ</h1>
    
    <div v-if="cartItems.length > 0">
      <!-- Список товаров -->
      <div v-for="item in cartItemsWithDetails" :key="item.productId" class="border-t border-black pt-4 md:pt-6 mb-4 md:mb-6">
        <div class="flex items-start justify-between">

          <div class="flex w-[90%] items-center">
          <div class="image md:w-1/5  w-full ">
            <img :src="item.productImg" :alt="item.productName" class="rounded-[10%] w-full max-w-[200px]">
          </div>
          
          <div class="disc-item flex-grow flex-col  ">
            <div class="flex justify-between items-start">
              <div>
                <h2 class="text-xl md:text-3xl font-medium">{{ item.productName }}</h2>
                <p class="text-base md:text-xl font-normal mt-1">{{ item.productPrice }} руб./шт</p>
              </div>
            </div>
            
            <!-- Управление количеством -->
            <div class="w-max rounded-2xl border border-black mt-3 md:mt-4 flex items-center">
              <button 
                @click="decreaseQuantity(item.productId)"
                class=" w-6 h-6 md:w-8 md:h-8 rounded-full flex items-center justify-center hover:bg-gray-300 transition"
              >
                -
              </button>
              <span class="mx-6  text-base md:text-lg">{{ item.itemQuantity }} шт</span>
              <button 
                @click="increaseQuantity(item.productId)"
                :disabled="item.itemQuantity >= item.productQuantity"
                class=" w-6 h-6 md:w-8 md:h-8 rounded-full flex items-center justify-center hover:bg-gray-300 transition disabled:opacity-50"
                :class="{'opacity-50 cursor-not-allowed': item.itemQuantity >= item.productQuantity}"
              >
                +
              </button>
            </div>
          </div>
            <!-- Итоговая цена за товар -->
          <div class="mt-3 md:mt-4 text-right">
            <p class="text-xl  md:text-4xl font-semibold">{{ (item.productPrice * item.itemQuantity) }} руб.</p>
          </div>
          </div>

          <button @click="removeItem(item.productId)" class="text-gray-500 hover:text-black">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 md:h-6 md:w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
            </svg>
          </button>
        </div>  
      </div>
      
      <!-- Общая сумма -->
      <div class="border-b border-t border-black pt-4 md:pt-6">
        <div class="flex items-center my-4 md:my-8">
          <h3 class="text-2xl md:text-4xl font-medium mr-2 md:mr-3">Итого: </h3>
          <p class="text-2xl md:text-4xl font-medium">{{ totalAmount }} руб.</p>
        </div>
      </div>
    </div>
    
    <!-- Пустая корзина -->
    <div v-else class="p-4 md:p-8 text-center">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 md:h-16 md:w-16 mx-auto text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
      </svg>
      <h2 class="text-xl md:text-2xl font-medium mt-3 md:mt-4">Ваша корзина пуста</h2>
      <p class="text-gray-600 mt-1 md:mt-2">Добавьте товары из каталога</p>
      <router-link 
        to="/catalog" 
        class="btn mt-3 md:mt-4 inline-block bg-[#F1DFCC] hover:bg-[#E8D4BE] text-black font-medium py-2 px-4 md:px-6 rounded-[15px] transition-colors duration-200"
      >
        Перейти в каталог
      </router-link>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      cartItems: [],
      products: [],
      sessionId: localStorage.getItem('sessionId') || null
    };
  },
  computed: {
    cartItemsWithDetails() {
      return this.cartItems.map(cartItem => {
        const product = this.products.find(p => p.productId === cartItem.productId) || {};
        return {
          ...cartItem,
          ...product
        };
      });
    },

    totalAmount() {
      return this.cartItemsWithDetails.reduce((total, item) => {
        return total + (item.productPrice * item.itemQuantity);
      }, 0);
    }
  },
  async created() {
    if (!this.sessionId) {
      this.$router.push('/catalog');
      return;
    }
    
    await this.loadCart();
  },
  methods: {
    async loadCart() {
      try {
        const cartResponse = await axios.get(`cart-api/cart/session/${this.sessionId}`);
        this.cartItems = cartResponse.data;
        
        this.$emit('cart-updated', this.cartItems);
        
        const productIds = this.cartItems.map(item => item.productId);
        if (productIds.length > 0) {
          const productsResponse = await axios.get('/api/products', {
            params: {
              ids: productIds.join(',')
            }
          });
          this.products = productsResponse.data;
        }
      } catch (error) {
        console.error('Ошибка загрузки корзины:', error);
      }
    },
    
    async increaseQuantity(productId) {
      try {
        await axios.post('/cart-api/cart', {
          itemSessionId: this.sessionId,
          productId: productId,
          itemQuantity: 1
        });
        await this.loadCart();
      } catch (error) {
        console.error('Ошибка увеличения количества:', error);
      }
    },
    
    async decreaseQuantity(productId) {
      try {
        const item = this.cartItems.find(item => item.productId === productId);
        
        if (item && item.itemQuantity === 1) {
          await this.removeItem(productId);
        } 
        else {
          await axios.patch(`/cart-api/cart/decrease/${this.sessionId}/${productId}`);
          await this.loadCart();
        }
      } catch (error) {
        console.error('Ошибка уменьшения количества:', error);
      }
    },
    
    async removeItem(productId) {
      try {
        await axios.delete(`/cart-api/cart/${this.sessionId}/${productId}`);
        await this.loadCart();
      } catch (error) {
        console.error('Ошибка удаления товара:', error);
      }
    }
  }
};
</script>

<style scoped>
.btn {
  border-width: 2px;
  border-color: black;
  opacity: 1;
}

img {
  box-shadow: 0px 5px 5px 0px rgba(0, 0, 0, 0.5);
}


@media (max-width: 425px) {
  .btn {
    padding: 0.5rem 1rem;
    font-size: 0.875rem;
  }
}

@media (max-width: 769px) {
  .image{
    max-width: 150px;
  }

  .disc-item{
    margin-left: 5%;
  }
}
</style>