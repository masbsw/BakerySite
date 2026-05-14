<template>
  <div class="btn">
    <button
      v-if="!inCart"
      :class="[
        'w-[100%] py-1 rounded-[15px] transition-colors duration-200',
        isAvailable ? 'bg-[rgba(241,223,204,0.65)] text-black hover:bg-[rgb(232,212,190)]' : 
        'bg-[rgba(233,212,190,1)] text-[rgba(0,0,0,0.6)] cursor-not-allowed'
      ]"
      :disabled="!isAvailable"
      @click="$emit('add')"
    >
      {{ buttonText }}
    </button>
    
    <div v-else class="flex items-center justify-center gap-2">
      <button 
          @click="$emit('decrease')"
          :disabled="cartQuantity < 1"
          class="bg-[#FADFC5] w-8 h-8 rounded-full flex items-center justify-center shadow-md hover:bg-[#F0D0B0] transition"
      >
          -
      </button>
      <span class="mx-2">{{ cartQuantity }}</span>
      <button 
        @click="$emit('increase')"
        :disabled="cartQuantity >= quantity"
        class="bg-[#FADFC5] w-8 h-8 rounded-full flex items-center justify-center shadow-md hover:bg-[#F0D0B0] transition"
        :class="{'opacity-50 cursor-not-allowed': cartQuantity >= quantity}">
        +
      </button>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    quantity: {
      type: Number,
      required: true,
    },
    inCart: {
      type: Boolean,
      default: false,
    },
    cartQuantity: {
      type: Number,
      default: 0,
    },
  },
  computed: {
    isAvailable() {
      return this.quantity > 0;
    },
    buttonText() {
      return this.isAvailable ? 'Добавить' : 'Нет в наличии';
    },
  },
};
</script>

<style scoped>
button {
  border-width: 2px;
  border-color: black;
  opacity: 1;
}

@media(max-width: 425px){
  .btn{}
}
</style>