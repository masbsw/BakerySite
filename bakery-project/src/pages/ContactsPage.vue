<script >
import Header from '@/cross-components/Header.vue';
import Breadcrumbs from '@/cross-components/Breadcrumbs.vue';
import AboutUs from '@/components/AboutUs.vue';
import AddressSection from '@/components/AddressSection.vue';
import FormContacts from '@/components/FormContacts.vue';
import Footer from '@/cross-components/Footer.vue';
export default{
    components:{
        Header,
        Breadcrumbs,
        AboutUs,
        AddressSection,
        FormContacts,
        Footer
    }
}
</script>

<template>

        <div class="container">
        <Header />
        <div class="content">
            <AboutUs />
            <AddressSection />
            <FormContacts />
        </div>
        <Footer class="footer" />
    </div>
    
</template>

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