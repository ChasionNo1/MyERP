<script setup>
import { getAction } from '@/api/manage';
import { ref } from 'vue';
defineOptions({})
const data = ref('')

const test = async () => {
    const res = await getAction('/user/test')
    data.value = res.message
}
test()

</script>

<template>
  <div>{{ data }}</div>
</template>

<style scoped>
</style>