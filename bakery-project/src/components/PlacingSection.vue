<template>
  <div class="p-4 md:p-6 mb-6 w-full md:w-[94%] mx-auto">
    <div class="w-full md:w-[51%] flex-col">
      <h3 class="text-2xl md:text-[48px] font-medium mb-6 md:mb-[42px]">Оформление заказа</h3>
      <p class="text-xl md:text-[32px] font-medium">Способ доставки</p>
      <form class="my-4">
        <label for="delivery">
          <select name="delivery" id="delivery" v-model="deliveryMethod" class="select-style w-full md:w-auto">
            <option value="pickup">Самовывоз</option>
            <option value="courier">Курьер</option>
          </select>
        </label>
      </form>
      <div v-if="deliveryMethod === 'pickup'">
        <p class="text-lg md:text-3xl font-medium mb-2 md:mb-4">Пункт самовывоза</p>
        <p class="text-base md:text-2xl font-regular">Ростов-на-Дону, 344092, проспект Космонавтов, 1/26</p>
        <p class="text-base md:text-2xl font-regular">пн-пт 8:00 - 21:00 <br> сб-вс 9:00 - 21:00 </p>
      </div>
      <div v-if="deliveryMethod === 'courier'" class="w-full md:w-[61%]">
        <input type="text" placeholder="Адрес доставки" v-model="deliveryAddress" class="input-style mt-4 md:mt-8" />
        <p class="text-sm md:text-[16px] font-light italic mb-4 md:mb-[43px]">*Бесплатная доставка осуществляется из нашей пекарни по Ростову-на-Дону и Ростовской области.</p>
      </div>
      <div>
        <p class="text-base md:text-[24px] font-light mt-4 md:mt-[42px] mb-2">Дата доставки*</p>
        <form @submit.prevent="submitForm" name="my_form" class="flex-col w-full md:w-[61%]">
          <input type="date" v-model="deliveryDate" placeholder="Дата доставки*" class="input-style mb-4 md:mb-10" />
          <input type="text" v-model="form.name" placeholder="Имя*" class="input-style mb-4 md:mb-10" />
          <input type="phone" v-model="form.phone" placeholder="Телефон*" class="input-style mb-4 md:mb-10" />
          <div>
            <textarea v-model="form.message" placeholder="Комментарий" class="input-style h-[150px] md:h-[254px] mb-2 md:mb-3"></textarea>
          </div>
          <p class="text-sm md:text-[20px] italic mb-4 md:mb-[43px]">*поля, обязательные для заполнения</p>
        </form>
        <ButtonBrown @click="submitForm" btnName="Оформить" :buttonWidth="windowWidth <= 768 ? '70%' : '50%'" class="btn-class"/>
      </div>
    </div>
  </div>
</template>

<style>
.input-style {
  width: 100%;
  padding: 8px 12px;
  border: none;
  border-bottom: 2px solid #ccc;
  border-radius: 0;
  background-color: transparent;
  transition: border-color 0.3s;
  font-size: 14px;
}

.input-style:focus {
  border-bottom-color: black;
  outline: none;
}

.select-style {
  background-color: transparent;
  padding: 8px 12px;
  border: 1px solid;
  border-radius: 15px;
  width: 100%;
  font-size: 14px;
}

option {
  background-color: transparent;
}

@media (min-width: 768px) {
  .input-style {
    font-size: 16px;
  }
  
  .select-style {
    width: 40%;
    font-size: 16px;
  }
}
</style>

<script>
import ButtonBrown from '@/cross-components/ButtonBrown.vue';
import axios from 'axios';

export default {
  components: {
    ButtonBrown
  },
  props: {
    cartItems: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      form: {
        name: '',
        phone: '',
        message: ''
      },
      deliveryDate: '',
      deliveryMethod: 'pickup',
      deliveryAddress: '',
      sessionId: localStorage.getItem('sessionId') || null,
      windowWidth: window.innerWidth
    };
  },

  computed:{
    cartItemsWithDetails() {
      return this.orderItems.map(orderItem => {
        const product = this.products.find(p => p.productId === cartItem.productId) || {};
        return {
          ...orderItem,
          ...product
        };
      });
    },
  },

  mounted() {
    window.addEventListener('resize', this.handleResize);
  },

  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize);
  },

  methods: {
    handleResize() {
      this.windowWidth = window.innerWidth;
    },

    async submitForm() {
      if (!this.form.name || !this.form.phone || !this.deliveryDate) {
        alert('Пожалуйста, заполните все обязательные поля');
        return;
      }

      if (!this.cartItems?.length) {
        alert('Корзина пуста');
        return;
      }

      if (!this.sessionId) {
        alert('Ошибка сессии. Пожалуйста, обновите страницу.');
        return;
      }

      try {
        const productIds = this.cartItems.map(item => item.productId);
        let productDetails =[];

        if(productIds.length >0){
          const productsResponse = await axios.get('/api/products',
            {
              params:{
                ids: productIds.join(',')
              }
            }
          );
          productDetails = productsResponse.data;
        }

        const cartItemsWithDetails = this.cartItems.map(cartItem =>{
          const product = productDetails.find(p => p.productId === cartItem.productId) || {};
          return {
            ...cartItem,
            ...product
          }
        })

        const orderData = {
          customerName: this.form.name,
          customerPhone: this.form.phone,
          deliveryMethod: this.deliveryMethod,
          deliveryAddress: this.deliveryMethod === 'pickup' ? 'Самовывоз' : this.deliveryAddress,
          deliveryDate: this.deliveryDate,
          orderComment: this.form.message,
          items: this.cartItems.map(item => ({
            productId: item.productId,
            productQuantity: item.itemQuantity,
          })),
          sessionId: this.sessionId
        };

        const response = await axios.post('/order-api/orders', orderData);
        
        await axios.delete(`/cart-api/cart/clear/${this.sessionId}`);
        this.$emit('order-created'); 
        
        alert('Заказ успешно оформлен!');
        
      } catch (error) {
        console.error('Full error:', error);
        if (error.response) {
          alert(`Ошибка: ${error.response.data?.message || error.message}`);
        } else {
          alert('Ошибка сети. Пожалуйста, попробуйте позже.');
        }
      }
    }
  }
};
</script>