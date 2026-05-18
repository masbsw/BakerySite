<template>
    <div class="catalog-container">
        <Breadcrumbs pageName="Каталог" />
        <div class="header-section">
            <h2 class="catalog-title">Каталог</h2>
            <input 
                v-model="searchQuery" 
                type="text" 
                placeholder="Поиск" 
                class="search-input"
            />
        </div>
        <div class="catalog-content">
            <div class="categories-section">
                <div 
                    v-for="category in [{categoryId: null, categoryName: 'Все'}, ...categories]" 
                    :key="category.categoryId" 
                    @click="selectCategory(category.categoryId)"
                    class="category-item"
                >
                    <h4 class="category-name">
                        {{ category.categoryName }}
                    </h4>
                </div>
            </div>
            <div class="products-section">
                <ProductCard :products="filteredProducts" />
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import ProductCard from './ProductCard.vue';
import Breadcrumbs from '@/cross-components/Breadcrumbs.vue';

export default {
    components: {
        Breadcrumbs,
        ProductCard
    },
    data() {
        return {
            categories: [],
            products: [],
            selectedCategoryId: null,
            searchQuery: '' 
        };
    },
    computed: {
        filteredProducts() {
            let filtered = this.products;

            if (this.selectedCategoryId !== null) {
                filtered = filtered.filter(
                    product => product.category?.categoryId === this.selectedCategoryId
                );
            }

            if (this.searchQuery) {
                const query = this.searchQuery.toLowerCase();
                filtered = filtered.filter(product =>
                    product.productName.toLowerCase().includes(query)
                );
            }

            return filtered;
        }
    },
    async created() {
  try {
    const categoriesResponse = await axios.get('/api/categories');
    this.categories = categoriesResponse.data;

    const productsResponse = await axios.get('/api/products');
    this.products = productsResponse.data;
  } catch (error) {
    console.error("Ошибка загрузки данных:", error);
  }
},
    methods: {
        selectCategory(categoryId) {
            this.selectedCategoryId = categoryId;
        }
    }
};
</script>

<style scoped>
.catalog-container {
    width: min(100%, var(--site-content-width));
    margin: 0 auto;
    padding: 0 var(--site-page-gutter);
    box-sizing: border-box;
}

.header-section {
    display: grid;
    grid-template-columns: minmax(0, 1fr) minmax(280px, 40%);
    align-items: center;
    gap: 24px;
    margin-bottom: 32px;
}

.catalog-title {
    font-size: clamp(2.25rem, 4vw, 3.5rem);
    font-weight: 600;
    line-height: 1.05;
}

.search-input {
    width: 100%;
    min-height: 44px;
    padding: 10px 14px;
    border-radius: 17px;
    border: 1px solid #cccccc;
    color: #1f1f1f;
    background-color: rgba(255, 255, 255, 0.55);
}

.search-input::placeholder {
    color: #c7c2bf;
}

.catalog-content {
    display: grid;
    grid-template-columns: minmax(170px, 220px) minmax(0, 1fr);
    gap: clamp(24px, 3vw, 48px);
    width: 100%;
}

.categories-section {
    display: flex;
    flex-direction: column;
    gap: 16px;
}

.category-item {
    cursor: pointer;
}

.category-name {
    font-size: clamp(1.125rem, 2vw, 1.5rem);
    font-weight: 400;
    transition: font-weight 0.2s ease;
}

.category-item:hover .category-name {
    font-weight: 600;
}

.products-section {
    min-width: 0;
}

@media (max-width: 1024px) {
    .catalog-container {
        padding-inline: clamp(16px, 3.5vw, 28px);
    }

    .header-section {
        grid-template-columns: 1fr;
        gap: 16px;
    }

    .catalog-content {
        grid-template-columns: 1fr;
        gap: 24px;
    }

    .categories-section {
        flex-direction: row;
        display: flex;
        flex-wrap: wrap;
        gap: 12px 20px;
    }
}

@media (max-width: 640px) {
    .header-section {
        margin-bottom: 24px;
    }

    .categories-section {
        gap: 10px 16px;
    }

    .category-name {
        font-size: 1rem;
    }
}
</style>
