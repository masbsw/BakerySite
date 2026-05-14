  <template>
      <div class="p-8">
        <h1 class="text-3xl font-bold mb-8">Управление заказами</h1>
        
        <div class="bg-white rounded-lg shadow-md overflow-hidden">
          <table class="min-w-full">
            <thead class="bg-gray-100">
              <tr>
                <th class="px-6 py-3 text-left">ID</th>
                <th class="px-6 py-3 text-left">Имя клиента</th>
                <th class="px-6 py-3 text-left">Телефон</th>
                <th class="px-6 py-3 text-left">Дата заказа</th>
                <th class="px-6 py-3 text-left">Статус</th>
                <th class="px-6 py-3 text-left">Действия</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="order in orders" :key="order.id" class="border-t">
                <td class="px-6 py-4">{{ order.orderId }}</td>
                <td class="px-6 py-4">{{ order.customerName }}</td>
                <td class="px-6 py-4">{{ order.customerPhone }}</td>
                <td class="px-6 py-4">{{ new Date(order.orderCreatedAt).toLocaleString() }}</td>
                <td class="px-6 py-4">
                  <span :class="statusClasses(order.orderStatus)">
                    {{ order.orderStatus }}
                  </span>
                </td>
                <td class="px-6 py-4 space-x-2">
                  <button 
                    @click="viewOrderDetails(order)"
                    class="text-blue-600 hover:text-blue-800"
                  >
                    Подробнее
                  </button>
                  <select 
                    v-model="order.orderStatus" 
                    @change="updateOrderStatus(order)"
                    class="border rounded px-2 py-1"
                  >
                    <option value="PENDING">В обработке</option>
                    <option value="COMPLETED">Завершен</option>
                    <option value="CANCELLED">Отменен</option>
                  </select>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
    
        <div v-if="selectedOrder" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
          <div class="bg-white p-6 rounded-lg w-full max-w-2xl max-h-[90vh] overflow-y-auto">
            <div class="flex justify-between items-start mb-4">
              <h2 class="text-xl font-semibold">Детали заказа #{{ selectedOrder.orderId }}</h2>
              <button @click="selectedOrder = null" class="text-gray-500 hover:text-gray-700">
                &times;
              </button>
            </div>
            
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
              <div>
                <h3 class="font-medium mb-2">Информация о клиенте</h3>
                <p><strong>Имя:</strong> {{ selectedOrder.customerName }}</p>
                <p><strong>Телефон:</strong> {{ selectedOrder.customerPhone }}</p>
                <p><strong>Комментарий:</strong> {{ selectedOrder.orderComment || 'Нет комментария' }}</p>
              </div>
              <div>
                <h3 class="font-medium mb-2">Доставка</h3>
                <p><strong>Способ:</strong> {{ deliveryMethodText(selectedOrder.deliveryMethod) }}</p>
                <p><strong>Адрес:</strong> {{ selectedOrder.deliveryAddress }}</p>
                <p><strong>Дата доставки:</strong> {{ formatDate(selectedOrder.deliveryDate) }}</p>
              </div>
            </div>
            
            <div>
              <h3 class="font-medium mb-2">Состав заказа</h3>
              <table class="min-w-full">
                <thead>
                  <tr class="border-b">
                    <th class="text-left py-2">Товар</th>
                    <th class="text-left py-2">Количество</th>
                    <th class="text-left py-2">Цена</th>
                    <th class="text-left py-2">Сумма</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="item in selectedOrder.orderItems" :key="item.productId" class="border-b">
                    <td class="py-2">{{ item.productName }}</td>
                    <td class="py-2">{{ item.productQuantity }}</td>
                    <td class="py-2">{{ item.productPrice }} руб.</td>
                    <td class="py-2">{{ item.productPrice * item.productQuantity }} руб.</td>
                  </tr>
                  <tr>
                    <td colspan="3" class="text-right font-medium py-2">Итого:</td>
                    <td class="font-medium py-2">
                      {{ calculateTotal(selectedOrder.orderItems) }} руб.
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </template>
    
    <script>
    import axios from 'axios';
    
    export default {
      data() {
        return {
          orders: [],
          selectedOrder: null
        };
      },
      async created() {
        await this.fetchOrders();
      },
      methods: {
        async fetchOrders() {
          try {
            const auth = localStorage.getItem('adminAuth');
            const response = await axios.get('http://localhost:8086/admin/orders', {
              headers: {
                'Authorization': `Basic ${auth}`
              }
            });
            this.orders = response.data;
          } catch (error) {
            console.error('Error fetching orders:', error);
            alert('Ошибка загрузки заказов: ' + (error.response?.data?.message || error.message));
          }
        },
        
        async updateOrderStatus(order) {
          try {
            const auth = localStorage.getItem('adminAuth');
            await axios.put(
              `http://localhost:8086/admin/orders/${order.orderId}/status`, 
              null,
              {
                headers: {
                  'Authorization': `Basic ${auth}`
                },
                params: {
                  status: order.orderStatus
                }
              }
            );
            await this.fetchOrders();
          } catch (error) {
            console.error('Error updating order status:', error);
            alert('Ошибка обновления статуса: ' + (error.response?.data?.message || error.message));
          }
        },
    
        async viewOrderDetails(order) {
    try {
      const auth = localStorage.getItem('adminAuth');
      const orderResponse = await axios.get(`http://localhost:8086/admin/orders/${order.orderId}`, {
        headers: {
          'Authorization': `Basic ${auth}`
        }
      });
      
      const orderData = orderResponse.data;
      const items = orderData.items || orderData.orderItems || [];
      
      
      const productIds = items.map(item => item.productId);
      let products = [];
      
      try {
        const productsResponse = await axios.get('http://localhost:8086/admin/products', {
          params: {
            ids: productIds.join(',')
          },
          headers: {
            'Authorization': `Basic ${auth}` 
          }
        });
        products = productsResponse.data;
      } catch (productsError) {
        console.warn('Could not fetch products details, showing basic order info', productsError);
        products = [];
      }
      
      this.selectedOrder = {
        ...orderData,
        orderItems: items.map(item => {
          const product = products.find(p => p.productId === item.productId) || {};
          return {
            ...item,
            productName: product.productName || `Товар #${item.productId}`,
            productPrice: product.productPrice || 0
          };
        })
      };
    } catch (error) {
      console.error('Error fetching order details:', error);
      alert('Ошибка загрузки деталей заказа: ' + (error.response?.data?.message || error.message));
    }
  },
    
        deliveryMethodText(method) {
          return method === 'pickup' ? 'Самовывоз' : 'Доставка';
        },
        formatDate(dateString) {
          return new Date(dateString).toLocaleDateString();
        },
        calculateTotal(items) {
          return items.reduce((sum, item) => sum + (item.productPrice * item.productQuantity), 0);
        },
        statusClasses(orderStatus) {
          return {
            'px-2 py-1 rounded text-sm': true,
            'bg-yellow-100 text-yellow-800': orderStatus === 'PENDING',
            'bg-green-100 text-green-800': orderStatus === 'COMPLETED',
            'bg-red-100 text-red-800': orderStatus === 'CANCELLED'
          };
        }
      }
    };
    </script>