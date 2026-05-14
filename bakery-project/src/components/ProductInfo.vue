<template>
  <div v-if="isVisible" class="modal-container">
    <div class="modal-backdrop" @click="closeModal"></div>

    <div class="modal-content">
      <button @click="closeModal" class="modal-close-button">
        &times;
      </button>

      <div class="modal-body">
          <img :src="product.productImg" :alt="product.productName" class="modal-image">
          <div class="modal-info">
              <h2 class="modal-title">{{ product.productName }}</h2>
              <p class="modal-description">Описание: {{ product.productDescription }}</p>
              <p class="modal-weight">Вес: {{ product.productWeight }} кг.</p>
              <p class="modal-compound">Состав: {{ product.productCompound }}</p>
              <p class="modal-price">Цена за 1 шт: {{ product.productPrice }} руб.</p>
          </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
props: {
  product: {
    type: Object,
    required: true,
  },
  isVisible: {
    type: Boolean,
    required: true,
  },
},
methods: {
  closeModal() {
    this.$emit('close');
  },
},
};
</script>

<style scoped>
.modal-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 50;
}

.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(5px);
}

.modal-content {
  background: rgba(255, 250, 245, 1);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 90%;
  max-width: 890px;
  height: auto;
  max-height: 90vh;
  position: relative;
  z-index: 10;
  padding: 20px;
  border-radius: 10px;
  overflow-y: auto;
}

.modal-close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 2rem;
  color: black;
  background: none;
  border: none;
  cursor: pointer;
}

.modal-body {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  width: 95%;
  height: 100%;
}

.modal-image {
  border-radius: 10%;
  box-shadow: 0px 5px 5px 0px rgba(0, 0, 0, 0.5);
  width: 40%;
  max-width: 300px;
  height: auto;
  margin-right: 5%;
}

.modal-info {
  width: 50%;
}

.modal-title {
  font-size: 2rem;
  font-weight: 500;
  margin-bottom: 1rem;
}

.modal-description {
  font-size: 1.25rem;
  font-weight: normal;
  margin-bottom: 1rem;
}

.modal-weight {
  font-size: 1.25rem;
  font-weight: 500;
  margin-bottom: 1rem;
}

.modal-compound {
  font-size: 0.875rem;
  font-weight: 300;
  margin-bottom: 0.5rem;
}

.modal-price {
  font-size: 1.5rem;
  font-weight: 500;
  margin-top: 0.5rem;
}

@media (max-width: 768px) {
  .modal-body {
      flex-direction: column;
  }
  
  .modal-image {
      width: 70%;
      margin-right: 0;
      margin-bottom: 20px;
  }
  
  .modal-info {
      width: 100%;
  }
  
  .modal-title {
      font-size: 1.5rem;
  }
  
  .modal-description,
  .modal-weight {
      font-size: 1rem;
  }
  
  .modal-price {
      font-size: 1.25rem;
  }
}

@media (max-width: 425px) {
  .modal-content {
      width: 95%;
      padding: 15px;
  }
  
  .modal-image {
      width: 90%;
  }
  
  .modal-title {
      font-size: 1.25rem;
  }
  
  .modal-description,
  .modal-weight {
      font-size: 0.875rem;
  }
  
  .modal-compound {
      font-size: 0.75rem;
  }
  
  .modal-price {
      font-size: 1rem;
  }
}
</style>