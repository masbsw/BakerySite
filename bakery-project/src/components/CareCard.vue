<template>
    <div>
        <div class="p-4 sm:p-6 rounded-xl sm:rounded-2xl">
            <img :src="imageSrc" :alt="title" class="image w-full max-w-[180px] sm:max-w-[220px] md:max-w-[270px] mx-auto mb-4 sm:mb-7 rounded-[10px] sm:rounded-[15px]" />
            <h3 class="text-[16px] sm:text-[18px] font-semibold mb-2 sm:mb-4 sm:text-left text-center">{{ title }}</h3>
            <p class="text-600 text-[12px] sm:text-[13px] sm:text-left text-center">{{ description }}</p>
        </div>
    </div>
</template>

<style scoped>
.image {
    box-shadow: 0px 3px 6px 0px rgba(0, 0, 0, 0.3);
    transition: transform 0.3s ease;
}

.image:hover {
    transform: scale(1.03);
}

@media (max-width: 425px) {
    .image {
        box-shadow: 0px 2px 4px 0px rgba(0, 0, 0, 0.2);
    }
}
</style>

<script>
export default {
    name: 'CareCard',
    props: {
        imageSrc: {
            type: String,
            required: true,
        },
        title: {
            type: String,
            required: true,
        },
        description: {
            type: String,
            required: true,
        }
    }
}
</script>