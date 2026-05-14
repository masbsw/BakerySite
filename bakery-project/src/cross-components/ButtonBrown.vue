<template>
    <button 
    type="submit" 
    :class="buttonClass"
    :style="{ width: buttonWidth }"
    class=" text-black font-regular text-2xl rounded-[15px] border-solid border-black mx-auto h-[50px] md:col-span-2">
        {{ btnName }}
    </button>
</template>

<style scoped>
button {
  border-width: 2px;
  opacity: 1;
  background-color: rgba(202, 159, 131, 0.5);
}

</style>

<script>
    export default{
        name: "ButtonBrown",
        props:{
            btnName:{
                type: String,
                required:true,
            },
            buttonWidth: {
                type: String,
                default: '25%'
            }
        }
    }
</script>