<template>
  <div class="w-full sm:w-[94%] mx-auto p-4 sm:p-6">
    <h2 class="text-[24px] sm:text-[32px] md:text-[40px] font-medium mb-8 sm:mb-16 ml-0 sm:ml-[8%]">Адрес на карте</h2>
    <div id="map" class="w-full sm:w-[90%] md:w-[79%] h-[300px] sm:h-[400px] md:h-[500px] mx-auto"></div>
    <hr class="h-[1px] sm:h-[1.5px] bg-black mt-8 sm:mt-12">
  </div>
</template>

<script>
export default {
  name: 'AddressSection',
  mounted() {
    if (typeof ymaps !== 'undefined') {
      ymaps.ready(this.initMap);
    } else {
      console.error('Yandex Maps API не загружен');
    }
  },
  methods: {
    initMap() {
      try {
        const map = new ymaps.Map('map', {
          center: [47.279815, 39.715908],
          zoom: 16,
          controls: ['zoomControl']
        });

        const placemark = new ymaps.Placemark(
          [47.279815, 39.715908],
          {
            balloonContent: 'Ростов-на-Дону, проспект Космонавтов, 1/26'
          },
          {
            preset: 'islands#blueDotIcon'
          }
        );

        map.geoObjects.add(placemark);
      } catch (e) {
        console.error('Ошибка инициализации карты:', e);
      }
    }
  }
};
</script>