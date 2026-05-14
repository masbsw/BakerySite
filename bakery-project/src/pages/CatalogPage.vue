<template>
    <div class="container">
        <Header />
        <div class="content">

            <CatalogSection />
        </div>
        <Footer class="footer" />
    </div>
</template>

<script>
import Header from '@/cross-components/Header.vue';
import Breadcrumbs from '@/cross-components/Breadcrumbs.vue';
import CatalogSection from '@/components/CatalogSection.vue';
import Footer from '@/cross-components/Footer.vue';

export default {
    components: {
        Header,
        Breadcrumbs,
        CatalogSection,
        Footer
    }
}
</script>

<style scoped>
.container {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
    width: 100%;
    max-width: 100%;
    margin: 0;
    padding: 0;
}

.content {
    flex: 1;
    width: 100%;
    padding: 0;
    box-sizing: border-box;
}

.footer {
    width: 100%;
    padding: 20px 0;
}

@media (max-width: 768px) {
    .content {
        padding: 0;
    }
}
</style>