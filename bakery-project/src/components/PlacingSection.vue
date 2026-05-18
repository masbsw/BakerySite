<template>
  <div class="placing-wrapper">
    <div class="placing-content">
      <h3 class="text-2xl md:text-[48px] font-medium mb-6 md:mb-[42px]">Оформление заказа</h3>
      <p class="text-xl md:text-[32px] font-medium">Способ доставки</p>
      <form class="my-4">
        <label for="delivery">
          <select name="delivery" id="delivery" v-model="deliveryMethod" class="select-style">
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
      <div v-if="deliveryMethod === 'courier'" class="form-column">
        <label for="deliveryAddress" class="field-label mt-4 md:mt-8">Адрес доставки</label>
        <input id="deliveryAddress" type="text" placeholder="Введите адрес доставки" v-model.trim="deliveryAddress" class="input-style mt-2" />
        <p class="text-sm md:text-[16px] font-light italic mb-4 md:mb-[43px]">*Бесплатная доставка осуществляется из нашей пекарни по Ростову-на-Дону и Ростовской области.</p>
      </div>
      <div>
        <form @submit.prevent="submitForm" name="my_form" class="form-column">
          <label for="deliveryDate" class="field-label mt-4 md:mt-[42px]">Дата доставки*</label>
          <input
            id="deliveryDate"
            type="date"
            v-model="deliveryDate"
            :min="minDeliveryDate"
            :class="['input-style', { 'input-style-error': fieldErrors.deliveryDate }]"
          />
          <p v-if="fieldErrors.deliveryDate" class="error-text mb-4 md:mb-8">{{ fieldErrors.deliveryDate }}</p>

          <label for="customerName" class="field-label">Имя получателя*</label>
          <input
            id="customerName"
            type="text"
            v-model.trim="form.name"
            placeholder="Введите имя"
            :class="['input-style', { 'input-style-error': fieldErrors.name }]"
          />
          <p v-if="fieldErrors.name" class="error-text mb-4 md:mb-8">{{ fieldErrors.name }}</p>

          <label for="customerPhone" class="field-label">Телефон для связи*</label>
          <input
            id="customerPhone"
            type="tel"
            v-model.trim="form.phone"
            placeholder="+79991234567"
            :class="['input-style', { 'input-style-error': fieldErrors.phone }]"
          />
          <p v-if="fieldErrors.phone" class="error-text mb-4 md:mb-8">{{ fieldErrors.phone }}</p>
          <p v-else class="hint-text mb-4 md:mb-8">Например: +79991234567</p>

          <div>
            <label for="orderComment" class="field-label">Комментарий</label>
            <textarea id="orderComment" v-model.trim="form.message" placeholder="Комментарий к заказу" class="input-style textarea-style mb-2 md:mb-3"></textarea>
          </div>
          <p class="text-sm md:text-[20px] italic mb-4 md:mb-[24px]">*поля, обязательные для заполнения</p>
        </form>

        <p v-if="showValidationSummary" class="error-text error-summary mb-4">
          Заполните обязательные поля корректно
        </p>
        <p v-if="fieldErrors.items" class="error-text mb-4">{{ fieldErrors.items }}</p>
        <p v-if="serverError" class="error-text mb-4">{{ serverError }}</p>

        <ButtonBrown
          @click="submitForm"
          btnName="Оформить"
          :buttonWidth="'min(100%, 320px)'"
          class="btn-class"
          :class="{ 'opacity-60': isSubmitting }"
        />
      </div>
    </div>
  </div>
</template>

<style>
.placing-wrapper {
  width: 100%;
  padding: 16px 0 24px;
}

.placing-content {
  width: min(100%, 720px);
}

.form-column {
  width: min(100%, 540px);
  display: flex;
  flex-direction: column;
}

.input-style {
  width: 100%;
  min-height: 56px;
  padding: 10px 16px 10px 16px;
  border: none;
  border-bottom: 1px solid rgba(0, 0, 0, 0.25);
  border-radius: 0;
  background-color: transparent;
  transition: border-color 0.3s;
  font-size: 16px;
  margin-top: 8px;
  line-height: 1.4;
  color: #1f1f1f;
}

.input-style:focus {
  border-bottom-color: rgba(0, 0, 0, 0.7);
  outline: none;
  box-shadow: none;
}

.input-style::placeholder {
  color: #9aa3b2;
  opacity: 1;
}

.input-style-error {
  border-bottom-width: 2px;
  border-bottom-color: rgba(0, 0, 0, 0.7);
}

.textarea-style {
  min-height: 240px;
  padding-top: 10px;
  resize: vertical;
}

.select-style {
  background-color: transparent;
  padding: 12px 16px;
  border: 1px solid #000;
  border-radius: 15px;
  width: min(100%, 260px);
  font-size: 14px;
}

.field-label {
  display: block;
  margin-top: 16px;
  font-size: 16px;
  font-weight: 500;
  color: #1f1f1f;
}

.error-text {
  color: #111111;
  font-size: 14px;
  margin-top: 6px;
  font-weight: 600;
}

.error-summary {
  padding: 0;
  font-weight: 600;
}

.hint-text {
  color: rgba(0, 0, 0, 0.58);
  font-size: 14px;
  margin-top: 6px;
}

option {
  background-color: transparent;
}

@media (min-width: 768px) {
  .field-label {
    font-size: 18px;
  }
  
  .select-style {
    font-size: 16px;
  }

  .error-text {
    font-size: 15px;
  }

  .hint-text {
    font-size: 15px;
  }
}

@media (max-width: 767px) {
  .placing-wrapper {
    padding: 8px 0 20px;
  }

  .placing-content,
  .form-column {
    width: 100%;
  }
}
</style>

<script>
import ButtonBrown from '@/cross-components/ButtonBrown.vue';
import axios from 'axios';
import { clearUserSession, getUserAuthHeaders, getUserToken } from '@/utils/userAuth';

const PHONE_REGEX = /^(\+7\d{10}|8\d{10})$/;

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
      hasAttemptedSubmit: false,
      backendFieldErrors: {},
      serverError: '',
      isSubmitting: false
    };
  },

  computed: {
    minDeliveryDate() {
      return new Date().toISOString().split('T')[0];
    },

    nameError() {
      if (!this.form.name) {
        return 'Имя обязательно';
      }
      if (this.form.name.length < 2) {
        return 'Имя должно содержать минимум 2 символа';
      }
      return '';
    },

    phoneError() {
      if (!this.form.phone) {
        return 'Телефон обязателен';
      }
      if (!PHONE_REGEX.test(this.form.phone)) {
        return 'Телефон должен быть в формате +7XXXXXXXXXX или 8XXXXXXXXXX';
      }
      return '';
    },

    deliveryDateError() {
      if (!this.deliveryDate) {
        return 'Дата доставки обязательна';
      }
      if (this.deliveryDate < this.minDeliveryDate) {
        return 'Нельзя выбрать прошедшую дату';
      }
      return '';
    },

    itemsError() {
      if (!this.cartItems?.length) {
        return 'Корзина пуста';
      }
      return '';
    },

    fieldErrors() {
      if (!this.hasAttemptedSubmit) {
        return {};
      }

      return {
        name: this.backendFieldErrors.customerName || this.nameError,
        phone: this.backendFieldErrors.customerPhone || this.phoneError,
        deliveryDate: this.backendFieldErrors.deliveryDate || this.deliveryDateError,
        items: this.backendFieldErrors.items || this.itemsError
      };
    },

    showValidationSummary() {
      return this.hasAttemptedSubmit && Object.values(this.fieldErrors).some(Boolean);
    },

    isClientFormValid() {
      return !this.nameError && !this.phoneError && !this.deliveryDateError && !this.itemsError;
    }
  },

  methods: {
    async submitForm() {
      this.hasAttemptedSubmit = true;
      this.backendFieldErrors = {};
      this.serverError = '';

      if (!getUserToken()) {
        this.$router.push({ path: '/login', query: { redirect: '/order' } });
        return;
      }

      if (!this.isClientFormValid) {
        return;
      }

      if (!this.sessionId) {
        this.serverError = 'Ошибка сессии. Пожалуйста, обновите страницу.';
        return;
      }

      this.isSubmitting = true;

      try {
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

        await axios.post('/order-api/orders', orderData, {
          headers: {
            ...getUserAuthHeaders()
          }
        });
        
        await axios.delete(`/cart-api/cart/clear/${this.sessionId}`);
        this.$emit('order-created');
        this.form.name = '';
        this.form.phone = '';
        this.form.message = '';
        this.deliveryDate = '';
        this.deliveryMethod = 'pickup';
        this.deliveryAddress = '';
        this.hasAttemptedSubmit = false;
        this.backendFieldErrors = {};
        this.serverError = '';
      } catch (error) {
        console.error('Full error:', error);
        if (error.response) {
          if (error.response.status === 401 || error.response.status === 403) {
            clearUserSession();
            this.$router.push({ path: '/login', query: { redirect: '/order' } });
            return;
          }

          if (error.response.status === 400) {
            this.backendFieldErrors = error.response.data?.fieldErrors || {};
            this.serverError = error.response.data?.message || 'Ошибка валидации заказа';
            return;
          }

          this.serverError = error.response.data?.message || error.message;
        } else {
          this.serverError = 'Ошибка сети. Пожалуйста, попробуйте позже.';
        }
      } finally {
        this.isSubmitting = false;
      }
    }
  }
};
</script>
