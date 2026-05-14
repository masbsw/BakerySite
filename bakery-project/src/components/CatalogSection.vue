<template>
    <div class="mt-[61px] px-4 md:px-6 lg:px-8 catalog-container max-w-screen-xl mx-auto">
        <Breadcrumbs pageName="Каталог" />
        <div class="w-full flex flex-col md:flex-row justify-between items-start md:items-center header-section mb-8">
            <h2 class="text-3xl md:text-5xl font-semibold mb-4 md:mb-0">Каталог</h2>
            <input 
                v-model="searchQuery" 
                type="text" 
                placeholder="Поиск" 
                class="p-2 w-full md:w-[67%] search-input"
            />
        </div>
        <div class="flex flex-col lg:flex-row mt-8 catalog-content">
            <div class="categories-section w-full lg:w-1/5 mb-6 lg:mb-0">
                <div 
                    v-for="category in [{categoryId: null, categoryName: 'Все'}, ...categories]" 
                    :key="category.categoryId" 
                    @click="selectCategory(category.categoryId)"
                    class="category-item mb-4"
                >
                    <h4 class="text-xl lg:text-2xl font-normal hover:font-semibold cursor-pointer">
                        {{ category.categoryName }}
                    </h4>
                </div>
            </div>
            <div class="products-section w-full lg:w-4/5 lg:pl-8">
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
                filtered = filtered.filter(product => product.category.categoryId === this.selectedCategoryId);
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
    width: 100%;
    max-width: 1440px;
    margin: 61px auto 0;
    padding: 0 20px;
    box-sizing: border-box;
}

.products-section {
    width: 100%;
    padding-left: 20px;
}

.search-input {
    border-radius: 17px;
    border: 1px solid #cccccc;
    color: rgba(250, 233, 216, 0.71);
}

.categories-section {
    min-width: auto;
}

.header-section {
    width: 100%;
    max-width: 1200px;
}


.catalog-content {
    display: flex;
    flex-direction: row;
    width: 100%;

}



.category-item {
    margin-bottom: 20px;
}



@media (max-width: 768px) {
    .catalog-container {
        margin-top: 30px;
    }
    
    .header-section {
        flex-direction: column;
        align-items: flex-start;
    }
    
    .search-input {
        width: 100%;
        margin-top: 15px;
    }
    
    .catalog-content {
        flex-direction: column;
    }
    
    .categories-section {
        width: 100%;
        display: flex;
        flex-wrap: wrap;
        gap: 10px;
        margin-bottom: 20px;
    }
    
    .category-item {
        margin-top: 0 !important;
        margin-right: 15px;
    }
    
    .products-section {
        width: 100%;
        margin-left: 0;
    }
}

@media (max-width: 425px) {
    .catalog-container {
        margin-left: 10px;
        margin-top: 20px;
    }
    
    h2 {
        font-size: 2rem;
    }
    
    .category-item h4 {
        font-size: 1.2rem;
    }
}
</style>