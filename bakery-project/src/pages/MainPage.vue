<template>
    <Header />
    <HomeOneSection />
    <HomeSecondSection />
    <CareSection />
    <Footer />

</template>

<script>
import Header from '@/cross-components/Header.vue';
import HomeOneSection from '@/components/HomeOneSection.vue';
import HomeSecondSection from '@/components/HomeSecondSection.vue';
import CareSection from '@/components/CareSection.vue';
import Footer from '@/cross-components/Footer.vue';

    export default{
        name: 'MainPage',
        components:{
            Header,
            HomeOneSection,
            HomeSecondSection,
            CareSection,
            Footer,
        },
    };
</script>