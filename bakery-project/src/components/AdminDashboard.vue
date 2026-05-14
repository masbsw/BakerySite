<template>
    <div class="p-8">
      <h1 class="text-3xl font-bold mb-8">Админ-панель пекарни</h1>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <router-link 
          to="/admin/catalog" 
          class="bg-white p-6 rounded-lg shadow-md hover:shadow-lg transition cursor-pointer"
        >
          <h2 class="text-xl font-semibold mb-4">Управление товарами</h2>
          <p>Добавление, редактирование и удаление товаров в каталоге</p>
        </router-link>
        <router-link 
          to="/admin/orders" 
          class="bg-white p-6 rounded-lg shadow-md hover:shadow-lg transition cursor-pointer"
        >
          <h2 class="text-xl font-semibold mb-4">Управление заказами</h2>
          <p>Просмотр и изменение статусов заказов</p>
        </router-link>
      </div>
    </div>
  </template>