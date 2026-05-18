import './assets/main.css'
import { createApp } from 'vue'
import App from './App.vue'
import { createRouter,  createWebHistory } from 'vue-router'
import MainPage from './pages/MainPage.vue'
import ContactsPage from './pages/ContactsPage.vue'
import OrderPage from './pages/OrderPage.vue'
import CatalogPage from './pages/CatalogPage.vue'
import AdminPage from './pages/AdminPage.vue'
import LoginPage from './pages/LoginPage.vue'
import RegisterPage from './pages/RegisterPage.vue'
import { clearAdminSession, getAdminRole, getAdminToken } from './utils/adminAuth'

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
        name: 'Login',
        path: '/login',
        component: LoginPage
    },
    {
        name: 'Register',
        path: '/register',
        component: RegisterPage
    },
    {
        path: '/admin/login',
        name: 'AdminLogin',
        component: () => import('./components/AdminLogin.vue'),
        meta: { requiresAuth: false }
    },
    {
        name: 'Admin',
        path: '/admin',
        component: AdminPage,
        meta: {requiresAuth: true},
        redirect: '/admin/dashboard',
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
      const token = getAdminToken();
      const role = getAdminRole();
      if (!token) {
        next({
          name: 'AdminLogin',
          query: { redirect: to.fullPath }
        });
      } else if (role !== 'ADMIN') {
        clearAdminSession();
        next({ name: 'AdminLogin' });
      } else {
        next();
      }
    } else {
      if (to.name === 'AdminLogin' && getAdminToken() && getAdminRole() === 'ADMIN') {
        next({ name: 'AdminDashboard' });
        return;
      }
      next();
    }
  });


createApp(App).use(router).mount('#app')
