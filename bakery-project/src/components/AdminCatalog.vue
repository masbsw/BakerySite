<template>
    <div class="p-8">
      <div class="flex justify-between items-center mb-8">
        <h1 class="text-3xl font-bold">Управление товарами</h1>
        <button 
          @click="openAddModal"
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
  
      <div v-if="showAddModal || currentProduct" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4">
        <div class="bg-white rounded-lg w-full max-w-md modal-card">
          <h2 class="text-xl font-semibold mb-2">
            {{ currentProduct ? 'Редактировать товар' : 'Добавить товар' }}
          </h2>
          <form @submit.prevent="submitProduct" class="modal-form">
            <div class="form-group">
              <label class="form-label">Название</label>
              <input v-model="productForm.productName" class="field-input" required>
            </div>
            <div class="form-group">
              <label class="form-label">Описание</label>
              <textarea v-model="productForm.productDescription" class="field-input field-textarea" rows="2"></textarea>
            </div>
            <div class="form-group">
              <label class="form-label">Состав</label>
              <textarea v-model="productForm.productCompound" class="field-input field-textarea" rows="2"></textarea>
            </div>
            <div class="form-group">
              <label class="form-label">Цена</label>
              <input v-model="productForm.productPrice" type="number" step="0.01" class="field-input" required>
            </div>
            <div class="form-group">
              <label class="form-label">Кол-во</label>
              <input v-model="productForm.productQuantity" type="number" class="field-input" required>
            </div>
            <div class="form-group">
              <label class="form-label">Вес</label>
              <input v-model="productForm.productWeight" type="number" step="0.01" class="field-input" required>
            </div>
            <div class="form-group">
              <label class="form-label">Категория</label>
              <select v-model="productForm.categoryId" class="field-input" required>
                <option v-for="category in categories" :key="category.categoryId" :value="category.categoryId">
                  {{ category.categoryName }}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Фото товара</label>
              <input
                type="file"
                accept=".jpg,.jpeg,.png,.webp,image/jpeg,image/png,image/webp"
                class="field-input file-input"
                @change="handleImageChange"
              >
              <p v-if="productForm.productImg" class="text-xs text-gray-600 mt-2 break-all">
                Текущий путь: {{ productForm.productImg }}
              </p>
              <img
                v-if="imagePreviewUrl"
                :src="imagePreviewUrl"
                alt="Предпросмотр изображения товара"
                class="preview-image"
              >
              <p v-if="imageError" class="text-sm text-red-600 mt-2">{{ imageError }}</p>
            </div>
            <div class="modal-actions">
              <button 
                type="button" 
                @click="closeModal"
                class="action-button action-secondary"
              >
                Отмена
              </button>
              <button 
                type="submit" 
                class="action-button action-primary"
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
  import { clearAdminSession, getAdminAuthHeaders } from '@/utils/adminAuth';
  
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
        },
        selectedImageFile: null,
        imagePreviewUrl: '',
        imageError: ''
      };
    },
    async created() {
      await this.fetchCategories();
      await this.fetchProducts();
    },
    methods: {
      async loadImageFromFile(file) {
        return await new Promise((resolve, reject) => {
          const image = new Image();
          const objectUrl = URL.createObjectURL(file);

          image.onload = () => {
            URL.revokeObjectURL(objectUrl);
            resolve(image);
          };

          image.onerror = () => {
            URL.revokeObjectURL(objectUrl);
            reject(new Error('Не удалось прочитать изображение.'));
          };

          image.src = objectUrl;
        });
      },

      getImageContentBounds(context, width, height) {
        const { data } = context.getImageData(0, 0, width, height);
        const whiteThreshold = 245;
        let minX = width;
        let minY = height;
        let maxX = -1;
        let maxY = -1;

        for (let y = 0; y < height; y += 1) {
          for (let x = 0; x < width; x += 1) {
            const offset = (y * width + x) * 4;
            const red = data[offset];
            const green = data[offset + 1];
            const blue = data[offset + 2];
            const alpha = data[offset + 3];
            const isBackground = alpha < 10 || (red >= whiteThreshold && green >= whiteThreshold && blue >= whiteThreshold);

            if (!isBackground) {
              minX = Math.min(minX, x);
              minY = Math.min(minY, y);
              maxX = Math.max(maxX, x);
              maxY = Math.max(maxY, y);
            }
          }
        }

        if (maxX < 0 || maxY < 0) {
          return null;
        }

        return {
          minX,
          minY,
          maxX,
          maxY
        };
      },

      buildSquareCrop(bounds, width, height) {
        if (!bounds) {
          const fallbackSize = Math.min(width, height);
          return {
            x: Math.round((width - fallbackSize) / 2),
            y: Math.round((height - fallbackSize) / 2),
            size: fallbackSize
          };
        }

        const contentWidth = bounds.maxX - bounds.minX + 1;
        const contentHeight = bounds.maxY - bounds.minY + 1;
        const padding = Math.round(Math.max(contentWidth, contentHeight) * 0.08);
        let size = Math.max(contentWidth, contentHeight) + padding * 2;
        size = Math.min(size, width, height);

        const centerX = (bounds.minX + bounds.maxX) / 2;
        const centerY = (bounds.minY + bounds.maxY) / 2;

        let x = Math.round(centerX - size / 2);
        let y = Math.round(centerY - size / 2);

        x = Math.max(0, Math.min(x, width - size));
        y = Math.max(0, Math.min(y, height - size));

        return { x, y, size };
      },

      async cropImageForProductCard(file) {
        const image = await this.loadImageFromFile(file);
        const analysisCanvas = document.createElement('canvas');
        analysisCanvas.width = image.naturalWidth || image.width;
        analysisCanvas.height = image.naturalHeight || image.height;
        const analysisContext = analysisCanvas.getContext('2d', { willReadFrequently: true });

        if (!analysisContext) {
          throw new Error('Не удалось подготовить обработку изображения.');
        }

        analysisContext.drawImage(image, 0, 0);
        const bounds = this.getImageContentBounds(analysisContext, analysisCanvas.width, analysisCanvas.height);
        const crop = this.buildSquareCrop(bounds, analysisCanvas.width, analysisCanvas.height);

        const resultCanvas = document.createElement('canvas');
        resultCanvas.width = 300;
        resultCanvas.height = 300;
        const resultContext = resultCanvas.getContext('2d');

        if (!resultContext) {
          throw new Error('Не удалось подготовить итоговое изображение.');
        }

        resultContext.fillStyle = '#ffffff';
        resultContext.fillRect(0, 0, 300, 300);
        resultContext.drawImage(
          analysisCanvas,
          crop.x,
          crop.y,
          crop.size,
          crop.size,
          0,
          0,
          300,
          300
        );

        const processedBlob = await new Promise((resolve, reject) => {
          resultCanvas.toBlob((blob) => {
            if (blob) {
              resolve(blob);
            } else {
              reject(new Error('Не удалось сохранить обработанное изображение.'));
            }
          }, 'image/png');
        });

        const baseName = (file.name || 'product-image').replace(/\.[^.]+$/, '');
        const processedFile = new File([processedBlob], `${baseName}.png`, { type: 'image/png' });

        return {
          file: processedFile,
          previewUrl: resultCanvas.toDataURL('image/png')
        };
      },

      handleAuthError(error) {
        if (error.response?.status === 401 || error.response?.status === 403) {
          clearAdminSession();
          this.$router.push('/admin/login');
          return true;
        }
        return false;
      },

      async fetchProducts() {
        try {
          const response = await axios.get('/admin-api/admin/products', {
            headers: {
              ...getAdminAuthHeaders()
            }
          });
          this.products = response.data;
        } catch (error) {
          if (this.handleAuthError(error)) return;
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

      openAddModal() {
        this.closeModal();
        this.showAddModal = true;
      },
      
      editProduct(product) {
        this.currentProduct = product;
        this.productForm = {
          productName: product.productName,
          productDescription: product.productDescription,
          productCompound: product.productCompound,
          productPrice: product.productPrice,
          productQuantity: product.productQuantity,
          productWeight: product.productWeight,
          productImg: product.productImg || '',
          categoryId: product.category?.categoryId || null
        };
        this.selectedImageFile = null;
        this.imagePreviewUrl = product.productImg || '';
        this.imageError = '';
      },

      async handleImageChange(event) {
        const [file] = event.target.files || [];
        this.imageError = '';

        if (!file) {
          this.selectedImageFile = null;
          this.imagePreviewUrl = this.productForm.productImg || '';
          return;
        }

        try {
          const preparedImage = await this.cropImageForProductCard(file);
          this.selectedImageFile = preparedImage.file;
          this.imagePreviewUrl = preparedImage.previewUrl;
        } catch (error) {
          console.error('Error preparing image:', error);
          this.selectedImageFile = null;
          this.imagePreviewUrl = this.productForm.productImg || '';
          this.imageError = error.message || 'Не удалось обработать изображение.';
          event.target.value = '';
        }
      },

      async uploadProductImage() {
        if (!this.selectedImageFile) {
          return this.productForm.productImg || '';
        }

        const formData = new FormData();
        formData.append('file', this.selectedImageFile);

        const response = await axios.post('/admin-api/admin/products/upload-image', formData, {
          headers: {
            ...getAdminAuthHeaders()
          }
        });

        return response.data.imagePath;
      },
      
      async deleteProduct(productId) {
        if (confirm('Вы уверены, что хотите удалить этот товар?')) {
          try {
            await axios.delete(`/admin-api/admin/products/${productId}`, {
              headers: {
                ...getAdminAuthHeaders()
              }
            });
            await this.fetchProducts();
          } catch (error) {
            if (this.handleAuthError(error)) return;
            console.error('Error deleting product:', error);
            alert(`Ошибка удаления товара: ${error.response?.data?.message || error.message}`);
          }
        }
      },
      
      async submitProduct() {
        try {
          this.imageError = '';
          const uploadedImagePath = await this.uploadProductImage();

          const config = {
            headers: {
              ...getAdminAuthHeaders(),
              'Content-Type': 'application/json'
            }
          };
  
          const selectedCategory = this.categories.find(c => c.categoryId == this.productForm.categoryId);
          
          const productData = {
            ...this.productForm,
            productImg: uploadedImagePath,
            category: selectedCategory
          };
  
          if (this.currentProduct) {
              await axios.put(
                `/admin-api/admin/products/${this.currentProduct.productId}`,
                productData,
                config
              );
          } else {
            await axios.post(
              '/admin-api/admin/products',
              productData,
              config
            );
          }
  
          this.closeModal();
          await this.fetchProducts();
        } catch (error) {
          if (this.handleAuthError(error)) return;
          console.error('Error saving product:', error);
          this.imageError = error.response?.data?.message || '';
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
          productWeight: '',
          productImg: '',
          categoryId: null
        };
        this.selectedImageFile = null;
        this.imagePreviewUrl = '';
        this.imageError = '';
      }
    }
  };
  </script>

  <style scoped>
  .modal-card {
    max-height: 80vh;
    overflow-y: auto;
    padding: 18px 18px 14px;
  }

  .modal-form {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }

  .form-group {
    margin-bottom: 0;
  }

  .form-label {
    display: block;
    margin-bottom: 6px;
    font-size: 14px;
    font-weight: 500;
  }

  .field-input {
    width: 100%;
    padding: 8px 10px;
    border: 1px solid #d1d5db;
    border-radius: 8px;
    font-size: 14px;
    line-height: 1.2;
  }

  .field-textarea {
    min-height: 64px;
    resize: vertical;
  }

  .file-input {
    padding: 6px 10px;
  }

  .preview-image {
    margin-top: 10px;
    width: 72px;
    height: 72px;
    object-fit: cover;
    border-radius: 10px;
    border: 1px solid #d1d5db;
  }

  .modal-actions {
    position: sticky;
    bottom: 0;
    display: flex;
    justify-content: flex-end;
    gap: 8px;
    padding-top: 10px;
    padding-bottom: 2px;
    background: #fff;
  }

  .action-button {
    padding: 8px 14px;
    border-radius: 8px;
  }

  .action-secondary {
    border: 1px solid #d1d5db;
  }

  .action-primary {
    background-color: rgb(202 159 131 / 0.55);
    color: #000;
    border: 1px solid transparent;
  }
  </style>
