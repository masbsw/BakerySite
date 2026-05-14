<template>
    <div  class="min-h-screen bg-gray-100">
      <div class="flex">
        <aside class="w-64 bg-white shadow-md min-h-screen p-4">
          <nav>
            <ul class="space-y-2">
              <li>
                <router-link 
                  to="/admin/dashboard" 
                  class="block px-4 py-2 rounded hover:bg-gray-100"
                  active-class="bg-gray-200"
                >
                  Главная
                </router-link>
              </li>
              <li>
                <router-link 
                  to="/admin/catalog" 
                  class="block px-4 py-2 rounded hover:bg-gray-100"
                  active-class="bg-gray-200"
                >
                  Товары
                </router-link>
              </li>
              <li>
                <router-link 
                  to="/admin/orders" 
                  class="block px-4 py-2 rounded hover:bg-gray-100"
                  active-class="bg-gray-200"
                >
                  Заказы
                </router-link>
              </li>
              <li>
                <button 
                  @click="logout"
                  class="w-full text-left px-4 py-2 rounded hover:bg-gray-100 text-red-600"
                >
                  Выйти
                </button>
              </li>
            </ul>
          </nav>
        </aside>
        <main class="flex-1 p-8">
          <router-view />
        </main>
      </div>
    </div>
  </template>
  
<script>
  export default {
    methods: {
      logout() {
        localStorage.removeItem('adminAuth');
        this.$router.push('/admin');
      }
    }
  };
</script>