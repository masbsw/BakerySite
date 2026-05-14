import './assets/main.css'
import { createApp } from 'vue'
import App from './App.vue'
import { createRouter,  createWebHistory } from 'vue-router'
import MainPage from './pages/MainPage.vue'
import ContactsPage from './pages/ContactsPage.vue'
import OrderPage from './pages/OrderPage.vue'
import CatalogPage from './pages/CatalogPage.vue'
import AdminPage from './pages/AdminPage.vue'

const router = createRouter({
    history: createWebHistory(),
    routes: [{
        name: 'Main',
        path: '/',
        component: MainPage
    },
    {
        name: 'Contacts',
        path: '/contacts',
        component: ContactsPage
    },
    {
        name: 'Catalog',
        path: '/catalog',
        component: CatalogPage
    },
    {
        name: 'Order',
        path: '/order',
        component: OrderPage
    },
    {
        path: '/admin',
        component: () => import('./components/AdminLogin.vue'),
        meta: { requiresAuth: false }
    },
    {
        name: 'Admin',
        path: '/admin',
        component: AdminPage,
        meta: {requiresAuth: true},
        children:[
            {
                name:'AdminDashboard',
                path: 'dashboard',
                component: () => import('./components/AdminDashboard.vue'),

            },
            {
                name:'AdminCatalog',
                path: 'catalog',
                component: () => import('./components/AdminCatalog.vue')
            },
            {
                name:'AdminOrders',
                path: 'orders',
                component: () => import('./components/AdminOrders.vue')
            }
        ]
    }
    ]
})

router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.requiresAuth)) {
      const auth = localStorage.getItem('adminAuth');
      if (!auth) {
        next({
          path: '/admin',
          query: { redirect: to.fullPath }
        });
      } else {
        next();
      }
    } else {
      next();
    }
  });


createApp(App).use(router).mount('#app')

