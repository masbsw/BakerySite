<template>
    <div class="p-8">
      <div class="flex justify-between items-center mb-8">
        <h1 class="text-3xl font-bold">Управление товарами</h1>
        <button 
          @click="showAddModal = true"
          class="bg-brown-600 text-black px-4 py-2 rounded-lg hover:bg-brown-700 transition"
        >
          Добавить товар
        </button>
      </div>
  
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <table class="min-w-full">
          <thead class="bg-gray-100">
            <tr>
              <th class="px-6 py-3 text-left">ID</th>
              <th class="px-6 py-3 text-left">Название</th>
              <th class="px-6 py-3 text-left">Кол-во</th>
              <th class="px-6 py-3 text-left">Цена</th>
              <th class="px-6 py-3 text-left">Вес</th>
              <th class="px-6 py-3 text-left">Категория</th>
              <th class="px-6 py-3 text-left">Действия</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="product in products" :key="product.productId" class="border-t">
              <td class="px-6 py-4">{{ product.productId }}</td>
              <td class="px-6 py-4">{{ product.productName }}</td>
              <td class="px-6 py-4">{{ product.productQuantity }}</td>
              <td class="px-6 py-4">{{ product.productPrice }} руб.</td>
              <td class="px-6 py-4">{{ product.productWeight }} кг.</td>
              <td class="px-6 py-4">{{ product.category?.categoryName || 'Без категории' }}</td>
              <td class="px-6 py-4 space-x-2">
                <button 
                  @click="editProduct(product)"
                  class="text-blue-600 hover:text-blue-800"
                >
                  Редактировать
                </button>
                <button 
                  @click="deleteProduct(product.productId)"
                  class="text-red-600 hover:text-red-800"
                >
                  Удалить
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
  
      <div v-if="showAddModal || currentProduct" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
        <div class="bg-white p-6 rounded-lg w-full max-w-md">
          <h2 class="text-xl font-semibold mb-2">
            {{ currentProduct ? 'Редактировать товар' : 'Добавить товар' }}
          </h2>
          <form @submit.prevent="submitProduct">
            <div class="mb-2">
              <label class="block mb-2">Название</label>
              <input v-model="productForm.productName" class="w-full px-3 py-1 border rounded" required>
            </div>
            <div class="mb-2">
              <label class="block mb-2">Описание</label>
              <textarea v-model="productForm.productDescription" class="w-full px-3 py-1 border rounded" rows="3"></textarea>
            </div>
            <div class="mb-2">
              <label class="block mb-2">Состав</label>
              <textarea v-model="productForm.productCompound" class="w-full px-3 py-1 border rounded" rows="3"></textarea>
            </div>
            <div class="mb-2">
              <label class="block mb-2">Цена</label>
              <input v-model="productForm.productPrice" type="number" step="0.01" class="w-full px-3 py-1 border rounded" required>
            </div>
            <div class="mb-2">
              <label class="block mb-2">Кол-во</label>
              <input v-model="productForm.productQuantity" type="number" class="w-full px-3 py-1 border rounded" required>
            </div>
            <div class="mb-2">
              <label class="block mb-2">Вес</label>
              <input v-model="productForm.productWeight" type="number" step="0.01" class="w-full px-3 py-1 border rounded" required>
            </div>
            <div class="mb-2">
              <label class="block mb-2">Категория</label>
              <select v-model="productForm.categoryId" class="w-full px-3 py-1 border rounded" required>
                <option v-for="category in categories" :key="category.categoryId" :value="category.categoryId">
                  {{ category.categoryName }}
                </option>
              </select>
            </div>
            <div class="flex justify-end space-x-2">
              <button 
                type="button" 
                @click="closeModal"
                class="px-4 py-2 border rounded"
              >
                Отмена
              </button>
              <button 
                type="submit" 
                class="px-4 py-2 bg-brown-600 text-black rounded hover:bg-brown-700"
              >
                Сохранить
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        products: [],
        categories: [],
        showAddModal: false,
        currentProduct: null,
        productForm: {
          productName: '',
          productDescription: '',
          productCompound: '',
          productPrice: '',
          productQuantity: '',
          productWeight: '',
          productImg: '',
          categoryId: null
        }
      };
    },
    async created() {
      await this.fetchCategories();
      await this.fetchProducts();
    },
    methods: {
      async fetchProducts() {
        try {
          const auth = localStorage.getItem('adminAuth');
          const response = await axios.get('http://localhost:8086/admin/products', {
            headers: {
              'Authorization': `Basic ${auth}`
            }
          });
          this.products = response.data;
        } catch (error) {
          console.error('Error fetching products:', error);
          alert('Ошибка загрузки товаров: ' + (error.response?.data?.message || error.message));
        }
      },
      
      async fetchCategories() {
        try {
          const response = await axios.get('/api/categories');
          this.categories = response.data;
        } catch (error) {
          console.error('Error fetching categories:', error);
          alert('Ошибка загрузки категорий: ' + (error.response?.data?.message || error.message));
        }
      },
      
      editProduct(product) {
        this.currentProduct = product;
        this.productForm = {
          productName: product.productName,
          productDescription: product.productDescription,
          productCompound: product.productCompound,
          productPrice: product.productPrice,
          productQuantity: product.productQuantity,
          categoryId: product.category?.categoryId || null
        };
      },
      
      async deleteProduct(productId) {
        if (confirm('Вы уверены, что хотите удалить этот товар?')) {
          try {
            const auth = localStorage.getItem('adminAuth');
            await axios.delete(`http://localhost:8086/admin/products/${productId}`, {
              headers: {
                'Authorization': `Basic ${auth}`
              }
            });
            await this.fetchProducts();
          } catch (error) {
            console.error('Error deleting product:', error);
            alert(`Ошибка удаления товара: ${error.response?.data?.message || error.message}`);
          }
        }
      },
      
      async submitProduct() {
        try {
          const auth = localStorage.getItem('adminAuth');
          const config = {
            headers: {
              'Authorization': `Basic ${auth}`,
              'Content-Type': 'application/json'
            }
          };
  
          const selectedCategory = this.categories.find(c => c.categoryId == this.productForm.categoryId);
          
          const productData = {
            ...this.productForm,
            category: selectedCategory
          };
  
          if (this.currentProduct) {
              await axios.put(
                `http://localhost:8086/admin/products/${this.currentProduct.productId}`,
                productData,
                config
              );
          } else {
            await axios.post(
              'http://localhost:8086/admin/products',
              productData,
              config
            );
          }
  
          this.closeModal();
          await this.fetchProducts();
        } catch (error) {
          console.error('Error saving product:', error);
          alert('Ошибка сохранения товара: ' + (error.response?.data?.message || error.message));
        }
      },
      
      closeModal() {
        this.showAddModal = false;
        this.currentProduct = null;
        this.productForm = {
          productName: '',
          productDescription: '',
          productCompound: '',
          productPrice: '',
          productQuantity: '',
          categoryId: null
        };
      }
    }
  };
  </script>